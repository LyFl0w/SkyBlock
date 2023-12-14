package net.quillcraft.skyblock.challenge.type;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.challenge.Challenge;
import net.quillcraft.skyblock.challenge.Reward;
import net.quillcraft.skyblock.event.itemshop.ShopEvent;
import net.quillcraft.skyblock.shop.ItemShop;
import org.bukkit.Material;

import java.util.List;

public abstract class ShopChallenge<T extends ShopEvent> extends Challenge<T> {

    public ShopChallenge(SkyBlock skyblock, int id, Difficulty difficulty, Type type, List<Integer> linkedChallengeID, List<Integer> counterList, List<List<ItemShop>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        super(skyblock, id, difficulty, type, linkedChallengeID, counterList, elementsCounter.stream().map(entityTypes -> entityTypes.stream().map(ItemShop::name).toList()).toList(), reward, slot, material, name, description);
    }

}
