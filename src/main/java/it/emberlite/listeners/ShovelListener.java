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

import java.util.Set;

public class ShovelListener implements Listener {

    private final EmberLitePlugin plugin;

    private static final Set<Material> DIRT_TYPES = Set.of(
            Material.DIRT, Material.GRASS_BLOCK, Material.COARSE_DIRT,
            Material.ROOTED_DIRT, Material.PODZOL, Material.MYCELIUM, Material.DIRT_PATH
    );
    private static final Set<Material> FALLING_TYPES = Set.of(
            Material.SAND, Material.RED_SAND, Material.GRAVEL
    );
    private static final Set<Material> OTHER = Set.of(
            Material.CLAY, Material.SOUL_SAND, Material.SOUL_SOIL,
            Material.SNOW, Material.SNOW_BLOCK
    );

    public ShovelListener(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

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
            if (DIRT_TYPES.contains(b.getType())) {
                b.setType(Material.DIRT_PATH);
                BlockUtils.damageTool(player, hand, 1);
            } else if (FALLING_TYPES.contains(b.getType()) || OTHER.contains(b.getType())) {
                b.breakNaturally(hand);
                BlockUtils.damageTool(player, hand, 1);
            }
        }
    }
}
