package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.entity.Player;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * 	EnchantmentAction: CARRY
 *  Called every X seconds on Players holding
 *  an Item that has this Enchantment applied
 * 
 */
@FunctionalInterface
public interface CarryAction extends EnchantmentAction {
	
	void onCarry(int level, Player p, int delay);
	
	@Override
	default EnchantmentActionType getType() {
		return EnchantmentActionType.CARRY;
	}
	
}