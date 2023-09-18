package org.ljc;

import org.ljc.annotations.MyComponentScan;
import org.ljc.context.AppContext;
import org.ljc.model.proto_single.Proto2;
import org.ljc.model.single_proto.Single3;

@MyComponentScan
public class MainApp {
    public static void main(String[] args) {
        AppContext appContext = new AppContext(MainApp.class);
        /*System.out.println(appContext.getBean("single1"));
        System.out.println(appContext.getBean("single1"));
        System.out.println(appContext.getBean("single2"));
        System.out.println(appContext.getBean("single2"));
        ((Single1) appContext.getBean("single1")).hello2();
        ((Single2) appContext.getBean("single2")).hello();*/

        ((Proto2) appContext.getBean("proto2")).hello();
        ((Proto2) appContext.getBean("proto2")).hello();
    }
}
