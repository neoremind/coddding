package net.neoremind.mycode.guice.order;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.neoremind.mycode.guice.item.ItemService;
import net.neoremind.mycode.guice.named.NamedService;
import net.neoremind.mycode.guice.price.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Inject
    private ItemService itemService;

    @Inject
    private PriceService priceService;

    @Inject
    @Named("impl1")
    private NamedService namedService;

    @Override
    public void add(Order order) {
        itemService.get(order.getId());
        priceService.getPrice();
    }

    @Override
    public void remove(Order order) {
    }

    @Override
    public Order get(int id) {
        itemService.get(id);
        return new Order(id);
    }

}
