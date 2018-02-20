package com.alessiodp.parties.storage.special;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.logging.LogLine;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.storage.DatabaseData;
import com.alessiodp.parties.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class NoneDao implements IDatabaseDispatcher {
	
	/*
	 * This is just a simple class to avoid to use if to check if the database is None
	 */
	
	public NoneDao() {
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
	public void updatePlayer(PartyPlayer player) {
		// Nothing to do
	}
	
	@Override
	public PartyPlayerEntity getPlayer(UUID uuid) {
		return null;
	}
	
	@Override
	public List<PartyPlayer> getPartyPlayersByName(String name) {
		return new ArrayList<PartyPlayer>();
	}
	
	@Override
	public void updateParty(Party party) {
		// Nothing to do
	}
	
	@Override
	public PartyEntity getParty(String party) {
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
	public List<PartyPlayer> getAllPlayers() {
		return new ArrayList<PartyPlayer>();
	}
	
	@Override
	public List<Party> getAllFixed() {
		return new ArrayList<Party>();
	}
	
	@Override
	public void insertLog(LogLine line) {
		// Nothing to do
	}
}
