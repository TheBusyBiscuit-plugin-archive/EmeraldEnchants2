package me.mrCookieSlime.EmeraldEnchants.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;

public class EnchantmentTableListener implements Listener {
	
	private final EmeraldEnchants plugin;
	private final Random random = new Random();
	
	public EnchantmentTableListener(EmeraldEnchants plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		ItemStack item = e.getItem();
		int levels = e.getExpLevelCost();
		
		plugin.getRegistry().getEnchantments(item.getType()).stream()
			.filter(enchantment -> plugin.getCfg().getBoolean("enchantment-table." + item.getType().toString() + "." + enchantment.getName()))
			.collect(Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), list -> {
				Collections.shuffle(list);
				return list;
			}))
			.stream()
			.limit((int) ((levels > 10 ? 2: 1) + random.nextInt(5) * levels / 30F))
			.forEach(enchantment -> {
				plugin.getRegistry().applyEnchantment(item, enchantment, (int) Math.ceil(enchantment.getMaxLevel() * (levels / 30F)));
			});
	}

}
