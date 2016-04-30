package me.mrCookieSlime.EmeraldEnchants;

import me.mrCookieSlime.PrisonUtils.PrisonUtils;

public class PrisonUtilsHook {

	public static void init() {
		PrisonUtils.registerMiningHandler(new EEMiningHandler());
	}

}
