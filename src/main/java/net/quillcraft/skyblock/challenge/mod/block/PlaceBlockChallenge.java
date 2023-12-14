package net.quillcraft.skyblock.challenge.mod.block;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.challenge.PlayerChallengeProgress;
import net.quillcraft.skyblock.challenge.Reward;
import net.quillcraft.skyblock.challenge.type.MaterialChallenge;
import net.quillcraft.skyblock.manager.ChallengeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PlaceBlockChallenge extends MaterialChallenge<BlockPlaceEvent> {

    public PlaceBlockChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> linkedChallengeID, List<Integer> counterList, List<List<Material>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        super(skyblock, id, difficulty, Type.PLACE_BLOCK, linkedChallengeID, counterList, elementsCounter, reward, slot, material, name, description);
    }

    public PlaceBlockChallenge(SkyBlock skyblock, int id, Difficulty difficulty, Type type, List<Integer> counterList, List<List<Material>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        this(skyblock, id, difficulty, Collections.emptyList(), counterList, elementsCounter, reward, slot, material, name, description);
    }

    @Override
    protected void onEvent(BlockPlaceEvent event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException {
        final Material material = event.getBlockPlaced().getType();
        if (!challengeProgress.isValidElement(material)) return;
        challengeProgress.incrementCounter(player, 1, material);
    }

    public static class ListenerEvent implements Listener {

        private final List<PlaceBlockChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<PlaceBlockChallenge>) challengeManager.getChallengesByType(Type.PLACE_BLOCK));
        }

        @EventHandler(ignoreCancelled = true)
        public void onBlockPlace(BlockPlaceEvent event) {
            challenges.stream().parallel().forEach(placeBlockChallenge -> placeBlockChallenge.onEventTriggered(event.getPlayer(), event));
        }

    }

}
