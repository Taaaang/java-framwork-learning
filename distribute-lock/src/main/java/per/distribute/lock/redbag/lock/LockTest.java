package per.distribute.lock.redbag.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.redisson.api.RLock;
import per.distribute.lock.redbag.config.JedisConfig;
import per.distribute.lock.redbag.config.RedissonConfig;
import per.distribute.lock.redbag.lock.DistributedLock.Lock;
import redis.clients.jedis.Jedis;

import static per.distribute.lock.redbag.common.LogSupport.*;

/**
 * @Description
 * @Author TangWenBiao
 * @Create 2021-12-10 6:28 PM
 **/
public class LockTest {

  public static void main(String[] args) throws InterruptedException {
    //初始化分布式锁
    DistributedLock.init();
    //获取锁
    IntWrapper intWrapper=new IntWrapper(0);
    CountDownLatch countDownLatch=new CountDownLatch(1);

    int threadCount=10;
    int count=1000;
    AtomicInteger endCount=new AtomicInteger(0);
    for (int i = 0; i < threadCount; i++) {
      new Thread(() -> {
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
          return;
        }
        Lock test1Lock = DistributedLock.getLock("test1");
        test1Lock.lock();
        try{
          for (int j = 0; j < count; j++) {
            intWrapper.add();
          }
          endCount.addAndGet(1);
          if(endCount.get()==7){
            Thread.sleep(20000L);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          test1Lock.release();
          log("释放锁");
        }

      }).start();
    }
    countDownLatch.countDown();

    /*try {
      */
    while (endCount.get() != threadCount) {
      Thread.sleep(200L);
    }
    log(intWrapper.get()+"");
  }

  private static class IntWrapper{
    private int amount;
    private String key="amount_test";
    public IntWrapper(int amount) {
      Jedis jedis = JedisConfig.get();
      jedis.del(key);
      jedis.set(key,amount+"");
    }

    public void add(){
      Jedis jedis = JedisConfig.get();
      try {
        String s = jedis.get(key);
        jedis.set(key,(Integer.parseInt(s)+1)+"");
      }finally {
        jedis.close();
      }
    }

    public int get(){
      return  Integer.parseInt(JedisConfig.get().get(key));
    }
  }
}
