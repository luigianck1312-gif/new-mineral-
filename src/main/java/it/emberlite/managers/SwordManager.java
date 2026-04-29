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
    // UUID giocatore -> numero di colpi consecutivi
    private final Map<UUID, Integer> comboCount = new HashMap<>();
    // UUID giocatore -> task di reset
    private final Map<UUID, BukkitTask> resetTasks = new HashMap<>();

    private static final int COMBO_REQUIRED = 4;        // Colpi per attivare area
    private static final long RESET_TICKS = 60L;        // 3 secondi (20 tick/s)
    private static final double AREA_RADIUS = 2.0;      // Raggio area
    private static final double AREA_DAMAGE = 4.0;      // Danno area (2 cuori)

    public SwordManager(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    public void registerHit(Player player, LivingEntity target) {
        UUID uuid = player.getUniqueId();

        // Cancella il vecchio reset timer
        cancelReset(uuid);

        // Incrementa combo
        int count = comboCount.getOrDefault(uuid, 0) + 1;
        comboCount.put(uuid, count);

        // Feedback combo al giocatore
        if (count < COMBO_REQUIRED) {
            player.sendActionBar("§6Combo: §e" + count + "§7/§e" + COMBO_REQUIRED);
        }

        // Ogni 4 colpi → area
        if (count >= COMBO_REQUIRED) {
            triggerAreaAttack(player, target.getLocation());
            comboCount.put(uuid, 0); // Reset combo dopo area
        }

        // Avvia timer reset (3 secondi senza colpire = reset)
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            comboCount.remove(uuid);
            resetTasks.remove(uuid);
        }, RESET_TICKS);
        resetTasks.put(uuid, task);
    }

    private void triggerAreaAttack(Player attacker, Location epicenter) {
        attacker.sendActionBar("§c💥 COLPO AD AREA!");
        epicenter.getWorld().playSound(epicenter, Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 1.5f);
        epicenter.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, epicenter, 20, 1, 0.5, 1, 0.1);
        epicenter.getWorld().spawnParticle(Particle.SWEEP_ATTACK, epicenter, 10, 1, 0.5, 1, 0);

        for (Entity entity : epicenter.getWorld().getNearbyEntities(epicenter, AREA_RADIUS, AREA_RADIUS, AREA_RADIUS)) {
            if (entity instanceof LivingEntity living && !entity.equals(attacker)) {
                living.damage(AREA_DAMAGE, attacker);
                // Piccolo knockback
                if (entity instanceof Player p) {
                    p.sendMessage("§c⚔ Sei stato colpito da un'onda d'urto Aetherium!");
                }
            }
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
