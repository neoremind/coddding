package net.neoremind.mycode.designpattern.filterchain;

/**
 * @author zhangxu
 */
public class Response {

    private String res;

    @Override
    public String toString() {
        return "Response{" +
                "res='" + res + '\'' +
                '}';
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
