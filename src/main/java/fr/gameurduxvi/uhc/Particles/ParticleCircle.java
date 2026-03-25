package fr.gameurduxvi.uhc.Particles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import fr.gameurduxvi.uhc.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class ParticleCircle extends DefaultParticle {
	Location loc;
	double range;
	double time;
	EnumParticle particleType;
	Material mat;
	int data;
	int red;
	int green;
	int blue;
	int dgr;
	
	public ParticleCircle(Location loc, double range, double time, EnumParticle particleType, Material mat, int data, int red, int green, int blue, int degree) {
		super(loc, particleType,red, green, blue,  mat, data);
		this.loc = loc;
		this.range = range;
		this.time = time;
		this.particleType = particleType;
		this.mat = mat;
		this.data = data;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.dgr = degree;
	}
	
	@Override
	public void run() {
		int times = 0;
		for(int degree=0; degree<=360; degree++) {
			times++;
			if(times >= dgr) {
				times = 0;
				Location poseLoc = loc.clone();
				double sin = Math.sin(Math.toRadians(degree));
				double cos = Math.cos(Math.toRadians(degree));
				
				poseLoc.setX(poseLoc.getX() + (range * sin));
				poseLoc.setZ(poseLoc.getZ() + (range * cos));
				
				new DefaultParticle(poseLoc, particleType, red, green, blue, mat, 0).run();
			}						
		}
		for(double i=0; i<=(time * 4);i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					int times = 0;
					for(int degree=0; degree<=360; degree++) {
						times++;
						if(times >= dgr) {
							times = 0;
							Location poseLoc = loc.clone();
							double sin = Math.sin(Math.toRadians(degree));
							double cos = Math.cos(Math.toRadians(degree));
							
							poseLoc.setX(poseLoc.getX() + (range * sin));
							poseLoc.setZ(poseLoc.getZ() + (range * cos));
							
							new DefaultParticle(poseLoc, particleType, red, green, blue, mat, 0).run();
						}						
					}
				}
			}, (long) (5 * i));
		}
	}
}
