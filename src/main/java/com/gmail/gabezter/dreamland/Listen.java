package com.gmail.gabezter.dreamland;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class Listen implements Listener {

	Main plugin;

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		final Player player = e.getPlayer();
		if (player.getSleepTicks() == 60) {
			player.eject();
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){

					public void run() {
						player.teleport(player.getLocation().add(0, 6, 0));
						player.sendMessage("So Bored");
						
						
					}
					
				}, 1L);
		}
	}

}
