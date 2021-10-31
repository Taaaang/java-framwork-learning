package per.spring.ioc.learning.support;

import org.springframework.util.Assert;
import per.spring.ioc.learning.annotation.MyComponent;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MyDefinitionScanner {

    //java的源文件后缀
    private static final String CLASS_FILE_SUFFIX="class";

    /**
     * 扫描指定包路径集合下的文件
     * @param basePackages 包路径集合
     * @return 生成bean定义
     */
    public Set<MyBeanDefinition> doScan(String... basePackages){
        Assert.notEmpty(basePackages,"at least one base package must be specified");
        Set<MyBeanDefinition> myBeanDefinitions=new HashSet<MyBeanDefinition>();
        String[] packages=basePackages;
        int packageLength=packages.length;
        for (int i = 0; i < packageLength; i++) {
            String pkg=packages[i];
            try {
                myBeanDefinitions.addAll(scanPackage(pkg,myBeanDefinitions));
            } catch (IOException e) {
                throw new RuntimeException(String.format("In base package [%s] To load java file failed !",pkg));
            }
        }
        return myBeanDefinitions;
    }

    private Set<MyBeanDefinition> scanPackage(String basePackage,Set<MyBeanDefinition> myBeanDefinitions) throws IOException {
        //查找basePackage下所有的文件路径
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources(basePackage.replace(".", "/"));
        //循环查找路径下所有的java文件并构建成BeanDefinition
        while(resources.hasMoreElements()){
            URL url = resources.nextElement();
            File basePackageFiles=new File(url.getPath());
            File[] files = basePackageFiles.listFiles();
            for (File file : files) {
                //如果是文件夹，则继续向下扫描
                if(file.isDirectory()){
                    scanPackage(basePackage+"."+file.getName(),myBeanDefinitions);
                }else if(file.isFile()){
                    //如果是java文件,则装配为BeanDefinition对象
                    String[] split = file.getName().split("\\.");
                    if(split[split.length-1].equals(CLASS_FILE_SUFFIX)) {
                        myBeanDefinitions.add(buildBeanDefinition(basePackage + "." + split[0]));
                    }
                }
            }
        }

        return myBeanDefinitions;
    }

    public Set<MyBeanDefinition> loadClass(String path){
        Set<MyBeanDefinition> beanDefinitions=new HashSet<>();
        beanDefinitions.add(buildBeanDefinition(path));
        return beanDefinitions;
    }

    /**
     * 扫描指定全限定名的文件
     * @param path 全限定名
     * @return 生成bean定义
     */
    public Set<MyBeanDefinition> loadClass(String... path){
        Set<MyBeanDefinition> beanDefinitions=new HashSet<>();
        for (String s : path) {
            beanDefinitions.add(buildBeanDefinition(s));
        }
        return beanDefinitions;
    }

    /**
     * 根据全限定名,组装bean定义
     * @param clazzName
     * @return
     */
    private MyBeanDefinition buildBeanDefinition(String clazzName){
        try {
            Class<?> aClass = Class.forName(clazzName);
            Annotation[] annotations = aClass.getAnnotations();
            MyBeanDefinition beanDefinition=new MyBeanDefinition(clazzName,annotations,false);
            return beanDefinition;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("not found class [%s]",clazzName));
        }
    }

}
