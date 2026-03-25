package fr.gameurduxvi.uhc.Particles;

import org.bukkit.Location;
import org.bukkit.Material;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class ParticleRectangle extends DefaultParticle {
	
	Location loc;
	double height;
	double width;
	double yaw;
	double ticks;
	EnumParticle particleType;
	Material mat;
	int data;
	int red;
	int green;
	int blue;
	
	public ParticleRectangle(Location loc, double height, double width, double yaw, double ticks, EnumParticle particleType, Material mat, int data, int red, int green, int blue) {
		super(loc, particleType, red, green, blue, mat, data);
		this.loc = loc;
		this.height = height;
		this.width = width;
		this.yaw = yaw;
		this.ticks = ticks;
		this.particleType = particleType;
		this.mat = mat;
		this.data = data;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	@Override
	public void run() {
		for(int tick = 0; tick <= ticks; tick+=10) {
			for(double height = -(this.height / 2); height <= (this.height / 2); height+=1) {
				Location locb = new Location(loc.getWorld(), loc.getX() + (Math.sin(Math.toRadians(-yaw)) * 2), loc.getY() + height, loc.getZ() + (Math.cos(Math.toRadians(-yaw)) * 2));
				for(double width = -(this.width / 2); width <= (this.width / 2); width+=1) {
					Location pLoc = new Location(locb.getWorld(), locb.getX() + (Math.sin(Math.toRadians(-yaw + 90)) * width), loc.getY() + height, locb.getZ() + (Math.cos(Math.toRadians(-yaw + 90)) * width));;
					
					
					//ArmorStand b = (ArmorStand) pLoc.getWorld().spawnCreature(pLoc, EntityType.ARMOR_STAND);
					//b.setGravity(false);
					
					new DelayedParticle(new DefaultParticle(pLoc, particleType, red, green, blue, mat, data), tick);
				}
			}
		}
	}
}
