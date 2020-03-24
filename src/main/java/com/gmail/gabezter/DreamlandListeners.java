package com.gmail.gabezter;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DreamlandListeners implements Listener {

	Plugin plugin;
	protected static ArrayList<Player> restlessPlayers = new ArrayList<Player>();

	public DreamlandListeners(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		ReadJSON file = new ReadJSON(player.getUniqueId().toString(), plugin);
		if (getWorld(player)) {
			String[] loc = file.bedLocation();
			player.teleport(new Location(plugin.getServer().getWorld(loc[0]), Double.parseDouble(loc[1]),
					Double.parseDouble(loc[2]), Double.parseDouble(loc[3])));
			player.setHealth(file.getHealth());
			player.setFoodLevel(file.getFood());
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(EntityDamageEvent e) {
		if (getWorld(e.getEntity())) {
			if (e.getEntity() instanceof Player) {
				Player player = (Player) e.getEntity();
				if (getDreamWorld(player)) {
					if (e.getCause().equals(DamageCause.FALL))
						e.setCancelled(true);
				}
				double dmg = e.getDamage();
				double pH = player.getHealth();
				if (pH - dmg <= 0) {
					e.setCancelled(true);
					if (getNightmareWorld(player)) {
						if (Dreamland.restless)
							restlessPlayers.add(player);
					}
					ReadJSON file = new ReadJSON(player.getUniqueId().toString(), plugin);
					String[] loc = file.bedLocation();
					player.teleport(new Location(plugin.getServer().getWorld(loc[0]), Double.parseDouble(loc[1]),
							Double.parseDouble(loc[2]), Double.parseDouble(loc[3])));
					player.setHealth(file.getHealth());
					player.setFoodLevel(file.getFood());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSleep(PlayerBedEnterEvent e) {
		final Player player = e.getPlayer();
		final PlayerBedEnterEvent env = e;
		if (!e.isCancelled()) {
			if (restlessPlayers.contains(player)) {
				e.setCancelled(true);
				player.sendMessage(Strings.RESTLESS);
			}
			if (!getWorld(player)) {
				player.sendMessage(Strings.SLEEP);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						if (!env.isCancelled())
							if (isInvEmpty(player)) {
								if (!teleportPlayer(player)) {
									player.sendMessage(Strings.ERROR);
								} else
									env.setCancelled(true);
							} else {
								player.sendMessage(Strings.EMPTY_INV);
							}
					}
				}, 60);
			} else {
				player.sendMessage(Strings.INCEPTION);
				player.sendMessage(Strings.INCEPTION2);
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (getDreamWorld(player)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 10));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3));
			if (Dreamland.allowDreamFlight)
				if (player.getInventory().getItemInMainHand().getType().equals(Dreamland.fly_item))
					player.setAllowFlight(true);
				else
					player.setAllowFlight(false);
		} else if (getNightmareWorld(player)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobSpawn(CreatureSpawnEvent e) {
		Entity entity = e.getEntity();
		if (getWorld(entity) && (e.getSpawnReason() != SpawnReason.CUSTOM)) {
			Location spawnLoc = e.getLocation();
			Random rand = new Random();
			e.setCancelled(true);
			try {
				if (getNightmareWorld(entity)) {
					spawnLoc.getWorld().spawnEntity(spawnLoc, spawnBadMob(rand.nextInt(15)));
				} else if (getDreamWorld(entity)) {
					spawnLoc.getWorld().spawnEntity(spawnLoc, spawnGoodMob(rand.nextInt(11)));
				} else {
					e.setCancelled(false);
				}
			} catch (Exception ex) {
				plugin.getServer().getLogger().warning(ex.getStackTrace().toString());
			}
		}
	}

	private boolean teleportPlayer(Player player) {
		ReadJSON file = new ReadJSON(player.getUniqueId().toString(), plugin);
		String[] loc = { plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getWorld().toString(),
				plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getBlockX() + "",
				plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getBlockY() + "",
				plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getBlockZ() + "" };
		try {
			try {
				loc[0] = player.getBedSpawnLocation().getWorld().toString();
				loc[1] = player.getBedSpawnLocation().getBlockX() + "";
				loc[2] = player.getBedSpawnLocation().getBlockY() + "";
				loc[3] = player.getBedSpawnLocation().getBlockZ() + "";

			} catch (NullPointerException e) {
				loc[0] = plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getWorld().toString();
				loc[1] = plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getBlockX() + "";
				loc[2] = plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getBlockY() + "";
				loc[3] = plugin.getServer().getWorld(Dreamland.mainWorld).getSpawnLocation().getBlockZ() + "";
			}
			file.setData(loc, player.getHealth(), player.getFoodLevel(), file.getOverride());
			if (Dreamland.nightmares) {
				Random rand = new Random();
				if (rand.nextInt(100) + 1 <= Dreamland.nightmare_chance) {
					// TODO Teleport the player to the nightmare world
					player.teleport(plugin.getServer().getWorld("nightmare").getSpawnLocation());
				} else {
					// TODO Teleport the player to the dream world
					player.teleport(plugin.getServer().getWorld("dreams").getSpawnLocation());
				}
				return true;
			} else {
				// TODO Teleport the player to the dream world
				player.teleport(plugin.getServer().getWorld("dreams").getSpawnLocation());
				return true;
			}
		} catch (Exception e) {
			// Error catching
			return false;
		}
	}

	private boolean isInvEmpty(Player player) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null)
				return false;
		}
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item != null)
				return false;
		}
		return true;
	}

	private boolean getWorld(Entity entity) {
		return getDreamWorld(entity) || getNightmareWorld(entity);
	}

	private boolean getNightmareWorld(Entity entity) {
		return (entity.getWorld() == plugin.getServer().getWorld("nightmare"));
	}

	private boolean getDreamWorld(Entity entity) {
		return (entity.getWorld() == plugin.getServer().getWorld("dreams"));
	}

	private EntityType spawnBadMob(int mobNumber) {
		switch (mobNumber) {
		case 0:
			return EntityType.CREEPER;
		case 1:
			return EntityType.PILLAGER;
		case 2:
			return EntityType.ZOMBIE;
		case 3:
			return EntityType.WITCH;
		case 4:
			return EntityType.RAVAGER;
		case 5:
			return EntityType.SPIDER;
		case 6:
			return EntityType.CAVE_SPIDER;
		case 7:
			return EntityType.BLAZE;
		case 8:
			return EntityType.WITHER_SKELETON;
		case 9:
			return EntityType.GHAST;
		case 10:
			return EntityType.ENDERMAN;
		case 11:
			return EntityType.SKELETON;
		case 12:
			return EntityType.GIANT;
		case 13:
			return EntityType.PHANTOM;
		case 14:
		default:
			return EntityType.PIG_ZOMBIE;
		}
	}

	private EntityType spawnGoodMob(int mobNumber) {
		switch (mobNumber) {
		case 0:
			return EntityType.PIG;
		case 1:
			return EntityType.BEE;
		case 2:
			return EntityType.SHEEP;
		case 3:
			return EntityType.CHICKEN;
		case 4:
			return EntityType.OCELOT;
		case 5:
			return EntityType.DONKEY;
		case 6:
			return EntityType.LLAMA;
		case 7:
			return EntityType.FOX;
		case 8:
			return EntityType.MULE;
		case 9:
			return EntityType.SNOWMAN;
		case 10:
		default:
			return EntityType.COW;
		}
	}

}
