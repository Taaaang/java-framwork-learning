package per.jdk.learn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/21 - 2:28 下午
 **/
public class OtherTest {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        String note="* Try to signal next queued node if:\n" +
                "         *   Propagation was indicated by caller,\n" +
                "         *     or was recorded (as h.waitStatus either before\n" +
                "         *     or after setHead) by a previous operation\n" +
                "         *     (note: this uses sign-check of waitStatus because\n" +
                "         *      PROPAGATE status may transition to SIGNAL.)\n" +
                "         * and\n" +
                "         *   The next node is waiting in shared mode,\n" +
                "         *     or we don't know, because it appears null\n" +
                "         *\n" +
                "         * The conservatism in both of these checks may cause\n" +
                "         * unnecessary wake-ups, but only when there are multiple\n" +
                "         * racing acquires/releases, so most need signals now or soon\n" +
                "         * anyway.";
        oneLineNote(note);
    }

    public void test(){

    }

    public static Map<String,String> SIGNS=new HashMap<>();
    static {
        SIGNS.put("<p>","");
        SIGNS.put("\n","");
        SIGNS.put("*"," ");
    }


    public static void excludeStar(String note){
        note=note.replace("*","");
        System.out.println(note);
    }

    public static void oneLineNote(String note){
        for (Map.Entry<String, String> entry : SIGNS.entrySet()) {
            note=note.replace(entry.getKey(),entry.getValue());
        }
        /*while (note.contains("{")){
            int start= note.indexOf("{");
            int end= note.indexOf("}");
            note=note.replace(note.substring(start,end+1),"");
        }*/
        System.out.println(note);
    }


}
