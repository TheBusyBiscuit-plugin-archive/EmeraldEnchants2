package me.mrCookieSlime.EmeraldEnchants;

import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SlimefunHook {

	public static void init() {
		Slimefun.registerGuideHandler(new EEGuideHandler());
	}

}
