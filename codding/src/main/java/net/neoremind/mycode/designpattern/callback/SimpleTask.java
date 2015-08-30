package net.neoremind.mycode.designpattern.callback;

/**
 * Implementation of task that need to be executed
 */
public class SimpleTask extends Task {

    @Override
    public void execute() {
        System.out.println("Perform some important activity.");
    }

}
