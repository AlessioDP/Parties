package com.alessiodp.parties.bungeecord.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Packet {
	/*
	 * [VERSION][SERVER][RANK_NEEDED][RANK_MINIMUM][MESSAGE][INFO]
	 * 
	 * @version = Version of the plugin
	 * @server = Destination
	 * @rank_needed = Rank needed to be followed by the party
	 * @rank_minimum = Rank needed to follow the party
	 * @message = Message for each player
	 * @info = Information of the party (List players)
	 */
	@Getter private String version;
	@Getter private String server;
	@Getter private int rankNeeded;
	@Getter private int rankMinimum;
	@Getter @Setter private String message;
	@Getter @Setter private List<String> info;
	
	public Packet(String version, String server, int rank_needed, int rank_minimum) {
		this.version = version;
		this.server = server;
		this.rankNeeded = rank_needed;
		this.rankMinimum = rank_minimum;
		message = "";
		info = null;
	}
	
	public Packet(DataInputStream data) throws IOException {
		version = data.readUTF();
		server = data.readUTF();
		rankNeeded = data.readInt();
		rankMinimum = data.readInt();
		message = data.readUTF();
		String ar = data.readUTF();
		info = new ArrayList<String>();
		for (String str : ar.split("\t"))
			info.add(str);
	}
	
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(version);
		out.writeUTF(server);
		out.writeInt(rankNeeded);
		out.writeInt(rankMinimum);
		out.writeUTF(message);
		String inf = "";
		if (info != null)
			for (String str : info)
				inf += str + "\t";
		out.writeUTF(inf);
	}
	
	@Override
	public String toString() {
		String l = "";
		for (String str : info) {
			if (l.isEmpty())
				l = str;
			else
				l += "," + str;
		}
		return "[" + version + "][" + server + "][" + rankNeeded + "][" + rankMinimum + "]["+message+"][" + l + "]";
	}
}
