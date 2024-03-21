package net.lyflow.skyblock.loader.island.upgrade.mod;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CobblestoneGeneratorData {

    public static class MaterialEntry {

        @SerializedName("item")
        private final String material;
        private final double probability;

        public MaterialEntry(String material, double probability) {
            this.material = material;
            this.probability = probability;
        }

    }

    private final String defaultMaterial;
    private final String materialActivation;
    private final List<MaterialEntry> materialProbability;

    public CobblestoneGeneratorData(String defaultMaterial, String materialActivation, List<MaterialEntry> materialProbability) {
        this.defaultMaterial = defaultMaterial;
        this.materialActivation = materialActivation;
        this.materialProbability = materialProbability;
    }

    public CobblestoneGeneratorData(LinkedTreeMap<String, Object> map) {
        this.defaultMaterial = (String) map.get("defaultMaterial");
        this.materialActivation = (String) map.get("materialActivation");
        this.materialProbability = new ArrayList<>();

        ((ArrayList<LinkedTreeMap<String, Object>>) map.get("materialProbability")).forEach(map1 ->
                this.materialProbability.add(new MaterialEntry((String) map1.get("item"), (double) map1.get("probability")))
        );
    }


    public CobblestoneGeneratorUpgrade.Generator toGenerator() {
        final HashMap<Material, Double> materialProbability = new HashMap<>();
        final Material materialActivation = Registry.MATERIAL.get(Objects.requireNonNull(NamespacedKey.fromString(this.materialActivation)));

        this.materialProbability.forEach(materialEntry ->
                materialProbability.put(
                        Registry.MATERIAL.get(Objects.requireNonNull(NamespacedKey.fromString(materialEntry.material))),
                        materialEntry.probability
                )
        );

        if (defaultMaterial == null)
            return new CobblestoneGeneratorUpgrade.Generator(materialActivation, materialProbability);

        return new CobblestoneGeneratorUpgrade.Generator(materialActivation,
                Registry.MATERIAL.get(Objects.requireNonNull(NamespacedKey.fromString(this.defaultMaterial))),
                materialProbability);
    }

}
