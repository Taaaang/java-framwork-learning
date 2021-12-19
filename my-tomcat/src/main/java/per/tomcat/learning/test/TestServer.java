package per.tomcat.learning.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/19 11:19
 */
public class TestServer {

  public static void main(String[] args) {
    try {
      ServerSocket serverSocket=new ServerSocket(22222);
      Socket accept = serverSocket.accept();
      accept.getOutputStream().write("ok! i got it!".getBytes());
      accept.getOutputStream().flush();
      accept.getOutputStream().close();
      accept.close();
      Thread.sleep(20000L);
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

}
