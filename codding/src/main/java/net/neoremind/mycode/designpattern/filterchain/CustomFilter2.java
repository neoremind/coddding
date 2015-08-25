package net.neoremind.mycode.designpattern.filterchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangxu
 */
public class CustomFilter2 implements Filter {

    @Override
    public Response doChain(MyService myService, Request request) {
        System.out.println("Here is pre - " + toString());
        Response response = myService.doAction(request);
        System.out.println("Here is post - " + toString());
        return response;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

}
