package com.alessiodp.parties.configuration.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.DatabaseInterface;
import com.alessiodp.parties.utils.LogLine;

public class NoneData implements DatabaseInterface {
	
	/*
	 * This is just a simple class to avoid to use if to check if the database is None
	 */
	
	public NoneData() {
		// Nothing to do
	}
	
	@Override
	public void init() {
		// Nothing to do
	}

	@Override
	public void stop() {
		// Nothing to do
	}

	@Override
	public boolean isFailed() {
		return false;
	}

	@Override
	public void updateSpies(List<UUID> list) {
		// Nothing to do
	}

	@Override
	public List<UUID> getSpies() {
		return new ArrayList<UUID>();
	}

	@Override
	public void updatePlayer(ThePlayer tp) {
		// Nothing to do
	}

	@Override
	public ThePlayer getPlayer(UUID uuid) {
		return null;
	}

	@Override
	public void removePlayer(UUID uuid) {
		// Nothing to do
	}

	@Override
	public String getPlayerPartyName(UUID uuid) {
		return "";
	}

	@Override
	public int getRank(UUID uuid) {
		return -1;
	}

	@Override
	public HashMap<UUID, Object[]> getPlayersRank(String party) {
		return new HashMap<UUID, Object[]>();
	}

	@Override
	public HashMap<UUID, Long> getPlayersFromName(String name) {
		return new HashMap<UUID, Long>();
	}

	@Override
	public String getOldPlayerName(UUID uuid) {
		return "";
	}

	@Override
	public void updateParty(Party party) {
		// Nothing to do
	}

	@Override
	public Party getParty(String party) {
		return null;
	}

	@Override
	public void renameParty(String prev, String next) {
		// Nothing to do
	}

	@Override
	public void removeParty(Party party) {
		// Nothing to do
	}

	@Override
	public boolean existParty(String party) {
		return false;
	}

	@Override
	public List<Party> getAllParties() {
		return new ArrayList<Party>();
	}

	@Override
	public List<String> getAllFixed() {
		return new ArrayList<String>();
	}

	@Override
	public void insertLog(LogLine line) {
		// Nothing to do
	}
}
