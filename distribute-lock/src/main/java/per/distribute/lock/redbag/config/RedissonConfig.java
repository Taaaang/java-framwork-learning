package per.distribute.lock.redbag.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author:TangFenQi
 * @Date:2021/12/2 17:46
 **/
public class RedissonConfig {

    private static RedissonClient REDISSON;

    private static AtomicBoolean init=new AtomicBoolean(false);

    private static void init(){
        if(init.compareAndSet(false,true)) {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://192.168.0.102:6379")
                    ;
            REDISSON = Redisson.create(config);
        }
    }


    public static RedissonClient getClient(){
        while(REDISSON==null){
            init();
        }
       return REDISSON;
    }


}
