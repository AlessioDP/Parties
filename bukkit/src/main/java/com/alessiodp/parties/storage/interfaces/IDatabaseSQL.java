package com.alessiodp.parties.storage.interfaces;

import java.sql.Connection;

import com.alessiodp.parties.storage.sql.SQLTable;

public interface IDatabaseSQL {
	
	public void initSQL();
	public void stopSQL();
	
	public Connection getConnection();
	public boolean isFailed();
	
	public void handleSchema();
	public void initTables(Connection connection);
	public void checkUpgrades(Connection connection, SQLTable table);
	public void createTable(Connection connection, SQLTable table);
}
