package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;

public class CustomEnchantment {
	
	private String name, display;
	private List<String> description;
	private List<ApplicableItem> applicable;
	private EnchantmentAction[] actions;
	private int maxLevel;
	private ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
	
	public CustomEnchantment(String name, int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions) {
		this.name = name;
		this.maxLevel = maxLevel;
		this.display = StringUtils.format(name);
		
		EmeraldEnchants.getInstance().getCfg().setDefaultValue(name + ".description", description);
		EmeraldEnchants.getInstance().getCfg().save();
		
		this.description = EmeraldEnchants.getInstance().getCfg().getStringList(name + ".description");
		this.applicable = applicableItems;
		this.actions = actions;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return display;
	}
	
	public List<String> getDescription() {
		return this.description;
	}
	
	public EnchantmentAction[] getActions() {
		return actions;
	}
	
	public List<ApplicableItem> getApplicableItems() {
		return applicable;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setItemStack(ItemStack stack) {
		this.stack = stack;
	}
	
	public ItemStack getItemStack() {
		return this.stack;
	}
	
	public List<String> getLore() {
		List<String> lore = new ArrayList<>();
		for (String line: description) {
			lore.add(ChatColor.RESET + line);
		}
		return lore;
	}

	public String listTools() {
		StringBuilder builder = new StringBuilder();
		boolean init = true;
		for (ApplicableItem item: getApplicableItems()) {
			if (init) {
				builder.append(StringUtils.format(item.toString()));
				init = false;
			}
			else {
				builder.append(ChatColor.GRAY + ", " + ChatColor.RESET + StringUtils.format(item.toString()));
			}
		}
		return builder.toString();
	}

}
