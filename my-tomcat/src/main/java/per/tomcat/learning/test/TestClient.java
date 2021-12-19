package per.tomcat.learning.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author: TangFenQi
 * @description:
 * @date：2021/12/19 11:20
 */
public class TestClient {

  public static void main(String[] args) {
    try {
      Socket socket=new Socket();
      socket.connect(new InetSocketAddress("127.0.0.1",33333));
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write("test! test!".getBytes());
      outputStream.write("efo".getBytes());
      outputStream.flush();
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      boolean loop=true;
      while (loop){
        if(reader.ready()){
          int i;
          StringBuilder stringBuilder=new StringBuilder();
          while ((i=reader.read())!=-1){
            stringBuilder.append((char)i);
            if(stringBuilder.toString().contains("efo")){
              break;
            }
          }
          loop=false;
          System.out.println("接收到："+stringBuilder.toString());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
