package per.spring.ioc.learning.processor;

import per.spring.ioc.learning.annotation.MyComponent;
import per.spring.ioc.learning.context.MyBeanFactory;
import per.spring.ioc.learning.support.MyBeanDefinition;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class MyAnnotationBeanDefinitionPostProcessor implements MyBeanFactoryAware {


    private MyBeanFactory beanFactory;

    public void postProcessBeanDefinitionRegistry(){
        //获取到所有的bean定义
        ConcurrentHashMap<String, MyBeanDefinition> beanDefinitionConcurrentHashMap = beanFactory.getBeanDefinitionConcurrentHashMap();
        CopyOnWriteArrayList<MyBeanDefinition> singletonList = beanFactory.getSingletonList();
        beanDefinitionConcurrentHashMap.forEach((k,v)->{
            for (Annotation annotation : v.getAnnotations()) {
                if(annotation.annotationType().isAssignableFrom(MyComponent.class)){
                    //将该注解下的BeanDefinition放入需要创建Bean容器中
                    singletonList.add(v);
                }
            }
        });
    }


    public void setBeanFactory(MyBeanFactory beanFactory) {

        this.beanFactory=beanFactory;

        postProcessBeanDefinitionRegistry();
    }
}
