package per.tomcat.learning.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import lombok.Data;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import per.tomcat.learning.server.HttpProcessor;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/19 12:51
 */
@Data
public class HttpConnector implements Runnable, Lifecycle {

  public static String SHUTDOWN_COMMAND = "SHUTDOWN";

  public Stack<HttpProcessor> processorStack;

  private Integer curProcessorNumber;
  private Integer maxProcessorNumber;
  private Integer minProcessorNumber;

  private int port;
  private boolean shutdown;
  private String threadName;

  public HttpConnector(int port){
    this.port=port;
    this.shutdown=false;
    this.processorStack=new Stack<>();
  }

  @Override
  public void run() {
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket();
    } catch (IOException e) {
      System.out.println("初始化serverSocket失败！");
      return;
    }
    try {
      serverSocket.bind(new InetSocketAddress(this.port), 10);
    } catch (IOException e) {
      System.out.println("开启"+this.port+"端口的监听失败！");
      return;
    }

    //1.连接管理
    while (!this.shutdown) {
      Socket accept = null;
      try {
        accept = serverSocket.accept();
      } catch (IOException e) {
        e.printStackTrace();
        continue;
      }

      HttpProcessor processor = createProcessor();
      if(processor==null){
        try {
          accept.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        continue;
      }
      processor.assign(accept);
    }
  }

  private HttpProcessor createProcessor(){
    synchronized (processorStack) {
      if (!processorStack.empty()) {
        return processorStack.pop();
      }
      if (curProcessorNumber < maxProcessorNumber) {
        curProcessorNumber++;
        return new HttpProcessor();
      } else {
        System.out.println("满了，无法处理，自己等会再来！");
        return null;
      }
    }
  }

  public void recycle(HttpProcessor httpProcessor){
    synchronized (processorStack){
      processorStack.push(httpProcessor);
    }
  }

  @Override
  public void addLifecycleListener(LifecycleListener lifecycleListener) {

  }

  @Override
  public LifecycleListener[] findLifecycleListeners() {
    return new LifecycleListener[0];
  }

  @Override
  public void removeLifecycleListener(LifecycleListener lifecycleListener) {

  }

  @Override
  public void init() throws LifecycleException {

  }

  @Override
  public void start() {
    this.curProcessorNumber=0;
    this.maxProcessorNumber=10;
    this.minProcessorNumber=5;
    this.threadName="my-connector";
    startProcessor();


    Thread thread = new Thread(this, threadName);
    if(thread.isDaemon())
      thread.setDaemon(false);
    thread.start();



  }

  private void startProcessor(){
    while (curProcessorNumber<minProcessorNumber){
      HttpProcessor processor = new HttpProcessor();
      processor.setConnector(this);
      recycle(processor);
      processor.start();
      curProcessorNumber++;
    }
  }

  @Override
  public void stop() throws LifecycleException {

  }

  @Override
  public void destroy() throws LifecycleException {

  }

  @Override
  public LifecycleState getState() {
    return null;
  }

  @Override
  public String getStateName() {
    return null;
  }
}
