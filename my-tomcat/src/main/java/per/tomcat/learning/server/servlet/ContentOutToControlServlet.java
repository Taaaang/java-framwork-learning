package per.tomcat.learning.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import per.tomcat.learning.server.http.HttpRequest;
import per.tomcat.learning.server.http.HttpResponse;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/18 15:20
 */
public class ContentOutToControlServlet implements Servlet {

  @Override
  public void init() {
    System.out.println("this is empty of init ,no config load!");
  }

  @Override
  public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
    System.out.println("received info :"+httpRequest.getBody());
    try {
      OutputStream outputStream = httpResponse.outputStream;
      outputStream.write("ok! I got it!".getBytes());
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void destroy() {
    try {
      Thread.sleep(2000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("yeh!  destroy completed!!!");
  }
}
