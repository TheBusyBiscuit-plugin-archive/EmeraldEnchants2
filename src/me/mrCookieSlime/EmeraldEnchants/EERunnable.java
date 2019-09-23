package me.mrCookieSlime.EmeraldEnchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.CarryAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.WearAction;

public class EERunnable implements Runnable {

	private final EnchantmentRegistry registry;
	private final int delay;
	
	public EERunnable(EnchantmentRegistry registry, int delay) {
		this.registry = registry;
		this.delay = delay;
	}
	
	@Override
	public void run() {
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (!registry.isItemInvalid(p.getInventory().getItemInMainHand())) {
				for (ItemEnchantment enchantment: registry.getEnchantments(p.getInventory().getItemInMainHand())) {
					for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
						if (action instanceof CarryAction) {
							((CarryAction) action).onCarry(enchantment.getLevel(), p, delay);
						}
					}
				}
			}
			if (!registry.isItemInvalid(p.getInventory().getItemInOffHand())) {
				for (ItemEnchantment enchantment: registry.getEnchantments(p.getInventory().getItemInOffHand())) {
					for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
						if (action instanceof CarryAction) {
							((CarryAction) action).onCarry(enchantment.getLevel(), p, delay);
						}
					}
				}
			}
			for (ItemStack armor: p.getInventory().getArmorContents()) {
				if (registry.isItemInvalid(armor)) continue;
				for (ItemEnchantment enchantment: registry.getEnchantments(armor)) {
					for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
						if (action instanceof WearAction) {
							((WearAction) action).onWear(enchantment.getLevel(), p, delay);
						}
					}
				}
			}
		}
	}

}
