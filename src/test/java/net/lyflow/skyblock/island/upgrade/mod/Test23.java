package net.lyflow.skyblock.island.upgrade.mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import net.lyflow.skyblock.island.upgrade.LevelUpgradeKey;
import net.lyflow.skyblock.loader.gson.EmptyListToNullFactory;
import net.lyflow.skyblock.loader.island.upgrade.IslandUpgradeData;

import java.util.List;
import java.util.Map;

public class Test23 {

    public static void main(String[] args) {
        final Gson gson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapterFactory(EmptyListToNullFactory.INSTANCE)
                .setPrettyPrinting()
                .create();

        final IslandUpgradeData upgrade = new IslandUpgradeData(4, 4, "tnt_drop_rate",
                "minecraft:tnt", "Tnt Drop Rate Upgrade", List.of(),
                List.of(
                        new LevelUpgrade(1_000f, 1, List.of("Vous en avez marre des 50% ?", "En voilà 60% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.6f)),
                        new LevelUpgrade(2_000f, 2, List.of("Vous en avez marre des 60% ?", "En voilà 80% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.8f)),
                        new LevelUpgrade(3_000f, 3, List.of("Coquin !!!", "En voilà 100% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 1.0f))
                ),
                Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.5f)
        );

        final String json = gson.toJson(upgrade);
        System.out.println(json);

        System.out.println("|-|".repeat(20));

        final IslandUpgradeData upgradeDeserialize = gson.fromJson(json, IslandUpgradeData.class);
        System.out.println(gson.toJson(upgradeDeserialize));
    }

}
