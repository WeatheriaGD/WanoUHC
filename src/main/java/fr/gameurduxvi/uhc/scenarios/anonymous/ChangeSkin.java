package fr.gameurduxvi.uhc.scenarios.anonymous;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;

public class ChangeSkin {
	//WrappedSignedProperty skin = new WrappedSignedProperty("textures", Main.instance.SkinValue, Main.instance.SkinSignature);
	
	
	public static final void reloadSkinForSelf(Player player) {
        final EntityPlayer ep = ((CraftPlayer) player).getHandle();
        final PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        final PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
        final Location loc = player.getLocation().clone();
        ep.playerConnection.sendPacket(removeInfo);
        ep.playerConnection.sendPacket(addInfo);
        player.teleport(loc);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(loc);
                ep.playerConnection.sendPacket(new PacketPlayOutRespawn(ep.dimension, ep.getWorld().getDifficulty(), ep.getWorld().getWorldData().getType(), ep.playerInteractManager.getGameMode()));
                player.updateInventory();
                player.teleport(loc);
            }
        }.runTaskLater(Main.getInstance(), 2L);
    }
	
	
	/*private void sendUpdateOthers(Player p) throws FieldAccessException {
        for(Player onlinePlayer: Bukkit.getOnlinePlayers()) {
        	if(!onlinePlayer.equals(p) && onlinePlayer.canSee(p)) {
        		hideAndShow(p, onlinePlayer);
        	}
        }
    }
	
	private void hideAndShow(Player p, Player other) {
		other.hidePlayer(p);
		other.showPlayer(p);
	}
	
	public void selfSkinUpdate(Player p, WrappedGameProfile gameProfile) {
		Optional.ofNullable(p.getVehicle()).ifPresent(Entity::eject);

		PacketContainer removeInfo;
        PacketContainer addInfo;
        PacketContainer respawn;
        PacketContainer teleport;

        try {
            NativeGameMode gamemode = NativeGameMode.fromBukkit(p.getGameMode());
            WrappedChatComponent displayName = WrappedChatComponent.fromText(p.getPlayerListName());
            PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, 0, gamemode, displayName);

            //remove the old skin - client updates it only on a complete remove and add
            removeInfo = new PacketContainer(PLAYER_INFO);
            removeInfo.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
            removeInfo.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

            //add info containing the skin data
            addInfo = removeInfo.deepClone();
            addInfo.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);

            // Respawn packet - notify the client that it should update the own skin
            respawn = createRespawnPacket(p, gamemode);

            //prevent the moved too quickly message
            teleport = createTeleportPacket(p.getLocation().clone());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return;
        }
        
        sendPackets(p, removeInfo, addInfo, respawn, teleport);

        p.setExp(p.getExp());
        p.setWalkSpeed(p.getWalkSpeed());
        p.updateInventory();

        PlayerInventory inventory = p.getInventory();
        inventory.setHeldItemSlot(inventory.getHeldItemSlot());

        try {
            p.getClass().getDeclaredMethod("updateScaledHealth").invoke(p);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
	}
	private PacketContainer createTeleportPacket(Location location) {
        PacketContainer teleport = new PacketContainer(POSITION);
        teleport.getModifier().writeDefaults();

        teleport.getDoubles().write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        teleport.getFloat().write(0, location.getYaw())
                .write(1, location.getPitch());

        //send an invalid teleport id in order to let Bukkit ignore the incoming confirm packet
        teleport.getIntegers().writeSafely(0, -1337);
        return teleport;
    }
	
	private PacketContainer createRespawnPacket(Player p, NativeGameMode gamemode) throws ReflectiveOperationException {
		PacketContainer respawn = new PacketContainer(RESPAWN);
		
		Difficulty difficulty = EnumWrappers.getDifficultyConverter().getSpecific(p.getWorld().getDifficulty());
		
		//<= 1.13.1
		@SuppressWarnings("deprecation")
		int dimensionId = p.getWorld().getEnvironment().getId();
		respawn.getIntegers().writeSafely(0, dimensionId);
		
		//> 1.13.1
		
		if (MinecraftVersion.getCurrentVersion().compareTo(MinecraftVersion.AQUATIC_UPDATE) > 0) {
			try {
				respawn.getDimensions().writeSafely(0, dimensionId);
			} catch (NoSuchMethodError noSuchMethodError) {
				throw new ReflectiveOperationException("Unable to find dimension setter. " +
				"Your ProtocolLib version is incompatible with this plugin version in combination with " +
				"Minecraft 1.13.1. " +
				"Try to download an update of ProtocolLib.", noSuchMethodError);
			}
		}
		
		respawn.getDifficulties().writeSafely(0, difficulty);
		respawn.getGameModes().write(0, gamemode);
		respawn.getWorldTypeModifier().write(0, p.getWorld().getWorldType());
		return respawn;
	}

	private void sendPackets(Player p, PacketContainer... packets) {
		try {
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			for (PacketContainer packet : packets) {
				protocolManager.sendServerPacket(p, packet);
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public void changeSkin(Player p){
		/*WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(p);
		gameProfile.getProperties().clear();
		gameProfile.getProperties().put("textures", skin);
		selfSkinUpdate(p, gameProfile);
		sendUpdateOthers(p);*/
		
		String value = "";
		String signature = "";
		
		for(Scenario sc: fr.gameurduxvi.uhc.Main.getInstance().getScenarios()) {
			if(sc.getScencarioName().equals(Main.getScenariosManager().ANONYMOUS_NAME)) {
				if(sc.isActive()) {
					value = Main.getScenariosManager().SkinValue;
					signature = Main.getScenariosManager().SkinSignature;
				}
				else {
					JSONArray ja = null;
					if(new File(Main.getInstance().pluginDir + "skins.json").exists()) {
						try {
							Object obj = new JSONParser().parse(new FileReader(Main.getInstance().pluginDir + "skins.json"));
							ja = (JSONArray) obj;
							Iterator<JSONObject> iterator = ja.iterator();
							while(iterator.hasNext()) {
								JSONObject jo = iterator.next();
								if(jo.get("uuid").equals(p.getUniqueId().toString())) {
									value = (String) jo.get("value");
									signature = (String) jo.get("signature");
									break;
								}
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}					
				}
			}
		}

		GameProfile gp = ((CraftPlayer)p).getProfile();
		gp.getProperties().removeAll("textures");
		gp.getProperties().put("textures", new Property("textures", value, signature));
		
		for(Player pl : Bukkit.getOnlinePlayers()){
			//((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)p).getHandle()));
			if(pl == p) continue;
			if(pl.canSee(p)) {
				/*((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)p).getHandle()));
				((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(p.getEntityId()));
				((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
				*/
				pl.hidePlayer(p);
				pl.showPlayer(p);
			}			
		}
        //reloadSkinForSelf(p);
    }
}
