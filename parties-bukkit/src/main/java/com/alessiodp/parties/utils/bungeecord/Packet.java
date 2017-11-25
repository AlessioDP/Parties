package com.alessiodp.parties.utils.bungeecord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private String version;
	private String server;
	private int rank_needed;
	private int rank_minimum;
	private String message;
	private List<String> info;
	
	public Packet(String a, String b, int c, int d, String f, List<String> g) {
		version = a;
		server = b;
		rank_needed = c;
		rank_minimum = d;
		message = f;
		info = g;
	}
	public Packet(DataInputStream data) throws IOException {
		version = data.readUTF();
		server = data.readUTF();
		rank_needed = data.readInt();
		rank_minimum = data.readInt();
		message = data.readUTF();
		String ar = data.readUTF();
		info = new ArrayList<String>();
		for (String str : ar.split("\t"))
			info.add(str);
	}
	
	
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(version);
		out.writeUTF(server);
		out.writeInt(rank_needed);
		out.writeInt(rank_minimum);
		out.writeUTF(message);
		String inf = "";
		if (info != null)
			for (String str : info)
				inf += str + "\t";
		out.writeUTF(inf);
	}
	public String toString() {
		String l = "";
		for (String str : info) {
			if (l.isEmpty())
				l = str;
			else
				l += "," + str;
		}
		return "[" + version + "][" + server + "][" + rank_needed + "][" + rank_minimum + "]["+message+"][" + l + "]";
	}
	public String getVersion() {return version;}
	public String getServer() {return server;}
	public int getRankNeeded() {return rank_needed;}
	public int getRankMinimum() {return rank_minimum;}
	public String getMessage() {return message;}
	public void setMessage(String msg) {message = msg;}
	public List<String> getInfo() {return info;}
	public void setInfo(List<String> a) {info = a;}
}
