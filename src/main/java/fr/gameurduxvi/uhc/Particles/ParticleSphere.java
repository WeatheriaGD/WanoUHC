package fr.gameurduxvi.uhc.Particles;

import org.bukkit.Location;
import org.bukkit.Material;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class ParticleSphere extends DefaultParticle {
	
	Location pLocoation;
	EnumParticle pEffect;
	int red;
	int green;
	int blue;
	float pSpeed;
	double range;
	double ticks;
	int dgr;
	
	public ParticleSphere(Location loc, EnumParticle pEffect, int red, int green, int blue, float pSpeed, double range, double ticks, int degree, Material mat, int data) {
		super(loc, pEffect, red, green, blue, mat, data);
		this.pEffect = pEffect;
		this.pLocoation = pLoc;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.pSpeed = pSpeed;
		this.range = range;
		this.ticks = ticks;
		this.dgr = degree;
	}
	
	@Override
	public void run() {
		//double phi = 0;
		for(int tick = 0; tick <= ticks; tick+=2) {
			/*phi += Math.PI/10;
			for(double theta = 0; theta <= 2*Math.PI; theta +=Math.PI/40) {
				Location pLoc = loc.clone();
				double r = 1.5;
				double x = r * Math.cos(theta) * Math.sin(phi);
				double y = r * Math.cos(phi) + 1.5;
				double z = r * Math.sin(theta) * Math.sin(phi);
				
				new DelayedParticle(new DefaultParticle(new Location(pLoc.getWorld(), x, y, z), particleType, mat, data, red, green, blue), tick);
			}*/
			
			//double dgrs = Math.cos(Math.toRadians(dgr));
			for(double y1 = -range; y1 <= range; y1+=Math.cos(Math.toRadians(dgr))) {
				for(int degree1 = 0; degree1 <= 360; degree1+=dgr) {
					double x1 = Math.cos(Math.toRadians(degree1));
					double z1 = Math.sin(Math.toRadians(degree1));
					
					Location pLoc = pLocoation.clone();
					
					double sin = Math.sin(Math.acos(y1 / range));
					
					double x = pLoc.getX() + (x1 * sin * range);
					double y = pLoc.getY() + y1;
					double z = pLoc.getZ() + (z1 * sin * range);
					
					new DelayedParticle(new DefaultParticle(new Location(pLoc.getWorld(), x, y, z), pEffect, red, green, blue, mat, data), tick);
				}
			}
			
			for(double z1 = -range; z1 <= range; z1+=Math.cos(Math.toRadians(dgr))) {
				for(int degree1 = 0; degree1 <= 360; degree1+=dgr) {
					double x1 = Math.cos(Math.toRadians(degree1));
					double y1 = Math.sin(Math.toRadians(degree1));
					
					Location pLoc = pLocoation.clone();
					
					double sin = Math.sin(Math.acos(z1 / range));
					
					double x = pLoc.getX() + (x1 * sin * range);
					double y = pLoc.getY() + (y1 * sin * range);
					double z = pLoc.getZ() + z1;

					new DelayedParticle(new DefaultParticle(new Location(pLoc.getWorld(), x, y, z), pEffect, red, green, blue, mat, data), tick);
				}
			}
			
			for(double x1 = -range; x1 <= range; x1+=Math.cos(Math.toRadians(dgr))) {
				for(int degree1 = 0; degree1 <= 360; degree1+=dgr) {
					double y1 = Math.cos(Math.toRadians(degree1));
					double z1 = Math.sin(Math.toRadians(degree1));
					
					Location pLoc = pLocoation.clone();
					
					double sin = Math.sin(Math.acos(x1 / range));
					
					double x = pLoc.getX() + x1;
					double y = pLoc.getY() + (y1 * sin * range);
					double z = pLoc.getZ() + (z1 * sin * range);

					new DelayedParticle(new DefaultParticle(new Location(pLoc.getWorld(), x, y, z), pEffect, red, green, blue, mat, data), tick);
				}
			}	
			
			// HYPERESPACE
			/*
			for(double z1 = -range; z1 <= range; z1+=0.5) {
				for(int degree1 = 0; degree1 <= 360; degree1+=dgr) {
					double x1 = Math.cos(Math.toRadians(degree1));
					double y1 = Math.sin(Math.toRadians(degree1));
					
					for(int degree2 = 0; degree2 <= 180; degree2+=dgr) {
						Location pLoc = loc.clone();
						
						double sin = Math.sin(Math.toRadians(degree2));
						
						double x = pLoc.getX() + (x1 * sin * range);
						double y = pLoc.getY() + (y1 * sin * range);
						double z = pLoc.getZ() + z1;
						
						new DelayedParticle(new DefaultParticle(new Location(pLoc.getWorld(), x, y, z), particleType, mat, data, red, green, blue), i*20);
					}
				}
			}
			*/
			
			
			
			/*for(int degreeH = 0; degreeH <= 180; degreeH+=dgr) {
				i++;
				Location pLoc = loc.clone();
				
				double xH = Math.sin(Math.toRadians(degreeH));
				double zH = Math.cos(Math.toRadians(degreeH));
				
				for(int degreeV = 0; degreeV <= 360; degreeV+=dgr) {
					double yV = Math.sin(Math.toRadians(degreeV));
					double xV = Math.cos(Math.toRadians(degreeV));
					
					double x = pLoc.getX() + ((xH * xV) * range);
					double y = pLoc.getY() + (yV * range);
					double z = pLoc.getZ() + ((zH * range);
					
					new DelayedParticle(new DefaultParticle(new Location(pLoc.getWorld(), x, y, z), particleType, mat, data, red, green, blue), i*20);
				}
			}*/
		}
	}
}
