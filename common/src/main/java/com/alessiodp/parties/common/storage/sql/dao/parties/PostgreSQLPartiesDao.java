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

public interface PostgreSQLPartiesDao extends PartiesDao {
	@Override
	@SqlUpdate(
			"INSERT INTO <prefix>parties (\"id\", \"name\", \"tag\", \"leader\", \"description\", \"motd\", \"color\", \"kills\", \"password\", \"home\", \"protection\", \"experience\", \"follow\")" +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
					" ON CONFLICT (\"id\") DO" +
					" UPDATE SET name=EXCLUDED.name, tag=EXCLUDED.tag, leader=EXCLUDED.leader, description=EXCLUDED.description, motd=EXCLUDED.motd, color=EXCLUDED.color, kills=EXCLUDED.kills, password=EXCLUDED.password, home=EXCLUDED.home, protection=EXCLUDED.protection, experience=EXCLUDED.experience, follow=EXCLUDED.follow"
	)
	void update(String id, String name, String tag, String leader, String description, String motd, String color, int kills, String password, String home, boolean protection, double experience, boolean follow);
	
	@Override
	@SqlUpdate("DELETE FROM <prefix>parties WHERE \"id\"=?")
	void remove(String id);
	
	@Override
	@SqlQuery("SELECT EXISTS(SELECT * FROM <prefix>parties WHERE \"name\"=?)")
	boolean exists(String name);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>parties WHERE \"id\"=?")
	@RegisterRowMapper(PartyRowMapper.class)
	Optional<PartyImpl> get(String id);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>parties WHERE \"name\"=?")
	@RegisterRowMapper(PartyRowMapper.class)
	Optional<PartyImpl> getByName(String name);
	
	@Override
	@SqlQuery("SELECT EXISTS(SELECT * FROM <prefix>parties WHERE \"tag\"=?) ")
	boolean existsTag(String tag);
	
	@Override
	@SqlQuery("SELECT count(*) FROM <prefix>parties WHERE name NOT IN (<blacklist>)")
	int getListNumber(@BindList("blacklist") List<String> blacklist);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>parties WHERE name NOT IN (<blacklist>) ORDER BY \"name\" ASC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByName(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@Override
	@SqlQuery("SELECT <prefix>parties.*, COUNT(id) AS total" +
			" FROM <prefix>parties" +
			" INNER JOIN <prefix>players ON <prefix>players.party = <prefix>parties.id" +
			" WHERE name NOT IN (<blacklist>)" +
			" GROUP BY id" +
			" ORDER BY total DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByMembers(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>parties WHERE name NOT IN (<blacklist>) ORDER BY \"kills\" DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByKills(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>parties WHERE name NOT IN (<blacklist>) ORDER BY \"experience\" DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByExperience(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@Override
	@SqlQuery("SELECT \"party\", count(*) as \"total\" FROM <prefix>players WHERE party NOT IN (<blacklist>) GROUP BY \"party\" DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(PartyRowMapper.class)
	Set<String> getListByPlayers(@BindList("blacklist") List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@Override
	@SqlQuery("SELECT count(DISTINCT \"party\") FROM <prefix>players WHERE party NOT IN (<blacklist>)")
	int getListNumberByPlayers(@BindList("blacklist") List<String> blacklist);
	
	@Override
	@SqlQuery("SELECT * FROM <prefix>parties WHERE leader IS NULL")
	@RegisterRowMapper(PartyRowMapper.class)
	Set<PartyImpl> getListFixed();
	
	@Override
	@SqlQuery("SELECT count(*) FROM <prefix>parties")
	int countAll();
}
