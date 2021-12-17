package per.tomcat.learning.server;

import lombok.Data;

import java.io.*;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 4:02 下午
 **/
@Data
public class HttpRequest {

    private InputStream inputStream;
    private Request request;

    public String getBody(){
        return this.request.getBody();
    }

    public void readInfo(){
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("转换inputStream失败");
            return;
        }
        String line = "";

        try {
            //bufferedReader.mark(0);
            boolean hasContent=false;
            boolean allContent=false;
            StringBuilder content=new StringBuilder();
            int length=0;
            int contentLength=-1;
            while ((line = bufferedReader.readLine()) != null) {
                if(contentLength==-1&&line.contains("Content-Length")){
                    contentLength=readContentLength(line);
                }
                if(hasContent){
                    length=length+line.length();
                }
                content.append(line);
                if(length==contentLength){
                    allContent=true;
                    break;
                }
                if(line.contains("\\r\\n")){
                    hasContent=true;
                }

            }
            if(allContent){
                buildRequest(content);
            }else {
                content=new StringBuilder();
                //bufferedReader.reset();
            }
        } catch (IOException ex) {
            System.out.println("读取数据失败！");
            ex.printStackTrace();
        }
    }

    private int readContentLength(String line){
        String[] split = line.split(":");
        return Integer.parseInt(split[1].trim());
    }

    private void buildRequest(StringBuilder content){
        this.request=new Request();
        String[] split = content.toString().split("\r\n");
        //第一层，协议方法
        buildRequestHeader(split[0],request);
        buildBody(split[1],request);

    }

    private void buildRequestHeader(String content,Request request){
        String[] split = content.split("\n");
        String[] requestHeader=split[0].split("\t");
        request.setMethod(requestHeader[0]);
        request.setUri(requestHeader[1]);
        request.setVersion(requestHeader[2]);
        for (int i = 1; i <split.length ; i++) {
            if(split[i].contains("Content-Type")){
                String[] headerKeyValue = split[i].split(":");
                request.setContentType(headerKeyValue[1].trim());
            }
        }
    }

    private void buildBody(String body,Request request){
        request.setBody(body);
    }
}
