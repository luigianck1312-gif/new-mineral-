package it.emberlite.listeners;

import it.emberlite.EmberLitePlugin;
import it.emberlite.items.AetheriumItems;
import it.emberlite.managers.SwordManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class SwordListener implements Listener {

    private final EmberLitePlugin plugin;
    private final SwordManager swordManager;

    public SwordListener(EmberLitePlugin plugin, SwordManager swordManager) {
        this.plugin = plugin;
        this.swordManager = swordManager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!AetheriumItems.isAetherium(hand, AetheriumItems.TYPE_SWORD)) return;

        swordManager.registerHit(player, target);
    }
}
