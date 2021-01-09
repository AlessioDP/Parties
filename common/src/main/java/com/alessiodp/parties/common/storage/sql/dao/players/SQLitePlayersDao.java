package com.alessiodp.parties.common.storage.sql.dao.players;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SQLitePlayersDao extends PlayersDao {
	@Override
	@SqlUpdate(
			"INSERT OR REPLACE INTO `<prefix>players` (`uuid`, `party`, `rank`, `nickname`, `chat`, `spy`, `mute`) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?)"
	)
	void update(String uuid, String party, int rank, String nickname, boolean chat, boolean spy, boolean mute);
}
