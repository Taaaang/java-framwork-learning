package per.spring.ioc.learning.test;

import per.spring.ioc.learning.support.MyBeanDefinition;
import per.spring.ioc.learning.support.MyDefinitionScanner;

import java.util.Set;

public class MyDefinitionScannerTest {

    public static void main(String[] args) {
        MyDefinitionScanner scanner=new MyDefinitionScanner();
        String servicePackage="per.spring.ioc.learning.service";
        Set<MyBeanDefinition> myBeanDefinitions = scanner.doScan(servicePackage);
        System.out.println();
    }

}
