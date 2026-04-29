package it.emberlite.listeners;

import it.emberlite.EmberLitePlugin;
import it.emberlite.items.AetheriumItems;
import it.emberlite.utils.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AxeListener implements Listener {

    private final EmberLitePlugin plugin;
    private static final int MAX_LOGS = 150;

    private static final Set<Material> LOG_TYPES = Set.of(
            Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG,
            Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG,
            Material.MANGROVE_LOG, Material.CHERRY_LOG, Material.BAMBOO_BLOCK,
            Material.OAK_WOOD, Material.BIRCH_WOOD, Material.SPRUCE_WOOD,
            Material.JUNGLE_WOOD, Material.ACACIA_WOOD, Material.DARK_OAK_WOOD,
            Material.MANGROVE_WOOD, Material.CHERRY_WOOD,
            Material.MUSHROOM_STEM, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK
    );

    private static final Set<Material> LEAF_TYPES = Set.of(
            Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES,
            Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES, Material.AZALEA_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES
    );

    public AxeListener(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!AetheriumItems.isAetherium(hand, AetheriumItems.TYPE_AXE)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Block broken = event.getBlock();
        if (!LOG_TYPES.contains(broken.getType())) return;
        if (!hasLeavesNearby(broken)) return;

        List<Block> logs = findConnectedLogs(broken);
        for (Block log : logs) {
            if (log.equals(broken)) continue;
            log.breakNaturally(hand);
            BlockUtils.damageTool(player, hand, 1);
        }
    }

    private List<Block> findConnectedLogs(Block start) {
        List<Block> found = new ArrayList<>();
        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty() && found.size() < MAX_LOGS) {
            Block current = queue.poll();
            found.add(current);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Block neighbor = current.getRelative(dx, dy, dz);
                        if (!visited.contains(neighbor) && LOG_TYPES.contains(neighbor.getType())) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return found;
    }

    private boolean hasLeavesNearby(Block block) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -1; dy <= 5; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if (LEAF_TYPES.contains(block.getRelative(dx, dy, dz).getType())) return true;
                }
            }
        }
        return false;
    }
}
