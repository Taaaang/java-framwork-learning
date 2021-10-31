package per.spring.ioc.learning.annotation;

import java.lang.annotation.*;

/**
 * 参考spring的component注解，主要是标注哪些类是需要被托管的
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyComponent {
}
