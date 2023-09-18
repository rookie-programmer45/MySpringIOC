package org.ljc.annotations;

import org.ljc.bean.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyScope {
    String value() default BeanDefinition.SCOPE_SINGLETON;
}
