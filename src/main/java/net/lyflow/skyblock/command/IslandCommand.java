package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.*;
import net.lyflow.skyblock.request.account.AccountRequest;
import net.lyflow.skyblock.request.island.IslandRequest;
import net.lyflow.skyblock.utils.CommandUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.*;

public class IslandCommand implements CommandExecutor, TabCompleter {

    private final static String[] argsCompletion = new String[]{"create", "tp", "delete", "config", "list", "invite", "accept", "kick", "setowner"};
    private final static ArrayList<IslandInvitation> islandInvitationList = new ArrayList();

    private final SkyBlock skyBlock;

    public IslandCommand(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if(args.length == 0) return false;
        if(commandSender instanceof final Player player) {
            final String sub = args[0];
            if(args.length == 1) {
                // CREATE
                if(sub.equalsIgnoreCase("create")) {
                    player.openInventory(IslandDifficulty.openInventoryDifficulty());
                    return true;
                }

                // TP
                if(sub.equalsIgnoreCase("tp")) {
                    final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

                    try {
                        if(!islandRequest.hasIsland(player)) {
                            player.sendMessage("§cVous n'avez pas d'île, créer vous en une avec la commande §6/island create");
                            return true;
                        }

                        player.sendMessage("§bTéléportation en cours sur votre île");
                        player.teleport(islandRequest.getSpawnLocation(islandRequest.getIslandID(player)));

                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }

                // DELETE
                if(sub.equalsIgnoreCase("delete")) {

                    return true;
                }

                // CONFIG
                if(sub.equalsIgnoreCase("config")) {

                    return true;
                }

                // LIST
                if(sub.equalsIgnoreCase("list")) {
                    final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

                    try {
                        if(!islandRequest.hasIsland(player)) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            return true;
                        }
                        final List<IslandMate> islandMates = islandRequest.getMates(player);

                        skyBlock.getDatabase().closeConnection();

                        final IslandMate owner = islandMates.stream().parallel().filter(islandMate -> islandMate.status() == MateStatus.OWNER).findFirst().get();

                        final StringBuilder messageBuilder = new StringBuilder("List des joueurs de votre île\n").append(owner.getStatus())
                                .append(" ").append(owner.player().getName());

                        islandMates.remove(owner);

                        islandMates.stream().sorted(Comparator.comparingInt(IslandMate::isOnline)).forEach(islandMate ->
                                messageBuilder.append("\n").append(islandMate.getStatus()).append(" ").append(islandMate.player().getName()));

                        player.sendMessage(messageBuilder.toString());
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                return false;
            }

            if(args.length == 2) {
                // INVITE
                if(sub.equalsIgnoreCase("invite")) {

                    if(player.getName().equalsIgnoreCase(args[1])) {
                        player.sendMessage("§cVous ne pouvez pas vous inviter vous même");
                        return true;
                    }

                    final Player target = skyBlock.getServer().getPlayerExact(args[1]);
                    if(target == null) {
                        player.sendMessage("§cLe joueur "+args[1]+" n'est pas connecté");
                        return true;
                    }

                    try {
                        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

                        if(!islandRequest.hasIsland(player)) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            return true;
                        }

                        if(hasInvitation(target.getUniqueId(), player.getName())) {
                            player.sendMessage("§cTu as déjà invité ce joueur !");
                            return true;
                        }

                        final int playerIslandID = islandRequest.getIslandID(player);
                        if(islandRequest.hasIsland(target)) {
                            if(playerIslandID != islandRequest.getIslandID(target)) {
                                player.sendMessage("§cLe joueur "+args[1]+" possède déjà une île");
                            } else {
                                player.sendMessage("§cLe joueur "+args[1]+" fait déjà partie de ton île");
                            }
                            skyBlock.getDatabase().closeConnection();
                            return true;
                        }
                        skyBlock.getDatabase().closeConnection();

                        final IslandInvitation islandInvitation = new IslandInvitation(target.getUniqueId(), playerIslandID, player.getName());

                        islandInvitationList.add(islandInvitation);
                        target.sendMessage("§5"+player.getName()+" §dvous a envoyé une invitation d'île");

                        // AUTO REMOVE INVITATION AFTER 5 MIN
                        skyBlock.getServer().getScheduler().runTaskLater(skyBlock, () -> {
                            if(islandInvitationList.contains(islandInvitation)) islandInvitationList.remove(islandInvitation);
                        }, 6000L);


                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    } return true;
                }

                // ACCEPT
                if(sub.equalsIgnoreCase("accept")) {

                    if(player.getName().equalsIgnoreCase(args[1])) {
                        player.sendMessage("§cVous ne pouvez pas vous accepter vous même");
                        return true;
                    }

                    try {
                        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

                        if(islandRequest.hasIsland(player)) {
                            player.sendMessage("§cVous devez quitter votre île avant de pouvoir rejoindre une autre");
                            skyBlock.getDatabase().closeConnection();
                            return true;
                        }

                        if(!hasInvitation(player.getUniqueId(), args[1])) {
                            player.sendMessage("§cVous n'avez pas reçu d'invitation de "+args[1]+", ou alors celle-ci a expiré");
                            skyBlock.getDatabase().closeConnection();
                            return true;
                        }

                        final OfflinePlayer targetOfflinePlayer = new AccountRequest(skyBlock.getDatabase(), true).getOfflinePlayerByName(args[1]);
                        if(targetOfflinePlayer == null) {
                            player.sendMessage("§cLe joueur "+args[1]+" n'existe pas");
                            skyBlock.getDatabase().closeConnection();
                            return true;
                        }

                        if(!islandRequest.hasIsland(targetOfflinePlayer)) {
                            player.sendMessage("§cLe joueur "+args[1]+" ne possède plus d'île");
                            skyBlock.getDatabase().closeConnection();
                            return true;
                        }

                        final IslandInvitation islandInvitation = getInvitation(player.getUniqueId(), args[1]);

                        if(islandRequest.getIslandID(targetOfflinePlayer) != islandInvitation.islandID()) {
                            player.sendMessage("§cLe joueur "+args[1]+"ne possède plus la même île");
                            skyBlock.getDatabase().closeConnection();
                            return true;
                        }

                        final List<IslandMate> islandMates = islandRequest.getMates(targetOfflinePlayer);
                        islandRequest.addMate(player, islandInvitation.islandID());

                        islandMates.stream().parallel().filter(islandMate ->
                                islandMate.player().isOnline()).forEach(islandMate -> skyBlock.getServer().getScheduler().runTask(skyBlock, () ->
                                islandMate.player().getPlayer().sendMessage("§6"+player.getName()+" §ea joint votre île")));

                        player.sendMessage("§6Vous avez rejoinds l'île de "+args[1]);

                        skyBlock.getDatabase().closeConnection();
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }

                // KICK
                if(sub.equalsIgnoreCase("kick")) {

                    return true;
                }

                // SET OWNER
                if(sub.equalsIgnoreCase("setowner")) {

                    return true;
                }
                return false;
            }
        } return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if(commandSender instanceof Player) {
            if(args.length == 1 && args[0] != null) {
                return CommandUtils.completionTable(args[0], argsCompletion);
            }
        }
        return null;
    }

    private boolean hasInvitation(UUID playerUUID, String targetPlayerName) {
        return islandInvitationList.stream().parallel().anyMatch(islandInvitation ->
                islandInvitation.playerUUID() == playerUUID && islandInvitation.targetPlayerName().equals(targetPlayerName));
    }

    private IslandInvitation getInvitation(UUID playerUUID, String targetPlayerName) {
        return islandInvitationList.stream().parallel().filter(islandInvitation ->
                islandInvitation.playerUUID() == playerUUID && islandInvitation.targetPlayerName().equals(targetPlayerName)).findFirst().get();
    }
}