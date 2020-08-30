package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.parties.api.interfaces.PartyHome;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
public class PartyHomeImpl implements PartyHome {
	@Getter @Setter private String name;
	@Getter @Setter private String world;
	@Getter @Setter private double x;
	@Getter @Setter private double y;
	@Getter @Setter private double z;
	@Getter @Setter private float yaw;
	@Getter @Setter private float pitch;
	@Getter @Setter private String server;
	
	public PartyHomeImpl(String serialized) throws NumberFormatException {
		String[] split = serialized.split(",");
		name = split[0];
		world = split[1];
		x = Double.parseDouble(split[2]);
		y = Double.parseDouble(split[3]);
		z = Double.parseDouble(split[4]);
		yaw = Float.parseFloat(split[5]);
		pitch = Float.parseFloat(split[6]);
		if (split.length > 7)
			server = split[7];
	}
	
	public PartyHomeImpl(String name, String world, double x, double y, double z, float yaw, float pitch) {
		this(name, world, x, y, z, yaw, pitch, null);
	}
	
	public PartyHomeImpl(String name, String world, double x, double y, double z, float yaw, float pitch, String server) {
		this.name = name;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.server = server;
	}
	
	@Override
	public String toString() {
		return (this.getName() != null ? this.getName() : "") + "," +
				this.getWorld() + "," +
				this.getX() + "," +
				this.getY() + "," +
				this.getZ() + "," +
				this.getYaw() + "," +
				this.getPitch() +
				(this.getServer() != null ? ("," + this.getServer()) : "");
	}
	
	public static String serializeMultiple(Set<? extends PartyHome> homes) {
		StringBuilder sb = new StringBuilder();
		for (PartyHome home : homes) {
			if (sb.length() > 0)
				sb.append(";");
			sb.append(home.toString());
		}
		return sb.toString();
	}
	
	public static PartyHomeImpl deserialize(String serialized) {
		PartyHomeImpl ret = null;
		if (serialized != null && !serialized.isEmpty())
			try {
				ret = new PartyHomeImpl(serialized);
			} catch (Exception ignored) {}
		return ret;
	}
	
	public static Set<? extends PartyHome> deserializeMultiple(String multipleSerialized) {
		HashSet<PartyHomeImpl> ret = new HashSet<>();
		if (multipleSerialized != null && !multipleSerialized.isEmpty()) {
			String[] split = multipleSerialized.split(";");
			for (String s : split) {
				ret.add(deserialize(s));
			}
		}
		return ret;
	}
}
