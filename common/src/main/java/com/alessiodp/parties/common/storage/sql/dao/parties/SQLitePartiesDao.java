package com.alessiodp.parties.common.storage.sql.dao.parties;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SQLitePartiesDao extends PartiesDao {
	@Override
	@SqlUpdate(
			"INSERT OR REPLACE INTO `<prefix>parties` (`id`, `name`, `tag`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`)" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	)
	void update(String id, String name, String tag, String leader, String description, String motd, String color, int kills, String password, String home, boolean protection, double experience, boolean follow);
}
