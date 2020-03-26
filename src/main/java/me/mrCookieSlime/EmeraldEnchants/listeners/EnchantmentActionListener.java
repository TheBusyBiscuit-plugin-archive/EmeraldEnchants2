package me.mrCookieSlime.EmeraldEnchants.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentRegistry;
import me.mrCookieSlime.EmeraldEnchants.IgnoredMiningEvent;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.EmeraldEnchants.actions.EnchantmentAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.CarryAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.DamageAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.DigAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.HitAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.InteractAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.ProjectileHitAction;
import me.mrCookieSlime.EmeraldEnchants.actions.implementations.ProjectileShootAction;

public class EnchantmentActionListener implements Listener {

    private final EnchantmentRegistry registry;

    public EnchantmentActionListener(EmeraldEnchants plugin) {
        this.registry = plugin.getRegistry();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        if (!registry.isItemInvalid(e.getPlayer().getInventory().getItemInMainHand())) {
            for (ItemEnchantment enchantment : registry.getEnchantments(e.getPlayer().getInventory().getItemInMainHand())) {
                for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                    if (action instanceof InteractAction) {
                        ((InteractAction) action).onInteract(enchantment.getLevel(), e.getPlayer(), e);
                    }
                }
            }
        }

        if (!registry.isItemInvalid(e.getPlayer().getInventory().getItemInOffHand())) {
            for (ItemEnchantment enchantment : registry.getEnchantments(e.getPlayer().getInventory().getItemInOffHand())) {
                for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                    if (action instanceof InteractAction) {
                        ((InteractAction) action).onInteract(enchantment.getLevel(), e.getPlayer(), e);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwitch(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        if (!registry.isItemInvalid(e.getPlayer().getInventory().getItemInMainHand())) {
            for (ItemEnchantment enchantment : registry.getEnchantments(p.getInventory().getItem(e.getNewSlot()))) {
                for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                    if (action instanceof CarryAction) {
                        ((CarryAction) action).onCarry(enchantment.getLevel(), p, 0);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDig(BlockBreakEvent e) {
        if (e instanceof IgnoredMiningEvent) return;
        if (registry.isItemInvalid(e.getPlayer().getInventory().getItemInMainHand())) return;

        List<ItemStack> comparison = (List<ItemStack>) e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());
        List<ItemStack> drops = (List<ItemStack>) e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());

        for (ItemEnchantment enchantment : registry.getEnchantments(e.getPlayer().getInventory().getItemInMainHand())) {
            for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                if (action instanceof DigAction) {
                    ((DigAction) action).onDig(enchantment.getLevel(), e.getPlayer(), e.getBlock(), drops);
                }
            }
        }

        if (!comparison.equals(drops)) {
            e.getBlock().setType(Material.AIR);

            for (ItemStack drop : drops) {
                if (drop != null && drop.getType() != Material.AIR) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
            if (!registry.isItemInvalid(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand())) {
                for (ItemEnchantment enchantment : registry.getEnchantments(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand())) {
                    for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                        if (action instanceof HitAction) ((HitAction) action).onHit(enchantment.getLevel(), (Player) ((EntityDamageByEntityEvent) e).getDamager(), (EntityDamageByEntityEvent) e);
                    }
                }
            }
            if (!registry.isItemInvalid(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInOffHand())) {
                for (ItemEnchantment enchantment : registry.getEnchantments(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInOffHand())) {
                    for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                        if (action instanceof HitAction) ((HitAction) action).onHit(enchantment.getLevel(), (Player) ((EntityDamageByEntityEvent) e).getDamager(), (EntityDamageByEntityEvent) e);
                    }
                }
            }
        }
        else if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) e).getDamager();
            if (projectile.hasMetadata("emeraldenchants")) {
                for (ItemEnchantment enchantment : (List<ItemEnchantment>) projectile.getMetadata("emeraldenchants").get(0)) {
                    for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                        if (action instanceof ProjectileHitAction) ((ProjectileHitAction) action).onHit(enchantment.getLevel(), (EntityDamageByEntityEvent) e);
                    }
                }
            }
        }
        else if (e.getEntity() instanceof Player) {
            for (ItemStack item : ((Player) e.getEntity()).getInventory().getArmorContents()) {
                if (registry.isItemInvalid(item)) continue;
                for (ItemEnchantment enchantment : registry.getEnchantments(item)) {
                    for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                        if (action instanceof DamageAction) ((DamageAction) action).onDamage(enchantment.getLevel(), (Player) e.getEntity(), e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBowUse(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getProjectile() instanceof Arrow)) return;

        if (!registry.isItemInvalid(((Player) e.getEntity()).getInventory().getItemInMainHand())) {
            List<ItemEnchantment> enchantments = registry.getEnchantments(e.getBow());
            if (!enchantments.isEmpty()) {
                for (ItemEnchantment enchantment : enchantments) {
                    for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                        if (action instanceof ProjectileShootAction) ((ProjectileShootAction) action).onShoot(enchantment.getLevel(), (Player) e.getEntity(), (Arrow) e.getProjectile());
                    }
                }
                e.getProjectile().setMetadata("emeraldenchants", new FixedMetadataValue(EmeraldEnchants.getInstance(), enchantments));
            }
        }
        if (!registry.isItemInvalid(((Player) e.getEntity()).getInventory().getItemInOffHand())) {
            List<ItemEnchantment> enchantments = registry.getEnchantments(e.getBow());
            if (!enchantments.isEmpty()) {
                for (ItemEnchantment enchantment : enchantments) {
                    for (EnchantmentAction action : enchantment.getEnchantment().getActions()) {
                        if (action instanceof ProjectileShootAction) ((ProjectileShootAction) action).onShoot(enchantment.getLevel(), (Player) e.getEntity(), (Arrow) e.getProjectile());
                    }
                }
                e.getProjectile().setMetadata("emeraldenchants", new FixedMetadataValue(EmeraldEnchants.getInstance(), enchantments));
            }
        }
    }

}
