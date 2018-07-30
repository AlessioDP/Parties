package com.alessiodp.parties.common.storage.interfaces;

import com.alessiodp.parties.common.storage.sql.SQLTable;

import java.sql.Connection;
import java.util.HashMap;

public interface IDatabaseSQL {
	
	void initSQL();
	void stopSQL();
	
	Connection getConnection();
	boolean isFailed();
	
	void handleSchema(HashMap<SQLTable, String> schema);
}
