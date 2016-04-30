package me.mrCookieSlime.EmeraldEnchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

public class EEEnchantmentTable implements Listener {
	
	public EEEnchantmentTable(EmeraldEnchants plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		ItemStack item = e.getItem();
		
	}
	
	@EventHandler
	public void onPreEnchant(PrepareItemEnchantEvent e) {
		ItemStack item = e.getItem();
		if (EmeraldEnchants.getInstance().getRegistry().isEnchanted(item)) e.setCancelled(true);
	}

}
