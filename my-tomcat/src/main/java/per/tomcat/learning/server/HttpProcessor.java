package per.tomcat.learning.server;

import jakarta.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import lombok.Data;
import org.apache.catalina.connector.CoyoteInputStream;
import per.tomcat.learning.server.http.HttpConnector;
import per.tomcat.learning.server.http.HttpRequest;
import per.tomcat.learning.server.http.HttpResponse;
import per.tomcat.learning.server.servlet.ContentOutToControlServlet;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/19 12:56
 */
@Data
public class HttpProcessor implements Runnable{

  private HttpConnector connector;

  private Socket socket;

  private boolean available=false;

  private boolean shutdown=false;

  private String threadName="my-processor";

  public void process(Socket accept) {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      inputStream = accept.getInputStream();
      outputStream = accept.getOutputStream();

      //2.封装管理
      HttpRequest httpRequest = new HttpRequest(inputStream);
      httpRequest.parse();
      HttpResponse httpResponse = new HttpResponse(outputStream, httpRequest);

      //3.分发与业务逻辑
      ContentOutToControlServlet servlet = new ContentOutToControlServlet();
      servlet.init();
      servlet.service(httpRequest, httpResponse);
      servlet.destroy();

      //4.检测执令
    } catch (IOException e) {
      System.out.println("接受请求失败！");
      System.exit(1);
      return;
    } finally {
      if (accept != null) {
        try {
          accept.close();
        } catch (IOException e) {
          System.out.println("关闭accept失败");
          return;
        }
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          System.out.println("关闭inputStream失败");
          return;
        }
      }
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          System.out.println("关闭outputStream失败");
          return;
        }
      }
    }
  }

  @Override
  public void run() {

    while (!shutdown) {
      Socket socket=await();
      //开始流程处理
      process(socket);
      //回收
      connector.recycle(this);
    }


  }

  public void start(){
    Thread thread = new Thread(this, threadName);
    thread.start();
  }

  private synchronized Socket await(){
    while (!available){
      try {
        wait();
      } catch (InterruptedException e) {
        System.out.println("别tmd打断我，还没有socket给我处理，小心空指针异常伺候！");
        e.printStackTrace();
      }
    }

    this.available=false;
    notifyAll();
    return this.socket;
  }

  public synchronized void assign(Socket socket){
    while (available){
      try {
        wait();
      } catch (InterruptedException e) {
        System.out.println("别tmd的打断我,不然把socket覆盖了就gg了！");
        e.printStackTrace();
      }
    }

    this.available=true;
    this.socket=socket;
    notifyAll();
  }
}
