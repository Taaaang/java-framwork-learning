package per.tomcat.learning.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 3:42 下午
 **/
public class Client {

  public void connect() {
    Socket socket = new Socket();
    try {
      socket.connect(new InetSocketAddress("127.0.0.1", 33333));
    } catch (IOException e) {
      System.out.println("连接失败!");
      return;
    }
    OutputStream outputStream;
    InputStream inputStream = null;
    try {
      inputStream = socket.getInputStream();
      outputStream = socket.getOutputStream();
      outputStr(buildHttpFormat("hello world!!"), outputStream);
      new ReadInStream(inputStream).run();
    } catch (IOException e) {
      System.out.println("获取输出流失败！");
      e.printStackTrace();
      return;
    }finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private class ReadInStream implements Runnable{
    private InputStream inputStream;

    public ReadInStream(InputStream inputStream){
      this.inputStream=inputStream;
    }

    @Override
    public void run() {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        char[] chars=new char[2048];
        int length=reader.read(chars);
        StringBuilder stringBuilder=new StringBuilder();
        if(length==-1){
          System.out.println("无数据返回!");
          return;
        }
        stringBuilder.append(chars,0,length);
        System.out.println("接收到："+stringBuilder.toString());
      }catch (IOException ex){
        System.out.println("读取服务端传递数据失败!");
        ex.printStackTrace();
      }finally {
        if(reader!=null){
          try {
            reader.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    public void start(){
      Thread thread=new Thread(this);
      thread.start();
    }
  }

  private static String buildHttpFormat(String body) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("GET\t/mix/76.html?name=kelvin&password=123456\tHTTP/1.1\n" +
        "Content-Type: www.fishbay.cn\n" +
        "Upgrade-Insecure-Requests: 1\n" +
        "Content-Length: " + body.length() + "\n");
    stringBuilder.append("\r\n");
    stringBuilder.append(body);
    return stringBuilder.toString();
  }

  private static String buildShutdownHttpFormat(String body) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("GET\t/SHUTDOWN\tHTTP/1.1\n" +
        "Content-Type: www.fishbay.cn\n" +
        "Upgrade-Insecure-Requests: 1\n" +
        "Content-Length: " + body.length() + "\n");
    stringBuilder.append("\r\n");
    stringBuilder.append(body);
    return stringBuilder.toString();
  }

  private static void outputStr(String str, OutputStream outputStream) {
    try {
      outputStream.write(str.getBytes());
    } catch (IOException e) {
      System.out.println("写入失败！");
    } finally {

    }
  }
}
