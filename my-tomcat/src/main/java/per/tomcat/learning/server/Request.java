package per.tomcat.learning.server;

import lombok.Data;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/17 - 4:27 下午
 **/
@Data
public class Request {

    private String method;
    private String uri;
    private String version;
    private String contentType;
    private String body;
    private int length;
}
