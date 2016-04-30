package me.mrCookieSlime.EmeraldEnchants;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface EnchantmentAction {
	
	public enum Type {
		DIG,
		HIT,
		CARRY,
		WEAR,
		DAMAGE,
		INTERACT,
		PROJECTILE_SHOOT,
		PROJECTILE_HIT;
	}
	
	EnchantmentAction.Type getType();

	/*
	 * 	EnchantmentAction: DIG
	 *  Called when a Player digs a Block with an Item
	 *  that has this Enchantment applied
	 * 
	 */
	public abstract class DigAction implements EnchantmentAction {
		
		abstract void onDig(int level, Player p, Block block, List<ItemStack> drops);
		
		@Override
		public Type getType() {
			return Type.DIG;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: HIT
	 *  Called when a Player hits an Entity with an Item
	 *  that has this Enchantment applied
	 * 
	 */
	public abstract class HitAction implements EnchantmentAction {
		
		abstract void onHit(int level, Player p, EntityDamageByEntityEvent event);
		
		@Override
		public Type getType() {
			return Type.HIT;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: PROJECTILE_HIT
	 *  Called when a Player hits an Entity with an Arrow shot using a specific Bow
	 *  that has this Enchantment applied
	 * 
	 */
	public abstract class ProjectileHitAction implements EnchantmentAction {
		
		abstract void onHit(int level, EntityDamageByEntityEvent event);
		
		@Override
		public Type getType() {
			return Type.PROJECTILE_HIT;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: PROJECTILE_SHOOT
	 *  Called when a Player shoots an Arrow using a specific Bow
	 *  that has this Enchantment applied
	 * 
	 */
	public abstract class ProjectileShootAction implements EnchantmentAction {
		
		abstract void onShoot(int level, Player p, Arrow arrow);
		
		@Override
		public Type getType() {
			return Type.PROJECTILE_SHOOT;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: CARRY
	 *  Called every X seconds on Players holding
	 *  an Item that has this Enchantment applied
	 * 
	 */
	// TODO: Implement
	public abstract class CarryAction implements EnchantmentAction {
		
		abstract void onCarry(int level, Player p, int delay);
		
		@Override
		public Type getType() {
			return Type.CARRY;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: WEAR
	 *  Called every X seconds on Players wearing
	 *  an Item that has this Enchantment applied
	 * 
	 */
	// TODO: Implement
	public abstract class WearAction implements EnchantmentAction {
		
		abstract void onWear(int level, Player p, int delay);
		
		@Override
		public Type getType() {
			return Type.WEAR;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: INTERACT
	 *  Called when a Player interacts with a Block
	 *  while holding an Item that has this
	 *  Enchantment applied
	 * 
	 */
	public abstract class InteractAction implements EnchantmentAction {
		
		abstract void onInteract(int level, Player p, PlayerInteractEvent event);
		
		@Override
		public Type getType() {
			return Type.INTERACT;
		}
		
	}
	
	/*
	 * 	EnchantmentAction: DAMAGE
	 *  Called when a Player gets damaged while
	 *  wearing an Item that has this
	 *  Enchantment applied
	 * 
	 */
	public abstract class DamageAction implements EnchantmentAction {
		
		abstract void onDamage(int level, Player p, EntityDamageEvent event);
		
		@Override
		public Type getType() {
			return Type.DAMAGE;
		}
		
	}

}
