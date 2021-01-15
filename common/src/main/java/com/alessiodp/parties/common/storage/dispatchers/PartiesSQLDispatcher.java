package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.sql.connection.ConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.H2ConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.MariaDBConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.MySQLConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.PostgreSQLConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.SQLiteConnectionFactory;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;
import com.alessiodp.parties.common.storage.sql.dao.parties.H2PartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.PartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.PostgreSQLPartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.SQLitePartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.players.H2PlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.PlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.PostgreSQLPlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.SQLitePlayersDao;
import org.jdbi.v3.core.Handle;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PartiesSQLDispatcher extends SQLDispatcher implements IPartiesDatabase {
	
	protected Class<? extends PlayersDao> playersDao = PlayersDao.class;
	protected Class<? extends PartiesDao> partiesDao = PartiesDao.class;
	
	public PartiesSQLDispatcher(ADPPlugin plugin, StorageType storageType) {
		super(plugin, storageType);
	}
	
	@Override
	public ConnectionFactory initConnectionFactory() {
		ConnectionFactory ret = null;
		switch (storageType) {
			case MARIADB:
				ret = new MariaDBConnectionFactory();
				((MariaDBConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				((MariaDBConnectionFactory) ret).setCharset(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET);
				((MariaDBConnectionFactory) ret).setServerName(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS);
				((MariaDBConnectionFactory) ret).setPort(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT);
				((MariaDBConnectionFactory) ret).setDatabaseName(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE);
				((MariaDBConnectionFactory) ret).setUsername(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME);
				((MariaDBConnectionFactory) ret).setPassword(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD);
				((MariaDBConnectionFactory) ret).setMaximumPoolSize(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE);
				((MariaDBConnectionFactory) ret).setMaxLifetime(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME);
				break;
			case MYSQL:
				ret = new MySQLConnectionFactory();
				((MySQLConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				((MySQLConnectionFactory) ret).setCharset(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET);
				((MySQLConnectionFactory) ret).setServerName(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS);
				((MySQLConnectionFactory) ret).setPort(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT);
				((MySQLConnectionFactory) ret).setDatabaseName(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE);
				((MySQLConnectionFactory) ret).setUsername(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME);
				((MySQLConnectionFactory) ret).setPassword(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD);
				((MySQLConnectionFactory) ret).setMaximumPoolSize(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE);
				((MySQLConnectionFactory) ret).setMaxLifetime(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME);
				((MySQLConnectionFactory) ret).setUseSSL(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL);
				break;
			case POSTGRESQL:
				ret = new PostgreSQLConnectionFactory();
				((PostgreSQLConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				((PostgreSQLConnectionFactory) ret).setCharset(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET);
				((PostgreSQLConnectionFactory) ret).setServerName(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS);
				((PostgreSQLConnectionFactory) ret).setPort(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT);
				((PostgreSQLConnectionFactory) ret).setDatabaseName(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE);
				((PostgreSQLConnectionFactory) ret).setUsername(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME);
				((PostgreSQLConnectionFactory) ret).setPassword(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD);
				((PostgreSQLConnectionFactory) ret).setMaximumPoolSize(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE);
				((PostgreSQLConnectionFactory) ret).setMaxLifetime(ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME);
				playersDao = PostgreSQLPlayersDao.class;
				partiesDao = PostgreSQLPartiesDao.class;
				break;
			case SQLITE:
				ret = new SQLiteConnectionFactory(plugin, plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE));
				((SQLiteConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				playersDao = SQLitePlayersDao.class;
				partiesDao = SQLitePartiesDao.class;
				break;
			case H2:
				ret = new H2ConnectionFactory(plugin, plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_H2_DBFILE));
				((H2ConnectionFactory) ret).setTablePrefix(ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX);
				playersDao = H2PlayersDao.class;
				partiesDao = H2PartiesDao.class;
				break;
			default:
				// Unsupported storage type
		}
		return ret;
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		if (player.getPartyId() != null || player.isSpy() || player.isMuted()) {
			this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(playersDao).update(
					player.getPlayerUUID().toString(),
					player.getPartyId() != null ? player.getPartyId().toString() : null,
					player.getPartyId() != null ? player.getRank() : 0,
					player.getPartyId() != null ? player.getNickname() : null,
					player.isChatParty(),
					player.isSpy(),
					player.isMuted()
			));
		} else {
			this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(playersDao).remove(player.getPlayerUUID().toString()));
		}
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(playersDao).get(uuid.toString())).orElse(null);
	}
	
	@Override
	public int getListPlayersInPartyNumber() {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(playersDao).countAllInParty());
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(partiesDao).update(
				party.getId().toString(),
				party.getName(),
				party.getTag(),
				party.getLeader() != null ? party.getLeader().toString() : null,
				party.getDescription(),
				party.getMotd(),
				party.getColor() != null ? party.getColor().getName() : null,
				party.getKills(),
				party.getPassword(),
				PartyHomeImpl.serializeMultiple(party.getHomes()),
				party.getProtection(),
				party.getExperience(),
				party.isFollowEnabled()
		));
	}
	
	@Override
	public PartyImpl getParty(UUID id) {
		PartyImpl ret = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).get(id.toString())).orElse(null);
		if (ret != null) {
			ret.setAccessible(true);
			ret.setMembers(this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(playersDao).getInParty(id.toString())));
			ret.setAccessible(false);
		}
		return ret;
	}
	
	@Override
	public PartyImpl getPartyByName(String name) {
		PartyImpl ret = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getByName(name)).orElse(null);
		if (ret != null) {
			ret.setAccessible(true);
			ret.setMembers(this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(playersDao).getInParty(ret.getId().toString())));
			ret.setAccessible(false);
		}
		return ret;
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		this.connectionFactory.getJdbi().useHandle(handle -> handle.attach(partiesDao).remove(party.getId().toString()));
	}
	
	@Override
	public boolean existsParty(String name) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).exists(name));
	}
	
	@Override
	public boolean existsTag(String tag) {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).existsTag(tag));
	}
	
	@Override
	public LinkedHashSet<PartyImpl> getListParties(PartiesDatabaseManager.ListOrder order, int limit, int offset) {
		LinkedHashSet<PartyImpl> ret;
		List<String> blacklist = ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES;
		
		if (order == PartiesDatabaseManager.ListOrder.NAME)
			ret = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getListByName(blacklist, limit, offset));
		else if (order == PartiesDatabaseManager.ListOrder.MEMBERS)
			ret = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getListByMembers(blacklist, limit, offset));
		else if (order == PartiesDatabaseManager.ListOrder.KILLS)
			ret = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getListByKills(blacklist, limit, offset));
		else if (order == PartiesDatabaseManager.ListOrder.EXPERIENCE)
			ret = this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getListByExperience(blacklist, limit, offset));
		else
			throw new IllegalStateException("Cannot get the list of parties with the order" + order.name());
		
		// Load members
		for (PartyImpl party : ret) {
			party.setAccessible(true);
			party.setMembers(this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(playersDao).getInParty(party.getId().toString())));
			party.setAccessible(false);
		}
		
		return ret;
	}
	
	@Override
	public int getListPartiesNumber() {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getListNumber(ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES));
	}
	
	@Override
	public Set<PartyImpl> getListFixed() {
		return this.connectionFactory.getJdbi().withHandle(handle -> handle.attach(partiesDao).getListFixed());
	}
	
	@Override
	protected int getBackwardMigration() {
		switch (storageType) {
			case H2:
			case MARIADB:
				return -1;
			case MYSQL:
			case SQLITE:
			default:
				return 0;
		}
	}
	
	@Override
	public void performMigration(Handle transaction, LinkedList<String> queries, int version) {
		if ((storageType == StorageType.SQLITE || storageType == StorageType.MYSQL) && version == 0)
			performMySQLiteBackwardMigration(transaction, queries);
		else
			super.performMigration(transaction, queries, version);
	}
	
	// Both MySQL and SQLite
	private void performMySQLiteBackwardMigration(Handle transaction, LinkedList<String> queries) {
		// Check for migrations
		boolean existsPartiesList;
		if (storageType == StorageType.SQLITE) {
			existsPartiesList = transaction.createQuery(queries.get(0))
					.bind("table", ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX + "parties")
					.mapToMap().list().size() > 0;
		} else {
			// MySQL
			existsPartiesList = transaction.createQuery(queries.get(0))
					.bind("table", ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX + "parties")
					.bind("database", ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE)
					.mapToMap().list().size() > 0;
		}
		if (existsPartiesList) {
			plugin.getLoggerManager().log(
					storageType == StorageType.SQLITE ? PartiesConstants.DEBUG_MIGRATE_SQLITE : PartiesConstants.DEBUG_MIGRATE_MYSQL,
					true);
			
			// Make the new table with a different name
			transaction.execute(queries.get(1));
			
			HashMap<String, String> idParties = new HashMap<>();
			
			transaction.createQuery(queries.get(2)).mapToMap().forEach(party -> {
				String newUuid = UUID.randomUUID().toString();
				idParties.put(party.get("name").toString(), newUuid);
				transaction.execute(queries.get(3),
						newUuid, // UUID
						party.get("name"),
						"", // Tag
						party.get("leader"),
						party.get("description"),
						party.get("motd"),
						party.get("color"),
						party.get("kills"),
						party.get("password"),
						"default," + party.get("home") + ",",
						party.get("protection"),
						party.get("experience"),
						party.get("follow")
				);
			});
			
			transaction.execute(queries.get(4));
			
			transaction.createQuery(queries.get(5)).mapToMap().forEach(player -> {
				String party = !player.get("party").toString().isEmpty() ? idParties.get(player.get("party").toString()) : null;
				transaction.execute(queries.get(6),
						player.get("uuid"),
						party, // Party name to UUID
						player.get("rank"),
						null, // Nickname
						false, // Chat
						player.get("spy"),
						player.get("mute")
				);
			});
			
			// Delete old tables
			transaction.execute(queries.get(7));
			transaction.execute(queries.get(8));
			transaction.execute(queries.get(9));
			
			// Rename new tables
			transaction.execute(queries.get(10));
			transaction.execute(queries.get(11));
		}
	}
}
