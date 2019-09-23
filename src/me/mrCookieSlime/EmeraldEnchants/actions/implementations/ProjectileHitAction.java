package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * 	EnchantmentAction: PROJECTILE_HIT
 *  Called when a Player hits an Entity with an Arrow shot using a specific Bow
 *  that has this Enchantment applied
 * 
 */
@FunctionalInterface
public interface ProjectileHitAction extends EnchantmentAction {
	
	void onHit(int level, EntityDamageByEntityEvent event);
	
	default EnchantmentActionType getType() {
		return EnchantmentActionType.PROJECTILE_HIT;
	}
	
}