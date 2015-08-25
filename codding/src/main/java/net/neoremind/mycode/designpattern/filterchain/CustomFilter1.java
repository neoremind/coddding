package net.neoremind.mycode.designpattern.filterchain;

/**
 * @author zhangxu
 */
public class CustomFilter1 implements Filter {

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
