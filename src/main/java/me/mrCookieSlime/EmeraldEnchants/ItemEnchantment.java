package me.mrCookieSlime.EmeraldEnchants;

public class ItemEnchantment {

    private int level;
    private CustomEnchantment enchantment;

    public ItemEnchantment(CustomEnchantment enchantment, int level) {
        this.level = level;
        this.enchantment = enchantment;
    }

    public int getLevel() {
        return level;
    }

    public CustomEnchantment getEnchantment() {
        return enchantment;
    }

}
