package me.mrCookieSlime.EmeraldEnchants;

import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.api.GuideHandler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class EEGuideHandler extends GuideHandler {
	
	PlayerRunnable runnable;
	
	public EEGuideHandler() {
		runnable = new PlayerRunnable(-1) {
			
			@Override
			public void run(Player p) {
				EnchantmentGuide.open(p);
			}
		};
	}

	@Override
	public int next(Player p, int index, ChestMenu menu) {
		menu.addItem(index, new CustomItem(new MaterialData(Material.ENCHANTED_BOOK), "§2EmeraldEnchants §a(Enchantment Guide)", "", "§a> Click to view a List of all custom Enchantments"));
		menu.addMenuClickHandler(index, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
				EnchantmentGuide.open(p);
				return false;
			}
		});
		
		return index + 1;
	}

	@Override
	public void addEntry(List<String> text, List<String> tooltip) {
		text.add("§2Enchantment Guide");
		tooltip.add("§aClick to open\n§aEmeraldEnchants' Enchantment Guide");
	}

	@Override
	public int getTier() {
		return 1;
	}

	@Override
	public PlayerRunnable getRunnable() {
		return runnable;
	}

	@Override
	public boolean trackHistory() {
		return false;
	}

}
