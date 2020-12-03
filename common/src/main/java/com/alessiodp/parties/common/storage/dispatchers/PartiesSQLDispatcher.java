package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.sql.connection.ConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.H2ConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.MariaDBConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.MySQLConnectionFactory;
import com.alessiodp.core.common.storage.sql.connection.SQLiteConnectionFactory;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;
import com.alessiodp.parties.common.storage.sql.dao.parties.H2PartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.PartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.SQLitePartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.players.H2PlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.PlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.SQLitePlayersDao;

import java.util.LinkedHashSet;
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
}
