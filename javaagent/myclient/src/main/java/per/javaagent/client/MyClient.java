package per.javaagent.client;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/14 - 3:14 下午
 **/
public class MyClient {

    public void baseMethod(Arguments arguments){

        System.out.println("进行基础信息处理！");

    }

    public void businessMethod(Arguments arguments){
        System.out.println("进行业务处理！");
    }

    @Data
    public static class Arguments{
        private String name;
        private Integer age;
    }

    public static void main(String[] args) {
        HashMap<Object,String> hashMap=new HashMap<>();
        hashMap.put("ABC","11");
        Object object;
        object="abc";
        System.out.println(object.getClass());
        System.out.println(Integer.toBinaryString(object.hashCode()));
    }

}
