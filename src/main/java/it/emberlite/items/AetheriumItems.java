package it.emberlite.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class AetheriumItems {

    public static final String KEY_TYPE = "aetherium_type";

    public static final String TYPE_SWORD      = "aetherium_sword";
    public static final String TYPE_HELMET     = "aetherium_helmet";
    public static final String TYPE_CHESTPLATE = "aetherium_chestplate";
    public static final String TYPE_LEGGINGS   = "aetherium_leggings";
    public static final String TYPE_BOOTS      = "aetherium_boots";
    public static final String TYPE_PICKAXE    = "aetherium_pickaxe";
    public static final String TYPE_AXE        = "aetherium_axe";
    public static final String TYPE_SHOVEL     = "aetherium_shovel";
    public static final String TYPE_HOE        = "aetherium_hoe";
    public static final String TYPE_BOW        = "aetherium_bow";

    private static ItemStack makeItem(Material mat, String name, List<String> lore, String typeKey) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        NamespacedKey key = new NamespacedKey("emberlite", KEY_TYPE);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, typeKey);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSword() {
        ItemStack item = makeItem(Material.NETHERITE_SWORD,
                "Spada Aetherium",
                Arrays.asList(
                        ChatColor.GRAY + "Danno: Netherite +1",
                        ChatColor.YELLOW + "Abilita: Colpo ad area",
                        ChatColor.GRAY + "Dopo 4 colpi consecutivi,",
                        ChatColor.GRAY + "colpisce in raggio 2 blocchi!",
                        ChatColor.RED + "Reset se non colpisci per 3 sec."
                ), TYPE_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack getHelmet() {
        return makeItem(Material.NETHERITE_HELMET,
                "Elmo Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Auto-Cura",
                        ChatColor.GRAY + "Sotto 6 cuori: +1 cuore",
                        ChatColor.GRAY + "Cooldown: 20 sec"
                ), TYPE_HELMET);
    }

    public static ItemStack getChestplate() {
        return makeItem(Material.NETHERITE_CHESTPLATE,
                "Petto Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Auto-Cura",
                        ChatColor.GRAY + "Sotto 6 cuori: +2 cuori",
                        ChatColor.GRAY + "Cooldown: 20 sec",
                        ChatColor.GREEN + "Set completo: Resistenza I (3s) + 5 cuori totali"
                ), TYPE_CHESTPLATE);
    }

    public static ItemStack getLeggings() {
        return makeItem(Material.NETHERITE_LEGGINGS,
                "Gambe Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Auto-Cura",
                        ChatColor.GRAY + "Sotto 6 cuori: +1.5 cuori",
                        ChatColor.GRAY + "Cooldown: 20 sec"
                ), TYPE_LEGGINGS);
    }

    public static ItemStack getBoots() {
        return makeItem(Material.NETHERITE_BOOTS,
                "Stivali Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Auto-Cura",
                        ChatColor.GRAY + "Sotto 6 cuori: +0.5 cuore",
                        ChatColor.GRAY + "Cooldown: 20 sec"
                ), TYPE_BOOTS);
    }

    public static ItemStack getPickaxe() {
        ItemStack item = makeItem(Material.NETHERITE_PICKAXE,
                "Piccone Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Scavo 3x3",
                        ChatColor.GRAY + "Scava sempre in area 3x3",
                        ChatColor.RED + "Durabilita x3"
                ), TYPE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack getAxe() {
        return makeItem(Material.NETHERITE_AXE,
                "Ascia Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Abbatti alberi interi",
                        ChatColor.GRAY + "Segue i blocchi di legno collegati",
                        ChatColor.RED + "Non funziona su costruzioni player"
                ), TYPE_AXE);
    }

    public static ItemStack getShovel() {
        return makeItem(Material.NETHERITE_SHOVEL,
                "Pala Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Scavo 3x3",
                        ChatColor.GRAY + "Terra -> Sentiero automatico",
                        ChatColor.GRAY + "Sabbia -> Cade istantaneamente",
                        ChatColor.RED + "Durabilita x2"
                ), TYPE_SHOVEL);
    }

    public static ItemStack getHoe() {
        return makeItem(Material.NETHERITE_HOE,
                "Zappa Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Area 3x3",
                        ChatColor.GRAY + "Ara tutto l'area",
                        ChatColor.GRAY + "Fa crescere le colture vicine di 1 stadio",
                        ChatColor.GREEN + "Utility farming potenziata!"
                ), TYPE_HOE);
    }

    public static ItemStack getBow() {
        return makeItem(Material.BOW,
                "Arco Aetherium",
                Arrays.asList(
                        ChatColor.YELLOW + "Abilita: Frecce esplosive",
                        ChatColor.GRAY + "Ogni freccia esplode all'impatto",
                        ChatColor.GRAY + "Esplosione piccola (meno di un creeper)",
                        ChatColor.RED + "Non distrugge minerali rari",
                        ChatColor.RED + "Consuma piu durabilita"
                ), TYPE_BOW);
    }

    public static String getAetheriumType(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey key = new NamespacedKey("emberlite", KEY_TYPE);
        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static boolean isAetherium(ItemStack item, String type) {
        return type.equals(getAetheriumType(item));
    }
}
