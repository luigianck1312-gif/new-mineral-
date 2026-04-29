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

    public static final String KEY_TYPE        = "aetherium_type";
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
    public static final String TYPE_GEM        = "aetherium_gem";
    public static final String TYPE_BLOCK      = "aetherium_block";

    private static final int CMD = 1001;

    private static void tag(ItemMeta meta, String type) {
        NamespacedKey key = new NamespacedKey("emberlite", KEY_TYPE);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
    }

    private static ItemStack build(Material mat, String name, List<String> lore, String type) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + name);
        meta.setLore(lore);
        meta.setCustomModelData(CMD);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DYE, ItemFlag.HIDE_ITEM_SPECIFICS);
        tag(meta, type);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getGem() {
        return build(Material.BAT_SPAWN_EGG, "Gemma Emberlite", Arrays.asList(
                ChatColor.GRAY + "Una gemma rara di colore rosso fuoco.",
                ChatColor.GRAY + "Usata per creare equipaggiamento Aetherium.",
                ChatColor.DARK_RED + "Rarissima."
        ), TYPE_GEM);
    }

    public static ItemStack getBlock() {
        return build(Material.BEE_SPAWN_EGG, "Blocco Emberlite", Arrays.asList(
                ChatColor.GRAY + "Un blocco di pura energia Emberlite.",
                ChatColor.DARK_RED + "Non si trova in natura."
        ), TYPE_BLOCK);
    }

    public static ItemStack getSword() {
        ItemStack item = build(Material.ZOMBIE_SPAWN_EGG, "Spada Aetherium", Arrays.asList(
                ChatColor.GRAY + "Danno: Netherite +1",
                ChatColor.YELLOW + "Abilita: Colpo ad area",
                ChatColor.GRAY + "Dopo 4 colpi consecutivi,",
                ChatColor.GRAY + "colpisce in raggio 2 blocchi!",
                ChatColor.RED + "Reset se non colpisci per 3 sec."
        ), TYPE_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.SHARPNESS, 6, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack getHelmet() {
        return build(Material.WITHER_SPAWN_EGG, "Elmo Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Auto-Cura",
                ChatColor.GRAY + "Sotto 6 cuori: +1 cuore",
                ChatColor.GRAY + "Cooldown: 20 sec"
        ), TYPE_HELMET);
    }

    public static ItemStack getChestplate() {
        return build(Material.ENDER_DRAGON_SPAWN_EGG, "Petto Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Auto-Cura",
                ChatColor.GRAY + "Sotto 6 cuori: +2 cuori",
                ChatColor.GRAY + "Cooldown: 20 sec",
                ChatColor.GREEN + "Set completo: Resistenza I (3s)"
        ), TYPE_CHESTPLATE);
    }

    public static ItemStack getLeggings() {
        return build(Material.GUARDIAN_SPAWN_EGG, "Gambe Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Auto-Cura",
                ChatColor.GRAY + "Sotto 6 cuori: +1.5 cuori",
                ChatColor.GRAY + "Cooldown: 20 sec"
        ), TYPE_LEGGINGS);
    }

    public static ItemStack getBoots() {
        return build(Material.ELDER_GUARDIAN_SPAWN_EGG, "Stivali Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Auto-Cura",
                ChatColor.GRAY + "Sotto 6 cuori: +0.5 cuore",
                ChatColor.GRAY + "Cooldown: 20 sec"
        ), TYPE_BOOTS);
    }

    public static ItemStack getPickaxe() {
        return build(Material.CREEPER_SPAWN_EGG, "Piccone Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Scavo 3x3",
                ChatColor.GRAY + "Scava sempre in area 3x3",
                ChatColor.RED + "Durabilita x3"
        ), TYPE_PICKAXE);
    }

    public static ItemStack getAxe() {
        return build(Material.SKELETON_SPAWN_EGG, "Ascia Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Abbatti alberi interi",
                ChatColor.GRAY + "Segue i blocchi di legno collegati",
                ChatColor.RED + "Non funziona su costruzioni player"
        ), TYPE_AXE);
    }

    public static ItemStack getShovel() {
        return build(Material.SPIDER_SPAWN_EGG, "Pala Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Scavo 3x3",
                ChatColor.GRAY + "Terra -> Sentiero automatico",
                ChatColor.GRAY + "Sabbia -> Cade istantaneamente",
                ChatColor.RED + "Durabilita x2"
        ), TYPE_SHOVEL);
    }

    public static ItemStack getHoe() {
        return build(Material.ENDERMAN_SPAWN_EGG, "Zappa Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Area 3x3",
                ChatColor.GRAY + "Ara tutto l'area",
                ChatColor.GRAY + "Fa crescere le colture di 1 stadio"
        ), TYPE_HOE);
    }

    public static ItemStack getBow() {
        return build(Material.BLAZE_SPAWN_EGG, "Arco Aetherium", Arrays.asList(
                ChatColor.YELLOW + "Abilita: Frecce esplosive",
                ChatColor.GRAY + "Ogni freccia esplode all'impatto",
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
