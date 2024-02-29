package net.lyflow.skyblock.loader.challenge;

import net.lyflow.skyblock.challenge.Reward;
import net.lyflow.skyblock.loader.minecraft.ItemStackData;

import java.util.Collections;
import java.util.List;

public class RewardData {

    private final List<ItemStackData> items;
    private final int level;
    private final float money;

    public RewardData(List<ItemStackData> items, int level, float money) {
        this.items = items;
        this.level = level;
        this.money = money;
    }

    public Reward toReward() {
        return new Reward(((items == null)
                ? Collections.emptyList()
                : items.stream().map(ItemStackData::toItemStack).toList()
        ), level, money);
    }
}
