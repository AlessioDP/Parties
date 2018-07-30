package com.alessiodp.parties.api;

import com.alessiodp.parties.api.interfaces.PartiesAPI;

public final class Parties {
	private static PartiesAPI api = null;
	private static boolean flagHook = false;
	
	private Parties() {
	}
	
	/**
	 * Get the {@link PartiesAPI} instance
	 *
	 * @return Returns the {@link PartiesAPI} interface
	 * @throws IllegalStateException if PartiesAPI has not been initialized, in other words,
	 *                               PartiesAPI has not been loaded
	 */
	public static PartiesAPI getApi() throws IllegalStateException {
		flagHook = true;
		if (api == null)
			throw new IllegalStateException("PartiesAPI has not been initialized");
		return api;
	}
	
	/**
	 * Set the Parties API instance. This should not be used.
	 *
	 * @param instance The PartiesAPI instance.
	 */
	public static void setApi(PartiesAPI instance) {
		api = instance;
	}
	
	/**
	 * Flag to know if Parties has been hooked
	 *
	 * @return Returns true if the API has been hooked at least one time
	 */
	public static boolean isFlagHook() {
		return flagHook;
	}
}
