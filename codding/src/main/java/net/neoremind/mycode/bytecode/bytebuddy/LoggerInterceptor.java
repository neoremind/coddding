package net.neoremind.mycode.bytecode.bytebuddy;

import java.util.List;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.SuperCall;

/**
 * @author zhangxu
 */
public class LoggerInterceptor {

    public static List<String> log(@SuperCall Callable<List<String>> zuper)
            throws Exception {
        System.out.println("Calling database");
        try {
            return zuper.call();
        } finally {
            System.out.println("Returned from database");
        }
    }

}
