package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * EnchantmentAction: DIG
 * Called when a Player digs a Block with an Item
 * that has this Enchantment applied
 * 
 */
@FunctionalInterface
public interface DigAction extends EnchantmentAction {

    void onDig(int level, Player p, Block block, List<ItemStack> drops);

    default EnchantmentActionType getType() {
        return EnchantmentActionType.DIG;
    }

}