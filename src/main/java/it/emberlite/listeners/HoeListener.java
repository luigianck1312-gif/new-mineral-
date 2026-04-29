package it.emberlite.listeners;

import it.emberlite.EmberLitePlugin;
import it.emberlite.items.AetheriumItems;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class HoeListener implements Listener {

    private final EmberLitePlugin plugin;

    public HoeListener(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!AetheriumItems.isAetherium(hand, AetheriumItems.TYPE_HOE)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (event.getClickedBlock() == null) return;

        Block center = event.getClickedBlock();
        Material centerMat = center.getType();

        // Area 3x3 - ara la terra e fa crescere le colture
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block b = center.getRelative(dx, 0, dz);

                // Ara: terra/erba → farmland
                if (b.getType() == Material.DIRT || b.getType() == Material.GRASS_BLOCK
                        || b.getType() == Material.DIRT_PATH) {
                    b.setType(Material.FARMLAND);
                }

                // Fai crescere le colture sopra di 1 stadio
                Block above = b.getRelative(0, 1, 0);
                if (isCrop(above.getType())) {
                    BlockData data = above.getBlockData();
                    if (data instanceof Ageable ageable) {
                        int current = ageable.getAge();
                        int max = ageable.getMaximumAge();
                        if (current < max) {
                            ageable.setAge(current + 1);
                            above.setBlockData(ageable);
                        }
                    }
                }
            }
        }

        event.setCancelled(true); // Previeni il click vanilla normale
    }

    private boolean isCrop(Material mat) {
        return switch (mat) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART,
                    MELON_STEM, PUMPKIN_STEM, SWEET_BERRY_BUSH,
                    COCOA, BAMBOO -> true;
            default -> false;
        };
    }
}
