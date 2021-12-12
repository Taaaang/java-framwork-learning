package per.distribute.lock.redbag.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author:TangFenQi
 * @Date:2021/12/3 17:47
 **/
public class JedisConfig {

    private static JedisPool jedisPool;

    private static AtomicBoolean init=new AtomicBoolean(false);

    public static void init(){
        if(init.compareAndSet(false,true)){
            GenericObjectPoolConfig<Jedis> poolConfig=new GenericObjectPoolConfig<>();
            poolConfig.setMaxTotal(30);
            poolConfig.setMaxWaitMillis(1000L);
            jedisPool=new JedisPool(poolConfig,"127.0.0.1",6379);
        }
    }

    public static Jedis get(){
        while (jedisPool==null){
            init();
        }

        return jedisPool.getResource();
    }

}
