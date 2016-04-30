package me.mrCookieSlime.EmeraldEnchants;

import java.util.Collection;
import java.util.List;

import me.mrCookieSlime.EmeraldEnchants.CustomEnchantment.ApplicableItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface EnchantmentRegistry {
	
	void registerEnchantment(String name, int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions);

	void registerEnchantment(String name, ItemStack stack, int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions);
	
	Collection<CustomEnchantment> getEnchantments();
	
	CustomEnchantment getEnchantment(String ench);
	
	CustomEnchantment getEnchantmentByID(String id);
	
	List<ItemEnchantment> getEnchantments(ItemStack item);
	
	List<CustomEnchantment> getEnchantments(Material type);
	
	void applyEnchantment(ItemStack item, CustomEnchantment enchantment, int level);
	
	void removeEnchantment(ItemStack item, CustomEnchantment enchantment);
	
	boolean isApplicable(ItemStack item, CustomEnchantment enchantment);
	
	int getEnchantmentLevel(ItemStack item, String ench);
	
	boolean doesEnchantmentExist(String ench);
	
	boolean isEnchantmentApplied(ItemStack item, String ench);

	boolean isEnchanted(ItemStack item);

	boolean isItemInvalid(ItemStack itemInHand);
}
