package me.mrCookieSlime.EmeraldEnchants;

import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EECommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) sendHelpMessage(sender);
		else if (args[0].equalsIgnoreCase("guide")) {
			if (sender instanceof Player) {
				EnchantmentGuide.open((Player) sender);
			}
			else sender.sendMessage(ChatColor.DARK_RED + "Only Players can use this Command");
		}
		else if (args[0].equalsIgnoreCase("list")) {
			if (sender instanceof ConsoleCommandSender || sender.hasPermission("EmeraldEnchants.command.list")) {
				StringBuilder builder = new StringBuilder();
				boolean empty = true;
				for (CustomEnchantment enchantment: EmeraldEnchants.getInstance().getRegistry().getEnchantments()) {
					if (empty) {
						builder.append(ChatColor.AQUA + enchantment.getName());
						empty = false;
					}
					else builder.append(ChatColor.RESET + ", " + ChatColor.AQUA + enchantment.getName());
				}
				sender.sendMessage(builder.toString());
			}
			else sender.sendMessage(ChatColor.DARK_RED + "You do not have Permission to use this Command!");
		}
		else if (args[0].equalsIgnoreCase("enchant") && args.length == 3) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("EmeraldEnchants.command.enchant")) {
					CustomEnchantment enchantment = EmeraldEnchants.getInstance().getRegistry().getEnchantment(StringUtils.format(args[1]));
					if (enchantment == null) {
						sender.sendMessage(ChatColor.DARK_RED + args[1] + " " + ChatColor.RED + "is not a valid Enchantment!");
					}
					else {
						try {
							int level = Integer.parseInt(args[2]);
							ItemStack item = p.getInventory().getItemInMainHand();
							if (item == null || item.getType() == null || item.getType() == Material.AIR) {
								sender.sendMessage(ChatColor.DARK_RED + "You must be holding an item in your Hand!");
							}
							else {
								EmeraldEnchants.getInstance().getRegistry().applyEnchantment(p.getInventory().getItemInMainHand(), enchantment, level);
								sender.sendMessage(ChatColor.AQUA + "Bling Bling! It's so shiny!");
							}
						} catch(NumberFormatException x) {
							sender.sendMessage(ChatColor.DARK_RED + args[2] + " " + ChatColor.RED +  "is not a valid Level!");
						}
					}
				}
				else sender.sendMessage(ChatColor.DARK_RED + "You do not have Permission to use this Command!");
			}
			else sender.sendMessage(ChatColor.DARK_RED + "Only Players can use this Command");
		}
		else {
			sendHelpMessage(sender);
		}
		return true;
	}

	private void sendHelpMessage(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&lEmeraldEnchants v" + EmeraldEnchants.getInstance().getDescription().getVersion() + " by &aTheBusyBiscuit"));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7\u21E8 /ee: &bDisplays this Help Menu"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7\u21E8 /ee guide: &bOpens the Enchantment Guide"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7\u21E8 /ee enchant <Enchantment> <Level>: &bApplies an Enchantment to the Item you are holding"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7\u21E8 /ee list: &bLists all registered EE-Enchantments"));
	}

}
