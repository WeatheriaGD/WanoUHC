package fr.gameurduxvi.uhc.Refractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;

import fr.gameurduxvi.uhc.Main;

public class SchematicLoad {
	public SchematicLoad(Location loc, String filePath, boolean copyAir) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Vector vector = new Vector(loc.getX(), loc.getY(), loc.getZ());
				File file = new File(filePath);
				BukkitWorld world = new BukkitWorld(loc.getWorld());
				try {
					//                                   paste(world, position, allowUndo, !noAir, (Transform) null);
					ClipboardFormat.SCHEMATIC.load(file).paste(world, vector, false, copyAir, null);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		});
	}
	
	public SchematicLoad(Location loc, String filePath, boolean copyAir, float angle) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				AffineTransform transform = new AffineTransform();
				transform = transform.rotateY(angle);
				
				Vector vector = new Vector(loc.getX(), loc.getY(), loc.getZ());
				File file = new File(filePath);
				BukkitWorld world = new BukkitWorld(loc.getWorld());
				try {
					//                                   paste(world, position, allowUndo, !noAir, (Transform) null);
					ClipboardFormat.SCHEMATIC.load(file).paste(world, vector, false, copyAir, transform);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		});
	}
	
	
	
	
	private Class<?> NBTCompressedStreamTools;

	private Class<?> getNMSClass(String string) throws ClassNotFoundException{
	    return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + string);
	}
	
	/*@SuppressWarnings("deprecation")
	public SchematicLoad(Location loc, String filePath, boolean copyAir, float angle) {
		synchronized (Main.getInstance()) {
			try {
				try {
			        NBTCompressedStreamTools = getNMSClass("NBTCompressedStreamTools");
			    } catch (ClassNotFoundException e) {
			        e.printStackTrace();
			    }
				FileInputStream fis = new FileInputStream(filePath);
				Object nbtData = NBTCompressedStreamTools.getMethod("a", InputStream.class).invoke(null, fis);
				Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
				Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);
				
				short width = ((short) getShort.invoke(nbtData, "Width"));
				short height = ((short) getShort.invoke(nbtData, "Height"));
				short length = ((short) getShort.invoke(nbtData, "Length"));
				
				short WEOffsetX = ((short) getShort.invoke(nbtData, "WEOffsetX"));
				short WEOffsetY = ((short) getShort.invoke(nbtData, "WEOffsetY"));
				short WEOffsetZ = ((short) getShort.invoke(nbtData, "WEOffsetZ"));
				
				int locX = (int) (loc.getX() + WEOffsetX);
				int locY = (int) (loc.getY() + WEOffsetY);
				int locZ = (int) (loc.getZ() + WEOffsetZ);
				
				System.out.println(locX + " " + locY + " " + locZ);
				
				byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
				byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));
				
				fis.close();
				for(int x = 0; x < width; ++x){
					for(int y = 0; y < height; ++y){
						for(int z = 0; z < length; ++z){
							int index = y * width * length + z * width + x;
							int b = blocks[index] & 0xFF;
							//Material m = Material.getMaterial(b);
							Block block = loc.getWorld().getBlockAt(locX + x, locY + y, locZ + z);
							if(Material.getMaterial(b).equals(Material.AIR) && copyAir || !Material.getMaterial(b).equals(Material.AIR)) {
								block.setTypeIdAndData(b, data[index], true);	
							}						
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}*/
	
	/*@SuppressWarnings("deprecation")
	public SchematicLoad(Location loc, String filePath, boolean copyAir, float angle, boolean replaceDestinationBlocks) {
		synchronized (Main.getInstance()) {
			try {
				try {
			        NBTCompressedStreamTools = getNMSClass("NBTCompressedStreamTools");
			    } catch (ClassNotFoundException e) {
			        e.printStackTrace();
			    }
				FileInputStream fis = new FileInputStream(filePath);
				Object nbtData = NBTCompressedStreamTools.getMethod("a", InputStream.class).invoke(null, fis);
				Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
				Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);
				
				short width = ((short) getShort.invoke(nbtData, "Width"));
				short height = ((short) getShort.invoke(nbtData, "Height"));
				short length = ((short) getShort.invoke(nbtData, "Length"));
				
				short WEOffsetX = ((short) getShort.invoke(nbtData, "WEOffsetX"));
				short WEOffsetY = ((short) getShort.invoke(nbtData, "WEOffsetY"));
				short WEOffsetZ = ((short) getShort.invoke(nbtData, "WEOffsetZ"));
				
				int locX = (int) (loc.getX() + WEOffsetX);
				int locY = (int) (loc.getY() + WEOffsetY);
				int locZ = (int) (loc.getZ() + WEOffsetZ);
				
				System.out.println(locX + " " + locY + " " + locZ);
				
				byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
				byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));
				
				fis.close();
				
				EditSession editSession = new EditSessionBuilder((World) loc.getWorld()).fastmode(true).build();
				
				for(int x = 0; x < width; ++x){
					for(int y = 0; y < height; ++y){
						for(int z = 0; z < length; ++z){
							int index = y * width * length + z * width + x;
							int b = blocks[index] & 0xFF;
							//Material m = Material.getMaterial(b);
							Block block = loc.getWorld().getBlockAt(locX + x, locY + y, locZ + z);
							if(Material.getMaterial(b).equals(Material.AIR) && copyAir || !Material.getMaterial(b).equals(Material.AIR)) {
								if(!block.getType().equals(Material.AIR) && !replaceDestinationBlocks){}else {
									//block.setTypeIdAndData(b, data[index], true);
									editSession.setBlock(new Vector(locX + x, locY + y, locZ + z), new BaseBlock(b, data[index]));
								}
							}						
						}
					}
				}
				editSession.flushQueue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
	
	@SuppressWarnings("deprecation")
	public SchematicLoad(Location loc, String filePath, boolean copyAir, Material matReplace) {
		synchronized (Main.getInstance()) {
			try {
				try {
			        NBTCompressedStreamTools = getNMSClass("NBTCompressedStreamTools");
			    } catch (ClassNotFoundException e) {
			        e.printStackTrace();
			    }
				FileInputStream fis = new FileInputStream(filePath);
				Object nbtData = NBTCompressedStreamTools.getMethod("a", InputStream.class).invoke(null, fis);
				Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
				Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);
				
				short width = ((short) getShort.invoke(nbtData, "Width"));
				short height = ((short) getShort.invoke(nbtData, "Height"));
				short length = ((short) getShort.invoke(nbtData, "Length"));
				
				short WEOffsetX = ((short) getShort.invoke(nbtData, "WEOffsetX"));
				short WEOffsetY = ((short) getShort.invoke(nbtData, "WEOffsetY"));
				short WEOffsetZ = ((short) getShort.invoke(nbtData, "WEOffsetZ"));
				
				int locX = (int) (loc.getX() + WEOffsetX);
				int locY = (int) (loc.getY() + WEOffsetY);
				int locZ = (int) (loc.getZ() + WEOffsetZ);
				
				byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
				//byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));
				
				fis.close();
				
				World world = BukkitUtil.getLocalWorld(loc.getWorld());
				EditSession editSession = new EditSessionBuilder(world).fastmode(true).build();
				
				for(int x = 0; x < width; ++x){
					for(int y = 0; y < height; ++y){
						for(int z = 0; z < length; ++z){
							int index = y * width * length + z * width + x;
							int b = blocks[index] & 0xFF;
							//Material m = Material.getMaterial(b);
							//Block block = loc.getWorld().getBlockAt(locX + x, locY + y, locZ + z);
							if(Material.getMaterial(b).equals(Material.AIR) && copyAir || !Material.getMaterial(b).equals(Material.AIR)) {
								editSession.setBlock(new Vector(locX + x, locY + y, locZ + z), new BaseBlock(matReplace.getId(), matReplace.getData().getModifiers()));
							}						
						}
					}
				}
				editSession.flushQueue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
