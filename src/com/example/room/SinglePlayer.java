package com.example.room;

public class SinglePlayer {
	private String PlayerName;
	
	public SinglePlayer()
	{
		
	}
	public SinglePlayer(String PlayerName)
	{
		this.PlayerName = PlayerName;
	}
	public String getPlayerName()
	{
		return PlayerName;
	}
	public void setPlayerName(String PlayerName)
	{
		this.PlayerName = PlayerName;
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof SinglePlayer){
			SinglePlayer player = (SinglePlayer) obj;
			return this.PlayerName.equals(player.getPlayerName());
		}
		return super.equals(obj);
	}
}
