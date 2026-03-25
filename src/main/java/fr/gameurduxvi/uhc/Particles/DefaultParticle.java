package fr.gameurduxvi.uhc.Particles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class DefaultParticle implements Runnable {	
	
	Location pLoc;
	EnumParticle pEffect;
	Material mat;
	int data;
	int pOffsetX;
	int pOffsetY;
	int pOffsetZ;
	float pSpeed;
	int pCount;
	int pRadius;

	public DefaultParticle(Location pLoc, EnumParticle pEffect, int pOffsetX, int pOffsetY, int pOffsetZ, Material mat, int data) {
		this.pLoc = pLoc;
		this.pEffect = pEffect;
		this.pOffsetX = pOffsetX;
		this.pOffsetY = pOffsetY;
		this.pOffsetZ = pOffsetZ;
		this.mat = mat;
		this.data = data;
	}	

	@Override
	public void run() {
		
		
		if(pEffect.equals(EnumParticle.BLOCK_CRACK) || pEffect.equals(EnumParticle.ITEM_CRACK)) {
			
			
			
			
			@SuppressWarnings("deprecation")
			PacketPlayOutWorldParticles part = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) pLoc.getX(), (float) pLoc.getY(), (float) pLoc.getZ(), (pOffsetX / 255), (pOffsetY / 255), (pOffsetZ / 255), 1f, 0, mat.getId() /*+ data * 4096*/);
			for(PlayerData pd: Main.getInstance().getPlayersData())
				if(pd.particles)
					((CraftPlayer)pd.getPlayer()).getHandle().playerConnection.sendPacket(part);
				
		}
		else{
			PacketPlayOutWorldParticles part = new PacketPlayOutWorldParticles(pEffect, true, (float) pLoc.getX(), (float) pLoc.getY(), (float) pLoc.getZ(), (pOffsetX / 255), (pOffsetY / 255), (pOffsetZ / 255), 1f, 0, 0);
			for(PlayerData pd: Main.getInstance().getPlayersData())
				if(pd.particles)
					((CraftPlayer)pd.getPlayer()).getHandle().playerConnection.sendPacket(part);
		}
	}
}
