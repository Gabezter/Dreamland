package com.gmail.gabezter.dreamland;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	Listen listen = new Listen();
	
	public File config = null;
	public FileConfiguration nc = null;

	@Override
	public void onEnable() {

		getServer().getPluginManager().registerEvents(listen, this);
		this.config = new File(this.getDataFolder(), "config.yml");
		this.nc = YamlConfiguration.loadConfiguration(config);

		if (!config.exists()) {
			this.getLogger().info("Gernerating the config.yml file...");
			nc.addDefault("Arena", "");
			nc.options().copyDefaults(true);
			try {
				nc.save(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onDisable() {

	}

	

}
