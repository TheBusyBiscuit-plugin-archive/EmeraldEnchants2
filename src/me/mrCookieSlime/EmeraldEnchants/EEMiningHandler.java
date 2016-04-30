package me.mrCookieSlime.EmeraldEnchants;

import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.DigAction;
import me.mrCookieSlime.PrisonUtils.NativeMiningHandler;

public class EEMiningHandler extends NativeMiningHandler {

	@Override
	public List<ItemStack> onBlockBreak(BlockBreakEvent e, List<ItemStack> drops ,ItemStack item) {
		if (e instanceof IgnoredMiningEvent) return drops;
		for (ItemEnchantment enchantment: EmeraldEnchants.getInstance().getRegistry().getEnchantments(e.getPlayer().getItemInHand())) {
			for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
				if (action instanceof DigAction) ((DigAction) action).onDig(enchantment.getLevel(), e.getPlayer(), e.getBlock(), drops);
			}
		}
		return drops;
	}

}
