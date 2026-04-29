package it.emberlite.listeners;

import it.emberlite.EmberLitePlugin;
import it.emberlite.managers.ArmorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArmorListener implements Listener {

    private final EmberLitePlugin plugin;
    private final ArmorManager armorManager;

    public ArmorListener(EmberLitePlugin plugin, ArmorManager armorManager) {
        this.plugin = plugin;
        this.armorManager = armorManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline() && player.getHealth() > 0) {
                armorManager.checkAndHeal(player);
            }
        }, 1L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        armorManager.clearData(event.getPlayer().getUniqueId());
    }
}
