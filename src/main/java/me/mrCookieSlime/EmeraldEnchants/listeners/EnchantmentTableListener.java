package me.mrCookieSlime.EmeraldEnchants.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Material;
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
        ItemStack item = e.getView().getItem(0);

        if (item.getType() == Material.BOOK) {
            item.setType(Material.ENCHANTED_BOOK);
            item.addUnsafeEnchantments(e.getEnchantsToAdd());
        }

        int levels = e.getExpLevelCost();
        plugin.getRegistry().getEnchantments(item.getType()).stream().filter(enchantment -> plugin.getCfg().getBoolean("enchantment-table." + item.getType().toString() + "." + enchantment.getName())).collect(Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), list -> {
            Collections.shuffle(list);
            return list;
        })).stream().limit((int) ((levels > 10 ? 2 : 1) + random.nextInt(5) * levels / 30F)).forEach(enchantment -> plugin.getRegistry().applyEnchantment(item, enchantment, (int) Math.ceil(enchantment.getMaxLevel() * (levels / 30F))));

        if (item.getType() == Material.ENCHANTED_BOOK) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                ItemStack book = e.getView().getItem(0);
                if (book != null && book.getType() == Material.ENCHANTED_BOOK) {
                    e.getView().setItem(0, item);
                }
            }, 1L);
        }
    }
    /*
     * @EventHandler
     * public void onAnvil(PrepareAnvilEvent e) {
     * ItemStack result = e.getResult();
     * ItemStack book = e.getView().getItem(1);
     * 
     * if (result == null || result.getType() == Material.AIR) return;
     * if (book == null || book.getType() == Material.AIR || book.getType() != Material.ENCHANTED_BOOK) return;
     * 
     * List<ItemEnchantment> enchantments = plugin.getRegistry().getEnchantments(book);
     * 
     * if (!enchantments.isEmpty()) {
     * for (ItemEnchantment enchantment: enchantments) {
     * if (plugin.getRegistry().isApplicable(result, enchantment.getEnchantment())) {
     * plugin.getRegistry().applyEnchantment(result, enchantment.getEnchantment(), enchantment.getLevel());
     * }
     * }
     * }
     * 
     * e.setResult(result);
     * }
     */
}
