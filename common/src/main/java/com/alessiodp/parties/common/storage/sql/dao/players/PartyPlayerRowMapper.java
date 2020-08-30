package com.alessiodp.parties.common.storage.sql.dao.players;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PartyPlayerRowMapper implements RowMapper<PartyPlayerImpl> {
	@Override
	public PartyPlayerImpl map(ResultSet rs, StatementContext ctx) throws SQLException {
		PartyPlayerImpl ret = ((PartiesPlugin) ADPPlugin.getInstance()).getPlayerManager().initializePlayer(UUID.fromString(rs.getString("uuid")));
		ret.setAccessible(true);
		if (rs.getString("party") != null && !rs.getString("party").isEmpty()) {
			ret.setPartyId(UUID.fromString(rs.getString("party")));
			ret.setRank(rs.getInt("rank"));
		}
		ret.setSpy(rs.getBoolean("spy"));
		ret.setMuted(rs.getBoolean("mute"));
		ret.setAccessible(false);
		return ret;
	}
}
