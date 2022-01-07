package per.distribute.lock.redbag;

import com.sun.tools.corba.se.idl.StringGen;
import org.redisson.api.RLock;
import per.distribute.lock.redbag.config.JedisConfig;
import per.distribute.lock.redbag.config.RedissonConfig;
import redis.clients.jedis.params.SetParams;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/12 16:10
 */
public class Test {

    private static Thread condition=new Thread();

    public static void main(String[] args) throws InterruptedException {
        ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(10);
        executor.scheduleWithFixedDelay()
    }

    public static class MyThread extends Thread{
        private Thread thread;
        public MyThread(Thread thread){
            this.thread=thread;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("释放锁");
            LockSupport.unpark(thread);
        }
    }

    public static class TestService{

        public void say(){
            System.out.println("abc");
        }
    }

}
