package per.spring.ioc.learning.context;

import per.spring.ioc.learning.support.MyBeanDefinition;
import per.spring.ioc.learning.support.MyDefinitionScanner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyBeanFactory {

    private MyDefinitionScanner scanner;

    //key:类全限定名称，value：bean定义
    private ConcurrentHashMap<String, MyBeanDefinition> beanDefinitionConcurrentHashMap;

    //key:类名称，value：实例化的bean
    private ConcurrentHashMap<String,Object> beanMap;

    //key：接口,value:全限定名
    private ConcurrentHashMap<Class, CopyOnWriteArrayList<Object>> singletonNameByType;

    //需要构建的应用Bean
    private CopyOnWriteArrayList<MyBeanDefinition> singletonList;

    public MyBeanFactory(MyDefinitionScanner scanner){
        beanMap=new ConcurrentHashMap<String, Object>(256);
        beanDefinitionConcurrentHashMap=new ConcurrentHashMap<String, MyBeanDefinition>(256);
        singletonNameByType=new ConcurrentHashMap<Class, CopyOnWriteArrayList<Object>>(256);
        singletonList=new CopyOnWriteArrayList<MyBeanDefinition>();
        this.scanner=scanner;
    }

    public Object getBean(String beanName){
        Object o = beanMap.get(beanName);
        //如果为空，代表容器中没有，需要走创建流程
        if(o==null){
            //获取bean定义
            MyBeanDefinition beanDefinition = beanDefinitionConcurrentHashMap.get(beanName);
            if(beanDefinition==null){
                throw new RuntimeException(String.format("not found bean definition [%s]",beanName));
            }
            //构造Bean对象
            Object beanClass = beanDefinition.getBeanClass();
            Class<?> aClass=null;
            if(beanClass instanceof String){
                try {
                    aClass = Class.forName((String) beanDefinition.getBeanClass());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(String.format("can not found class [%s] , beanName [%s]",(String)beanDefinition.getBeanClass(),beanName));
                }
            }else if(beanClass instanceof Class){
                aClass= ((Class) beanClass);
            }else {
                throw new RuntimeException(String.format("beanClass type error ! beanName [%s]",beanName));
            }

            if(aClass!=null){
                try {
                    o = aClass.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(String.format("class[%s] constructed failed!",beanName));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(String.format("class[%s] constructed failed!",beanName));
                }
                beanMap.put(beanName,o);
                putByClass(o,beanDefinition);
            }
        }
        return o;
    }

    public Object getBean(MyBeanDefinition beanDefinition){
        //获取bean定义
        String beanName=beanDefinition.getBeanClass() instanceof Class?
                ((Class)beanDefinition.getBeanClass()).getName()
                :(String) beanDefinition.getBeanClass();
        Object o=beanMap.get(beanName);
        if(o!=null){
            return o;
        }
        if(beanDefinition==null){
            throw new RuntimeException(String.format("not found bean definition [%s]",beanName));
        }
        //构造Bean对象
        Object beanClass = beanDefinition.getBeanClass();
        Class<?> aClass=null;
        if(beanClass instanceof String){
            try {
                aClass = Class.forName((String) beanDefinition.getBeanClass());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(String.format("can not found class [%s] , beanName [%s]",(String)beanDefinition.getBeanClass(),beanName));
            }
        }else if(beanClass instanceof Class){
            aClass= ((Class) beanClass);
        }else {
            throw new RuntimeException(String.format("beanClass type error ! beanName [%s]",beanName));
        }

        if(aClass!=null){
            try {
                o = aClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(String.format("class[%s] constructed failed!",beanName));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("class[%s] constructed failed!",beanName));
            }
            beanMap.put(beanName,o);
            putByClass(o,beanDefinition);
        }
        return o;
    }

    public <T> List<T> getBeanByType(Class<T> type){
        return (List<T>)singletonNameByType.get(type);
    }

    private void putByClass(Object o,MyBeanDefinition beanDefinition){
        Class<?>[] interfaces = o.getClass().getInterfaces();
        for (Class<?> aClass : interfaces) {
            CopyOnWriteArrayList<Object> myBeanDefinitions = singletonNameByType.get(aClass);
            if(myBeanDefinitions!=null){
                myBeanDefinitions.add(o);
            }else {
                myBeanDefinitions=new CopyOnWriteArrayList<Object>();
                myBeanDefinitions.add(o);
                singletonNameByType.put(aClass,myBeanDefinitions);
            }
        }

    }


    public MyDefinitionScanner getScanner(){
        return this.scanner;
    }

    public CopyOnWriteArrayList<MyBeanDefinition> getSingletonList(){
        return this.singletonList;
    }

    public ConcurrentHashMap<String, MyBeanDefinition> getBeanDefinitionConcurrentHashMap(){
        return this.beanDefinitionConcurrentHashMap;
    }
}
