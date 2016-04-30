package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantment {
	
	public enum ApplicableItem {
		
		SWORD(Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD),
		BOW(Material.BOW),
		PICKAXE(Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE),
		SHOVEL(Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE),
		AXE(Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE),
		HOE(Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE),
		FLINT_AND_STEEL(Material.FLINT_AND_STEEL),
		FISHING_ROD(Material.FISHING_ROD),
		SHEARS(Material.SHEARS),
		HELMET(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLD_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET),
		CHESTPLATE(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE),
		LEGGINGS(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLD_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS),
		BOOTS(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS);
		
		List<Material> items;
		
		private ApplicableItem(Material... items) {
			this.items = Arrays.asList(items);
		}
		
		public List<Material> getItems() {
			return items;
		}
	}
	
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
		List<String> lore = new ArrayList<String>();
		for (String line: description) {
			lore.add("§r" + line);
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
				builder.append("§7, §r" + StringUtils.format(item.toString()));
			}
		}
		return builder.toString();
	}

}
