package com.gmail.gabezter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

public class ReadJSON {

	private String filename;
	private File file;
	private JSONObject jsonData;
	Plugin plugin;

	public ReadJSON(String uuid, Plugin plugin) {
		this.plugin = plugin;
		filename = uuid + ".json";
		file = new File(plugin.getDataFolder() + "/" + filename);
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
				String[] loc = { "World", "0", "0", "0" };
				setData(loc, 20.0, 10, false);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String[] bedLocation() {
		ArrayList<String> loc = new ArrayList<String>();
		try {
			if (jsonData == null) {
				Scanner sc = new Scanner(file);
				String data = sc.nextLine();
				JSONObject json = new JSONObject(data);
				JSONObject locObj = json.getJSONObject("location");
				loc.add(locObj.getString("world"));
				loc.add(locObj.getString("x"));
				loc.add(locObj.getString("y"));
				loc.add(locObj.getString("z"));
				sc.close();
			} else {
				JSONObject locObj = jsonData.getJSONObject("location");
				loc.add(locObj.getString("world"));
				loc.add(locObj.getString("x"));
				loc.add(locObj.getString("y"));
				loc.add(locObj.getString("z"));
			}
		} catch (Exception e) {
			plugin.getServer().getLogger().warning(e.toString());
		}

		return loc.toArray(new String[loc.size()]);
	}

	public double getHealth() {
		double health = 20.0;
		try {
			if (jsonData == null) {
				Scanner sc = new Scanner(file);
				String data = sc.nextLine();
				JSONObject json = new JSONObject(data);
				health = json.getDouble("health");
				sc.close();
			} else {
				health = jsonData.getDouble("health");
			}
		} catch (Exception e) {
			plugin.getServer().getLogger().warning(e.toString());
		}
		return health;
	}

	public int getFood() {
		int food = 10;
		try {
			if (jsonData == null) {
				Scanner sc = new Scanner(file);
				String data = sc.nextLine();
				JSONObject json = new JSONObject(data);
				food = json.getInt("food");
				sc.close();
			} else {
				food = jsonData.getInt("food");
			}
		} catch (Exception e) {
			plugin.getServer().getLogger().warning(e.toString());
		}
		return food;
	}

	public boolean getOverride() {
		boolean override = false;
		try {
			if (jsonData == null) {
				Scanner sc = new Scanner(file);
				String data = sc.nextLine();
				JSONObject json = new JSONObject(data);
				sc.close();
				override = json.getBoolean("override");
			} else {
				override = jsonData.getBoolean("override");
			}
		} catch (Exception e) {
			override = false;
		}
		return override;
	}

	public void setData(String[] loc, double health, int food, boolean override) {
		String data = "";
		try {
			Scanner sc = new Scanner(file);
			data = sc.nextLine();
			JSONObject json = new JSONObject(data);
			sc.close();
			JSONObject JSONLoc = new JSONObject();
			JSONLoc.put("world", loc[0]);
			JSONLoc.put("x", loc[1]);
			JSONLoc.put("y", loc[2]);
			JSONLoc.put("z", loc[3]);
			json.put("location", JSONLoc);
			json.put("health", health);
			json.put("food", food);
			json.put("override", override);
			jsonData = json;
			data = json.toString();
			FileWriter fw = new FileWriter(file);
			fw.write(data);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			plugin.getServer().getLogger().warning(e.toString());
		}
	}
}
