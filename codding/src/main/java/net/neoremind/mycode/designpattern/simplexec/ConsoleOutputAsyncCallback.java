package net.neoremind.mycode.designpattern.simplexec;

import com.google.common.base.Optional;

/**
 * Created by helechen on 2017/1/7.
 */
public class ConsoleOutputAsyncCallback<T> implements AsyncCallback<T> {

    @Override
    public void onComplete(T value, Optional<Exception> ex) {
        if (ex.isPresent()) {
            System.err.println("Exception occurred: " + ex.get().getMessage());
        } else {
            System.out.println("Get value: " + value);
        }
    }

}
