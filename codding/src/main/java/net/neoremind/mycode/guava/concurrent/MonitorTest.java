package net.neoremind.mycode.guava.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.util.concurrent.Monitor;

/**
 * <pre>
 * monitor.enter()
 * try{
 * ...work..
 * }finally{
 * monitor.leave();
 * }
 * </pre>
 * <p>
 * <pre>
 * if(monitor.enterIf(guard)){
 * try{
 * ...work..
 * }finally{
 * monitor.leave();
 * }
 * }else{
 * .. monitor not available..
 * }
 * </pre>
 * http://codingjunkie.net/google-guava-synchronization-with-monitor/
 *
 * @author zhangxu
 */
public class MonitorTest {

    private List<String> list = new ArrayList<String>();
    private static final int MAX_SIZE = 10;

    private Monitor monitor = new Monitor();
    private Monitor.Guard listBelowCapacity = new Monitor.Guard(monitor) {
        @Override
        public boolean isSatisfied() {
            return (list.size() < MAX_SIZE);
        }
    };

    public void addToList(String item) throws InterruptedException {
        monitor.enterWhen(listBelowCapacity);
        try {
            list.add(item);
        } finally {
            monitor.leave();
        }
    }

    private ReentrantLock rLock = new ReentrantLock();
    private Condition notFull = rLock.newCondition();

    public void addToList2(String item) throws InterruptedException {
        rLock.lock();
        try {
            while (list.size() == MAX_SIZE) {
                notFull.await();
            }
            list.add(item);
        } finally {
            rLock.unlock();
        }
    }

}
