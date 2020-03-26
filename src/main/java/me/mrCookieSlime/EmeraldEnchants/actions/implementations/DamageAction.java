package me.mrCookieSlime.EmeraldEnchants.actions.implementations;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentActionType;

/*
 * EnchantmentAction: DAMAGE
 * Called when a Player gets damaged while
 * wearing an Item that has this
 * Enchantment applied
 * 
 */
@FunctionalInterface
public interface DamageAction extends EnchantmentAction {

    void onDamage(int level, Player p, EntityDamageEvent event);

    @Override
    default EnchantmentActionType getType() {
        return EnchantmentActionType.DAMAGE;
    }

}