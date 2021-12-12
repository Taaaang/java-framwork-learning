package per.distribute.lock.redbag.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: TangFenQi
 * @description: 日志支持（输出到控制台）
 * @date：2021/12/12 11:04
 */
public class LogSupport {

  private static DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void log(String info){
    //时间+线程编号+内容
    System.out.println(String.format("[%s]-[%s]：%s",formatter.format(LocalDateTime.now()),Thread.currentThread().getId(),info));
  }

}
