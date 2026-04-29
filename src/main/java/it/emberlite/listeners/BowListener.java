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

public class BowListener implements Listener {

    private final EmberLitePlugin plugin;
    private static final String META_KEY = "aetherium_arrow";

    // Blocchi rari che non vengono distrutti dall'esplosione
    private static final java.util.Set<Material> PROTECTED = java.util.Set.of(
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

        // Consuma più durabilità arco
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            it.emberlite.utils.BlockUtils.damageTool(player, bow, 2);
        }, 1L);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!arrow.hasMetadata(META_KEY)) return;
        if (!(arrow.getShooter() instanceof Player shooter)) return;

        Location loc = arrow.getLocation();
        arrow.remove();
        event.setCancelled(true); // Previeni il danno vanilla della freccia

        // Effetti visivi/sonori
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 5, 0.5, 0.5, 0.5, 0);
        loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 15, 0.5, 0.5, 0.5, 0.05);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.8f);

        // Danno ai mob/giocatori vicini (raggio ~2)
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2.5, 2.5, 2.5)) {
            if (entity instanceof LivingEntity living && !entity.equals(shooter)) {
                double dist = entity.getLocation().distance(loc);
                double damage = Math.max(1.0, 6.0 - dist * 1.5); // Danno ridotto con distanza
                living.damage(damage, shooter);
            }
        }

        // Rompe blocchi vicini (esclusi quelli protetti)
        breakNearbyBlocks(loc, 1);
    }

    private void breakNearbyBlocks(Location center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Block b = center.getBlock().getRelative(dx, dy, dz);
                    if (b.getType().isAir()) continue;
                    if (PROTECTED.contains(b.getType())) continue;
                    // Solo blocchi deboli
                    if (isFragile(b.getType())) {
                        b.breakNaturally();
                    }
                }
            }
        }
    }

    private boolean isFragile(Material mat) {
        return switch (mat) {
            case DIRT, GRASS_BLOCK, SAND, RED_SAND, GRAVEL, CLAY,
                    GLASS, GLASS_PANE, LEAVES, OAK_LEAVES, BIRCH_LEAVES,
                    ACACIA_LEAVES, JUNGLE_LEAVES, SPRUCE_LEAVES, DARK_OAK_LEAVES,
                    MANGROVE_LEAVES, SNOW, SNOW_BLOCK, ICE, COBWEB -> true;
            default -> false;
        };
    }
}
