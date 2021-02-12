package com.alessiodp.parties.common.storage.sql.dao.parties;

import com.alessiodp.parties.common.parties.objects.PartyImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateEngine;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PartiesDao {
	String QUERY_UPDATE = "INSERT INTO `<prefix>parties` (`id`, `name`, `tag`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`)" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
			" ON DUPLICATE KEY" +
			" UPDATE `name`=VALUES(`name`), `tag`=VALUES(`tag`), `leader`=VALUES(`leader`), `description`=VALUES(`description`), `motd`=VALUES(`motd`), `color`=VALUES(`color`), `kills`=VALUES(`kills`), `password`=VALUES(`password`), `home`=VALUES(`home`), `protection`=VALUES(`protection`), `experience`=VALUES(`experience`), `follow`=VALUES(`follow`)";
	
	String QUERY_REMOVE = "DELETE FROM `<prefix>parties` WHERE `id`=?";
	String QUERY_EXISTS = "SELECT EXISTS(SELECT * FROM `<prefix>parties` WHERE `name`=?)";
	String QUERY_GET = "SELECT * FROM `<prefix>parties` WHERE `id`=?";
	String QUERY_GET_BY_NAME = "SELECT * FROM `<prefix>parties` WHERE `name`=?";
	String QUERY_EXISTS_TAG = "SELECT EXISTS(SELECT * FROM `<prefix>parties` WHERE `tag`=?) ";
	String QUERY_GET_LIST_NUMBER = "SELECT count(*) FROM `<prefix>parties` <if(blacklist)>WHERE name NOT IN (<blacklist>)<endif>";
	String QUERY_GET_LIST_BY_NAME = "SELECT * FROM `<prefix>parties` <if(blacklist)>WHERE name NOT IN (<blacklist>)<endif> ORDER BY `name` ASC LIMIT :limit OFFSET :offset";
	String QUERY_GET_LIST_BY_MEMBERS = "SELECT `<prefix>parties`.*, COUNT(id) AS total" +
			" FROM `<prefix>parties`" +
			" INNER JOIN `<prefix>players` ON `<prefix>players`.party = `<prefix>parties`.id" +
			"<if(blacklist)> WHERE name NOT IN (<blacklist>)<endif>" +
			" GROUP BY id" +
			" ORDER BY total DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_LIST_BY_KILLS = "SELECT * FROM `<prefix>parties` <if(blacklist)>WHERE name NOT IN (<blacklist>) <endif>ORDER BY `kills` DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_LIST_BY_EXPERIENCE = "SELECT * FROM `<prefix>parties` <if(blacklist)>WHERE name NOT IN (<blacklist>) <endif>ORDER BY `experience` DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_LIST_FIXED = "SELECT * FROM `<prefix>parties` WHERE leader IS NULL";
	String QUERY_COUNT_ALL = "SELECT count(*) FROM `<prefix>parties`";
	String QUERY_DELETE_ALL = "DELETE FROM `<prefix>parties`";
	
	@SqlUpdate(QUERY_UPDATE)
	void update(String id, String name, String tag, String leader, String description, String motd, String color, int kills, String password, String home, boolean protection, double experience, boolean follow);
	
	@SqlUpdate(QUERY_REMOVE)
	void remove(String id);
	
	@SqlQuery(QUERY_EXISTS)
	boolean exists(String name);
	
	@SqlQuery(QUERY_GET)
	@RegisterRowMapper(PartyRowMapper.class)
	Optional<PartyImpl> get(String id);
	
	@SqlQuery(QUERY_GET_BY_NAME)
	@RegisterRowMapper(PartyRowMapper.class)
	Optional<PartyImpl> getByName(String name);
	
	@SqlQuery(QUERY_EXISTS_TAG)
	boolean existsTag(String tag);
	
	@SqlQuery(QUERY_GET_LIST_NUMBER)
	@UseStringTemplateEngine
	int getListNumber(@BindList(value = "blacklist", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> blacklist);
	
	@SqlQuery(QUERY_GET_LIST_BY_NAME)
	@UseStringTemplateEngine
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByName(@BindList(value = "blacklist", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_LIST_BY_MEMBERS)
	@UseStringTemplateEngine
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByMembers(@BindList(value = "blacklist", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_LIST_BY_KILLS)
	@UseStringTemplateEngine
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByKills(@BindList(value = "blacklist", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_LIST_BY_EXPERIENCE)
	@UseStringTemplateEngine
	@RegisterRowMapper(PartyRowMapper.class)
	LinkedHashSet<PartyImpl> getListByExperience(@BindList(value = "blacklist", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> blacklist, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_LIST_FIXED)
	@RegisterRowMapper(PartyRowMapper.class)
	Set<PartyImpl> getListFixed();
	
	@SqlQuery(QUERY_COUNT_ALL)
	int countAll();
	
	@SqlUpdate(QUERY_DELETE_ALL)
	void deleteAll();
}
