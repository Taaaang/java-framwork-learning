package per.tomcat.learning.server;

import lombok.Data;

import java.io.OutputStream;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 4:03 下午
 **/
@Data
public class HttpResponse {

    private OutputStream outputStream;

}
