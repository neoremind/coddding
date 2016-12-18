package net.neoremind.mycode.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.neoremind.mycode.guice.app.AppModule;
import net.neoremind.mycode.guice.order.Order;
import net.neoremind.mycode.guice.order.OrderService;
import net.neoremind.mycode.guice.order.OrderServiceImpl;
import net.neoremind.mycode.guice.price.PriceService;
import net.neoremind.mycode.guice.runtime.RuntimeService;
import net.neoremind.mycode.guice.runtime.RuntimeServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by xu.zhang on 12/18/16.
 */
public class AppModuleTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AppModule(new RuntimeServiceImpl()));
    }

    @Test
    public void should_get_order_service_from_guice_module() throws Exception {
        final OrderService instance = injector.getInstance(OrderService.class);
        assertThat(instance, is(instanceOf(OrderServiceImpl.class)));
        instance.add(new Order(100));
    }


    @Test
    public void should_register_service_runtime() throws Exception {
        final RuntimeService instance = injector.getInstance(RuntimeService.class);
        assertThat(instance, is(instanceOf(RuntimeServiceImpl.class)));
    }

    @Test
    public void should_be_singleton_for_one_without_interface_bean() throws Exception {
        final PriceService first = injector.getInstance(PriceService.class);
        final PriceService second = injector.getInstance(PriceService.class);
        assertThat(first, is(sameInstance(second)));
    }

}
