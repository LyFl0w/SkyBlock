package net.lyflow.skyblock.listener.inventory;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.ChallengeStatus;
import net.lyflow.skyblock.challenge.PlayerChallengeProgress;
import net.lyflow.skyblock.database.request.account.AccountRequest;

import net.lyflow.skyblock.event.island.CreateIslandEvent;
import net.lyflow.skyblock.event.itemshop.PlayerBuyItemEvent;
import net.lyflow.skyblock.event.itemshop.PlayerSellItemEvent;
import net.lyflow.skyblock.inventory.challenge.ChallengeInventory;
import net.lyflow.skyblock.inventory.shop.AmountItemShopInventory;
import net.lyflow.skyblock.inventory.shop.ShopCategoryInventory;
import net.lyflow.skyblock.inventory.shop.ShopInventory;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.shop.ShopCategory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.sql.SQLException;


public class InventoryClickListener implements Listener {

    private final SkyBlock skyblock;
    public InventoryClickListener(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        final ItemStack item = event.getCurrentItem();

        if(inventory == null || inventory == player.getInventory() || item == null || item.getType() == Material.AIR) return;

        final String title = event.getView().getTitle();

        if(title.equalsIgnoreCase("§6Difficulté de l'île")) {
            event.setCancelled(true);
            player.closeInventory();
            if(item.getType() == Material.STRUCTURE_VOID) {
                //player.closeInventory();
                return;
            }
            //player.closeInventory();
            skyblock.getServer().getPluginManager()
                    .callEvent(new CreateIslandEvent(skyblock, player, IslandDifficulty.getIslandDifficultyByMaterial(item.getType())));
            return;
        }

        if(title.equalsIgnoreCase("§9Shop")) {
            event.setCancelled(true);

            switch(item.getType()) {
                case CLOCK -> player.openInventory(ShopInventory.getServeurShopInventory());
                case RECOVERY_COMPASS -> player.openInventory(ShopInventory.getPlayerShopInventory());
            }
            return;
        }

        if(title.equals("§aShop")) {
            event.setCancelled(true);

            switch(item.getType()) {
                case PAPER -> player.openInventory(ShopInventory.getShopMenuInventory());
                case AMETHYST_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(false));
                case ECHO_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(true));
            }

            return;
        }

        if(title.startsWith("§aShop/Category")) {
            event.setCancelled(true);
            player.openInventory((item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§9Back"))
                    ? ShopInventory.getServeurShopInventory()
                    : ShopInventory.getShopServerInventory(0, ShopCategory.getShopCategory(event.getSlot()), title.contains("Buy")));
            return;
        }

        if(title.startsWith("§aShop/Amount")) {
            event.setCancelled(true);

            final int page = Integer.parseInt(title.substring(title.length()-1));
            final boolean isBuyInventory = title.contains("Buy");
            if(item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§9Back")) {
                final ShopCategory shopCategory = ShopCategory.getShopCategoryByInventoryName(title);
                player.openInventory(ShopInventory.getShopServerInventory(page, shopCategory, isBuyInventory));
                return;
            }

            final ItemStack selectedItem = inventory.getItem(4);
            final ItemShop itemShop = ItemShop.getItemShopByMaterial(selectedItem.getType());
            switch(item.getType()) {
                case LIME_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE -> {
                    try {
                        final String lore = selectedItem.getItemMeta().getLore().get(0);
                        player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(
                                skyblock, player.getUniqueId(), itemShop, Math.min(Math.max(Integer.parseInt(lore.substring(lore.lastIndexOf(":")+2)) + Integer.parseInt(
                                        ChatColor.stripColor(item.getItemMeta().getDisplayName())), 0), 2304), page, isBuyInventory));
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                case LIGHT_BLUE_STAINED_GLASS_PANE -> {
                    try {
                        final int count = (isBuyInventory)
                                ? (int) Math.floor(new AccountRequest(skyblock.getDatabase(), true).getMoney(player.getUniqueId()) / itemShop.getBuyPrice())
                                : countItemInventory(player.getInventory(), selectedItem.getType());
                        player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(
                                skyblock, player.getUniqueId(), itemShop, count , page, isBuyInventory));
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                default -> {
                    final String lore = selectedItem.getItemMeta().getLore().get(0);
                    final int count = Integer.parseInt(lore.substring(lore.lastIndexOf(":")+2));
                    skyblock.getServer().getPluginManager().callEvent((isBuyInventory)
                            ? new PlayerBuyItemEvent(skyblock, player, itemShop, count)
                            : new PlayerSellItemEvent(skyblock, player, itemShop, count));
                }
            }

            return;
        }

        if(title.startsWith("§aShop")) {
            event.setCancelled(true);

            final int page = Integer.parseInt(title.substring(title.length()-1));
            final boolean isBuyInventory = title.contains("Buy");
            if(item.getType() == Material.PAPER) {
                final boolean isPrevious = item.getItemMeta().getDisplayName().equals("Previous");
                if(isPrevious || item.getItemMeta().getDisplayName().equals("Next")) {
                    final ShopCategory shopCategory = ShopCategory.getShopCategoryByInventoryName(title);
                    if(isPrevious) {
                        player.openInventory((page == 0) ? ShopCategoryInventory.getShopCategoryInventory(isBuyInventory) :
                                ShopInventory.getShopServerInventory(page-1, shopCategory, isBuyInventory));
                        return;
                    }
                    player.openInventory(ShopInventory.getShopServerInventory(page+1, shopCategory, isBuyInventory));
                    return;
                }
            }
            if(item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
            try {
                player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(skyblock, player.getUniqueId(), ItemShop.getItemShopByMaterial(item.getType()), 0, page, isBuyInventory));
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if(title.equals("§gChallenges - Menu")) {
            event.setCancelled(true);

            final Challenge.Difficulty difficulty = Challenge.Difficulty.getChallengeBySlot(event.getSlot());
            if(skyblock.getChallengeManager().getChallengesByDifficulty(difficulty).isEmpty()) {
                player.sendMessage("§cIl n'y a pas de défis encore dans cette section");
                return;
            }
            if(!difficulty.playerHasAccess(skyblock.getChallengeManager(), player)) {
                try {
                    player.sendMessage("§cVeuillez terminer la moitié des challenges "+difficulty.getBefore().getName());
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            player.openInventory(ChallengeInventory.getChallengeInventory(skyblock.getChallengeManager(), player, difficulty));
            return;
        }

        if(title.startsWith("§gChallenges")) {
            event.setCancelled(true);

            final Challenge.Difficulty difficultyPage = Arrays.stream(Challenge.Difficulty.values()).filter(difficulty -> title.contains(difficulty.getName()))
                    .findFirst().get();
            final Challenge<? extends Event> challenge = skyblock.getChallengeManager().getChallengesByDifficulty(difficultyPage).stream().parallel()
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
                    player.openInventory(ChallengeInventory.getChallengeInventory(skyblock.getChallengeManager(), player, difficultyPage));
                }
            }
            return;

        }

    }

    private int countItemInventory(Inventory inventory, Material material) {
        int result = 0;
        for(int i=0; i<inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);
            if(itemStack == null || itemStack.getType() != material) continue;
            result += itemStack.getAmount();
        }
        return result;
    }
}
