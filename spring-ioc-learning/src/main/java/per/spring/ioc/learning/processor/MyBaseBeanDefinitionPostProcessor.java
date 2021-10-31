package per.spring.ioc.learning.processor;

import per.spring.ioc.learning.annotation.MyComponent;
import per.spring.ioc.learning.context.MyBeanFactory;
import per.spring.ioc.learning.support.MyBeanDefinition;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyBaseBeanDefinitionPostProcessor implements MyBeanFactoryAware {

    private MyBeanFactory beanFactory;

    private static final String packages="per.spring.ioc.learning";

    public void postProcessBeanDefinitionRegistry(){
        //扫描路径下的所有Bean
        Set<MyBeanDefinition> myBeanDefinitions = beanFactory.getScanner().doScan(packages);
        //将bean定义放入容器中
        ConcurrentHashMap<String, MyBeanDefinition> beanDefinitionConcurrentHashMap = beanFactory.getBeanDefinitionConcurrentHashMap();
        myBeanDefinitions.forEach(k->{
            beanDefinitionConcurrentHashMap.put((String) k.getBeanClass(),k);
        });
    }

    public void setBeanFactory(MyBeanFactory beanFactory) {

        this.beanFactory=beanFactory;

        postProcessBeanDefinitionRegistry();
    }
}
