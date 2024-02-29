package net.lyflow.skyblock.island.upgrade;

import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelUpgradeTest {

    @Test
    void testGetData() {
        float price = 10.0f;
        int slot = 1;
        List<String> description = List.of("Upgrade description");
        Map<String, Object> data = Map.of(LevelUpgradeKey.GENERATOR.getKey(), CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1.getGenerator());
        System.out.println(CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1.getGenerator());

        LevelUpgrade upgrade = new LevelUpgrade(price, slot, description, data);
        final CobblestoneGeneratorUpgrade.Generator generator = upgrade.getData(LevelUpgradeKey.GENERATOR);

        // Vérification des résultats
        assertEquals(CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1.getGenerator(), generator);
    }

}
