package com.alessiodp.parties.common.storage.sql.dao.players;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.util.UUID;

public class UUIDRowMapper implements RowMapper<UUID> {
	@Override
	public UUID map(ResultSet rs, StatementContext ctx) {
		UUID ret = null;
		try {
			ret = UUID.fromString(rs.getString("uuid"));
		} catch (Exception ignored) {}
		return ret;
	}
}
