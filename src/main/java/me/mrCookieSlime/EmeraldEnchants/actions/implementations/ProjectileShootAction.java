package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * EnchantmentAction: PROJECTILE_SHOOT
 * Called when a Player shoots an Arrow using a specific Bow
 * that has this Enchantment applied
 * 
 */
@FunctionalInterface
public interface ProjectileShootAction extends EnchantmentAction {

    void onShoot(int level, Player p, Arrow arrow);

    @Override
    default EnchantmentActionType getType() {
        return EnchantmentActionType.PROJECTILE_SHOOT;
    }

}