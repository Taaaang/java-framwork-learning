package per.spring.ioc.learning.test;

import per.spring.ioc.learning.context.MyBeanFactory;
import per.spring.ioc.learning.support.MyBeanDefinition;
import per.spring.ioc.learning.support.MyDefinitionScanner;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactoryTest {

    public static void main(String[] args) {
        MyDefinitionScanner scanner=new MyDefinitionScanner();
        MyBeanFactory beanFactory=new MyBeanFactory(scanner);
        String servicePackage="per.spring.ioc.learning.service";
        Set<MyBeanDefinition> myBeanDefinitions = scanner.doScan(servicePackage);
        ConcurrentHashMap<String, MyBeanDefinition> beanDefinitionConcurrentHashMap = beanFactory.getBeanDefinitionConcurrentHashMap();
        for (MyBeanDefinition myBeanDefinition : myBeanDefinitions) {
            beanDefinitionConcurrentHashMap.put((String) myBeanDefinition.getBeanClass(),myBeanDefinition);
        }
        Object bean = beanFactory.getBean(servicePackage + ".MyService");
    }

}
