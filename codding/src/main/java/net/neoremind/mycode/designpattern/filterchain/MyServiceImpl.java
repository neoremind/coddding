package net.neoremind.mycode.designpattern.filterchain;

/**
 * @author zhangxu
 */
public class MyServiceImpl implements MyService {

    @Override
    public Response doAction(Request request) {
        Response res = new Response();
        if (request == null || request.getReq() == null) {
            return res;
        }
        res.setRes(request.getReq().toUpperCase());
        return res;
    }

}
