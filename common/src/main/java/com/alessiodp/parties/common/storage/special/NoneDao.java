package com.alessiodp.parties.common.storage.special;

import com.alessiodp.parties.common.logging.LogLine;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.DatabaseData;
import com.alessiodp.parties.common.storage.StorageType;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoneDao implements IDatabaseDispatcher {
	
	/*
	 * This is just a simple class to avoid to use if to check if the database is None
	 */
	
	public NoneDao() {
		// Nothing to do
	}
	
	@Override
	public void init(StorageType type) {
		// Nothing to do
	}
	
	@Override
	public void stop() {
		// Nothing to do
	}
	
	@Override
	public DatabaseData loadEntireData() {
		return null;
	}
	
	@Override
	public boolean saveEntireData(DatabaseData data) {
		return true;
	}
	
	@Override
	public boolean prepareNewOutput() {
		return true;
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		// Nothing to do
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		return null;
	}
	
	@Override
	public List<PartyPlayerImpl> getPartyPlayersByName(String name) {
		return new ArrayList<>();
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		// Nothing to do
	}
	
	@Override
	public PartyImpl getParty(String party) {
		return null;
	}
	
	@Override
	public void renameParty(String prev, String next) {
		// Nothing to do
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		// Nothing to do
	}
	
	@Override
	public boolean existParty(String party) {
		return false;
	}
	
	@Override
	public List<PartyImpl> getAllParties() {
		return new ArrayList<>();
	}
	
	@Override
	public List<PartyPlayerImpl> getAllPlayers() {
		return new ArrayList<>();
	}
	
	@Override
	public List<PartyImpl> getAllFixed() {
		return new ArrayList<>();
	}
	
	@Override
	public void insertLog(LogLine line) {
		// Nothing to do
	}
}
