package net.neoremind.mycode.designpattern.filterchain;

/**
 * @author zhangxu
 */
public class Request {

    private String req;

    @Override
    public String toString() {
        return "Request{" +
                "req='" + req + '\'' +
                '}';
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
}
