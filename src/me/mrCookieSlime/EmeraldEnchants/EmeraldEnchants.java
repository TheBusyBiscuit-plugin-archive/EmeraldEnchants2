package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.audio.Soundboard;
import me.mrCookieSlime.CSCoreLibSetup.CSCoreLibLoader;
import me.mrCookieSlime.EmeraldEnchants.CustomEnchantment.ApplicableItem;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.CarryAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.DamageAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.DigAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.HitAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.InteractAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.ProjectileHitAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.ProjectileShootAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.WearAction;

public class EmeraldEnchants extends JavaPlugin implements Listener {
	
	private static EmeraldEnchants instance;
	private Config cfg;
	private EnchantmentRegistry registry;
	private Map<String, CustomEnchantment> map;
	private Map<UUID, List<ItemEnchantment>> bows = new HashMap<UUID, List<ItemEnchantment>>();
	
	private final List<BlockFace> faces = new ArrayList<BlockFace>();
	private final List<Material> blockblacklist = new ArrayList<Material>();
	
	@Override
	public void onEnable() {
		if (new CSCoreLibLoader(this).load()) {
			PluginUtils utils = new PluginUtils(this);
			utils.setupConfig();
			cfg = utils.getConfig();
			
			utils.setupMetrics();
			utils.setupUpdater(99746, getFile());
			
			instance = this;
			map = new HashMap<String, CustomEnchantment>();
			
			blockblacklist.add(Material.BEDROCK);
			blockblacklist.add(Material.ENDER_PORTAL_FRAME);
			blockblacklist.add(Material.PORTAL);
			blockblacklist.add(Material.ENDER_PORTAL);
			blockblacklist.add(Material.BARRIER);
			
			for (BlockFace face: BlockFace.values())  {
				if (!face.equals(BlockFace.SELF)) faces.add(face);
			}
			
			registry = new EnchantmentRegistry() {
				
				@Override
				public void registerEnchantment(String name, int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions) {
					cfg.setDefaultValue(name + ".enabled", true);
					for (ApplicableItem item: applicableItems) {
						for (Material type: item.getItems()) {
							cfg.setDefaultValue("enchantment-table." + type.toString() + "." + name, true);
						}
					}
					cfg.save();
					if (cfg.getBoolean(name + ".enabled")) map.put(StringUtils.format(name), new CustomEnchantment(name, maxLevel, applicableItems, description, actions));
				}

				@Override
				public void registerEnchantment(String name, ItemStack stack,int maxLevel, List<ApplicableItem> applicableItems, List<String> description, EnchantmentAction... actions) {
					this.registerEnchantment(name, maxLevel, applicableItems, description, actions);
					
					CustomEnchantment enchantment = getEnchantmentByID(name);
					if (enchantment != null) enchantment.setItemStack(stack);
				}

				@Override
				public List<ItemEnchantment> getEnchantments(ItemStack item) {
					if (item == null || item.getType() == null || item.getType().equals(Material.AIR) || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return new ArrayList<ItemEnchantment>();
					List<ItemEnchantment> enchantments = new ArrayList<ItemEnchantment>();
					for (String line: item.getItemMeta().getLore()) {
						if (line.startsWith("§e§e§7")) {
							CustomEnchantment enchantment = getEnchantment(ChatColor.stripColor(line.split("§l§e§7 ")[0]));
							if (enchantment != null) enchantments.add(new ItemEnchantment(enchantment, RomanNumberConverter.convertRoman(ChatColor.stripColor(line.split("§l§e§7 ")[1]))));
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
					if (item == null || item.getType() == null || item.getType().equals(Material.AIR)) return;
					List<String> lore = new ArrayList<String>();
					if (!isEnchantmentApplied(item, enchantment.getName())) lore.add("§e§e§7" + enchantment.getDisplayName() + "§l§e§7 " + RomanNumberConverter.convertNumber(level));
					if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
						for (String line: item.getItemMeta().getLore()) {
							if (line.startsWith("§e§e§7")) {
								if (enchantment.getDisplayName().equals(ChatColor.stripColor(line.split("§l§e§7 ")[0]))) {
									if (level > 0) lore.add("§e§e§7" + enchantment.getDisplayName() + "§l§e§7 " + RomanNumberConverter.convertNumber(level));
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
					for (ApplicableItem a: enchantment.getApplicableItems()) {
						if (a.getItems().contains(item.getType())) return true;
					}
					return false;
				}

				@Override
				public int getEnchantmentLevel(ItemStack item, String ench) {
					for (ItemEnchantment enchantment: getEnchantments(item)) {
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
					List<CustomEnchantment>  list = new ArrayList<CustomEnchantment>();
					for (CustomEnchantment enchantment: getEnchantments()) {
						types:
						for (ApplicableItem a: enchantment.getApplicableItems()) {
							if (a.getItems().contains(type)) {
								list.add(enchantment);
								break types;
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
			
			getServer().getPluginManager().registerEvents(new Listener() {
				
				@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
				public void onInteract(PlayerInteractEvent e) {
					if (!registry.isItemInvalid(e.getPlayer().getInventory().getItemInMainHand())) {
						for (ItemEnchantment enchantment: registry.getEnchantments(e.getPlayer().getInventory().getItemInMainHand())) {
							for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
								if (action instanceof InteractAction) ((InteractAction) action).onInteract(enchantment.getLevel(), e.getPlayer(), e);
							}
						}
					}
					
					if (!registry.isItemInvalid(e.getPlayer().getInventory().getItemInOffHand())) {
						for (ItemEnchantment enchantment: registry.getEnchantments(e.getPlayer().getInventory().getItemInOffHand())) {
							for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
								if (action instanceof InteractAction) ((InteractAction) action).onInteract(enchantment.getLevel(), e.getPlayer(), e);
							}
						}
					}
				}
				
				@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
				public void onSwitch(PlayerItemHeldEvent e) {
					Player p = e.getPlayer();
					if (!registry.isItemInvalid(e.getPlayer().getInventory().getItemInMainHand())) {
						for (ItemEnchantment enchantment: registry.getEnchantments(p.getInventory().getItem(e.getNewSlot()))) {
							for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
								if (action instanceof CarryAction) {
									((CarryAction) action).onCarry(enchantment.getLevel(), p, 0);
								}
							}
						}
					}
				}
				
			}, this);
			
			getServer().getPluginManager().registerEvents(new Listener() {
				
				@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
				public void onDamage(EntityDamageEvent e) {
					if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
						if (!registry.isItemInvalid(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand())) {
							for (ItemEnchantment enchantment: registry.getEnchantments(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand())) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof HitAction) ((HitAction) action).onHit(enchantment.getLevel(), (Player) ((EntityDamageByEntityEvent) e).getDamager(), (EntityDamageByEntityEvent) e);
								}
							}
						}
						if (!registry.isItemInvalid(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInOffHand())) {
							for (ItemEnchantment enchantment: registry.getEnchantments(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInOffHand())) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof HitAction) ((HitAction) action).onHit(enchantment.getLevel(), (Player) ((EntityDamageByEntityEvent) e).getDamager(), (EntityDamageByEntityEvent) e);
								}
							}
						}
					}
					else if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Arrow) {
						Arrow arrow = (Arrow) ((EntityDamageByEntityEvent) e).getDamager();
						if (bows.containsKey(arrow.getUniqueId())) {
							for (ItemEnchantment enchantment: bows.get(arrow.getUniqueId())) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof ProjectileHitAction) ((ProjectileHitAction) action).onHit(enchantment.getLevel(), (EntityDamageByEntityEvent) e);
								}
							}
						}
					}
					else if (e.getEntity() instanceof Player) {
						for (ItemStack item: ((Player) e.getEntity()).getInventory().getArmorContents()) {
							if (registry.isItemInvalid(item)) continue;
							for (ItemEnchantment enchantment: registry.getEnchantments(item)) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
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
							for (ItemEnchantment enchantment: enchantments) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof ProjectileShootAction) ((ProjectileShootAction) action).onShoot(enchantment.getLevel(), (Player) e.getEntity(), (Arrow) e.getProjectile());
								}
							}
							bows.put(e.getProjectile().getUniqueId(), enchantments);
						}
					}
					if (!registry.isItemInvalid(((Player) e.getEntity()).getInventory().getItemInOffHand())) {
						List<ItemEnchantment> enchantments = registry.getEnchantments(e.getBow());
						if (!enchantments.isEmpty()) {
							for (ItemEnchantment enchantment: enchantments) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof ProjectileShootAction) ((ProjectileShootAction) action).onShoot(enchantment.getLevel(), (Player) e.getEntity(), (Arrow) e.getProjectile());
								}
							}
							bows.put(e.getProjectile().getUniqueId(), enchantments);
						}
					}
				}
				
				@EventHandler
				public void onArrowHit(final ProjectileHitEvent e) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
						
						@Override
						public void run() {
							if (bows.containsKey(e.getEntity().getUniqueId())) bows.remove(e.getEntity().getUniqueId());
						}
					}, 2L);
				}
				
			}, this);
			
			if (!getServer().getPluginManager().isPluginEnabled("PrisonUtils")) {
				getServer().getPluginManager().registerEvents(new Listener() {
					
					@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
					public void onDig(BlockBreakEvent e) {
						if (e instanceof IgnoredMiningEvent) return;
						if (registry.isItemInvalid(e.getPlayer().getInventory().getItemInMainHand())) return;
						List<ItemStack> comparison = (List<ItemStack>) e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());
						List<ItemStack> drops = (List<ItemStack>) e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());
						
						for (ItemEnchantment enchantment: registry.getEnchantments(e.getPlayer().getInventory().getItemInMainHand())) {
							for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
								if (action instanceof DigAction) ((DigAction) action).onDig(enchantment.getLevel(), e.getPlayer(), e.getBlock(), drops);
							}
						}
						
						if (!comparison.equals(drops)) {
							e.getBlock().setType(Material.AIR);
							for (ItemStack drop: drops) {
								e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
							}
						}
					}
					
				}, this);
			}
			else {
				PrisonUtilsHook.init();
			}
			
			if (getServer().getPluginManager().isPluginEnabled("Slimefun")) SlimefunHook.init();
			
			EnchantmentSetup.setupDefaultEnchantments(registry);
			
			getCommand("ee").setExecutor(new EECommand());
			
			final int delay = cfg.getInt("effect-delay");
			
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				
				@Override
				public void run() {
					for (Player p: Bukkit.getOnlinePlayers()) {
						if (!registry.isItemInvalid(p.getInventory().getItemInMainHand())) {
							for (ItemEnchantment enchantment: registry.getEnchantments(p.getInventory().getItemInMainHand())) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof CarryAction) {
										((CarryAction) action).onCarry(enchantment.getLevel(), p, delay);
									}
								}
							}
						}
						if (!registry.isItemInvalid(p.getInventory().getItemInOffHand())) {
							for (ItemEnchantment enchantment: registry.getEnchantments(p.getInventory().getItemInOffHand())) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof CarryAction) {
										((CarryAction) action).onCarry(enchantment.getLevel(), p, delay);
									}
								}
							}
						}
						for (ItemStack armor: p.getInventory().getArmorContents()) {
							if (registry.isItemInvalid(armor)) continue;
							for (ItemEnchantment enchantment: registry.getEnchantments(armor)) {
								for (EnchantmentAction action: enchantment.getEnchantment().getActions()) {
									if (action instanceof WearAction) {
										((WearAction) action).onWear(enchantment.getLevel(), p, delay);
									}
								}
							}
						}
					}
				}
			}, 0L, delay * 20L);
		}
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
	
	public void createExplosion(Player p, Block block, int level) {
		Location l = block.getLocation();
		Collections.shuffle(faces);
		boolean broken = false;
		for (int i = 0; i < (level / 10) + (level % 10 == 0 ? 0: 1); i++) {
			faces:
			for (BlockFace face: faces) {
				Block b = block.getRelative(face);
				if (b.getType() != Material.AIR && !blockblacklist.contains(b.getType())) {
					if (CSCoreLib.randomizer().nextInt(100 * ((level / 10) + (level % 10 == 0 ? 0: 1))) < level * 10) {
						broken = true;
						IgnoredMiningEvent event = new IgnoredMiningEvent(b, p);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
							for (ItemStack item: b.getDrops()) {
								b.getWorld().dropItemNaturally(l, item);
							}
							b.setType(Material.AIR);
						}
					}
					break faces;
				}
			}
		}
		if (broken) {
			l.getWorld().createExplosion(l, 0F);
			p.playSound(l, Soundboard.getLegacySounds("ENTITY_GENERIC_EXPLODE", "EXPLODE"), 0.3F, 1F);
		}
	}
}
