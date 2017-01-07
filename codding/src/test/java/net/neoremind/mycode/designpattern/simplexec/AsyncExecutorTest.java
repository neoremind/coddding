package net.neoremind.mycode.designpattern.simplexec;

import fj.F;
import fj.control.parallel.ParModule;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.List;
import fj.data.Option;
import fj.data.optic.Optional;
import net.neoremind.mycode.concurrent.juc.lock.Backoff;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static fj.data.List.list;

/**
 * Created by helechen on 2017/1/7.
 */
public class AsyncExecutorTest {

    @Test
    public void test() {
        AsyncExecutor<String> executor = new SerialExecutorHandler<>();
        AsyncResult<String> result = executor.run(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "haha";
            }
        });
        System.out.println(result.getResult());
    }

    @Test
    public void test2() {
        AsyncExecutor<String> executor = new AsyncExecutorHandler<>();
        AsyncResult<String> result = executor.run(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "haha";
            }
        });
        System.out.println(result.getResult());
    }

    @Test
    public void test3() {
        final List<Pipeline> words = list(new Pipeline(1, "abc"),
                new Pipeline(2, "abcdd"),
                new Pipeline(3, "abcfffff"),
                new Pipeline(4, "abceeeeeee"));

        words.forall(new F<Pipeline, Boolean>() {
            @Override
            public Boolean f(Pipeline pipeline) {
                System.out.println(pipeline.id);
                return Boolean.TRUE;
            }
        });
    }

    @Test
    public void test4() {
        Backoff backoff = new Backoff(10000, 20000);
        final List<Pipeline> words = list(new Pipeline(1, "abc"),
                new Pipeline(2, "abcdd"),
                new Pipeline(3, "abcfffff"),
                new Pipeline(4, "abceeeeeee"));
        final ExecutorService pool = Executors.newFixedThreadPool(5);
        ParModule parModule = ParModule.parModule(Strategy.executorStrategy(pool));
        Promise<List<String>> promise = parModule.parMap(words, new F<Pipeline, String>() {
            @Override
            public String f(Pipeline pipeline) {
                try {
                    backoff.backoff();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return pipeline.name;
            }
        });
        Option<List<String>> res = promise.claim(30, TimeUnit.SECONDS);
        if (res.isNone()) {
            throw new RuntimeException("no result got");
        }
        System.out.println(res.some());
    }

    class Pipeline {
        int id;

        String name;

        public Pipeline(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
