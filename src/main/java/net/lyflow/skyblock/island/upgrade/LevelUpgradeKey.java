package net.lyflow.skyblock.island.upgrade;

import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;

import java.util.Map;

public enum LevelUpgradeKey {

    GENERATOR("generator", CobblestoneGeneratorUpgrade.Generator.class),
    DROP_RATE("drop_rate", Double.class);

    private final String key;
    private final Class<?> type;

    LevelUpgradeKey(String key, Class<?> type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class<?> getType() {
        return type;
    }

    public <T> T getData(Map<String, Object> data) {
        return (T) data.get(key);
    }

    public Object getNonFormalizedData(Map<String, Object> data) {
        return data.get(key);
    }

}
