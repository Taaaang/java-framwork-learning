package per.distribute.lock.redbag;

import org.apache.commons.codec.digest.DigestUtils;
import per.distribute.lock.redbag.config.JedisConfig;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 借助于redis的list完成红包的发放
 * @Author:TangFenQi
 * @Date:2021/12/3 21:33
 **/
public class RedisListService implements IBagService {
    @Override
    public String fill(Long userId, BigDecimal money) {
        //1.红包编号
        String redBagId = UUID.randomUUID().toString();
        //2.根据红包金额拆分金额
        final List<Integer> splitMoney = splitMoney(money);
        List<String> splitMoneyStr=splitMoney.stream().map(v -> v + "").collect(Collectors.toList());
        String[] splitMoneyArr=new String[splitMoneyStr.size()];
        splitMoneyStr.toArray(splitMoneyArr);
        //3.存入redis的List中
        JedisConfig.get().lpush(RED_BAG_PREFIX+redBagId,splitMoneyArr);
        return redBagId;
    }

    private List<Integer> splitMoney(BigDecimal money){
        int copyMoney=money.intValue();
        Random random=new Random();
        List<Integer> moneyList=new ArrayList<>();
        while (copyMoney>0){
            int randomMoney=random.nextInt(3)+1;
            if(copyMoney>=randomMoney){
                moneyList.add(randomMoney);
                copyMoney-=randomMoney;
            }else {
                moneyList.add(copyMoney);
                copyMoney=0;
            }

        }
        return moneyList;
    }

    @Override
    public BigDecimal take(Long userId, Long takeUserId, String redBagId) {
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
            takeMoney=checkMoney(((Long) jedis.evalsha(sha1, buildKeys(redBagId), buildArgv(takeUserId))).intValue(),jedis,redBagId);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return new BigDecimal(takeMoney);
    }

    private List<String> buildKeys(String redBagId){
        List<String> keys = new ArrayList<>();
        keys.add(RED_BAG_PREFIX + redBagId);
        keys.add(redBagId);
        return keys;
    }

    private List<String> buildArgv(long takeUserId){
        List<String> argv = new ArrayList<>();
        argv.add(takeUserId + "");
        return argv;
    }

    private int checkMoney(int money,Jedis jedis,String redBagId){
        switch (money){
            case -2:
                throw new RuntimeException("该用户已领取过红包!");
            case -1:
            case 0:
                throw new RuntimeException("红包已被领取完！-1");
            default:
                return money;
        }
    }

    private String luaOfTakeMoney(){
        /**
         * userId: KEYS[1], redBagId:KEYS[2]
         * randomMoney:ARGV[1], takeUserId:ARGV[2]
         */
        String lua="if redis.call(\"hexists\",KEYS[2],ARGV[1])==1 then\n" +
                "    return -2;\n" +
                "end\n" +
                "\n" +
                "if redis.call(\"exists\",KEYS[1])==0 then\n" +
                "    if redis.call(\"exists\",KEYS[2])==1 then\n" +
                "       redis.call(\"del\",KEYS[2])\n" +
                "    end\n" +
                "    return -1;\n" +
                "end\n" +
                "\n" +
                "local money=redis.call(\"lpop\",KEYS[1])\n" +
                "if money==nil then\n" +
                "    redis.call(\"del\",KEYS[2])\n" +
                "    redis.call(\"del\",KEYS[1])\n" +
                "    return 0;\n" +
                "else\n" +
                "    redis.call(\"hset\",KEYS[2],ARGV[1],1)\n" +
                "    return tonumber(money)\n" +
                "end";
        return lua;
    }
}
