package fr.gameurduxvi.uhc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.base.Strings;

public class LoadingChunkTask {

    private World world;
    private int size;
    private BukkitTask task;
    private int nChunk;
    private int last;
    private long startTime;

    public LoadingChunkTask(World world) {
        this.size = (int) 1000 / 2;
        this.world = world;
        world.setGameRuleValue("randomTickSpeed", "0");
        load();
    }

    private void load() {
        new Thread(() -> {
            this.startTime = System.currentTimeMillis();
            this.task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
                private int todo = LoadingChunkTask.this.size * 2 * (LoadingChunkTask.this.size * 2) / 256;
                private int x = -LoadingChunkTask.this.size;
                private int z = -LoadingChunkTask.this.size;

                @Override
                public void run() {
                    for (int i = 0; i < 50; ++i) {
                        Chunk chunk = LoadingChunkTask.this.world.getChunkAt(LoadingChunkTask.this.world.getBlockAt(this.x, 64, this.z));
                        chunk.load(true);
                        chunk.load(false);
                        int percentage = LoadingChunkTask.this.nChunk * 100 / this.todo;
                        if (percentage > LoadingChunkTask.this.last) {
                            LoadingChunkTask.this.last = percentage;
                        }
                        this.z += 16;
                        if (this.z >= LoadingChunkTask.this.size) {
                            this.z = -LoadingChunkTask.this.size;
                            this.x += 16;
                        }
                        if (this.x >= LoadingChunkTask.this.size) {
                            LoadingChunkTask.this.task.cancel();
                            int calculedTime = Math.round((System.currentTimeMillis() - LoadingChunkTask.this.startTime) / 1000L);
                            if (world.getName().equalsIgnoreCase("world")){
                                new LoadingChunkTask(Bukkit.getWorld("wano"));
                            }
                            System.out.println("Finished preload after " + calculedTime + "s");
                            return;
                        }
                        LoadingChunkTask.this.nChunk++;
                    }
                }
            }, 0L, 0L);
        }).run();
    }


    public static String getProgressBar(int current, int max, int totalBars, String symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        return Strings.repeat("" + completedColor + symbol, progressBars) + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}