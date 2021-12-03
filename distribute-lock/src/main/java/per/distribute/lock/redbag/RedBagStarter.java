package per.distribute.lock.redbag;

import com.google.common.util.concurrent.AtomicLongMap;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 借助于redis实现红包功能
 *
 * @Author:TangFenQi
 * @Date:2021/12/2 17:39
 **/
public class RedBagStarter {


    public static void main(String[] args) throws InterruptedException {
        IBagService luaService = new RedisListService();
        runTest(luaService);
    }

    public static void runTest(IBagService bagService) throws InterruptedException {
        Random random = new Random();

        //发包用户编号
        Long userId = 1L;
        //发包金额
        BigDecimal bagMoney = new BigDecimal(15);

        AtomicInteger totalMoney = new AtomicInteger(0); //最终领取的总金额
        AtomicLongMap<Long> userAndCount = AtomicLongMap.create(); //领取成功的用户编号与次数
        AtomicLongMap<Long> failUserAndCount = AtomicLongMap.create();//领取失败的用户编号与次数
        ConcurrentHashMap<Long,Integer> userAndMoney=new ConcurrentHashMap<>();//领取成功的用户编号与金额
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger threadEnd = new AtomicInteger(0);//线程完毕计数（执行完毕信号量）
        int count = 40;//总访问线程数
        ThreadGroup testRedBag = new ThreadGroup("testRedBag");
        int takeUserIdUpper=20;

        //1.用户封红包
        String redBagId = bagService.fill(userId, bagMoney);
        //2.用户开红包
        Supplier<Runnable> takeMoneySupplier = () -> () -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long takeUserId = (random.nextInt(takeUserIdUpper) + 2);
            try {
                final BigDecimal take = bagService.take(userId, takeUserId, redBagId);
                userAndMoney.putIfAbsent(takeUserId,take.intValue());
                userAndCount.getAndIncrement(takeUserId);
                totalMoney.addAndGet(take.intValue());
            } catch (Exception ex) {
                failUserAndCount.getAndIncrement(takeUserId);
                ex.printStackTrace();
            }
            threadEnd.incrementAndGet();
        };

        for (int i = 0; i < count; i++) {
            new Thread(testRedBag, takeMoneySupplier.get()).start();
        }
        Thread.sleep(1000L);
        countDownLatch.countDown();

        while (true) {
            System.out.println("总金额:" + totalMoney + " 线程数:" + threadEnd);
            if (threadEnd.intValue()==count) {
                break;
            }
            Thread.sleep(1000L);
        }


        System.out.println("开始验证");
        if(totalMoney.intValue()!=bagMoney.intValue()){
            System.out.println("【失败】金额!");
        }else {
            System.out.println("【成功】金额!");
        }
        boolean repeatTake=false;
        for (Long value : userAndCount.asMap().values()) {
            if(value>1){
                repeatTake=true;
                break;
            }
        }
        if(repeatTake) {
            System.out.println("【失败】次数!");
        }else {
            System.out.println("【成功】次数!");
        }

        System.out.println("原始数据:");
        userAndMoney.forEach((takeUserId, integer) -> System.out.println(String.format("用户编号:[%s]，领取金额:[%s]", takeUserId, integer)));
    }



}
