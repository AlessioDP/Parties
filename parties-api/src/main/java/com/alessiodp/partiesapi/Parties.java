package com.alessiodp.partiesapi;

import com.alessiodp.partiesapi.interfaces.PartiesAPI;

public final class Parties {
	private static PartiesAPI api = null;
	private static boolean flagHook = false;
	
	/**
	 * Get the PartiesAPI
	 * 
	 * @return PartiesAPI interface
	 */
	public static PartiesAPI getApi() {
		flagHook = true;
		return api;
	}
	
	/**
	 * Set the Parties api, this shouldn't be used
	 */
	public static void setApi(PartiesAPI instance) {
		api = instance;
	}
	
	/**
	 * Flag to know if Parties has been hooked
	 */
	public static boolean isFlagHook() {
		return flagHook;
	}
}
