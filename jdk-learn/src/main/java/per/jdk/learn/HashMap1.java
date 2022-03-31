package per.jdk.learn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/18 - 5:31 下午
 **/
public class HashMap1 {


    public static void main(String[] args) {

        ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    }

    private static class TestHashCode{
        private int i;

        public TestHashCode(int i) {
            this.i = i;
        }

        @Override
        public int hashCode() {
            return 100;
        }
    }

    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final int tableSizeFor(int c) {
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    private static int RESIZE_STAMP_BITS = 16;

    /**
     * Returns the stamp bits for resizing a table of size n.
     * Must be negative when shifted left by RESIZE_STAMP_SHIFT.
     */
    static final int resizeStamp(int n) {
        return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));
    }

}
