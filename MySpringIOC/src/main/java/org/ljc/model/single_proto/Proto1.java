package org.ljc.model.single_proto;

import org.ljc.annotations.MyComponent;
import org.ljc.annotations.MyScope;
import org.ljc.bean.BeanDefinition;

@MyComponent
@MyScope(BeanDefinition.SCOPE_PROTOTYPE)
public class Proto1 {

    public void hello_proto() {
        System.out.println("hello proto1");
    }
}
