package net.neoremind.mycode.javassist.impl;

import net.neoremind.mycode.javassist.HelloService;

/**
 * @author zhangxu
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hi(String input) {
        return input;
    }
}
