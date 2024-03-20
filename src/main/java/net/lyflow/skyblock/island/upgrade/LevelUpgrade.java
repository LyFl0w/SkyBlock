package net.lyflow.skyblock.island.upgrade;

import java.util.List;
import java.util.Map;

public class LevelUpgrade {

    private final float price;
    private final int slot;
    private final List<String> description;
    private final Map<String, Object> data;

    public LevelUpgrade(float price, int slot, List<String> description, Map<String, Object> data) {
        this.price = price;
        this.slot = slot;
        this.description = description;
        this.data = data;
    }

    public LevelUpgrade(LevelUpgrade levelUpgrade, Map<String, Object> data) {
        this(levelUpgrade.price, levelUpgrade.slot, levelUpgrade.description, data);
    }


    public float getPrice() {
        return price;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getDescription() {
        return description;
    }

    public <T> T getData(LevelUpgradeKey key) {
        return key.getData(data);
    }

    public Map<String, Object> getData() {
        return data;
    }
}