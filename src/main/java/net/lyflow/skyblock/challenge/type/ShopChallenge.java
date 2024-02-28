package net.lyflow.skyblock.challenge.type;

import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.event.itemshop.ShopEvent;
import net.lyflow.skyblock.shop.ItemShop;

import java.util.List;

public abstract class ShopChallenge<T extends ShopEvent> extends SubChallenge<T> {

    protected ShopChallenge(Type type, List<Integer> counterList, List<List<ItemShop>> elementsCounter) {
        super(type, counterList, elementsCounter.stream().map(entityTypes -> entityTypes.stream().map(ItemShop::name).toList()).toList());
    }

}
