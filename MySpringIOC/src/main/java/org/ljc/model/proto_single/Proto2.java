package org.ljc.model.proto_single;

import org.ljc.annotations.MyAutowired;
import org.ljc.annotations.MyComponent;
import org.ljc.annotations.MyScope;
import org.ljc.bean.BeanDefinition;

@MyComponent
@MyScope(BeanDefinition.SCOPE_PROTOTYPE)
public class Proto2 {

    @MyAutowired
    private Single4 single4;

    public void hello() {
        single4.hello();
    }

    public void hello_proto2() {
        System.out.println("hello proto2");
    }
}
