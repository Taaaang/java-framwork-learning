package per.distribute.lock.redbag.lock;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.api.RScript.Mode;
import org.redisson.api.RedissonClient;
import per.distribute.lock.redbag.common.LogSupport;
import per.distribute.lock.redbag.config.JedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import static per.distribute.lock.redbag.common.LogSupport.log;

/**
 * @Description
 * @Author TangWenBiao
 * @Create 2021-12-10 6:15 PM
 **/
public class DistributedLock {

  //持有redis客户端
  private static JedisPool jedisPool;
  //watchDog的线程池
  private static ThreadPoolExecutor executor;

  //默认过期时间，单位秒
  private static long defaultExpiredTime = 10;

  public static void init() {
    GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
    poolConfig.setMaxTotal(30);
    poolConfig.setMaxWaitMillis(1000L);
    jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);

    executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));

  }

  public static Lock getLock(String name) {
    String sourceName = name;
    return new Lock(sourceName, defaultExpiredTime);
  }

  public static class Lock {

    /**
     * 资源名称
     */
    private String sourceName;

    /**
     * 过期时间
     */
    private long expiredTime;

    /**
     * 时间类型
     */
    private int timeType;
    private WatchDog watchDog;

    private Lock(String sourceName, long expiredTime) {
      this.sourceName = sourceName;
      this.expiredTime = expiredTime;
    }

    public boolean lock() {
      //1.持有锁
      SetParams setParams = new SetParams();
      setParams.nx();
      setParams.ex(expiredTime);
      Jedis jedis = null;
      try {
        jedis = jedisPool.getResource();
        while (jedis.set(sourceName, "1", setParams) == null) {
          Thread.sleep(100L);
        }
        //2.启动watchdog
        watchDog = new WatchDog(expiredTime, sourceName, jedisPool);
        executor.execute(watchDog);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        if (jedis != null) {
          jedis.close();
        }
      }
      log("持有锁");
      return true;
    }

    public boolean release() {
      //1.移除key
      Jedis jedis = null;
      try {
        Jedis resource = jedisPool.getResource();
        resource.del(sourceName);
        //2.关闭线程
        watchDog.stopRenew();
      }finally {
        if(jedis!=null){
          jedis.close();
        }
      }
      return true;
    }
  }

  private static class WatchDog extends Thread {

    private JedisPool jedisPool;
    private long scale;
    private long expiredTime;
    private String sourceName;
    //true:续约 false:停止续约
    private boolean status;

    public WatchDog(long expiredTime, String sourceName, JedisPool jedisPool) {
      super(new ThreadGroup("distribute_lock"), "distribute_watch_dog_" + sourceName);
      this.jedisPool = jedisPool;
      this.expiredTime = expiredTime;
      this.sourceName = sourceName;
      this.scale = 3;
      this.status = true;
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(expiredTime * 1000 / scale);
        } catch (InterruptedException e) {
          e.printStackTrace();
          log("线程阻塞失败!");
        }
        if (!status) {
          log("续约被中止");
          break;
        }
        //进行续约
        renewalExpiredTime();
      }
    }

    public void renewalExpiredTime() {
      Jedis resource = null;
      try {
        resource = jedisPool.getResource();
        //1.对资源进行续约
        long status = (long) resource.eval(luaOfRenewal(), 1, sourceName, expiredTime + "");
        if (status != 0) {
          log("续约失败[" + sourceName + "]!返回参数:" + status);
        } else {
          log("续约成功!");
        }
      } finally {
        if (resource != null) {
          resource.close();
        }
      }

    }

    public void stopRenew() {
      this.status = false;
      this.interrupt();
    }

    private String luaOfRenewal() {
      return "if redis.call(\"exists\",KEYS[1])==0 then\n"
          + "  return -1;\n"
          + "else\n"
          + "  redis.call('expire', KEYS[1], tonumber(ARGV[1]));\n"
          + "  return 0;\n"
          + "end\n";
    }

  }

}
