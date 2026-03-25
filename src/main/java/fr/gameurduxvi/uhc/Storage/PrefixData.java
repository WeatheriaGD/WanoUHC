package fr.gameurduxvi.uhc.Storage;

public class PrefixData {
	private String prefix;
	private String suffix;
	private int[] whoCanSee;
	
	public PrefixData(String prefix, String suffix, int[] whoCanSee) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.whoCanSee = whoCanSee;
	}
	
	/*******************************************************
	 * Get Functions
	 *******************************************************/
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public int[] getWhoCanSee() {
		return whoCanSee;
	}
	
	
	/*******************************************************
	 * Set Functions
	 *******************************************************/	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public void setWhoCanSee(int[] whoCanSee) {
		this.whoCanSee = whoCanSee;
	}
}
