package net.lyflow.skyblock.inventory.challenge;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.ChallengeStatus;
import net.lyflow.skyblock.challenge.PlayerChallengeProgress;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
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

    public static void inventoryDifficultyChallengeEvent(SkyBlock skyBlock, InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        final Challenge.Difficulty difficulty = Challenge.Difficulty.getChallengeBySlot(event.getSlot());
        if(skyBlock.getChallengeManager().getChallengesByDifficulty(difficulty).isEmpty()) {
            player.sendMessage("§cIl n'y a pas de défis encore dans cette section");
            return;
        }
        if(!difficulty.playerHasAccess(skyBlock.getChallengeManager(), player)) {
            try {
                player.sendMessage("§cVeuillez terminer la moitié des challenges "+difficulty.getBefore().getName());
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        player.openInventory(ChallengeInventory.getChallengeInventory(skyBlock.getChallengeManager(), player, difficulty));
    }

    public static void inventoryDifficultyChallengeEvent(SkyBlock skyBlock, InventoryClickEvent event, String title, Player player) {
        event.setCancelled(true);

        final Challenge.Difficulty difficultyPage = Arrays.stream(Challenge.Difficulty.values()).filter(difficulty -> title.contains(difficulty.getName()))
                .findFirst().get();
        final Challenge<? extends Event> challenge = skyBlock.getChallengeManager().getChallengesByDifficulty(difficultyPage).stream().parallel()
                .filter(challenges -> challenges.getSlot() == event.getSlot()).findFirst().get();

        final PlayerChallengeProgress playerChallengeProgress = challenge.getChallengeProgress().getPlayerChallengeProgress(player);
        final ChallengeStatus challengeStatus = playerChallengeProgress.getStatus();

        switch(challengeStatus) {
            case LOCKED -> player.sendMessage("§cPour débloquer ce défi, il vous faudra faire les défis suivants : ...");
            case IN_PROGRESS -> player.sendMessage("§cVeuillez terminer le défi avant de vouloir récupérer les récompenses");
            case REWARD_RECOVERED -> player.sendMessage("§cVous avez déjà validé ce défi");
            case SUCCESSFUL -> {
                player.sendMessage("§aVous avez validé le défi §b"+challenge.getName());
                challenge.getChallengeProgress().accessReward(player);
                player.openInventory(ChallengeInventory.getChallengeInventory(skyBlock.getChallengeManager(), player, difficultyPage));
            }
        }
    }

}
