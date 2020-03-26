package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.listeners.EnchantmentActionListener;
import me.mrCookieSlime.EmeraldEnchants.listeners.EnchantmentTableListener;

public class EmeraldEnchants extends JavaPlugin implements Listener {

    public static final String LORE_PREFIX = ChatColor.YELLOW + ChatColor.YELLOW.toString() + ChatColor.GRAY;
    public static final String LORE_SUFFIX = ChatColor.BOLD + ChatColor.YELLOW.toString() + ChatColor.GRAY;

    private static EmeraldEnchants instance;
    private Config cfg;
    private EnchantmentRegistry registry;
    private Map<String, CustomEnchantment> map;

    @Override
    public void onEnable() {
        cfg = new Config(this);

        // Setting up bStats
        new Metrics(this, 5675);

        // Setting up the Auto-Updater
        Updater updater = null;

        if (getDescription().getVersion().startsWith("DEV - ")) {
            // If we are using a development build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/EmeraldEnchants2/master");
        }

        if (updater != null && cfg.getBoolean("options.auto-update")) updater.start();

        instance = this;
        map = new HashMap<>();

        registry = new EnchantmentRegistry() {

            @Override
            public void registerEnchantment(String name, int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions) {
                cfg.setDefaultValue(name + ".enabled", true);
                boolean enabled = cfg.getBoolean(name + ".enabled");

                cfg.setDefaultValue("enchantment-table.ENCHANTED_BOOK." + name, enabled);
                for (ApplicableItem item : applicableItems) {
                    for (Material type : item.getItems()) {
                        cfg.setDefaultValue("enchantment-table." + type.toString() + "." + name, enabled);
                    }
                }
                cfg.save();
                if (enabled) {
                    map.put(StringUtils.format(name), new CustomEnchantment(name, maxLevel, applicableItems, description, actions));
                }
            }

            @Override
            public void registerEnchantment(String name, ItemStack stack, int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions) {
                this.registerEnchantment(name, maxLevel, applicableItems, description, actions);

                CustomEnchantment enchantment = getEnchantmentByID(name);
                if (enchantment != null) enchantment.setItemStack(stack);
            }

            @Override
            public List<ItemEnchantment> getEnchantments(ItemStack item) {
                if (item == null || item.getType() == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return new ArrayList<>();
                List<ItemEnchantment> enchantments = new ArrayList<>();
                for (String line : item.getItemMeta().getLore()) {
                    if (line.startsWith(LORE_PREFIX)) {
                        CustomEnchantment enchantment = getEnchantment(ChatColor.stripColor(line.split(LORE_SUFFIX + " ")[0]));
                        if (enchantment != null) enchantments.add(new ItemEnchantment(enchantment, RomanNumberConverter.convertRoman(ChatColor.stripColor(line.split(LORE_SUFFIX + " ")[1]))));
                    }
                }
                return enchantments;
            }

            @Override
            public Collection<CustomEnchantment> getEnchantments() {
                return map.values();
            }

            @Override
            public CustomEnchantment getEnchantment(String ench) {
                return map.get(ench);
            }

            @Override
            public void applyEnchantment(ItemStack item, CustomEnchantment enchantment, int level) {
                if (item == null || item.getType() == null || item.getType() == Material.AIR || level < 0) return;
                List<String> lore = new ArrayList<>();
                if (!isEnchantmentApplied(item, enchantment.getName())) {
                    lore.add(LORE_PREFIX + enchantment.getDisplayName() + LORE_SUFFIX + " " + RomanNumberConverter.convertNumber(level));
                }
                if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                    for (String line : item.getItemMeta().getLore()) {
                        if (line.startsWith(LORE_PREFIX)) {
                            if (enchantment.getDisplayName().equals(ChatColor.stripColor(line.split(LORE_SUFFIX + " ")[0]))) {
                                if (level > 0) {
                                    lore.add(LORE_PREFIX + enchantment.getDisplayName() + LORE_SUFFIX + " " + RomanNumberConverter.convertNumber(level));
                                }
                            }
                            else lore.add(line);
                        }
                        else lore.add(line);
                    }
                }

                ItemMeta im = item.getItemMeta();
                im.setLore(lore);
                item.setItemMeta(im);
            }

            @Override
            public void removeEnchantment(ItemStack item, CustomEnchantment enchantment) {
                this.applyEnchantment(item, enchantment, 0);
            }

            @Override
            public CustomEnchantment getEnchantmentByID(String id) {
                return getEnchantment(StringUtils.format(id));
            }

            @Override
            public boolean isApplicable(ItemStack item, CustomEnchantment enchantment) {
                for (ApplicableItem a : enchantment.getApplicableItems()) {
                    if (a.getItems().contains(item.getType())) return true;
                }
                return false;
            }

            @Override
            public int getEnchantmentLevel(ItemStack item, String ench) {
                for (ItemEnchantment enchantment : getEnchantments(item)) {
                    if (enchantment.getEnchantment().getName().equalsIgnoreCase(ench)) return enchantment.getLevel();
                }
                return 0;
            }

            @Override
            public boolean doesEnchantmentExist(String ench) {
                return map.containsKey(ench) || map.containsKey(StringUtils.format(ench));
            }

            @Override
            public boolean isEnchantmentApplied(ItemStack item, String ench) {
                return getEnchantmentLevel(item, ench) > 0;
            }

            @Override
            public boolean isEnchanted(ItemStack item) {
                return !getEnchantments(item).isEmpty();
            }

            @Override
            public List<CustomEnchantment> getEnchantments(Material type) {
                if (type == Material.ENCHANTED_BOOK) {
                    return new ArrayList<>(getEnchantments());
                }

                List<CustomEnchantment> list = new ArrayList<>();
                for (CustomEnchantment enchantment : getEnchantments()) {
                    for (ApplicableItem a : enchantment.getApplicableItems()) {
                        if (a.getItems().contains(type)) {
                            list.add(enchantment);
                            break;
                        }
                    }
                }
                return list;
            }

            @Override
            public boolean isItemInvalid(ItemStack stack) {
                return stack == null || stack.getType() == Material.AIR || stack.getType() == Material.ENCHANTED_BOOK;
            }
        };

        new EnchantmentTableListener(this);
        new EnchantmentActionListener(this);

        EnchantmentSetup.setupDefaultEnchantments(registry, new Random());

        new EECommand(this, "help", "guide", "list", "enchant", "disenchant");

        final int delay = cfg.getInt("effect-delay");

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new EnchantmentTask(registry, delay), 0L, delay * 20L);
    }

    public Config getCfg() {
        return cfg;
    }

    public EnchantmentRegistry getRegistry() {
        return registry;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static EmeraldEnchants getInstance() {
        return instance;
    }
}
