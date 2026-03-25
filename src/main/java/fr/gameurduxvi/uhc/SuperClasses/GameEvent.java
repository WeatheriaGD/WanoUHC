package fr.gameurduxvi.uhc.SuperClasses;

import org.bukkit.Bukkit;

public abstract class GameEvent {
	
	private String name = getClass().getName();
	private String description = "Default Description";
	private int minute = 1000;
	private int second = 0;
	private boolean inScoreboard = false;
	private String scoreboardText = "";
	abstract public void run();
	
	
	
	/*
	 * ============================================================================
	 * ========================== Get Functions ===================================
	 * ============================================================================
	 * */
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public int getMinute() {
		return minute;
	}
	public int getSecond() {
		return second;
	}
	public boolean canBeInScoreboard() {
		return inScoreboard;
	}
	public String getScoreboardText() {
		return scoreboardText;
	}
	
	/*
	 * ============================================================================
	 * ========================== Set Functions ===================================
	 * ============================================================================
	 * */
	public GameEvent setName(String name) {
		this.name = name;
		return this;
	}
	public GameEvent setDescription(String description) {
		this.description = description;
		return this;
	}
	public GameEvent setMinute(int minute) {
		this.minute = minute;
		return this;
	}
	public GameEvent setSecond(int second) {
		this.second = second;
		return this;
	}
	public GameEvent setInScoreboard(boolean inScoreboard) {
		this.inScoreboard = inScoreboard;
		return this;
	}
	public GameEvent setScoreboardText(String scoreboardText) {
		if(scoreboardText != null) {
			this.scoreboardText = scoreboardText;
			return this;
		}
		Bukkit.getConsoleSender().sendMessage("§bOPU §7GameEventManager §8>> §cUne variable a été définie par \"null\"");
		return this;
	}
}
