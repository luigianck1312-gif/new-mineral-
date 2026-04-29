package it.emberlite.utils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {

    /**
     * Restituisce i 9 blocchi 3x3 centrati sul blocco rotto,
     * orientati rispetto alla faccia che il giocatore stava guardando.
     */
    public static List<Block> get3x3Blocks(Block center, Player player) {
        List<Block> blocks = new ArrayList<>();
        BlockFace face = getTargetFace(player);

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                Block rel;
                switch (face) {
                    case UP, DOWN -> rel = center.getRelative(a, 0, b);
                    case NORTH, SOUTH -> rel = center.getRelative(a, b, 0);
                    default -> rel = center.getRelative(0, b, a); // EAST/WEST
                }
                blocks.add(rel);
            }
        }
        return blocks;
    }

    private static BlockFace getTargetFace(Player player) {
        float pitch = player.getLocation().getPitch();
        if (pitch < -45) return BlockFace.UP;
        if (pitch > 45) return BlockFace.DOWN;

        float yaw = player.getLocation().getYaw();
        if (yaw < 0) yaw += 360;
        if (yaw < 45 || yaw >= 315) return BlockFace.SOUTH;
        if (yaw < 135) return BlockFace.WEST;
        if (yaw < 225) return BlockFace.NORTH;
        return BlockFace.EAST;
    }

    /**
     * Controlla se un blocco può essere minato con il piccone Aetherium.
     * Esclude blocchi indistruttibili come la bedrock.
     */
    public static boolean isMineable(Block b) {
        return switch (b.getType()) {
            case BEDROCK, BARRIER, COMMAND_BLOCK, CHAIN_COMMAND_BLOCK,
                    REPEATING_COMMAND_BLOCK, STRUCTURE_BLOCK, END_PORTAL_FRAME,
                    END_PORTAL, END_GATEWAY, NETHER_PORTAL -> false;
            default -> true;
        };
    }

    /**
     * Applica danno allo strumento in mano al giocatore.
     */
    public static void damageTool(Player player, ItemStack tool, int amount) {
        if (tool == null || !tool.hasItemMeta()) return;
        ItemMeta meta = tool.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return;

        // Controlla Unbreaking
        if (tool.getEnchantments().containsKey(org.bukkit.enchantments.Enchantment.DURABILITY)) {
            int level = tool.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.DURABILITY);
            // Probabilità di saltare il danno
            if (Math.random() < (double) level / (level + 1)) return;
        }

        int newDamage = damageable.getDamage() + amount;
        if (newDamage >= tool.getType().getMaxDurability()) {
            // Rompi lo strumento
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        } else {
            damageable.setDamage(newDamage);
            tool.setItemMeta(meta);
        }
    }
}
