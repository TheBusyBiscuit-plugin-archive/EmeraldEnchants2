package me.mrCookieSlime.EmeraldEnchants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.TreeCalculator;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.Vein;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.audio.Soundboard;
import me.mrCookieSlime.EmeraldEnchants.CustomEnchantment.ApplicableItem;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.CarryAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.DamageAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.DigAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.HitAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.InteractAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.ProjectileHitAction;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentAction.WearAction;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnchantmentSetup {
	
	
	public static void setupDefaultEnchantments(EnchantmentRegistry registry) {
		Set<PotionEffectType> effect_blacklist = new HashSet<PotionEffectType>();
		Map<PotionEffectType, String> aliases = new HashMap<PotionEffectType, String>();
		effect_blacklist.add(PotionEffectType.HARM);
		effect_blacklist.add(PotionEffectType.HEAL);
		aliases.put(PotionEffectType.CONFUSION, "NAUSEA");
		aliases.put(PotionEffectType.DAMAGE_RESISTANCE, "RESISTANCE");
		aliases.put(PotionEffectType.FAST_DIGGING, "HASTE");
		aliases.put(PotionEffectType.INCREASE_DAMAGE, "STRENGTH");
		aliases.put(PotionEffectType.JUMP, "JUMP_BOOST");
		aliases.put(PotionEffectType.SLOW, "SLOWNESS");
		aliases.put(PotionEffectType.SLOW_DIGGING, "MINING_FATIQUE");
		
		registry.registerEnchantment("FARMER_SPIRIT", new ItemStack(Material.SEEDS), 1, Arrays.asList(ApplicableItem.BOOTS), Arrays.asList("Trampling Crops will never have", "to worry you again"),
			new InteractAction() {

				@Override
				public void onInteract(int level, Player p, PlayerInteractEvent event) {
					if (event.getAction() == Action.PHYSICAL) event.setCancelled(true);
				}
			
			}
		);
		
		registry.registerEnchantment("EXPLOSIVE", new ItemStack(Material.TNT), 1000, Arrays.asList(ApplicableItem.PICKAXE, ApplicableItem.AXE, ApplicableItem.SHOVEL), Arrays.asList("Allows you to mine multiple", "Blocks at once"),
			new DigAction() {

				@Override
				public void onDig(int level, Player p, Block block, List<ItemStack> drops) {
					EmeraldEnchants.getInstance().createExplosion(p, block, level);
				}
					
			}
		);
		
		registry.registerEnchantment("VEIN_MINER", new ItemStack(Material.DIAMOND_PICKAXE), 50, Arrays.asList(ApplicableItem.PICKAXE, ApplicableItem.AXE, ApplicableItem.SHOVEL), Arrays.asList("Allows you to mine all", "Blocks of the same Type", "at once"),
			new DigAction() {

				@Override
				public void onDig(int level, Player p, Block block, List<ItemStack> drops) {
					PlayerInventory.damageItemInHand(p);
					List<Location> blocks = new ArrayList<Location>();
					Vein.calculate(block.getLocation(), block.getLocation(), blocks, level);
					blocks.remove(block.getLocation());
					for (Location l: blocks) {
						IgnoredMiningEvent event = new IgnoredMiningEvent(l.getBlock(), p);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							l.getWorld().playEffect(l, Effect.STEP_SOUND, l.getBlock().getType());
							for (ItemStack item: l.getBlock().getDrops()) {
								drops.add(item);
							}
							l.getBlock().setType(Material.AIR);
						}
					}
				}
					
			}
		);
		
		registry.registerEnchantment("TIMBER", new ItemStack(Material.DIAMOND_AXE), 50, Arrays.asList(ApplicableItem.AXE), Arrays.asList("Allows you to chop all", "Logs of a Tree at once"),
			new DigAction() {

				@Override
				public void onDig(int level, Player p, Block block, List<ItemStack> drops) {
					if (block.getType().equals(Material.LOG) || block.getType().equals(Material.LOG_2)) {
						PlayerInventory.damageItemInHand(p);
						List<Location> blocks = new ArrayList<Location>();
						TreeCalculator.getTree(block.getLocation(), block.getLocation(), blocks);
						blocks.remove(block.getLocation());
						for (Location l: blocks) {
							IgnoredMiningEvent event = new IgnoredMiningEvent(l.getBlock(), p);
							Bukkit.getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								l.getWorld().playEffect(l, Effect.STEP_SOUND, l.getBlock().getType());
								for (ItemStack item: l.getBlock().getDrops()) {
									drops.add(item);
								}
								l.getBlock().setType(Material.AIR);
							}
						}
					}
				}
					
			}
		);
		
		registry.registerEnchantment("ICE_ASPECT", new ItemStack(Material.ICE), 2, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE), Arrays.asList("Slows down and weakens your", "Enemies"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0));
					}
				}
			}
		);
		
		registry.registerEnchantment("VAMPIRISM", new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 4, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE), Arrays.asList("Absorbs a small fraction of your Opponent's Health"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					LivingEntity n = (LivingEntity) e.getEntity();
					double health = p.getHealth() + (n.getHealth() / 20 * level);
					if (health > p.getMaxHealth()) health = p.getMaxHealth();
					p.setHealth(health);
				}
			}
		);
		
		registry.registerEnchantment("PECKISH", new ItemStack(Material.ROTTEN_FLESH), 4, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE), Arrays.asList("Absorbs a small fraction of your Opponent's Food Level"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof Player)) return;
					Player n = (Player) e.getEntity();
					if (p.getFoodLevel() < 20) {
						int food = n.getFoodLevel() / 20 * level;
						if (food > 0) {
							if (food > 20 - p.getFoodLevel()) food = 20 - p.getFoodLevel();
							p.setFoodLevel(p.getFoodLevel() + food);
							n.setFoodLevel(n.getFoodLevel() - food > 0 ? n.getFoodLevel() - food: 0);
						}
					}
				}
			}
		);
		
		registry.registerEnchantment("ARROW_REFLECTOR", new ItemStack(Material.ARROW), 10, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Reflects Arrows "), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (!(e.getEntity() instanceof EntityDamageByEntityEvent)) return;
					if (!(((EntityDamageByEntityEvent) e).getDamager() instanceof Arrow)) return;
					
					if (CSCoreLib.randomizer().nextInt(10) < level) e.setCancelled(true);
				}
			}
		);
		
		registry.registerEnchantment("VENOM", new ItemStack(Material.SPIDER_EYE), 2, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE, ApplicableItem.BOW), Arrays.asList("Poisons your Enemies"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
					}
				}
			},
			new ProjectileHitAction() {

				@Override
				void onHit(int level, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
					}
				}
				
			}
		);
		
		registry.registerEnchantment("ZOMBIE_FLU", new ItemStack(Material.SKULL_ITEM, 1, (short) 2), 2, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE, ApplicableItem.BOW), Arrays.asList("Gives your Enemies Hunger"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 0));
					}
				}
			},
			new ProjectileHitAction() {

				@Override
				void onHit(int level, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 0));
					}
				}
				
			}
		);
		
		registry.registerEnchantment("CONCEALING", new ItemStack(Material.WOOL, 1, (short) 15), 2, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE, ApplicableItem.BOW), Arrays.asList("Gives your Enemies Blindness"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
					}
				}
			},
			new ProjectileHitAction() {

				@Override
				void onHit(int level, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
					}
				}
				
			}
		);
		
		registry.registerEnchantment("PUZZLEMENT", new ItemStack(Material.MELON_SEEDS), 2, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE, ApplicableItem.BOW), Arrays.asList("Gives your Enemies Nausea"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
					}
				}
			},
			new ProjectileHitAction() {

				@Override
				void onHit(int level, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
					}
				}
				
			}
		);
		
		registry.registerEnchantment("WITHERING", new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 2, Arrays.asList(ApplicableItem.SWORD, ApplicableItem.AXE), Arrays.asList("Gives your Enemies the Wither Effect"), 
			new HitAction() {
				
				@Override
				public void onHit(int level, Player p, EntityDamageByEntityEvent e) {
					if (!(e.getEntity() instanceof LivingEntity)) return;
					if (level == 1) {
						if (CSCoreLib.randomizer().nextInt(10) < 4) ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
					}
					else if (CSCoreLib.randomizer().nextInt(10) < 5) {
						((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
					}
				}
			}
		);
		
		registry.registerEnchantment("THERMAL", new ItemStack(Material.LAVA_BUCKET), 4, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Sets Attackers on Fire"), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (!(e.getEntity() instanceof EntityDamageByEntityEvent)) return;
					Entity n = ((EntityDamageByEntityEvent) e).getDamager();
					if (!(n instanceof LivingEntity)) return;
					if (CSCoreLib.randomizer().nextInt(100) < 25 + level * 25) n.setFireTicks(20 * (4 + level * 2));
				}
			}
		);
		
		registry.registerEnchantment("MAGICAL_PROTECTION", new ItemStack(Material.BLAZE_POWDER), 4, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Protection against Potions", "of Harming"), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (e.getCause() == DamageCause.MAGIC) e.setDamage(e.getDamage() / (level + 1));
				}
			}
		);
		
		registry.registerEnchantment("POISONOUS_PROTECTION", new ItemStack(Material.POISONOUS_POTATO), 4, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Protection against Potions", "of Poison"), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (e.getCause() == DamageCause.POISON) e.setDamage(e.getDamage() / (level + 1));
				}
			}
		);
		
		registry.registerEnchantment("WITHER_PROTECTION", new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 4, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Protection against Damage taken", "from the Wither Effect"), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (e.getCause() == DamageCause.WITHER) e.setDamage(e.getDamage() / (level + 1));
				}
			}
		);
		
		registry.registerEnchantment("ENDER_PEARL_PROTECTION", new ItemStack(Material.ENDER_PEARL), 2, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Protection against Damage taken", "from throwing an Enderpearl"), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof EnderPearl) {
						e.setDamage(e.getDamage() / (level + 1));
					}
				}
			}
		);
		
		registry.registerEnchantment("MAGNETIC_AURA", new ItemStack(Material.HOPPER), 10, Arrays.asList(ApplicableItem.BOOTS, ApplicableItem.LEGGINGS, ApplicableItem.CHESTPLATE, ApplicableItem.HELMET), Arrays.asList("Picks up all nearby Items while", "being worn"), 
			new WearAction() {

				@Override
				void onWear(int level, Player p, int delay) {
					boolean sound = false;
					for (Entity n: p.getNearbyEntities(level, level, level)) {
						if (n instanceof Item && !n.hasMetadata("no_pickup")) {
							n.teleport(p);
							sound = true;
						}
					}
					if (sound) p.playSound(p.getLocation(), Soundboard.getLegacySounds("ENTITY_ENDERMEN_TELEPORT", "ENDERMAN_TELEPORT"), 5F, 2F);
				}
			
			}
		);
		
		registry.registerEnchantment("METAL_HEAD", new ItemStack(Material.ANVIL), 4, Arrays.asList(ApplicableItem.HELMET), Arrays.asList("Protection against Damage taken", "from fallen Blocks (e.g. Anvils)"), 
			new DamageAction() {

				@Override
				void onDamage(int level, Player p, EntityDamageEvent e) {
					if (e.getCause() == DamageCause.FALLING_BLOCK) e.setDamage(e.getDamage() / (level + 1));
				}
			}
		);
		
		
		for (final PotionEffectType type: PotionEffectType.values()) {
			if (type != null && !effect_blacklist.contains(type)) {
				String name = aliases.containsKey(type) ? aliases.get(type): type.getName();
				registry.registerEnchantment(name, new ItemStack(Material.POTION), 4, Arrays.asList(ApplicableItem.values()), Arrays.asList("Gives you the Potion Effect", "\"" + StringUtils.format(name) + "\"", "while wearing or carrying an Item", "with this Enchantment"), 
					new CarryAction() {
	
						@Override
						void onCarry(int level, Player p, int delay) {
							final double health = p.getHealth();
							if (p.hasPotionEffect(type)) p.removePotionEffect(type);
							p.addPotionEffect(new PotionEffect(type, delay * 20 + 100, level - 1));
							if (health <= p.getMaxHealth()) p.setHealth(health);
						}
							
					},
					new WearAction() {
	
						@Override
						void onWear(int level, Player p, int delay) {
							final double health = p.getHealth();
							if (p.hasPotionEffect(type)) p.removePotionEffect(type);
							p.addPotionEffect(new PotionEffect(type, delay * 20 + 100, level - 1));
							if (health <= p.getMaxHealth()) p.setHealth(health);
						}
									
					}
				);
			}
		}
	}

}
