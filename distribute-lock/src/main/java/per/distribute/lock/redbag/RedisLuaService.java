package per.distribute.lock.redbag;

import org.apache.commons.codec.digest.DigestUtils;
import per.distribute.lock.redbag.config.JedisConfig;
import per.distribute.lock.redbag.config.RedissonConfig;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 借助于redis执行lua脚本来完成红包的获取
 *
 * @Author:TangFenQi
 * @Date:2021/12/2 17:40
 **/
public class RedisLuaService implements IBagService {

    private static Random random = new Random();

    @Override
    public String fill(Long userId, BigDecimal bigDecimal) {
        String redBagId = UUID.randomUUID().toString();
        //在redis中初始化用户钱包的金额
        Jedis jedis = JedisConfig.get();
        jedis.hset(RED_BAG_PREFIX + userId, redBagId, bigDecimal.toString());
        return redBagId;
    }

    @Override
    public BigDecimal take(Long userId, Long takeUserId, String redBagId) {
        //通过lua控制并发
        return new BigDecimal(takeMoney(userId,takeUserId,redBagId));
    }

    private int takeMoney(Long userId, Long takeUserId, String redBagId) {
        //判断redis中是否已经load了脚本
        Jedis jedis=null;
        int takeMoney=0;
        try {
            jedis = JedisConfig.get();
            String sha1 = DigestUtils.sha1Hex(luaOfTakeMoney());
            //1.没load则优先进行load
            if (!jedis.scriptExists(sha1)) {
                jedis.scriptLoad(luaOfTakeMoney());
            }
            //2.通过sha1进行访问脚本获取结果
            int randomMoney = random.nextInt(4);
            takeMoney=checkMoney(((Long) jedis.evalsha(sha1, buildKeys(userId,redBagId), buildArgv(randomMoney+1,takeUserId))).intValue());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return takeMoney;
    }

    private List<String> buildKeys(Long userId,String redBagId){
        List<String> keys = new ArrayList<>();
        keys.add(RED_BAG_PREFIX + userId);
        keys.add(redBagId);
        return keys;
    }

    private List<String> buildArgv(int money,long takeUserId){
        List<String> argv = new ArrayList<>();
        argv.add(money + "");
        argv.add(takeUserId + "");
        return argv;
    }

    private int checkMoney(int money){
        switch (money){
            case -2:
                throw new RuntimeException("该用户已领取过红包!");
            case -1:
            case 0:
                throw new RuntimeException("红包已被领取完！");
            default:
                return money;
        }
    }

    /**
     * 获取金额的lua脚本
     *
     * @return
     */
    private String luaOfTakeMoney() {
        /**
         * 1.判断该用户是否获取过
         * 2.若未获取过，判断金额是否大于0
         *  2.1 若大于0则，判断是否小于随机值
         *   2.1.1若小于则直接返回金额结果
         *   2.1.2若大于则减去随机值，返回随机值
         *
         * userId: KEYS[1], redBagId:KEYS[2]
         * randomMoney:ARGV[1], takeUserId:ARGV[2]
         */
        //1.判断用户是否领取过
        //2.判断红包是否存在
        //3.判断金额是否满足
        String lua = "if redis.call(\"hexists\", KEYS[2], ARGV[2]) == 1 then\n" +
                "    return -2\n" +
                "end\n" +
                "\n" +
                "if redis.call(\"hexists\", KEYS[1], KEYS[2]) == 0 then\n" +
                "    return -1\n" +
                "end\n" +
                "\n" +
                "local money = tonumber(redis.call(\"hget\", KEYS[1], KEYS[2]))\n" +
                "local takeMoney = tonumber(ARGV[1])\n" +
                "\n" +
                "if money >= takeMoney then\n" +
                "    redis.call(\"hset\", KEYS[2], ARGV[2], 1)\n" +
                "    redis.call(\"hset\", KEYS[1], KEYS[2], money - takeMoney)\n" +
                "    return takeMoney\n" +
                "elseif money > 0 then\n" +
                "    redis.call(\"hset\", KEYS[2], ARGV[2], 1)\n" +
                "    redis.call(\"hset\", KEYS[1], KEYS[2], 0)\n" +
                "    return money\n" +
                "else\n" +
                "    redis.call(\"hdel\", KEYS[1], KEYS[2])\n" +
                "    return 0\n" +
                "end\n" +
                "\n";
        return lua;
    }
}
