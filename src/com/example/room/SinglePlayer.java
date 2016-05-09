package com.example.room;

public class SinglePlayer {
	private String PlayerName;
	private int Serial;
	public SinglePlayer()
	{
		Serial = 0;
	}
	public SinglePlayer(String PlayerName)
	{
		this.PlayerName = PlayerName;
	}
	public SinglePlayer(String PlayerName, int Serial)
	{
		this.PlayerName = PlayerName;
		this.Serial = Serial;
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
