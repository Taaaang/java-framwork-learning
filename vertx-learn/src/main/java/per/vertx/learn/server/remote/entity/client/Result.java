package per.vertx.learn.server.remote.entity.client;

import lombok.Data;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/14 - 5:55 下午
 **/
@Data
public class Result<T> {
    private boolean success;
    private T data;
    private String msg;
    private String code;
    private String traceId;
    private static final String SUCCESS_MSG = "success";
    public static final String SUCCESS_CODE = "200";
    private static final String FAIL_CODE = "500";

    private Result(boolean success, T data, String msg, String code) {
        this.success = success;
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public static <T> Result<T> success(T data) {
        return new Result(true, data, "success", "200");
    }

    public static <T> Result<T> fail(String msg) {
        return fail(msg, "500");
    }

    public static <T> Result<T> fail(String msg, String code) {
        return new Result(false, (Object)null, msg, code);
    }

    public boolean isSuccess() {
        return this.success;
    }

}
