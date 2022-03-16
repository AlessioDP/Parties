package com.alessiodp.parties.common.storage.sql.dao.parties;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PartyRowMapper implements RowMapper<PartyImpl> {
	@Override
	public PartyImpl map(ResultSet rs, StatementContext ctx) throws SQLException {
		PartyImpl ret = ((PartiesPlugin) ADPPlugin.getInstance()).getPartyManager().initializeParty(UUID.fromString(rs.getString("id")));
		ret.setAccessible(true);
		ret.setup(rs.getString("name"), rs.getString("leader"));
		ret.setTag(rs.getString("tag"));
		ret.setDescription(rs.getString("description"));
		ret.setMotd(rs.getString("motd"));
		ret.setColor(((PartiesPlugin) ADPPlugin.getInstance()).getColorManager().searchColorByName(rs.getString("color")));
		ret.setKills(rs.getInt("kills"));
		ret.setPassword(rs.getString("password"));
		ret.getHomes().addAll(PartyHomeImpl.deserializeMultiple(rs.getString("home")));
		ret.setProtection(rs.getBoolean("protection"));
		ret.setExperience(rs.getDouble("experience"));
		ret.setFollowEnabled(rs.getBoolean("follow"));
		boolean isopen = rs.getBoolean("isopen");
		if (!rs.wasNull())
			ret.setOpenNullable(isopen);
		ret.setAccessible(false);
		
		return ret;
	}
}
