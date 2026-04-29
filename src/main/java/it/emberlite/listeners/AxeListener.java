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
    private static final int MAX_LOGS = 150; // Limite anti-lag

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
        if (!isLog(broken.getType())) return;

        // Controlla che non sia una costruzione player (legno ma con foglie sopra = albero vero)
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

            // Controlla solo in verticale e diagonale vicina (albero = cresce su)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Block neighbor = current.getRelative(dx, dy, dz);
                        if (!visited.contains(neighbor) && isLog(neighbor.getType())) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return found;
    }

    private boolean isLog(Material mat) {
        String name = mat.name();
        return name.endsWith("_LOG") || name.endsWith("_WOOD") || name.equals("MUSHROOM_STEM")
                || name.equals("BROWN_MUSHROOM_BLOCK") || name.equals("RED_MUSHROOM_BLOCK");
    }

    private boolean hasLeavesNearby(Block block) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -1; dy <= 5; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    Material mat = block.getRelative(dx, dy, dz).getType();
                    if (mat.name().contains("LEAVES")) return true;
                }
            }
        }
        return false;
    }
}
