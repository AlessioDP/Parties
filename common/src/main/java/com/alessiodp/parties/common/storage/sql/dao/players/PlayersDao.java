package com.alessiodp.parties.common.storage.sql.dao.players;

import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PlayersDao {
	String QUERY_UPDATE = "INSERT INTO `<prefix>players` (`uuid`, `party`, `rank`, `nickname`, `chat`, `spy`, `mute`)" +
			" VALUES (?, ?, ?, ?, ?, ?, ?)" +
			" ON DUPLICATE KEY UPDATE `party`=VALUES(`party`), `rank`=VALUES(`rank`), `nickname`=VALUES(`nickname`), `chat`=VALUES(`chat`), `spy`=VALUES(`spy`), `mute`=VALUES(`mute`)";
	String QUERY_REMOVE = "DELETE FROM `<prefix>players` WHERE `uuid` = ?";
	String QUERY_GET = "SELECT * FROM `<prefix>players` WHERE `uuid` = ?";
	String QUERY_GET_IN_PARTY = "SELECT * FROM `<prefix>players` WHERE `party` = ?";
	String QUERY_COUNT_ALL = "SELECT count(*) FROM `<prefix>players`";
	String QUERY_COUNT_ALL_IN_PARTY = "SELECT count(*) FROM `<prefix>players` WHERE `party` IS NOT NULL AND `party` != ''";
	String QUERY_DELETE_ALL = "DELETE FROM `<prefix>players`";
	
	@SqlUpdate(QUERY_UPDATE)
	void update(String uuid, String party, int rank, String nickname, boolean chat, boolean spy, boolean mute);
	
	@SqlUpdate(QUERY_REMOVE)
	void remove(String uuid);
	
	@SqlQuery(QUERY_GET)
	@RegisterRowMapper(PartyPlayerRowMapper.class)
	Optional<PartyPlayerImpl> get(String uuid);
	
	@SqlQuery(QUERY_GET_IN_PARTY)
	@RegisterRowMapper(UUIDRowMapper.class)
	Set<UUID> getInParty(String party);
	
	@SqlQuery(QUERY_COUNT_ALL)
	int countAll();
	
	@SqlQuery(QUERY_COUNT_ALL_IN_PARTY)
	int countAllInParty();
	
	@SqlUpdate(QUERY_DELETE_ALL)
	void deleteAll();
}
