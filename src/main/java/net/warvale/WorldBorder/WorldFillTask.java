package net.warvale.WorldBorder;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class WorldFillTask implements Runnable
{
    private Server server;
    private final World world;
    private final BorderData border;
    private final WorldFileData worldData;
    private boolean readyToGo;
    private boolean paused;
    private boolean pausedForMemory;
    private int taskID;
    private final int chunksPerRun;
    private int x;
    private int z;
    private boolean isZLeg;
    private boolean isNeg;
    private int length;
    private int current;
    private boolean insideBorder;
    private final List<CoordXZ> storedChunks;
    private final Set<CoordXZ> originalChunks;
    private final CoordXZ lastChunk;
    private long lastReport;
    private long lastAutosave;
    private int reportTarget;
    private int reportTotal;
    private int reportNum;
    private boolean finish;
    
    @SuppressWarnings("unused")
	public WorldFillTask(final String worldName, final int chunksPerRun, final int radius) {
        this.readyToGo = false;
        this.paused = false;
        this.pausedForMemory = false;
        this.taskID = -1;
        this.x = 0;
        this.z = 0;
        this.isZLeg = false;
        this.isNeg = false;
        this.length = -1;
        this.current = 0;
        this.insideBorder = true;
        this.storedChunks = new LinkedList<CoordXZ>();
        this.originalChunks = new HashSet<CoordXZ>();
        this.lastChunk = new CoordXZ(0, 0);
        this.lastReport = System.currentTimeMillis();
        this.lastAutosave = System.currentTimeMillis();
        this.reportTarget = 0;
        this.reportTotal = 0;
        this.reportNum = 0;
        this.finish = false;
        this.server = Bukkit.getServer();
        this.chunksPerRun = chunksPerRun;
        this.world = this.server.getWorld(worldName);
        final Location spawn = Bukkit.getWorld("world").getSpawnLocation();
        this.border = new BorderData(spawn.getX(), spawn.getZ(), radius, radius);
        this.worldData = WorldFileData.create(this.world);
        if (this.worldData == null) {
            this.stop();
            return;
        }
        this.x = CoordXZ.blockToChunk((int)this.border.getX());
        this.z = CoordXZ.blockToChunk((int)this.border.getZ());
        final int chunkWidthX = (int)Math.ceil((this.border.getRadiusX() + 16) * 2 / 16.0);
        final int chunkWidthZ = (int)Math.ceil((this.border.getRadiusZ() + 16) * 2 / 16.0);
        final int biggerWidth = Math.max(chunkWidthX, chunkWidthZ);
        this.reportTarget = biggerWidth * biggerWidth + biggerWidth + 1;
        final Chunk[] loadedChunks;
        final Chunk[] originals = loadedChunks = this.world.getLoadedChunks();
        for (final Chunk original : loadedChunks) {
            this.originalChunks.add(new CoordXZ(original.getX(), original.getZ()));
        }
        this.readyToGo = true;
    }
    
    @SuppressWarnings("unused")
	public WorldFillTask(final String worldName, final int chunksPerRun, final int radius, Location spawn) {
        this.readyToGo = false;
        this.paused = false;
        this.pausedForMemory = false;
        this.taskID = -1;
        this.x = 0;
        this.z = 0;
        this.isZLeg = false;
        this.isNeg = false;
        this.length = -1;
        this.current = 0;
        this.insideBorder = true;
        this.storedChunks = new LinkedList<CoordXZ>();
        this.originalChunks = new HashSet<CoordXZ>();
        this.lastChunk = new CoordXZ(0, 0);
        this.lastReport = System.currentTimeMillis();
        this.lastAutosave = System.currentTimeMillis();
        this.reportTarget = 0;
        this.reportTotal = 0;
        this.reportNum = 0;
        this.finish = false;
        this.server = Bukkit.getServer();
        this.chunksPerRun = chunksPerRun;
        this.world = this.server.getWorld(worldName);
        this.border = new BorderData(spawn.getX(), spawn.getZ(), radius, radius);
        this.worldData = WorldFileData.create(this.world);
        if (this.worldData == null) {
            this.stop();
            return;
        }
        this.x = CoordXZ.blockToChunk((int)this.border.getX());
        this.z = CoordXZ.blockToChunk((int)this.border.getZ());
        final int chunkWidthX = (int)Math.ceil((this.border.getRadiusX() + 16) * 2 / 16.0);
        final int chunkWidthZ = (int)Math.ceil((this.border.getRadiusZ() + 16) * 2 / 16.0);
        final int biggerWidth = Math.max(chunkWidthX, chunkWidthZ);
        this.reportTarget = biggerWidth * biggerWidth + biggerWidth + 1;
        final Chunk[] loadedChunks;
        final Chunk[] originals = loadedChunks = this.world.getLoadedChunks();
        for (final Chunk original : loadedChunks) {
            this.originalChunks.add(new CoordXZ(original.getX(), original.getZ()));
        }
        this.readyToGo = true;
    }
    
    
    public void setTaskID(final int ID) {
        if (ID == -1) {
            this.stop();
        }
        this.taskID = ID;
    }
    
    @Override
    public void run() {
        if (this.pausedForMemory) {
            if (this.AvailableMemoryTooLow()) {
                return;
            }
            this.pausedForMemory = false;
            this.readyToGo = true;
            this.sendMessage("Available memory is sufficient, automatically continuing.");
        }
        if (this.server == null || !this.readyToGo || this.paused) {
            return;
        }
        this.readyToGo = false;
        final long loopStartTime = System.currentTimeMillis();
        for (int loop = 0; loop < this.chunksPerRun; ++loop) {
            if (this.paused || this.pausedForMemory) {
                return;
            }
            final long now = System.currentTimeMillis();
            if (now > this.lastReport + 5000L) {
                this.reportProgress();
            }
            if (now > loopStartTime + 45L) {
                this.readyToGo = true;
                return;
            }
            while (!this.border.insideBorder(CoordXZ.chunkToBlock(this.x) + 8, CoordXZ.chunkToBlock(this.z) + 8)) {
                if (this.cannotMoveToNext()) {
                    return;
                }
            }
            this.insideBorder = true;
            while (this.worldData.isChunkFullyGenerated(this.x, this.z)) {
                this.insideBorder = true;
                if (this.cannotMoveToNext()) {
                    return;
                }
            }
            this.world.loadChunk(this.x, this.z, true);
            this.worldData.chunkExistsNow(this.x, this.z);
            final int popX = this.isZLeg ? (this.x + (this.isNeg ? -1 : 1)) : this.x;
            final int popZ = this.isZLeg ? this.z : (this.z + (this.isNeg ? 1 : -1));
            this.world.loadChunk(popX, popZ, false);
            if (!this.storedChunks.contains(this.lastChunk) && !this.originalChunks.contains(this.lastChunk)) {
                this.world.loadChunk(this.lastChunk.x, this.lastChunk.z, false);
                this.storedChunks.add(new CoordXZ(this.lastChunk.x, this.lastChunk.z));
            }
            this.storedChunks.add(new CoordXZ(popX, popZ));
            this.storedChunks.add(new CoordXZ(this.x, this.z));
            while (this.storedChunks.size() > 8) {
                final CoordXZ coord = this.storedChunks.remove(0);
                if (!this.originalChunks.contains(coord)) {
                    this.world.unloadChunkRequest(coord.x, coord.z);
                }
            }
            if (this.cannotMoveToNext()) {
                return;
            }
        }
        this.readyToGo = true;
    }
    
    public boolean cannotMoveToNext() {
        if (this.paused || this.pausedForMemory) {
            return true;
        }
        ++this.reportNum;
        if (this.current < this.length) {
            ++this.current;
        }
        else {
            this.current = 0;
            this.isZLeg ^= true;
            if (this.isZLeg) {
                this.isNeg ^= true;
                ++this.length;
            }
        }
        this.lastChunk.x = this.x;
        this.lastChunk.z = this.z;
        if (this.isZLeg) {
            this.z += (this.isNeg ? -1 : 1);
        }
        else {
            this.x += (this.isNeg ? -1 : 1);
        }
        if (this.isZLeg && this.isNeg && this.current == 0) {
            if (!this.insideBorder) {
                this.finish();
                return true;
            }
            this.insideBorder = false;
        }
        return false;
    }
    
    public void finish() {
        this.paused = true;
        this.finish = true;
        this.reportProgress();
        this.world.save();
        this.sendMessage("§atask successfully completed for world \"" + this.refWorld() + "\"!");
        this.stop();
    }
    
    private void stop() {
        if (this.server == null) {
            return;
        }
        this.readyToGo = false;
        if (this.taskID != -1) {
            this.server.getScheduler().cancelTask(this.taskID);
        }
        this.server = null;
        while (!this.storedChunks.isEmpty()) {
            final CoordXZ coord = this.storedChunks.remove(0);
            if (!this.originalChunks.contains(coord)) {
                this.world.unloadChunkRequest(coord.x, coord.z);
            }
        }
    }
    
    private void reportProgress() {
        this.lastReport = System.currentTimeMillis();
        double perc = this.getPercentageCompleted();
        if (perc > 100.0) {
            perc = 100.0;
        }
        this.sendMessage(this.reportNum + " more chunks processed (" + (this.reportTotal + this.reportNum) + " total, ~" + new DecimalFormat("0.0").format(perc) + "%)");
        this.reportTotal += this.reportNum;
        this.reportNum = 0;
        final int fillAutoSaveFrequency = 30;
        if (this.lastAutosave + fillAutoSaveFrequency * 1000 < this.lastReport) {
            this.lastAutosave = this.lastReport;
            this.sendMessage("Saving the world to disk, just to be on the safe side.");
            this.world.save();
        }
    }
    
    private void sendMessage(String text) {
        final int availMem = this.AvailableMemory();
        Bukkit.getConsoleSender().sendMessage("[Fill] " + text + " (free mem: " + availMem + " MB)");
        if (availMem < 200) {
            this.pausedForMemory = true;
            text = "Available memory is very low, task is pausing. A cleanup will be attempted now, and the task will automatically continue if/when sufficient memory is freed up.\n Alternatively, if you restart the server, this task will automatically continue once the server is back up.";
            Bukkit.getConsoleSender().sendMessage("§7Pregen §8>> §b" + text);
            System.gc();
        }
    }
    
    public String refWorld() {
        return this.world.getName();
    }
    
    public double getPercentageCompleted() {
        if (this.finish) {
            return 100.0;
        }
        return Math.min(100.0, (this.reportTotal + this.reportNum) / (double)this.reportTarget * 100.0);
    }
    
    public int AvailableMemory() {
        final Runtime rt = Runtime.getRuntime();
        return (int)((rt.maxMemory() - rt.totalMemory() + rt.freeMemory()) / 1048576L);
    }
    
    public boolean AvailableMemoryTooLow() {
        return this.AvailableMemory() < 500;
    }
}
