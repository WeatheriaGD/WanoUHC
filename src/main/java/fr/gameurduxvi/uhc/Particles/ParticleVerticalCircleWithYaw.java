package fr.gameurduxvi.uhc.Particles;

import org.bukkit.Location;
import org.bukkit.Material;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class ParticleVerticalCircleWithYaw extends DefaultParticle {
	
	Location loc;
	double yaw;
	double ticks;
	double range;
	EnumParticle particleType;
	Material mat;
	int data;
	int red;
	int green;
	int blue;
	int degree;
	
	public ParticleVerticalCircleWithYaw(Location loc, double range, double yaw, double ticks, EnumParticle particleType, Material mat, int data, int red, int green, int blue, int degree) {
		super(loc, particleType, red, green, blue, mat, data);
		this.loc = loc;
		this.range = range;
		this.yaw = yaw;
		this.ticks = ticks;
		this.particleType = particleType;
		this.mat = mat;
		this.data = data;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.degree = degree;
	}
	
	@Override
	public void run() {
		for(int tick = 0; tick <= ticks; tick+=5) {			
			Location locb = new Location(loc.getWorld(), loc.getX() + (Math.sin(Math.toRadians(-yaw)) * 2), loc.getY(), loc.getZ() + (Math.cos(Math.toRadians(-yaw)) * 2));			
			for(double degree = 0; degree <= 360; degree+=this.degree) {
				double heigth = Math.sin(Math.toRadians(degree)) * range;
				double width = Math.cos(Math.toRadians(degree)) * range;
				Location pLoc = new Location(locb.getWorld(), locb.getX() + (Math.sin(Math.toRadians(-yaw + 90)) * width), loc.getY() + heigth, locb.getZ() + (Math.cos(Math.toRadians(-yaw + 90)) * width));
				
				new DelayedParticle(new DefaultParticle(pLoc, particleType, red, green, blue, mat, data), tick);
			}
		}
	}
}
