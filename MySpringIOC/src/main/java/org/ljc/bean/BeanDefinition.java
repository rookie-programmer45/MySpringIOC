package org.ljc.bean;

import org.ljc.annotations.MyScope;

public class BeanDefinition {
    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";

    private Class beanClass;

    private String beanName;

    private String scope;

    private BeanDefinition() {
    }

    public static BeanDefinition buildBeanDefinition(Class<?> beanClass, String beanName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setBeanName(beanName);
        if (beanClass.isAnnotationPresent(MyScope.class)) { // TODO: 这里应该对MyScope的值进行合法性检查
            beanDefinition.setScope(beanClass.getAnnotation(MyScope.class).value());
        } else {
            beanDefinition.setScope(SCOPE_SINGLETON);
        }
        return beanDefinition;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
