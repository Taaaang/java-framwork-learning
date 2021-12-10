package per.distribute.lock.redbag.lock;

/**
 * @Description
 * @Author TangWenBiao
 * @Create 2021-12-10 6:15 PM
 **/
public class DistributedLock {
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

    private Thread watchDog;


    private static class WatchDog extends Thread{

        private long scale;
        private long expiredTime;

        public WatchDog(long expiredTime){
            this.expiredTime=expiredTime;
            this.scale=3;
        }



    }
}
