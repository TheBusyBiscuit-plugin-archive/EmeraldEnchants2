package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;

public final class EnchantmentGuide {

    private EnchantmentGuide() {}

    public static void open(Player p) {
        ChestMenu menu = new ChestMenu("&bEnchantment Guide");
        int index = 0;

        for (ApplicableItem item : ApplicableItem.values()) {
            List<CustomEnchantment> enchantments = new ArrayList<>();

            for (CustomEnchantment ench : EmeraldEnchants.getInstance().getRegistry().getEnchantments()) {
                if (ench.getApplicableItems().contains(item)) {
                    enchantments.add(ench);
                }
            }

            menu.addItem(index, new CustomItem(item.getItems().get(0), "&7Tool: &e" + StringUtils.format(item.toString()), "", "&7Contains &6" + enchantments.size() + "&7 Enchantment(s)"));
            menu.addMenuClickHandler(index, (pl, slot, stack, action) -> {
                ChestMenu chestmenu = new ChestMenu("&bEnchantment Guide");
                int i = 0;

                for (CustomEnchantment ench : enchantments) {
                    List<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add(ChatColor.GRAY + ench.getDisplayName() + " " + RomanNumberConverter.convertNumber(1) + "-" + RomanNumberConverter.convertNumber(ench.getMaxLevel()));
                    lore.add("");

                    for (String desc : ench.getDescription()) {
                        lore.add(ChatColor.RESET + desc);
                    }

                    chestmenu.addItem(i, new CustomItem(ench.getItemStack(), "&7Enchantment Info: &r" + ench.getDisplayName(), lore.toArray(new String[lore.size()])));
                    chestmenu.addMenuClickHandler(i, (a, b, c, d) -> false);
                    i++;
                }

                chestmenu.open(p);
                return false;
            });

            index++;
        }

        menu.open(p);
    }

}
