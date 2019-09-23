package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * 	EnchantmentAction: HIT
 *  Called when a Player hits an Entity with an Item
 *  that has this Enchantment applied
 * 
 */
@FunctionalInterface
public interface HitAction extends EnchantmentAction {
	
	void onHit(int level, Player p, EntityDamageByEntityEvent event);
	
	@Override
	default EnchantmentActionType getType() {
		return EnchantmentActionType.HIT;
	}
	
}