package per.pay.business.framwork.server.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Twitter的雪花算法,实现地址{https://github.com/twitter-archive/snowflake/blob/snowflake-2010/src/main/scala/com/twitter/service/snowflake/IdWorker.scala}
 * 从高到低bit位数含义
 * 1:最高位，正负标识位，不使用
 * 41:当前时间戳
 * 5:工作空间（应用标识位）
 * 5:机器码（多台相同应用进行区分）
 * 12:同时间的自增序列
 * 通过 [时间戳]，[自增序列] 进行单机唯一性保证
 * 通过 [工作空间]，[机器码] 进行分布式唯一性保证
 */

/**
 * @Author:TangFenQi
 * @Date:2021/11/4 11:43
 */
public class IdWorker {

    private static final int TIMESTAMP_BIT=41;//时间戳位数
    private static final int WORKSPACE_BIT=5;//工作空间位数
    private static final int MACHINE_BIT=5;//机器码位数
    private static final int SEQUENCE_BIT=12;//自增序列位数

    private static final int WORKSPACE_MASK=(1<<WORKSPACE_BIT)-1;//工作空间上界
    private static final int MACHINE_MASK=(1<<MACHINE_BIT)-1;//机器码上界
    private static final int SEQUENCE_MASK=(1<<SEQUENCE_BIT)-1; //自增寻列上界

    private static long lastTimestamp=-1; //记录上一次生成id时的时间戳
    private static long sequence=0;

    public static void main(String[] args) throws InterruptedException {
        ScheduledThreadPoolExecutor executor= new ScheduledThreadPoolExecutor(8);
        int totalTimes=100000;
        int threadNumbers=16;
        Map<Long,Integer> counterMap=new ConcurrentHashMap<>(totalTimes*threadNumbers);
        //任务集合
        for (int i = 0; i < threadNumbers; i++) {
            executor.submit(() -> {
                System.out.println("线程编号:"+Thread.currentThread().getId());
                for (int j = 0; j <totalTimes ; j++) {
                    long nextTime = IdWorker.nextId((int)Thread.currentThread().getId(),(int)Thread.currentThread().getId());
                    Integer orDefault = counterMap.getOrDefault(nextTime, 0);
                    counterMap.put(nextTime,orDefault+1);
                }
            });
        }
        Thread.sleep(1000*20);
        //验证
        long repeatCount = counterMap.values().stream().filter(integer -> integer > 1).count();
        System.out.println(String.format("总条数:[%d],重复的条数:[%d]",counterMap.size(),repeatCount));

        executor.shutdown();

    }

    public static synchronized long nextId(int workspace,int machineId){
        Assert.isTrue(workspace>=0,String.format("workspace only be positive ! workspace[%n]",workspace));
        Assert.isTrue(workspace<=WORKSPACE_MASK,String.format("workspace number out of bound ! workspace:[%n]，limit:[%n]",workspace,WORKSPACE_MASK));
        Assert.isTrue(machineId>=0,String.format("workspace only be positive ! machineId[%n]",workspace));
        Assert.isTrue(machineId<=MACHINE_MASK,String.format("workspace number out of bound ! machineId:[%n]，limit:[%n]",workspace,MACHINE_MASK));
        //获取当前时间戳
        long timestamp = System.currentTimeMillis();
        if(timestamp<lastTimestamp){
            //比较是否大于上一次时间（防止时钟回拨导致生成重复ID）
            throw new IllegalArgumentException(String.format("lastTimestamp later then current time! pls check whether turn the clock?? current timestamp [%s] ,last timestamp [%s] ",timestamp,lastTimestamp));
        }else if(timestamp==lastTimestamp){
            //是否与上一次时间戳相等（需要分情况，可能出现向后借时间的问题）
            sequence=(sequence+1)&SEQUENCE_MASK;
            //如果越界
            if(sequence==0){
                timestamp=getNextTime(timestamp);
            }
        }else {
            //是否大于上一次时间戳（安全）
            sequence=0;
        }

        lastTimestamp=timestamp;
        return timestamp<<(WORKSPACE_BIT+MACHINE_BIT+SEQUENCE_BIT)|
                workspace<<(MACHINE_BIT+SEQUENCE_BIT)|
                machineId<<(SEQUENCE_BIT)|
                sequence;
    }

    private static long getNextTime(long currentTimestamp){
        long newTimestamp=System.currentTimeMillis();
        while (newTimestamp<=currentTimestamp){
            newTimestamp=System.currentTimeMillis();
        }
        return newTimestamp;
    }

}
