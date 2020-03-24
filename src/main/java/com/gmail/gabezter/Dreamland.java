package com.gmail.gabezter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.gabezter.worldgen.dream.DreamlandGen;
import com.gmail.gabezter.worldgen.nightmare.NightmareGen;

public class Dreamland extends JavaPlugin {

	protected static boolean nightmares = false;
	protected static int nightmare_chance = 25;
	protected static boolean restless = false;
	protected static boolean allowDreamFlight = true;

	protected static String mainWorld = "world";
	protected static Material fly_item = Material.getMaterial("feather");

	@Override
	public void onEnable() {
		saveDefaultConfig();
		setConfigVars();
		getServer().getPluginManager().registerEvents(new DreamlandListeners(this), this);
		try {
			getServer().getLogger().info(Strings.GEN_DREAM);
			getServer().createWorld(dreamLand());
			getServer().getLogger().info(Strings.GEN_DONE);
		} catch (Exception e) {
			getServer().getLogger().info(Strings.GEN_ERROR);
		}
		if (nightmares) {
			try {
				getServer().getLogger().info(Strings.GEN_NIGHT);
				getServer().createWorld(nightmareLand());
				getServer().getLogger().info(Strings.GEN_DONE);
			} catch (Exception e) {
				getServer().getLogger().info(Strings.GEN_ERROR);
			}
		}
		setTime();
		getLogger().info("Dreamland has been enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Dreamland has been disabled.");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String cmd2 = args[0];
		if (cmd.getName().equalsIgnoreCase("dreamland")) {
			if (sender instanceof Player) {
				if (cmd2.equalsIgnoreCase("wakeup")) {
					if (sender.hasPermission("dreamland.wakeup")) {
						try {
							((Player) sender).teleport(((Player) sender).getBedSpawnLocation());
						} catch (NullPointerException e) {
							((Player) sender).teleport(getServer().getWorld(mainWorld).getSpawnLocation());
							sender.sendMessage(Strings.INVALID_BED);
						}
					} else {
						sender.sendMessage(Strings.PERMISSION);
					}
					return true;
				}
				if (cmd2.equalsIgnoreCase("dream")) {
					if (sender.hasPermission("dreamland.cmd.dream")) {
						((Player) sender).teleport(getServer().getWorld("dreams").getSpawnLocation());
					} else {
						sender.sendMessage(Strings.PERMISSION);
					}
					return true;
				}
				if (cmd2.equalsIgnoreCase("nightmare")) {
					if (sender.hasPermission("dreamland.cmd.nightmare")) {
						if(nightmares)
							((Player) sender).teleport(getServer().getWorld("nightmare").getSpawnLocation());
						else
							sender.sendMessage(Strings.NIGHTMARE_DISABLED);
					} else {
						sender.sendMessage(Strings.PERMISSION);
					}
					return true;
				}
				if (cmd2.equalsIgnoreCase("override")) {
					if (sender.hasPermission("dreamland.override")) {
						ReadJSON file = new ReadJSON(((Player) sender).getUniqueId().toString(), this);
						if (file.getOverride())
							sender.sendMessage(Strings.OVERRIDE_OFF);
						else
							sender.sendMessage(Strings.OVERRIDE_ON);
						file.setData(file.bedLocation(), file.getHealth(), file.getFood(), !file.getOverride());
					} else {
						sender.sendMessage(Strings.PERMISSION);
					}
					return true;
				}
			} else {
				sender.sendMessage(Strings.PLAYER_ONLY);
				return true;
			}
			/*
			 * if (cmd2.equalsIgnoreCase("")) { return true; }
			 */
		}
		return false;
	}

	private WorldCreator dreamLand() {
		WorldCreator world = new WorldCreator("dreams");
		world.type(WorldType.CUSTOMIZED);
		world.environment(World.Environment.NORMAL);
		world.generator(new DreamlandGen());
		world.generateStructures(true);
		return world;
	}

	private WorldCreator nightmareLand() {
		WorldCreator world = new WorldCreator("nightmare");
		world.type(WorldType.FLAT);
		world.environment(World.Environment.NETHER);
		world.generateStructures(true);
		world.generator(new NightmareGen());
		return world;
	}

	private void setTime() {
		if (getServer().getWorld("dreams") != null) {
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					Bukkit.getServer().getWorld("dreams").setTime(6000L);
				}
			}, 0L, 20L);
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					long mainWorldtime = Bukkit.getServer().getWorld(mainWorld).getTime();
					long wakeupTime = 23460L;
					long bedTime = 12542L;
					if (mainWorldtime <= bedTime || mainWorldtime >= wakeupTime) {
						wakeup();
						DreamlandListeners.restlessPlayers.clear();
					}
				}
			}, 0L, 500L);
		}
	}

	private void wakeup() {
		Player[] players = Bukkit.getServer().getOnlinePlayers()
				.toArray(new Player[Bukkit.getServer().getOnlinePlayers().size()]);
		for (Player player : players) {
			World world = player.getWorld();
			World dream = getServer().getWorld("dreams");
			World nMare = getServer().getWorld("nightmare");
			player.getInventory().clear();
			ReadJSON file = new ReadJSON(player.getUniqueId().toString(), this);
			if ((world == dream || world == nMare) && (file.getOverride() == false)) {
				player.sendMessage(world.toString() + "     " + file.getOverride());
				for (PotionEffect pe : player.getActivePotionEffects())
					player.removePotionEffect(pe.getType());
				if (world == nMare) {
					player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					player.getActivePotionEffects();
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2));
					player.sendMessage(Strings.WAKEUP_BAD);
				} else {
					player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 1200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 1));
					player.sendMessage(Strings.WAKEUP_GOOD);
				}
				try {
					player.teleport(player.getBedSpawnLocation());
					player.setHealth(file.getHealth());
					player.setFoodLevel(file.getFood());
				} catch (NullPointerException e) {
					player.teleport(getServer().getWorld(mainWorld).getSpawnLocation());
					player.sendMessage(Strings.INVALID_BED);
				}
			}
		}
	}

	private void setConfigVars() {
		nightmares = getConfig().getBoolean("nightmares");
		nightmare_chance = getConfig().getInt("nightmare-ratio");
		restless = getConfig().getBoolean("restless");
//		try {
//			fly_item = Material.getMaterial(getConfig().getString("fly-item"));
//		} catch (Exception e) {
//			fly_item = Material.getMaterial("FEATHER");
//		}
		mainWorld = getConfig().getString("default-world-name");
		allowDreamFlight = getConfig().getBoolean("allow-dream-flight");
	}

}
