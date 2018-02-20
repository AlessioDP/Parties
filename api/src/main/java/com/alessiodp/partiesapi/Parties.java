package com.alessiodp.partiesapi;

import com.alessiodp.partiesapi.interfaces.PartiesAPI;

public final class Parties {
	private static PartiesAPI api = null;
	private static boolean flagHook = false;
	
	private Parties() {
	}
	
	/**
	 * Get the {@link PartiesAPI} instance
	 * 
	 * @return Returns the {@link PartiesAPI} interface
	 * @throws IllegalStateException
	 *             if PartiesAPI has not been initialized, in other words,
	 *             PartiesAPI has not been loaded
	 */
	public static PartiesAPI getApi() throws IllegalStateException {
		flagHook = true;
		if (api == null)
			throw new IllegalStateException("PartiesAPI has not been initialized");
		return api;
	}
	
	/**
	 * Set the Parties API instance. This should not be used.
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
