package per.tomcat.learning.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 3:43 下午
 **/
public class Server {

    public static void main(String[] args) {
        boolean closeSign=false;
        ServerSocket serverSocket;
        try {
            serverSocket=new ServerSocket();
        } catch (IOException e) {
            System.out.println("初始化serverSocket失败！");
            return;
        }
        try {
            serverSocket.bind(new InetSocketAddress(33333),10);
        } catch (IOException e) {
            System.out.println("开启33333端口的监听失败！");
            return;
        }
        while (!closeSign) {
            Socket accept;
            try {
                accept = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("接受端口请求失败！");
                return;
            }
            InputStream inputStream;
            try {
                inputStream = accept.getInputStream();
            } catch (IOException e) {
                System.out.println("获取port:[" + accept.getPort() + "]输入流失败！");
                return;
            }
            OutputStream outputStream;
            try {
                outputStream = accept.getOutputStream();
            } catch (IOException e) {
                System.out.println("获取port:[" + accept.getPort() + "]输出流失败！");
                return;
            }

            HttpRequest httpRequest=new HttpRequest();
            HttpResponse httpResponse=new HttpResponse();
            httpRequest.setInputStream(inputStream);
            httpRequest.readInfo();
            System.out.println(httpRequest.getBody());
            httpResponse.setOutputStream(outputStream);
        }

    }

}
