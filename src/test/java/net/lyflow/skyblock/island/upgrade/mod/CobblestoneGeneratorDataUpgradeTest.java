package net.lyflow.skyblock.island.upgrade.mod;

class CobblestoneGeneratorDataUpgradeTest {

    /*@Test
    void testUpgradeRandomMaterialProbability() {
        final int iterations = 10_000;
        final double marginError = 0.03;

        for (CobblestoneGeneratorUpgrade.Generator.PreGenerator preGenerator : CobblestoneGeneratorUpgrade.Generator.PreGenerator.values()) {
            final CobblestoneGeneratorUpgrade.Generator generator = preGenerator.generator;
            final Map<Material, Integer> materialCounts = new HashMap<>();

            for (int i = 0; i < iterations; i++) {
                final Material material = generator.getRandomMaterial();
                materialCounts.put(material, materialCounts.getOrDefault(material, 0) + 1);
            }

            for (Map.Entry<Material, Double> entry : generator.getMaterialProbability().entrySet()) {
                final Material material = entry.getKey();
                final double expectedProbability = entry.getValue();
                final double actualProbability = (double) materialCounts.get(material) / iterations;
                assertEquals(expectedProbability, actualProbability, marginError);
            }
        }
    }*/
}
