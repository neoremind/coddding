package net.neoremind.mycode.nio.nio.netty.protocol;

public class PbrpcMsg {

    private int id;

    private String name;
    
    public String toString() {
        return "PbrpcMsg=>id:" + id + ", name=" + name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
