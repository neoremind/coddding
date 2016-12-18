package net.neoremind.mycode.guice.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemServiceImpl1 implements ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl1.class);

    @Override
    public Item get(int id) {
        return new Item();
    }
}
