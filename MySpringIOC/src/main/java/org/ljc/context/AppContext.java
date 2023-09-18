package org.ljc.context;

import org.ljc.annotations.MyAutowired;
import org.ljc.annotations.MyComponentScan;
import org.ljc.bean.BeanDefinition;
import org.ljc.bean.MyBeanPostProcessor;
import org.ljc.bean.MyInitializingBean;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppContext {
    private Map<String, BeanDefinition> beanDefinitionMap;

    // 一级缓存，存放已生成的单例bean
    private Map<String, Object> singletonObjects = new HashMap<>();

    // 二级缓存，存放当前正在创建的bean，主要用于解决循环依赖问题
    private Map<String, Object> creatingBeanMap = new HashMap<>();

    private List<MyBeanPostProcessor> myBeanPostProcessors = new ArrayList<>();

    public AppContext(Class<?> starter) {
        try {
            beanDefinitionMap = ScanSupport.scan(getScanClassPath(starter));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        instance();
    }

    /**
     * 获取要扫描的路径
     * @param starter
     * @return
     */
    private String getScanClassPath(Class<?> starter) {
        return starter.isAnnotationPresent(MyComponentScan.class) ?
                starter.getAnnotation(MyComponentScan.class).value() :
                starter.getPackageName();
    }

    private void instance() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            String beanName = entry.getKey();
            if (beanDefinition.isSingleton()) {
                getSingleton(beanName);
            }
        }
    }

    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new RuntimeException(beanName + "is not exist!");
        }

        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        } else if (creatingBeanMap.containsKey(beanName)) {
            return creatingBeanMap.get(beanName);
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition.isSingleton()) {
            return getSingleton(beanName);
        } else if (beanDefinition.isPrototype()) {
            return createBean(beanName);
        } else {
            throw new RuntimeException("unsupport scope: " + beanDefinition.getScope() + ", beanName: " + beanName);
        }
    }

    private Object createBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        try {
            Object beanInstance = beanDefinition.getBeanClass().getConstructor().newInstance();
            creatingBeanMap.put(beanName, beanInstance);
            populateBean(beanName, beanDefinition, beanInstance);
            beanInstance = initializeBean(beanName, beanInstance, beanDefinition);
            creatingBeanMap.remove(beanName);
            return beanInstance;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("create bean error, bean name=" + beanName);
        }
    }

    private Object getSingleton(String beanName) {
        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        }
        Object bean = createBean(beanName);
        singletonObjects.put(beanName, bean);
        return bean;
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, Object beanInstance) throws IllegalAccessException {
        for (Field declaredField : beanDefinition.getBeanClass().getDeclaredFields()) {
            if (!declaredField.isAnnotationPresent(MyAutowired.class)) {
                continue;
            }
            autowireBean(declaredField, beanInstance);
        }
    }

    private void autowireBean(Field field, Object beanInstance) throws IllegalAccessException {
        MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
        String autowireBeanName = myAutowired.value().equals("") ? field.getName() : myAutowired.value();
        field.setAccessible(true);
        field.set(beanInstance, getBean(autowireBeanName));
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        Object wrappedBean = bean;
        wrappedBean = applyBeanPostProcessBeforeInitialization(wrappedBean, beanName);
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        wrappedBean = applyBeanPostProcessAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    private Object applyBeanPostProcessBeforeInitialization(Object bean, String beanName) {
        Object result = bean;
        for (MyBeanPostProcessor myBeanPostProcessor : myBeanPostProcessors) {
            Object current = myBeanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    private Object applyBeanPostProcessAfterInitialization(Object bean, String beanName) {
        Object result = bean;
        for (MyBeanPostProcessor myBeanPostProcessor : myBeanPostProcessors) {
            Object current = myBeanPostProcessor.postProcessAfterInitialization(bean, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof MyInitializingBean) {
            ((MyInitializingBean) bean).afterPropertiesSet();
        }
    }
}
