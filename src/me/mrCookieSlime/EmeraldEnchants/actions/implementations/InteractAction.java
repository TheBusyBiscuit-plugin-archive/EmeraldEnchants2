package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * 	EnchantmentAction: INTERACT
 *  Called when a Player interacts with a Block
 *  while holding an Item that has this
 *  Enchantment applied
 * 
 */
@FunctionalInterface
public interface InteractAction extends EnchantmentAction {
	
	void onInteract(int level, Player p, PlayerInteractEvent event);
	
	@Override
	default EnchantmentActionType getType() {
		return EnchantmentActionType.INTERACT;
	}
	
}