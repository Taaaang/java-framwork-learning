package per.tomcat.learning.server.http;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import lombok.Data;

import java.io.*;
import org.apache.catalina.util.ParameterMap;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 4:02 下午
 **/
@Data
public class HttpRequest implements ServletRequest {

    private InputStream inputStream;
    private ParameterMap<String,Object> parameterMap;
    private String method;
    private String uri;
    private String version;
    private String contentType;
    private String contentLength;
    private String body;
    private int length;

    public ServletInputStream getInputStream(){
        return null;
    }
    public void setInputStream(ServletInputStream inputStream){}

    public HttpRequest(InputStream inputStream){
        this.inputStream=inputStream;
    }

    public void parse(){
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
            StandardCharsets.UTF_8));

        char[] bytes=new char[2048];
        try {
            int length = bufferedReader.read(bytes);
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(bytes,0,length);
            parseRequest(stringBuilder);
        } catch (IOException ex) {
            System.out.println("读取数据失败！");
            ex.printStackTrace();
        }
    }

    private void parseRequest(StringBuilder content){
        String[] split = content.toString().split("\r\n");
        //第一层，协议方法
        buildRequestHeader(split[0]);
        buildBody(split[1]);

    }

    private void buildRequestHeader(String content){
        String[] split = content.split("\n");
        String[] requestHeader=split[0].split("\t");
        setMethod(requestHeader[0]);
        setUri(requestHeader[1]);
        setVersion(requestHeader[2]);
        for (int i = 1; i <split.length ; i++) {
            if(split[i].contains("Content-Type")){
                String[] headerKeyValue = split[i].split(":");
                setContentType(headerKeyValue[1].trim());
            }
        }
    }

    private void buildBody(String body){
        setBody(body);
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    /**
     * @deprecated
     */
    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
        throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
