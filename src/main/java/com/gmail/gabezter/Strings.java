package com.gmail.gabezter;

import org.bukkit.ChatColor;

public class Strings {

	private final static ChatColor RED = ChatColor.RED;
	private final static ChatColor RESET = ChatColor.RESET;
	private final static ChatColor PURPLE = ChatColor.LIGHT_PURPLE;

	protected final static String TAG = RED + "[Dreamland] " + RESET;
	protected final static String EMPTY_INV = TAG + "Please empty your inventory and remove your armor.";
	protected final static String RESTLESS = RED + "That nightmare seem to make you unable to sleep.";
	protected final static String WAKEUP_GOOD = "You wake up feeling well rested.";
	protected final static String WAKEUP_BAD = "You wake up and don't feel so well.";
	protected final static String ERROR = "You are having trouble dreaming.";
	protected final static String INCEPTION = "You can't sleep here!";
	protected final static String INCEPTION2 = "Do you think is this inception or something?";
	protected final static String GEN_DREAM = TAG + "Generating Dreamland world";
	protected final static String GEN_NIGHT = TAG + "Generating Nightmare world";
	protected final static String GEN_ERROR = TAG + "Could not create world";
	protected final static String GEN_DONE = TAG + "World has been created.";
	protected final static String PLAYER_ONLY = TAG + "Only players may issue this command!";
	protected final static String OVERRIDE_ON = TAG + "Override has been turned on. You will now not wake up on time.";
	protected final static String OVERRIDE_OFF = TAG + "Override has been turned off. You will now wake up on time.";
	protected final static String SLEEP = "You fall in to a deep sleep";
	protected final static String PERMISSION = TAG + "You do not have permission for this command.";
	protected final static String INVALID_BED = TAG + "Your home bed was missing or obstructed!";
	protected final static String NIGHTMARE_DISABLED = TAG + "The nightmare world is not enabled";
	protected final static String HELP_MENU_TITLE = PURPLE + "_________Dreamland Commands_________";
	protected final static String[] help_menu = { PURPLE + "/dreamland help        -  Displays this menu.",
			PURPLE + "/dreamland wakeup      -  Wakes up the user from the dream.",
			PURPLE + "/dreamland dream       -  Teleports the user to the dream world.",
			PURPLE + "/dreamland nightmare   -  Teleports the user to the nightmare world.",
			PURPLE + "/dreamland override    -  Allows the user to stay in the sleep worlds" };
}
