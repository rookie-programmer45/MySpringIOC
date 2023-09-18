package org.ljc.model.single_single;

import org.ljc.annotations.MyAutowired;
import org.ljc.annotations.MyComponent;
import org.ljc.model.proto_single.Proto2;

@MyComponent
public class Single1 {
    @MyAutowired
    private Single2 single2;

    @MyAutowired
    private Proto2 proto2;

    public void hello() {
        System.out.println("hello single1");
    }

    public void hello2() {
        single2.hello2();
    }
}
