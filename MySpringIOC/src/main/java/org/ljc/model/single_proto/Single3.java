package org.ljc.model.single_proto;

import org.ljc.annotations.MyAutowired;
import org.ljc.annotations.MyComponent;

@MyComponent
public class Single3 {
    @MyAutowired
    private Proto1 proto1;

    public void hello() {
        System.out.println("hello single1");
    }

    public void hello2() {
        proto1.hello_proto();
    }
}
