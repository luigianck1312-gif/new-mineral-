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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShovelListener implements Listener {

    private final EmberLitePlugin plugin;

    public ShovelListener(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    // Scavo 3x3 con effetti speciali
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!AetheriumItems.isAetherium(hand, AetheriumItems.TYPE_SHOVEL)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Block center = event.getBlock();

        for (Block b : BlockUtils.get3x3Blocks(center, player)) {
            if (b.equals(center)) continue;
            if (b.getType().isAir()) continue;
            if (!isShoveable(b.getType())) continue;

            // Terra → sentiero
            if (b.getType() == Material.GRASS_BLOCK || b.getType() == Material.DIRT) {
                b.setType(Material.DIRT_PATH);
            }
            // Sabbia → cade istantaneamente (break)
            else if (b.getType() == Material.SAND || b.getType() == Material.RED_SAND
                    || b.getType() == Material.GRAVEL) {
                b.breakNaturally(hand);
            } else {
                b.breakNaturally(hand);
            }
            BlockUtils.damageTool(player, hand, 1); // x2 durabilità totale
        }
    }

    private boolean isShoveable(Material mat) {
        return switch (mat) {
            case DIRT, GRASS_BLOCK, COARSE_DIRT, ROOTED_DIRT, PODZOL,
                    SAND, RED_SAND, GRAVEL, CLAY, SOUL_SAND, SOUL_SOIL,
                    DIRT_PATH, MYCELIUM, SNOW, SNOW_BLOCK -> true;
            default -> false;
        };
    }
}
