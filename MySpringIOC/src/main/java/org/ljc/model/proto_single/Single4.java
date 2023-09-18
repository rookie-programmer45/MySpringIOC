package org.ljc.model.proto_single;

import org.ljc.annotations.MyAutowired;
import org.ljc.annotations.MyComponent;
import org.ljc.model.single_single.Single1;

@MyComponent
public class Single4 {

    @MyAutowired
    private Single1 single1;

    public void hello() {
        System.out.println("hello single4");
    }

}
