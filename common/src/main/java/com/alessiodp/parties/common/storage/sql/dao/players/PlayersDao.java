package com.alessiodp.parties.common.storage.sql.dao.players;

import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PlayersDao {
	@SqlUpdate(
			"INSERT INTO `<prefix>players` (`uuid`, `party`, `rank`, `chat`, `spy`, `mute`) " +
			"VALUES (?, ?, ?, ?, ?, ?) " +
			"ON DUPLICATE KEY UPDATE `party`=VALUES(`party`), `rank`=VALUES(`rank`), `chat`=VALUES(`chat`), `spy`=VALUES(`spy`), `mute`=VALUES(`mute`)"
	)
	void update(String uuid, String party, int rank, boolean chat, boolean spy, boolean mute);
	
	@SqlUpdate("DELETE FROM `<prefix>players` WHERE `uuid` = ?")
	void remove(String uuid);
	
	@SqlQuery("SELECT * FROM `<prefix>players` WHERE `uuid` = ?")
	@RegisterRowMapper(PartyPlayerRowMapper.class)
	Optional<PartyPlayerImpl> get(String uuid);
	
	//@SqlQuery("SELECT * FROM `<prefix>players` WHERE `party`=?")
	//@RegisterRowMapper(BlockDestroyRowMapper.class)
	//Set<PartyPlayerImpl> getInParty(String party);
	
	@SqlQuery("SELECT * FROM `<prefix>players` WHERE `party` = ?")
	@RegisterRowMapper(UUIDRowMapper.class)
	Set<UUID> getInParty(String party);
	
	//@SqlUpdate("UPDATE `<prefix>players` SET `party` = :newParty WHERE `party` = :party")
	//void renameParty(@Bind("party") String party, @Bind("newParty") String newParty);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>players`")
	int countAll();
}
