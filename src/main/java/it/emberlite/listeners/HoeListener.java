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

import java.util.Set;

public class HoeListener implements Listener {

    private final EmberLitePlugin plugin;

    private static final Set<Material> TILLABLE = Set.of(
            Material.DIRT, Material.GRASS_BLOCK, Material.DIRT_PATH,
            Material.COARSE_DIRT, Material.ROOTED_DIRT
    );
    private static final Set<Material> CROPS = Set.of(
            Material.WHEAT, Material.CARROTS, Material.POTATOES,
            Material.BEETROOTS, Material.NETHER_WART, Material.MELON_STEM,
            Material.PUMPKIN_STEM, Material.SWEET_BERRY_BUSH, Material.COCOA
    );

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
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block b = center.getRelative(dx, 0, dz);
                if (TILLABLE.contains(b.getType())) b.setType(Material.FARMLAND);
                Block above = b.getRelative(0, 1, 0);
                if (CROPS.contains(above.getType())) {
                    BlockData data = above.getBlockData();
                    if (data instanceof Ageable ageable && ageable.getAge() < ageable.getMaximumAge()) {
                        ageable.setAge(ageable.getAge() + 1);
                        above.setBlockData(ageable);
                    }
                }
            }
        }
        event.setCancelled(true);
    }
}
