package com.alessiodp.parties.storage.interfaces;

import java.sql.Connection;
import java.util.HashMap;

import com.alessiodp.parties.storage.sql.SQLTable;

public interface IDatabaseSQL {
	
	public void initSQL();
	public void stopSQL();
	
	public Connection getConnection();
	public boolean isFailed();
	
	public void handleSchema(HashMap<SQLTable, String> schema);
}
