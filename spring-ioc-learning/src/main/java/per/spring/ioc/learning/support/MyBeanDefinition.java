package per.spring.ioc.learning.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * bean的定义类，主要是用于在运行前期准备时，解析后续创建实例Bean需要的一些信息，类似于Bean的模板信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyBeanDefinition {

    /**
     * Bean的类信息
     * 1.String，类的权限定名
     * 2.Class对象
     */
    private Object beanClass;

    /**
     * 类所拥有的注解
     */
    private Annotation[] annotations;

    /**
     * 是否是托管给容器的Bean信息
     */
    private boolean containsMyComponent;

}
