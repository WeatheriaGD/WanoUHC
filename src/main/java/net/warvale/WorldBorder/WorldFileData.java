package net.warvale.WorldBorder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.World;

public class WorldFileData
{
    private final transient World world;
    private transient File regionFolder;
    private transient File[] regionFiles;
    private final transient Map<CoordXZ, List<Boolean>> regionChunkExistence;
    
    public static WorldFileData create(final World world) {
        final WorldFileData newData = new WorldFileData(world);
        newData.regionFolder = new File(newData.world.getWorldFolder(), "region");
        if (!newData.regionFolder.exists() || !newData.regionFolder.isDirectory()) {
            final File[] possibleDimFolders = newData.world.getWorldFolder().listFiles(new DimFolderFileFilter());
            for (final File possibleDimFolder : Objects.requireNonNull(possibleDimFolders)) {
                final File possible = new File(newData.world.getWorldFolder(), possibleDimFolder.getName() + File.separator + "region");
                if (possible.exists() && possible.isDirectory()) {
                    newData.regionFolder = possible;
                    break;
                }
            }
            if (!newData.regionFolder.exists() || !newData.regionFolder.isDirectory()) {
                newData.sendMessage("Could not validate folder for world's region files. Looked in " + newData.world.getWorldFolder().getPath() + " for valid DIM* folder with a region folder in it.");
                return null;
            }
        }
        newData.regionFiles = newData.regionFolder.listFiles(new ExtFileFilter(".MCA"));
        if (newData.regionFiles == null || newData.regionFiles.length == 0) {
            newData.regionFiles = newData.regionFolder.listFiles(new ExtFileFilter(".MCR"));
            if (newData.regionFiles == null || newData.regionFiles.length == 0) {
                newData.sendMessage("Could not find any region files. Looked in: " + newData.regionFolder.getPath());
                return null;
            }
        }
        return newData;
    }
    
    private WorldFileData(final World world) {
        this.regionFolder = null;
        this.regionFiles = null;
        this.regionChunkExistence = Collections.synchronizedMap(new HashMap<CoordXZ, List<Boolean>>());
        this.world = world;
    }
    
    public File regionFile(final int index) {
        if (this.regionFiles.length < index) {
            return null;
        }
        return this.regionFiles[index];
    }
    
    public CoordXZ regionFileCoordinates(final int index) {
        final File regionFile = this.regionFile(index);
        final String[] coords = regionFile.getName().split("\\.");
        try {
            final int x = Integer.parseInt(coords[1]);
            final int z = Integer.parseInt(coords[2]);
            return new CoordXZ(x, z);
        }
        catch (Exception ex) {
            this.sendMessage("Error! Region file found with abnormal name: " + regionFile.getName());
            return null;
        }
    }
    
    public boolean doesChunkNotExist(final int x, final int z) {
        final CoordXZ region = new CoordXZ(CoordXZ.chunkToRegion(x), CoordXZ.chunkToRegion(z));
        final List<Boolean> regionChunks = this.getRegionData(region);
        return !regionChunks.get(this.coordToRegionOffset(x, z));
    }
    
    public boolean isChunkFullyGenerated(final int x, final int z) {
        return !this.doesChunkNotExist(x, z) && !this.doesChunkNotExist(x + 1, z) && !this.doesChunkNotExist(x - 1, z) && !this.doesChunkNotExist(x, z + 1) && !this.doesChunkNotExist(x, z - 1);
    }
    
    public void chunkExistsNow(final int x, final int z) {
        final CoordXZ region = new CoordXZ(CoordXZ.chunkToRegion(x), CoordXZ.chunkToRegion(z));
        final List<Boolean> regionChunks = this.getRegionData(region);
        regionChunks.set(this.coordToRegionOffset(x, z), true);
    }
    
    private int coordToRegionOffset(int x, int z) {
        x %= 32;
        z %= 32;
        if (x < 0) {
            x += 32;
        }
        if (z < 0) {
            z += 32;
        }
        return x + z * 32;
    }
    
    private List<Boolean> getRegionData(final CoordXZ region) {
        List<Boolean> data = this.regionChunkExistence.get(region);
        if (data != null) {
            return data;
        }
        data = new ArrayList<Boolean>(1024);
        for (int i = 0; i < 1024; ++i) {
            data.add(Boolean.FALSE);
        }
        for (int i = 0; i < this.regionFiles.length; ++i) {
            final CoordXZ coord = this.regionFileCoordinates(i);
            if (coord.equals(region)) {
                try {
                    final RandomAccessFile regionData = new RandomAccessFile(this.regionFile(i), "r");
                    for (int j = 0; j < 1024; ++j) {
                        if (regionData.readInt() != 0) {
                            data.set(j, true);
                        }
                    }
                    regionData.close();
                }
                catch (FileNotFoundException ex) {
                    this.sendMessage("Error! Could not open region file to find generated chunks: " + this.regionFile(i).getName());
                }
                catch (IOException ex2) {
                    this.sendMessage("Error! Could not read region file to find generated chunks: " + this.regionFile(i).getName());
                }
            }
        }
        this.regionChunkExistence.put(region, data);
        return data;
    }
    
    private void sendMessage(final String text) {
        System.out.println("[WorldData] " + text);
    }
    
    private static class ExtFileFilter implements FileFilter
    {
        final String ext;
        
        public ExtFileFilter(final String extension) {
            this.ext = extension.toLowerCase();
        }
        
        @Override
        public boolean accept(final File file) {
            return file.exists() && file.isFile() && file.getName().toLowerCase().endsWith(this.ext);
        }
    }
    
    private static class DimFolderFileFilter implements FileFilter
    {
        @Override
        public boolean accept(final File file) {
            return file.exists() && file.isDirectory() && file.getName().toLowerCase().startsWith("dim");
        }
    }
}
