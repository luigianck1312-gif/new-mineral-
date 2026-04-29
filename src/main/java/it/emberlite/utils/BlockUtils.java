package it.emberlite.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockUtils {

    private static final Set<Material> INDESTRUCTIBLE = Set.of(
            Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
            Material.STRUCTURE_BLOCK, Material.END_PORTAL_FRAME,
            Material.END_PORTAL, Material.END_GATEWAY, Material.NETHER_PORTAL
    );

    public static List<Block> get3x3Blocks(Block center, Player player) {
        List<Block> blocks = new ArrayList<>();
        BlockFace face = getTargetFace(player);
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                Block rel;
                if (face == BlockFace.UP || face == BlockFace.DOWN) {
                    rel = center.getRelative(a, 0, b);
                } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                    rel = center.getRelative(a, b, 0);
                } else {
                    rel = center.getRelative(0, b, a);
                }
                blocks.add(rel);
            }
        }
        return blocks;
    }

    private static BlockFace getTargetFace(Player player) {
        float pitch = player.getLocation().getPitch();
        if (pitch < -45) return BlockFace.UP;
        if (pitch > 45)  return BlockFace.DOWN;
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) yaw += 360;
        if (yaw < 45 || yaw >= 315) return BlockFace.SOUTH;
        if (yaw < 135) return BlockFace.WEST;
        if (yaw < 225) return BlockFace.NORTH;
        return BlockFace.EAST;
    }

    public static boolean isMineable(Block b) {
        return !INDESTRUCTIBLE.contains(b.getType());
    }

    public static void damageTool(Player player, ItemStack tool, int amount) {
        if (tool == null || !tool.hasItemMeta()) return;
        ItemMeta meta = tool.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return;
        if (tool.getEnchantments().containsKey(Enchantment.UNBREAKING)) {
            int level = tool.getEnchantmentLevel(Enchantment.UNBREAKING);
            if (Math.random() < (double) level / (level + 1)) return;
        }
        int newDamage = damageable.getDamage() + amount;
        if (newDamage >= tool.getType().getMaxDurability()) {
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        } else {
            damageable.setDamage(newDamage);
            tool.setItemMeta(meta);
        }
    }
}
