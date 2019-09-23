package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.entity.Player;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * 	EnchantmentAction: WEAR
 *  Called every X seconds on Players wearing
 *  an Item that has this Enchantment applied
 * 
 */
@FunctionalInterface
public interface WearAction extends EnchantmentAction {
	
	void onWear(int level, Player p, int delay);
	
	@Override
	default EnchantmentActionType getType() {
		return EnchantmentActionType.WEAR;
	}
	
}