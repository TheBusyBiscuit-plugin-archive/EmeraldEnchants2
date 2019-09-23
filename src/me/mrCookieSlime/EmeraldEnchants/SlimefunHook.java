package me.mrCookieSlime.EmeraldEnchants;

import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class SlimefunHook {

	private SlimefunHook() {}
	
	public static void init() {
		Slimefun.registerGuideHandler(new EEGuideHandler());
	}

}
