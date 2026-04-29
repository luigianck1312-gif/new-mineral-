package it.emberlite;

import it.emberlite.items.AetheriumItems;
import it.emberlite.listeners.*;
import it.emberlite.managers.ArmorManager;
import it.emberlite.managers.SwordManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EmberLitePlugin extends JavaPlugin {

    private static EmberLitePlugin instance;
    private SwordManager swordManager;
    private ArmorManager armorManager;

    @Override
    public void onEnable() {
        instance = this;
        swordManager = new SwordManager(this);
        armorManager = new ArmorManager(this);

        getServer().getPluginManager().registerEvents(new SwordListener(this, swordManager), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(this, armorManager), this);
        getServer().getPluginManager().registerEvents(new PickaxeListener(this), this);
        getServer().getPluginManager().registerEvents(new AxeListener(this), this);
        getServer().getPluginManager().registerEvents(new ShovelListener(this), this);
        getServer().getPluginManager().registerEvents(new HoeListener(this), this);
        getServer().getPluginManager().registerEvents(new BowListener(this), this);

        getLogger().info("Emberlite caricato! Set Aetherium pronto.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Emberlite disabilitato.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("emberlite")) return false;
        if (!sender.hasPermission("emberlite.admin")) {
            sender.sendMessage("§cNon hai il permesso!");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("§eUso: /emberlite give <player> <spada|elmo|petto|gambe|stivali|piccone|ascia|pala|zappa|arco>");
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            Player target = getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cGiocatore non trovato!");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("§eSpecifica l'oggetto!");
                return true;
            }
            giveItem(sender, target, args[2]);
        }
        return true;
    }

    private void giveItem(CommandSender sender, Player target, String itemName) {
        org.bukkit.inventory.ItemStack item = switch (itemName.toLowerCase()) {
            case "spada" -> AetheriumItems.getSword();
            case "elmo" -> AetheriumItems.getHelmet();
            case "petto" -> AetheriumItems.getChestplate();
            case "gambe" -> AetheriumItems.getLeggings();
            case "stivali" -> AetheriumItems.getBoots();
            case "piccone" -> AetheriumItems.getPickaxe();
            case "ascia" -> AetheriumItems.getAxe();
            case "pala" -> AetheriumItems.getShovel();
            case "zappa" -> AetheriumItems.getHoe();
            case "arco" -> AetheriumItems.getBow();
            default -> null;
        };
        if (item == null) {
            sender.sendMessage("§cOggetto non valido!");
            return;
        }
        target.getInventory().addItem(item);
        sender.sendMessage("§aDato §e" + itemName + " Aetherium §aa §e" + target.getName());
        target.sendMessage("§6Hai ricevuto un oggetto §eAetherium§6!");
    }

    public static EmberLitePlugin getInstance() { return instance; }
    public SwordManager getSwordManager() { return swordManager; }
    public ArmorManager getArmorManager() { return armorManager; }
}
