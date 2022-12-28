package net.lyflow.skyblock.inventory.challenge;

import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ChallengeInventory {

    public static Inventory getMenuChallengeInventory() {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§gChallenges - Menu");
        Arrays.stream(Challenge.Difficulty.values()).forEach(difficulty -> inventoryBuilder.setItem(difficulty.getSlot(), difficulty.getItemStack()));
        return inventoryBuilder.toInventory();
    }

    public static Inventory getChallengeInventory(ChallengeManager challengeManager, Player player, Challenge.Difficulty difficulty) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§gChallenges - "+difficulty.getName());
        challengeManager.getChallengesByDifficulty(difficulty).stream().parallel().forEach(challenge ->
                inventoryBuilder.setItem(challenge.getSlot(), challenge.getRepresentation(player)));
        return inventoryBuilder.toInventory();
    }

}
