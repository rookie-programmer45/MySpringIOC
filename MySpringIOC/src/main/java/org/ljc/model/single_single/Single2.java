package org.ljc.model.single_single;

import org.ljc.annotations.MyAutowired;
import org.ljc.annotations.MyComponent;

@MyComponent
public class Single2 {
    @MyAutowired
    private Single1 single1;

    public void hello() {
        single1.hello();
    }

    public void hello2() {
        System.out.println("hello single2");
    }
}
