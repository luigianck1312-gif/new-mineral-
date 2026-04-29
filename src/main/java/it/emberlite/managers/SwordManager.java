package it.emberlite.managers;

import it.emberlite.EmberLitePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SwordManager {

    private final EmberLitePlugin plugin;
    private final Map<UUID, Integer> comboCount = new HashMap<>();
    private final Map<UUID, BukkitTask> resetTasks = new HashMap<>();

    private static final int COMBO_REQUIRED = 4;
    private static final long RESET_TICKS = 60L;
    private static final double AREA_RADIUS = 2.0;
    private static final double AREA_DAMAGE = 4.0;

    public SwordManager(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    public void registerHit(Player player, LivingEntity target) {
        UUID uuid = player.getUniqueId();
        cancelReset(uuid);

        int count = comboCount.getOrDefault(uuid, 0) + 1;
        comboCount.put(uuid, count);

        if (count < COMBO_REQUIRED) {
            player.sendActionBar("§6Combo: §e" + count + "§7/§e" + COMBO_REQUIRED);
        }

        if (count >= COMBO_REQUIRED) {
            comboCount.put(uuid, 0);
            final Location epicenter = target.getLocation().clone();
            Bukkit.getScheduler().runTask(plugin, () -> triggerAreaAttack(player, epicenter));
        }

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            comboCount.remove(uuid);
            resetTasks.remove(uuid);
        }, RESET_TICKS);
        resetTasks.put(uuid, task);
    }

    private void triggerAreaAttack(Player attacker, Location epicenter) {
        if (!attacker.isOnline()) return;
        if (epicenter.getWorld() == null) return;
        try {
            attacker.sendActionBar("§cCOLPO AD AREA!");
            epicenter.getWorld().playSound(epicenter, Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 1.5f);
            epicenter.getWorld().spawnParticle(Particle.POOF, epicenter, 20, 1.0, 0.5, 1.0, 0.1);
            epicenter.getWorld().spawnParticle(Particle.SWEEP_ATTACK, epicenter, 10, 1.0, 0.5, 1.0, 0.0);
            for (Entity entity : epicenter.getWorld().getNearbyEntities(epicenter, AREA_RADIUS, AREA_RADIUS, AREA_RADIUS)) {
                if (entity.equals(attacker)) continue;
                if (!(entity instanceof LivingEntity living)) continue;
                if (!living.isValid()) continue;
                living.damage(AREA_DAMAGE, attacker);
                if (entity instanceof Player p) {
                    p.sendMessage("§cSei stato colpito da un'onda Aetherium!");
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Errore area attack: " + e.getMessage());
        }
    }

    private void cancelReset(UUID uuid) {
        BukkitTask old = resetTasks.remove(uuid);
        if (old != null) old.cancel();
    }

    public int getCombo(UUID uuid) {
        return comboCount.getOrDefault(uuid, 0);
    }
}
