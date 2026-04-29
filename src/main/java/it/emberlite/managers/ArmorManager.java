package it.emberlite.managers;

import it.emberlite.EmberLitePlugin;
import it.emberlite.items.AetheriumItems;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmorManager {

    private final EmberLitePlugin plugin;
    private final Map<UUID, Long> helmetCooldown    = new HashMap<>();
    private final Map<UUID, Long> chestCooldown     = new HashMap<>();
    private final Map<UUID, Long> legsCooldown      = new HashMap<>();
    private final Map<UUID, Long> bootsCooldown     = new HashMap<>();

    private static final long   COOLDOWN_MS    = 20_000L;
    private static final double TRIGGER_HEALTH = 12.0;
    private static final double HELMET_HEAL    = 2.0;
    private static final double CHEST_HEAL     = 4.0;
    private static final double LEGS_HEAL      = 3.0;
    private static final double BOOTS_HEAL     = 1.0;

    public ArmorManager(EmberLitePlugin plugin) {
        this.plugin = plugin;
    }

    public void checkAndHeal(Player player) {
        double health = player.getHealth();
        if (health <= 0 || health > TRIGGER_HEALTH) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        boolean hasHelmet = AetheriumItems.isAetherium(player.getInventory().getHelmet(),     AetheriumItems.TYPE_HELMET);
        boolean hasChest  = AetheriumItems.isAetherium(player.getInventory().getChestplate(), AetheriumItems.TYPE_CHESTPLATE);
        boolean hasLegs   = AetheriumItems.isAetherium(player.getInventory().getLeggings(),   AetheriumItems.TYPE_LEGGINGS);
        boolean hasBoots  = AetheriumItems.isAetherium(player.getInventory().getBoots(),      AetheriumItems.TYPE_BOOTS);

        boolean healed = false;

        if (hasHelmet && canTrigger(helmetCooldown, uuid, now)) {
            heal(player, HELMET_HEAL); helmetCooldown.put(uuid, now); healed = true;
            player.sendMessage(ChatColor.GOLD + "Elmo Aetherium: +1 cuore");
        }
        if (hasChest && canTrigger(chestCooldown, uuid, now)) {
            heal(player, CHEST_HEAL); chestCooldown.put(uuid, now); healed = true;
            player.sendMessage(ChatColor.GOLD + "Petto Aetherium: +2 cuori");
        }
        if (hasLegs && canTrigger(legsCooldown, uuid, now)) {
            heal(player, LEGS_HEAL); legsCooldown.put(uuid, now); healed = true;
            player.sendMessage(ChatColor.GOLD + "Gambe Aetherium: +1.5 cuori");
        }
        if (hasBoots && canTrigger(bootsCooldown, uuid, now)) {
            heal(player, BOOTS_HEAL); bootsCooldown.put(uuid, now); healed = true;
            player.sendMessage(ChatColor.GOLD + "Stivali Aetherium: +0.5 cuore");
        }

        if (hasHelmet && hasChest && hasLegs && hasBoots && healed) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 0, true, false));
            player.sendMessage(ChatColor.AQUA + "Set Aetherium: Resistenza I per 3 sec!");
        }

        if (healed) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
        }
    }

    private void heal(Player player, double amount) {
        double newHealth = Math.min(player.getMaxHealth(), player.getHealth() + amount);
        player.setHealth(newHealth);
    }

    private boolean canTrigger(Map<UUID, Long> map, UUID uuid, long now) {
        Long last = map.get(uuid);
        return last == null || (now - last) >= COOLDOWN_MS;
    }

    public void clearData(UUID uuid) {
        helmetCooldown.remove(uuid);
        chestCooldown.remove(uuid);
        legsCooldown.remove(uuid);
        bootsCooldown.remove(uuid);
    }
}
