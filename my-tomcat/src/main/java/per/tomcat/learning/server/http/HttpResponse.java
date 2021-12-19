package per.tomcat.learning.server.http;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import lombok.Data;

import java.io.OutputStream;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 4:03 下午
 **/
@Data
public class HttpResponse implements ServletResponse {

    public OutputStream outputStream;
    private HttpRequest request;

    public HttpResponse(OutputStream outputStream,HttpRequest request){
        this.outputStream=outputStream;
        this.request=request;
    }

    public void setOutputStream(ServletOutputStream outputStream){}

    public ServletOutputStream getOutputStream(){
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
