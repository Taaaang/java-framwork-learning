package per.tomcat.learning.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 3:42 下午
 **/
public class Client {

    public static void main(String[] args) {
        Socket socket=new Socket();
        try {
            socket.connect(new InetSocketAddress("127.0.0.1",33333));
        } catch (IOException e) {
            System.out.println("连接失败!");
            return;
        }
        OutputStream outputStream;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("获取输出流失败！");
            return;
        }

        try {
            outputStr(buildHttpFormat("hello world!!"),outputStream);
        } catch (Exception ex){

        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("关闭socket失败！");
            }
        }
    }

    private static String buildHttpFormat(String body){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("GET /mix/76.html?name=kelvin&password=123456 HTTP/1.1\n" +
                "Content-Type: www.fishbay.cn\n" +
                "Upgrade-Insecure-Requests: 1\n"+
                "Content-Length: "+body.length()+"\n");
        stringBuilder.append("\\r\\n\n");
        stringBuilder.append(body);
        return stringBuilder.toString();
    }

    private static void outputStr(String str,OutputStream outputStream){
        try {
            outputStream.write(str.getBytes());
        } catch (IOException e) {
            System.out.println("写入失败！");
        }finally {
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("关闭channel失败！");
                }
            }
        }
    }
}
