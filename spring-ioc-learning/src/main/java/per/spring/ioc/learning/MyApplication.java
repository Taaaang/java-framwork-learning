package per.spring.ioc.learning;

import per.spring.ioc.learning.context.MyBeanFactory;
import per.spring.ioc.learning.processor.MyBeanFactoryAware;
import per.spring.ioc.learning.processor.MySmartBeanPostProcessor;
import per.spring.ioc.learning.service.MyOrder;
import per.spring.ioc.learning.service.MyService;
import per.spring.ioc.learning.support.MyBeanDefinition;
import per.spring.ioc.learning.support.MyDefinitionScanner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyApplication {

    /**
     * 加载框架需要的基础类
     */
    private static final String[] SPRING_FACTORIES
            =new String[]{
                    "per.spring.ioc.learning.processor.MyBaseBeanDefinitionPostProcessor",//用来扫描包下面的类
                    "per.spring.ioc.learning.processor.MyAnnotationBeanDefinitionPostProcessor", //用来加载被注解标识的类
                    "per.spring.ioc.learning.processor.MyAutowiredPostProcessor"};//用来处理自动装配的成员变量


    public MyBeanFactory run(){
        //创建扫描器
        MyDefinitionScanner scanner=new MyDefinitionScanner();
        //创建对应的容器信息
        MyBeanFactory beanFactory=new MyBeanFactory(scanner);
        //准备
        prepareContext(beanFactory);
        //创建
        createContext(beanFactory);
        //通知容器创建完毕
        prepareBeanDefinitionPostProcessor(beanFactory);
        //刷新容器
        refreshContext(beanFactory);

        return beanFactory;
    }

    public void prepareContext(MyBeanFactory beanFactory){
        //加载指定的类信息
        Set<MyBeanDefinition> myBeanDefinitions = beanFactory.getScanner().loadClass(SPRING_FACTORIES);
        ConcurrentHashMap<String, MyBeanDefinition> beanDefinitionConcurrentHashMap = beanFactory.getBeanDefinitionConcurrentHashMap();
        for (MyBeanDefinition myBeanDefinition : myBeanDefinitions) {
            beanDefinitionConcurrentHashMap.put((String)myBeanDefinition.getBeanClass(),myBeanDefinition);
        }
        for (String springFactory : SPRING_FACTORIES) {
            beanFactory.getBean(springFactory);
        }
    }

    public void prepareBeanDefinitionPostProcessor(MyBeanFactory beanFactory){
        //进行容器创建完毕的通知
        List<MyBeanFactoryAware> beanByType = beanFactory.getBeanByType(MyBeanFactoryAware.class);
        beanByType.forEach(v->{
            v.setBeanFactory(beanFactory);
        });
    }

    public void createContext(MyBeanFactory myBeanFactory){
        //无
    }

    public void refreshContext(MyBeanFactory myBeanFactory){
        //根据Bean定义创建Bean
        for (MyBeanDefinition beanDefinition : myBeanFactory.getSingletonList()) {
            //创建bean
            Object bean=myBeanFactory.getBean(beanDefinition);
            //进行bean创建完毕的通知，自动装配就在这里处理
            List<MySmartBeanPostProcessor> beanPostProcessors = myBeanFactory.getBeanByType(MySmartBeanPostProcessor.class);
            beanPostProcessors.forEach(v->v.postProcessProperties(bean));
        }
    }

    public static void main(String[] args) {
        MyApplication application=new MyApplication();
        MyBeanFactory beanFactory = application.run();
        MyOrder bean = (MyOrder)beanFactory.getBean("per.spring.ioc.learning.service.MyOrder");
        bean.sayInOrder();
    }

}
