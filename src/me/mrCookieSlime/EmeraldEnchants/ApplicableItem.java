package me.mrCookieSlime.EmeraldEnchants;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public enum ApplicableItem {
	
	SWORD(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD),
	BOW(Material.BOW),
	CARROT_ON_A_STICK(Material.CARROT_ON_A_STICK),
	ELYTRA(Material.ELYTRA),
	CROSSBOW(Material.CROSSBOW),
	PICKAXE(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE),
	SHIELD(Material.SHIELD),
	SHOVEL(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL),
	AXE(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE),
	HOE(Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE),
	FLINT_AND_STEEL(Material.FLINT_AND_STEEL),
	FISHING_ROD(Material.FISHING_ROD),
	SHEARS(Material.SHEARS),
	TRIDENT(Material.TRIDENT),
	
	HELMET(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.TURTLE_HELMET),
	CHESTPLATE(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE),
	LEGGINGS(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS),
	BOOTS(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS);
	
	private List<Material> items;
	
	private ApplicableItem(Material... items) {
		this.items = Arrays.asList(items);
	}
	
	public List<Material> getItems() {
		return items;
	}
}