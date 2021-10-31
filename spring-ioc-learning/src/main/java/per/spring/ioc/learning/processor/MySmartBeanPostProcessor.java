package per.spring.ioc.learning.processor;

public interface MySmartBeanPostProcessor {

    /**
     * 在bean创建完毕后，调用该方法，并传入此前创建的bean
     * @param bean
     */
    void postProcessProperties( Object bean);

}
