package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.parties.api.interfaces.HomeLocation;
import lombok.Getter;
import lombok.Setter;

public class HomeLocationImpl implements HomeLocation {
	@Getter @Setter private String world;
	@Getter @Setter private double x;
	@Getter @Setter private double y;
	@Getter @Setter private double z;
	@Getter @Setter private float yaw;
	@Getter @Setter private float pitch;
	
	public HomeLocationImpl(String serialized) throws Exception {
		String[] split = serialized.split(",");
		world = split[0];
		x = Double.parseDouble(split[1]);
		y = Double.parseDouble(split[2]);
		z = Double.parseDouble(split[3]);
		yaw = Float.parseFloat(split[4]);
		pitch = Float.parseFloat(split[5]);
	}
	
	public HomeLocationImpl(String world, double x, double y, double z, float yaw, float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	@Override
	public String toString() {
		return this.getWorld() + "," +
				this.getX() + "," +
				this.getY() + "," +
				this.getZ() + "," +
				this.getYaw() + "," +
				this.getPitch();
	}
	
	public static HomeLocationImpl deserialize(String serialized) {
		HomeLocationImpl ret = null;
		if (!serialized.isEmpty())
			try {
				ret = new HomeLocationImpl(serialized);
			} catch (Exception ignored) {}
		return ret;
	}
}
