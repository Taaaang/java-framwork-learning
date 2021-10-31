package per.spring.ioc.learning.service;

import per.spring.ioc.learning.annotation.MyAutowire;
import per.spring.ioc.learning.annotation.MyComponent;

@MyComponent
public class MyOrder {

    @MyAutowire
    private MyService myService;

    public void sayInOrder(){
        System.out.println("这是订单");
        System.out.println("我要访问服务！");
        myService.say();
    }

}
