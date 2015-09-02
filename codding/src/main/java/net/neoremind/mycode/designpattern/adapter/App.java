package net.neoremind.mycode.designpattern.adapter;

public class App {

    public static void main(String[] args) {
        Engineer manager = new GnomeEngineeringManager();
        manager.operateDevice();
    }
}
