package per.vertx.learn.server.remote.entity.server;

import lombok.Data;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 2:46 下午
 **/
@Data
public class HttpResponse<T> {

    private String code;
    private String message;
    private T data;
    private Long timestamp;

    private HttpResponse() {
    }
    private static final String SUCCESS="1";
    private static final String ERROR="500";
    public static <K> HttpResponse<K> success(){
        return success(null);
    }

    public static <K> HttpResponse<K> success(K date){
        HttpResponse<K> httpResponse = new HttpResponse<>();
        httpResponse.setCode(SUCCESS);
        httpResponse.setData(date);
        httpResponse.setTimestamp(System.currentTimeMillis());
        return httpResponse;
    }

    public static  HttpResponse failure(){
        return failure(null);
    }

    public static  HttpResponse failure(String message){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setCode(ERROR);
        httpResponse.setData(null);
        httpResponse.setMessage(message);
        httpResponse.setTimestamp(System.currentTimeMillis());
        return httpResponse;
    }
}
