package per.jdk.learn.clh;

import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * CLH 锁，通过持有前驱，隐式成链，
 * 优点：自身与前驱存储在ThreadLocal，将入队的竞争点锁定在tail的获取与设置上
 * 缺点：通过自旋等待获取锁，非常浪费cpu资源
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/31 - 1:35 下午
 **/
public class CLHLock implements Lock {

    private AtomicReference<Node> tail;

    private ThreadLocal<Node> current;

    private ThreadLocal<Node> pre;


    public CLHLock() {
        tail=new AtomicReference<>(new Node(false));
        current=new ThreadLocal<>();
        pre=new ThreadLocal<>();
    }

    @Override
    public void lock() {
        //自身
        Node node=new Node();
        current.set(node);
        //前驱
        Node preNode = tail.getAndSet(node);
        pre.set(preNode);
        //等待前驱节点运行完毕
        while (preNode.isStatus()){}
    }

    @Override
    public void unlock() {
        //标记释放
        Node node = current.get();
        if(node==null){
            throw new RuntimeException("未发现当前节点，请检查前置是否存在lock()方法");
        }
        node.setStatus(false);
        //清空
        current.set(null);
        pre.set(null);
    }

    public static void main(String[] args) throws InterruptedException {
        CLHLock lock=new CLHLock();
        MyInteger number=new MyInteger(0);
        int size=10;
        int countSize=100;
        CountDownLatch countDownLatch=new CountDownLatch(size);
        AtomicInteger sign=new AtomicInteger(0);
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < countSize; j++) {
                    lock.lock();
                    number.increment();
                    lock.unlock();
                }
                sign.incrementAndGet();
            }).start();
            countDownLatch.countDown();
        }

        while (sign.get()!=size){

        }
        System.out.println(number.get());
    }

    private static class MyInteger{
        private int number;

        public MyInteger(int number) {
            this.number = number;
        }

        public void increment(){
            number++;
        }

        public int get(){
            return number;
        }
    }



}
