package per.spring.ioc.learning.processor;

import per.spring.ioc.learning.context.MyBeanFactory;

/**
 * beanFactory的通知接口
 */
public interface MyBeanFactoryAware {

    /**
     * 在BeanFactory容器创建完毕后，调用该方法传入容器
     * @param beanFactory
     */
    void setBeanFactory(MyBeanFactory beanFactory);
}
