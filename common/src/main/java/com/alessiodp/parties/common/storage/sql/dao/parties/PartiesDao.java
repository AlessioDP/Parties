package com.alessiodp.parties.common.storage.sql.dao.parties;

import com.alessiodp.parties.common.parties.objects.PartyImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PartiesDao {
	@SqlUpdate(
			"INSERT INTO `<prefix>parties` (`id`, `name`, `tag`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`)" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
			" ON DUPLICATE KEY" +
			" UPDATE `name`=VALUES(`name`), `tag`=VALUES(`tag`), `leader`=VALUES(`leader`), `description`=VALUES(`description`), `motd`=VALUES(`motd`), `color`=VALUES(`color`), `kills`=VALUES(`kills`), `password`=VALUES(`password`), `home`=VALUES(`home`), `protection`=VALUES(`protection`), `experience`=VALUES(`experience`), `follow`=VALUES(`follow`)"
	)
	void update(String id, String name, String tag, String leader, String description, String motd, String color, int kills, String password, String home, boolean protection, double experience, boolean follow);
	
	@SqlUpdate("DELETE FROM `<prefix>parties` WHERE `id`=?")
	void remove(String id);
	
	@SqlQuery("SELECT EXISTS(SELECT * FROM `<prefix>parties` WHERE `name`=?)")
	boolean exists(String name);
	
	@SqlQuery("SELECT * FROM `<prefix>parties` WHERE `id`=?")
	@RegisterRowMapper(PartyRowMapper.class)
	Optional<PartyImpl> get(String id);
	
	@SqlQuery("SELECT * FROM `<prefix>parties` WHERE `name`=?")
	@RegisterRowMapper(PartyRowMapper.class)
	Optional<PartyImpl> getByName(String name);
	
	@SqlQuery("SELECT EXISTS(SELECT * FROM `<prefix>parties` WHERE `tag`=?) ")
	boolean existsTag(String tag);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>parties` WHERE name NOT IN (<blacklist>)")
	int getListNumber(@BindList("blacklist") List<String> blacklist);
	
	@SqlQuery("SELECT * FROM `<prefix>parties` WHERE name NOT IN (<blacklist>) ORDER BY `name` ASC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByName(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT `<prefix>parties`.*, COUNT(id) AS total" +
			" FROM `<prefix>parties`" +
			" INNER JOIN `<prefix>players` ON `<prefix>players`.party = `<prefix>parties`.id" +
			" WHERE name NOT IN (<blacklist>)" +
			" GROUP BY id" +
			" ORDER BY total DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByMembers(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT * FROM `<prefix>parties` WHERE name NOT IN (<blacklist>) ORDER BY `kills` DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByKills(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT * FROM `<prefix>parties` WHERE name NOT IN (<blacklist>) ORDER BY `experience` DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByExperience(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT `party`, count(*) as `total` FROM `<prefix>players` WHERE party NOT IN (<blacklist>) GROUP BY `party` DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	Set<String> getListByPlayers(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT count(DISTINCT `party`) FROM `<prefix>players` WHERE party NOT IN (<blacklist>)")
	int getListNumberByPlayers(@BindList("blacklist") List<String> blacklist);
	
	@SqlQuery("SELECT * FROM `<prefix>parties` WHERE leader IS NULL")
	@RegisterRowMapper(PartyRowMapper.class)
	Set<PartyImpl> getListFixed();
	
	@SqlQuery("SELECT count(*) FROM `<prefix>parties`")
	int countAll();
}
