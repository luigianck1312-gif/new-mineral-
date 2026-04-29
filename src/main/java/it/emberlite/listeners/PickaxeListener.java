package it.emberlite.listeners;

import it.emberlite.EmberLitePlugin;
import it.emberlite.items.AetheriumItems;
import it.emberlite.utils.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PickaxeListener implements Listener {

    private final EmberLitePlugin plugin;

    public PickaxeListener(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!AetheriumItems.isAetherium(hand, AetheriumItems.TYPE_PICKAXE)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;
        Block center = event.getBlock();
        for (Block b : BlockUtils.get3x3Blocks(center, player)) {
            if (b.equals(center)) continue;
            if (b.getType().isAir()) continue;
            if (!BlockUtils.isMineable(b)) continue;
            b.breakNaturally(hand);
            BlockUtils.damageTool(player, hand, 2);
        }
    }
}
