package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.EmeraldEnchants.CustomEnchantment.ApplicableItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class EnchantmentGuide {

	public static void open(Player p) {
		ChestMenu menu = new ChestMenu("§bEnchantment Guide");
		int index = 0;
		for (ApplicableItem item: ApplicableItem.values()) {
			final List<CustomEnchantment> enchantments = new ArrayList<CustomEnchantment>();
			for (CustomEnchantment ench: EmeraldEnchants.getInstance().getRegistry().getEnchantments()) {
				if (ench.getApplicableItems().contains(item)) enchantments.add(ench);
			}
			menu.addItem(index, new CustomItem(new MaterialData(item.getItems().get(0)), "§7Tool: §e" + StringUtils.format(item.toString()), "", "§7Contains §6" + enchantments.size() + "§7 Enchantment(s)"));
			menu.addMenuClickHandler(index, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					ChestMenu menu = new ChestMenu("§bEnchantment Guide");
					int index = 0;
					for (CustomEnchantment ench: enchantments) {
						List<String> lore = new ArrayList<String>();
						lore.add("");
						lore.add("§7" + ench.getDisplayName() + " " + RomanNumberConverter.convertNumber(1) + "-" + RomanNumberConverter.convertNumber(ench.getMaxLevel()));
						lore.add("");
						for (String desc: ench.getDescription()) {
							lore.add("§r" + desc);
						}
						menu.addItem(index, new CustomItem(ench.getItemStack(), "§7Enchantment Info: §r" + ench.getDisplayName(), lore.toArray(new String[lore.size()])));
						menu.addMenuClickHandler(index, new MenuClickHandler() {
							
							@Override
							public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
								return false;
							}
						});
						index++;
					}
					
					menu.open(p);
					return false;
				}
			});
			index++;
		}
		
		menu.open(p);
	}

}
