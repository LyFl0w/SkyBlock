package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.inventory.island.IslandDifficultyInventory;
import net.lyflow.skyblock.inventory.upgrade.UpgradeInventory;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.island.IslandMate;
import net.lyflow.skyblock.island.PlayerIslandStatus;
import net.lyflow.skyblock.utils.CommandUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class IslandCommand implements CommandExecutor, TabCompleter {

    private static final String[] fArgsCompletion = new String[]{"create", "sell", "tp", "list", "config", "upgrade", "invite", "accept", "kick", "leave", "set"};
    private static final String[] sArgsCompletion = new String[]{"owner", "spawn"};
    private static final ArrayList<IslandInvitation> islandInvitationList = new ArrayList<>();

    private final SkyBlock skyblock;

    public IslandCommand(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (args.length == 0) return false;
        if (commandSender instanceof final Player player) {
            final String sub = args[0];

            if (args.length == 1) {
                // CREATE
                if (sub.equalsIgnoreCase("create")) {
                    player.openInventory(IslandDifficultyInventory.getInventory());
                    return true;
                }

                // CREATE
                if (sub.equalsIgnoreCase("sell")) {
                    player.sendMessage("§cWorking Progress");
                    return true;
                }

                // TP
                if (sub.equalsIgnoreCase("tp")) {
                    final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);

                    try {
                        if (!islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous n'avez pas d'île, créer vous en une avec la commande §6/island create");
                            return true;
                        }

                        player.sendMessage("§bTéléportation vers votre île");
                        final Location location = islandRequest.getSpawnLocation(islandRequest.getIslandID(player.getUniqueId()));
                        if(location == null) {
                            player.sendMessage("LOCATION NULL !! :(");
                        } else {
                            player.teleport(location);

                        }

                    } catch (SQLException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    }
                    return true;
                }

                // CONFIG
                if (sub.equalsIgnoreCase("config")) {
                    player.sendMessage("§cWorking Progress");
                    return true;
                }

                // UPGRADE
                if(sub.equalsIgnoreCase("upgrade")) {
                    final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);

                    try {
                        if(!islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }
                        final int islandID = islandRequest.getIslandID(player.getUniqueId());
                        skyblock.getDatabase().closeConnection();

                        player.openInventory(UpgradeInventory.getUpgradeInventory(skyblock.getIslandUpgradeManager(), islandID));
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }

                // LIST
                if (sub.equalsIgnoreCase("list")) {
                    final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);

                    try {
                        if (!islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            return true;
                        }
                        final List<IslandMate> islandMates = islandRequest.getMates(player.getUniqueId());

                        skyblock.getDatabase().closeConnection();

                        final Optional<IslandMate> optionalIslandMate = islandMates.stream().parallel().filter(islandMate -> islandMate.status() == PlayerIslandStatus.OWNER).findFirst();
                        if (optionalIslandMate.isEmpty())
                            throw new IllegalCallerException("Le joueur ne possède pas d'owner sur son ile !");
                        final IslandMate owner = optionalIslandMate.get();

                        final StringBuilder messageBuilder = new StringBuilder("List des joueurs de votre île\n").append(owner.getStatus())
                                .append(" ").append(owner.player().getName());

                        islandMates.remove(owner);

                        islandMates.stream().sorted(Comparator.comparingInt(IslandMate::isOnline)).forEach(islandMate ->
                                messageBuilder.append("\n").append(islandMate.getStatus()).append(" ").append(islandMate.player().getName()));

                        player.sendMessage(messageBuilder.toString());
                    } catch (SQLException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    }
                    return true;
                }

                // LEAVE
                if (sub.equalsIgnoreCase("leave")) {
                    final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
                    try {
                        if (!islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        final boolean hasTeamMates = islandRequest.getMates(player.getUniqueId()).size() > 1;

                        if (hasTeamMates && islandRequest.getPlayerIslandStatus(player.getUniqueId()) == PlayerIslandStatus.OWNER) {
                            player.sendMessage("§cVous ne pouvez pas quitter l'île étant Owner, veuillez choisir un autre owner avec la commande §6/island set owner <player>");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        // TELEPORT THE PLAYER TO THE LOBBY IF HE IS ON THE ISLAND WORLD
                        final int islandID = islandRequest.getIslandID(player.getUniqueId());
                        if (player.getWorld().getName().equals("skyblock-map/" + islandID))
                            player.teleport(LobbyCommand.spawn);

                        if (!hasTeamMates) {
                            final String path = "skyblock-map/" + islandID;
                            final File islandWorld = new File(skyblock.getDataFolder(), "../../" + path);
                            skyblock.getLogger().info(islandWorld.getAbsolutePath());

                            skyblock.getServer().unloadWorld(path, false);
                            FileUtils.deleteDirectory(islandWorld);

                            islandRequest.leaveIsland(player.getUniqueId());

                            new UpgradeIslandRequest(skyblock.getDatabase(), false).deleteIsland(islandID);

                            islandRequest.deleteIsland(islandID);
                        } else {
                            islandRequest.leaveIsland(player.getUniqueId());
                        }
                        skyblock.getDatabase().closeConnection();

                        player.sendMessage("§aVous avez bien quitté votre île");
                    } catch (SQLException | IOException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    }
                    return true;
                }

                return false;
            }

            if (args.length == 2) {
                // INVITE
                if (sub.equalsIgnoreCase("invite")) {

                    if (player.getName().equalsIgnoreCase(args[1])) {
                        player.sendMessage("§cVous ne pouvez pas vous inviter vous même");
                        return true;
                    }

                    final Player target = skyblock.getServer().getPlayerExact(args[1]);
                    if (target == null) {
                        player.sendMessage("§cLe joueur " + args[1] + " n'est pas connecté");
                        return true;
                    }

                    try {
                        final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);

                        if (!islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            return true;
                        }

                        if (hasInvitation(target.getUniqueId(), player.getName())) {
                            player.sendMessage("§cTu as déjà invité ce joueur !");
                            return true;
                        }

                        final int playerIslandID = islandRequest.getIslandID(player.getUniqueId());
                        if (islandRequest.hasIsland(target.getUniqueId())) {
                            if (playerIslandID != islandRequest.getIslandID(target.getUniqueId())) {
                                player.sendMessage("§cLe joueur " + args[1] + " possède déjà une île");
                            } else {
                                player.sendMessage("§cLe joueur " + args[1] + " fait déjà partie de ton île");
                            }
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }
                        skyblock.getDatabase().closeConnection();

                        final IslandInvitation islandInvitation = new IslandInvitation(target.getUniqueId(), playerIslandID, player.getName());

                        islandInvitationList.add(islandInvitation);
                        target.sendMessage("§5" + player.getName() + " §dvous a envoyé une invitation d'île");

                        // AUTO REMOVE INVITATION AFTER 5 MIN
                        skyblock.getServer().getScheduler().runTaskLater(skyblock, () -> {
                            islandInvitationList.remove(islandInvitation);
                        }, 6000L);


                    } catch (SQLException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    }
                    return true;
                }

                // ACCEPT
                if (sub.equalsIgnoreCase("accept")) {

                    if (player.getName().equalsIgnoreCase(args[1])) {
                        player.sendMessage("§cVous ne pouvez pas vous accepter vous même");
                        return true;
                    }

                    try {
                        final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);

                        if (islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous devez quitter votre île avant de pouvoir rejoindre une autre");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        if (!hasInvitation(player.getUniqueId(), args[1])) {
                            player.sendMessage("§cVous n'avez pas reçu d'invitation de " + args[1] + ", ou alors celle-ci a expiré");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        final OfflinePlayer targetOfflinePlayer = new AccountRequest(skyblock.getDatabase(), true).getOfflinePlayerByName(args[1]);
                        if (targetOfflinePlayer == null) {
                            player.sendMessage("§cLe joueur " + args[1] + " n'existe pas");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        if (!islandRequest.hasIsland(targetOfflinePlayer.getUniqueId())) {
                            player.sendMessage("§cLe joueur " + args[1] + " ne possède plus d'île");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        final IslandInvitation islandInvitation = getInvitation(player.getUniqueId(), args[1]);
                        islandInvitationList.remove(islandInvitation);

                        if (islandRequest.getIslandID(targetOfflinePlayer.getUniqueId()) != islandInvitation.islandID()) {
                            player.sendMessage("§cLe joueur " + args[1] + "ne possède plus la même île");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        final List<IslandMate> islandMates = islandRequest.getMates(targetOfflinePlayer.getUniqueId());
                        islandRequest.addMate(player.getUniqueId(), islandInvitation.islandID());

                        islandMates.stream().parallel().filter(islandMate ->
                                islandMate.player().isOnline()).forEach(islandMate -> skyblock.getServer().getScheduler().runTask(skyblock, () ->
                                islandMate.player().getPlayer().sendMessage("§6" + player.getName() + " §ea joint votre île")));


                        player.sendMessage("§6Vous avez rejoinds l'île de " + args[1]);

                        skyblock.getDatabase().closeConnection();
                    } catch (SQLException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    }
                    return true;
                }

                // KICK
                if (sub.equalsIgnoreCase("kick")) {

                    if (player.getName().equalsIgnoreCase(args[1])) {
                        player.sendMessage("§cVous ne pouvez pas vous exclure vous même");
                        return true;
                    }

                    final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
                    try {
                        if (!islandRequest.hasIsland(player.getUniqueId())) {
                            player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        if (islandRequest.getPlayerIslandStatus(player.getUniqueId()) == PlayerIslandStatus.MATE) {
                            player.sendMessage("§cIl faut être Owner de l'île pour pouvoir exécuter cette commande !");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        final List<IslandMate> mates = islandRequest.getMates(player.getUniqueId());
                        final Optional<IslandMate> optionalIslandMate = mates.stream().parallel().filter(islandMates ->
                                islandMates.player().getName().equalsIgnoreCase(args[1])).findFirst();

                        if (optionalIslandMate.isEmpty()) {
                            player.sendMessage("§cLe joueur " + args[1] + " n'est pas dans vos alliés");
                            skyblock.getDatabase().closeConnection();
                            return true;
                        }

                        final OfflinePlayer targetPlayerOffline = optionalIslandMate.get().player();
                        // KICK PLAYER FROM ISLAND IN DB
                        islandRequest.leaveIsland(targetPlayerOffline.getUniqueId());

                        if (targetPlayerOffline.isOnline()) {
                            final Player targetPlayer = targetPlayerOffline.getPlayer();
                            // TELEPORT THE PLAYER TO THE LOBBY IF HE IS ON THE ISLAND WORLD
                            if (player.getWorld().getName().equals("skyblock-map/" + islandRequest.getIslandID(player.getUniqueId())))
                                targetPlayer.teleport(LobbyCommand.spawn);
                            targetPlayer.sendMessage("§cVous avez été exclu de votre île !");
                        }

                        skyblock.getDatabase().closeConnection();

                        player.sendMessage("§aLe joueur §2" + args[1] + " §aa bien été exclu de votre île");
                    } catch (SQLException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    }
                    return true;
                }

                if (sub.equalsIgnoreCase("set")) {

                    if (args[1].equalsIgnoreCase("spawn")) {
                        final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
                        try {
                            if (!islandRequest.hasIsland(player.getUniqueId())) {
                                player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }

                            if (islandRequest.getPlayerIslandStatus(player.getUniqueId()) != PlayerIslandStatus.OWNER) {
                                player.sendMessage("§cIl faut être owner pour pouvoir exécuter cette commande");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }

                            final int islandID = islandRequest.getIslandID(player.getUniqueId());
                            if (!player.getWorld().getName().equals("skyblock-map/" + islandID)) {
                                player.sendMessage("§cIl faut être sur votre île pour pouvoir exécuter cette commande");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }

                            final Location location = player.getLocation();
                            if (player.getFallDistance() > 0 || location.clone().add(0, -1, 0).getBlock().getType() == Material.AIR) {
                                player.sendMessage("§cCe lieu n'est pas adapté pour être un spawn d'île");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }
                            islandRequest.setIslandSpawn(islandID, location);

                            skyblock.getDatabase().closeConnection();

                            player.sendMessage("§aLe spawn d'île a bien été changé");
                        } catch (SQLException e) {
                            skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                        }
                        return true;
                    }

                    return false;
                }

                return false;
            }

            if (args.length == 3) {
                final String last = args[1];

                if (sub.equalsIgnoreCase("set")) {

                    if (last.equalsIgnoreCase("owner")) {

                        if (player.getName().equalsIgnoreCase(args[1])) {
                            player.sendMessage("§cVous ne pouvez pas vous mettre Owner vous même");
                            return true;
                        }

                        final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
                        try {
                            if (!islandRequest.hasIsland(player.getUniqueId())) {
                                player.sendMessage("§cVous n'avez pas d'île, créez-vous en une avec la commande §6/island create");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }

                            if (islandRequest.getPlayerIslandStatus(player.getUniqueId()) != PlayerIslandStatus.OWNER) {
                                player.sendMessage("§cIl faut être owner pour pouvoir exécuter cette commande");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }

                            final Optional<IslandMate> optionalIslandMate = islandRequest.getMates(player.getUniqueId()).stream().parallel()
                                    .filter(islandMates -> islandMates.player().getName().equalsIgnoreCase(args[2])).findFirst();

                            if (optionalIslandMate.isEmpty()) {
                                player.sendMessage("§cLe joueur " + args[2] + " n'est pas dans vos alliés");
                                skyblock.getDatabase().closeConnection();
                                return true;
                            }

                            final OfflinePlayer targetPlayerOffline = optionalIslandMate.get().player();
                            islandRequest.setPlayerIslandStatus(player.getUniqueId(), PlayerIslandStatus.MATE);
                            islandRequest.setPlayerIslandStatus(targetPlayerOffline.getUniqueId(), PlayerIslandStatus.OWNER);

                            skyblock.getDatabase().closeConnection();

                            if (targetPlayerOffline.isOnline()) {
                                targetPlayerOffline.getPlayer().sendMessage("§6Vous avez été promu Owner de votre île");
                            }

                            player.sendMessage("§6Vous n'êtes plus Owner de votre île");
                        } catch (SQLException e) {
                            skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                        }
                        return true;
                    }

                    return false;
                }

                return false;
            }

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (commandSender instanceof Player) {
            if (args.length == 1) {
                return CommandUtils.completionTable(args[0], fArgsCompletion);
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                return CommandUtils.completionTable(args[1], sArgsCompletion);
            }
        }
        return null;
    }

    private boolean hasInvitation(UUID playerUUID, String targetPlayerName) {
        return islandInvitationList.stream().parallel().anyMatch(islandInvitation ->
                islandInvitation.playerUUID() == playerUUID && islandInvitation.targetPlayerName().equals(targetPlayerName));
    }

    private IslandInvitation getInvitation(UUID playerUUID, String targetPlayerName) {
        final Optional<IslandInvitation> optionalIslandInvitation = islandInvitationList.stream().parallel().filter(islandInvitation ->
                islandInvitation.playerUUID() == playerUUID && islandInvitation.targetPlayerName().equals(targetPlayerName)).findFirst();

        if (optionalIslandInvitation.isEmpty()) throw new IllegalCallerException("Le joueur n'a pas recu d'invitation");
        return optionalIslandInvitation.get();
    }

    private record IslandInvitation(UUID playerUUID, int islandID, String targetPlayerName) {}
}