package it.emberlite.listeners;

import it.emberlite.EmberLitePlugin;
import it.emberlite.items.AetheriumItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Set;

public class BowListener implements Listener {

    private final EmberLitePlugin plugin;
    private static final String META_KEY = "aetherium_arrow";

    private static final Set<Material> PROTECTED = Set.of(
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.ANCIENT_DEBRIS, Material.NETHER_GOLD_ORE,
            Material.GILDED_BLACKSTONE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE
    );

    private static final Set<Material> FRAGILE = Set.of(
            Material.DIRT, Material.GRASS_BLOCK, Material.SAND, Material.RED_SAND,
            Material.GRAVEL, Material.CLAY, Material.GLASS, Material.GLASS_PANE,
            Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.ACACIA_LEAVES,
            Material.JUNGLE_LEAVES, Material.SPRUCE_LEAVES, Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES,
            Material.SNOW, Material.SNOW_BLOCK, Material.ICE, Material.COBWEB
    );

    public BowListener(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        ItemStack bow = player.getInventory().getItemInMainHand();
        if (!AetheriumItems.isAetherium(bow, AetheriumItems.TYPE_BOW)) return;
        arrow.setMetadata(META_KEY, new FixedMetadataValue(plugin, true));
        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                it.emberlite.utils.BlockUtils.damageTool(player, bow, 2), 1L);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!arrow.hasMetadata(META_KEY)) return;
        if (!(arrow.getShooter() instanceof Player shooter)) return;
        Location loc = arrow.getLocation().clone();
        arrow.remove();
        event.setCancelled(true);
        if (loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 5, 0.5, 0.5, 0.5, 0);
        loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 15, 0.5, 0.5, 0.5, 0.05);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.8f);
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2.5, 2.5, 2.5)) {
            if (entity.equals(shooter)) continue;
            if (!(entity instanceof LivingEntity living)) continue;
            double dist = entity.getLocation().distance(loc);
            double damage = Math.max(1.0, 6.0 - dist * 1.5);
            living.damage(damage, shooter);
        }
        breakNearbyBlocks(loc, 1);
    }

    private void breakNearbyBlocks(Location center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Block b = center.getBlock().getRelative(dx, dy, dz);
                    if (b.getType().isAir()) continue;
                    if (PROTECTED.contains(b.getType())) continue;
                    if (FRAGILE.contains(b.getType())) b.breakNaturally();
                }
            }
        }
    }
}
