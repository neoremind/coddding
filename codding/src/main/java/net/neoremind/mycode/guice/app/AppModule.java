package net.neoremind.mycode.guice.app;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import net.neoremind.mycode.guice.annotation.NS2;
import net.neoremind.mycode.guice.item.ItemService;
import net.neoremind.mycode.guice.item.ItemServiceImpl1;
import net.neoremind.mycode.guice.named.NamedService;
import net.neoremind.mycode.guice.named.NamedServiceImpl1;
import net.neoremind.mycode.guice.named.NamedServiceImpl2;
import net.neoremind.mycode.guice.order.OrderService;
import net.neoremind.mycode.guice.order.OrderServiceImpl;
import net.neoremind.mycode.guice.price.PriceService;
import net.neoremind.mycode.guice.runtime.RuntimeService;
import net.neoremind.mycode.guice.runtime.RuntimeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.matcher.Matchers.any;
import static net.neoremind.mycode.guice.app.ExceptionMethodInterceptor.exception;


public class AppModule extends AbstractModule implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppModule.class);

    private final RuntimeServiceImpl runtimeService;

    public AppModule(RuntimeServiceImpl runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void configure() {
        final Binder binder = binder();
        if (LOGGER.isDebugEnabled()) {
            binder.bindInterceptor(any(), any(), exception());
        }
        //TODO: bind named instance;
        binder.bind(NamedService.class).annotatedWith(Names.named("impl1")).to(NamedServiceImpl1.class);
        binder.bind(NamedService.class).annotatedWith(NS2.class).to(NamedServiceImpl2.class);

        //TODO: bind interface
        binder.bind(OrderService.class).to(OrderServiceImpl.class).in(SINGLETON);
        binder.bind(ItemService.class).to(ItemServiceImpl1.class).in(Scopes.SINGLETON);
        binder.bind(PriceService.class).in(Scopes.SINGLETON);

        //TODO: bind instance not class.
        binder.bind(RuntimeService.class).toInstance(runtimeService);

    }

}
