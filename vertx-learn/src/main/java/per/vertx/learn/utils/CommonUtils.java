package per.vertx.learn.utils;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/5/12 - 2:34 下午
 **/
public class CommonUtils {

    private static String ENV="env";

    public static String getEnv(){
        return System.getProperty(ENV);
    }

    public static void setEnv(String env){
        System.setProperty(ENV,env);
    }

    public static String getEnvName(){
        String env = System.getProperty(ENV);
        switch (env){
            case "test":
                return "测试";
            case "pre":
                return "预发";
            case "pub":
                return "线上";
            default:
                return "未知环境";
        }
    }

}
