package per.spring.ioc.learning.processor;

import per.spring.ioc.learning.annotation.MyAutowire;
import per.spring.ioc.learning.context.MyBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MyAutowiredPostProcessor implements MySmartBeanPostProcessor,MyBeanFactoryAware {

    private MyBeanFactory myBeanFactory;

    @Override
    public void postProcessProperties(Object bean) {
        //获取所有成员变量
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        //遍历查询成员
        for (Field declaredField : declaredFields) {
            //遍历成员上的注解
            for (Annotation declaredAnnotation : declaredField.getDeclaredAnnotations()) {
                if(declaredAnnotation.annotationType().isAssignableFrom(MyAutowire.class)){
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(bean,myBeanFactory.getBean(declaredField.getType().getName()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(String.format("inject field failed! source bean[%s], target bean[%]",bean.getClass().getName(),declaredField.getType().getName()));
                    }
                }
            }
            declaredField.getAnnotatedType().getType();
        }
    }

    @Override
    public void setBeanFactory(MyBeanFactory beanFactory) {
        this.myBeanFactory=beanFactory;
    }
}
