package net.neoremind.mycode.designpattern.filterchain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangxu
 */
public class FilterBuilder {

    /**
     * 构造调用链
     */
    public static MyService buildFilterChain(MyService myService) {
        MyService last = myService;
        List<Filter> filters = Arrays.asList(getAllFilters());
        Collections.reverse(filters);
        for (final Filter filter : filters) {
            final MyService next = last;
            last = new MyService() {
                @Override
                public Response doAction(Request request) {
                    return filter.doChain(next, request);
                }
            };
            System.out.println("Place " + filter.getClass().getName() + " filter to core processor");
        }

        return last;
    }

    private static Filter[] getAllFilters() {
        return new Filter[] {new CustomFilter1(), new CustomFilter2()};
    }

}

