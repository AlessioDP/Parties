package com.alessiodp.parties.common.storage.sql.dao.players;

import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PostgreSQLPlayersDao extends PlayersDao {
	@Override
	@SqlUpdate(
			"INSERT INTO <prefix>players (\"uuid\", \"party\", \"rank\", \"nickname\", \"chat\", \"spy\", \"mute\") " +
					"VALUES (?, ?, ?, ?, ?, ?, ?) " +
					"ON CONFLICT (\"uuid\") DO UPDATE SET party=EXCLUDED.party, rank=EXCLUDED.rank, nickname=EXCLUDED.nickname, chat=EXCLUDED.chat, spy=EXCLUDED.spy, mute=EXCLUDED.mute"
	)
	void update(String uuid, String party, int rank, String nickname, boolean chat, boolean spy, boolean mute);
	
	@Override
	@SqlUpdate("DELETE FROM <prefix>players WHERE \"uuid\" = ?")
	void remove(String uuid);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>players WHERE \"uuid\" = ?")
	@RegisterRowMapper(PartyPlayerRowMapper.class)
	Optional<PartyPlayerImpl> get(String uuid);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>players WHERE \"party\" = ?")
	@RegisterRowMapper(UUIDRowMapper.class)
	Set<UUID> getInParty(String party);
	
	@Override
	@SqlQuery("SELECT count(*) FROM <prefix>players")
	int countAll();
	
	@Override
	@SqlQuery("SELECT count(*) FROM <prefix>players WHERE \"party\" IS NOT NULL AND \"party\" != ''")
	int countAllInParty();
}
