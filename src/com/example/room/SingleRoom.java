package com.example.room;

public class SingleRoom {
	private String RoomId;
	private String RoomName;
	private String RoomStyle;
	private String RoomHolder_Id;
	private String RoomHolder_Name;
	//Player0_Name = RoomHolder 为房间管理者
	private String Player1_Id;
	private String Player2_Id;
	private String Player3_Id;
	private String Player1_Name;
	private String Player2_Name;
	private String Player3_Name;
	public SingleRoom()
	{
		
	}
	public SingleRoom(String RoomId, String RoomName, String RoomStyle, String RoomHolder_Name,  
				String Player1_Name, String Player2_Name, String Player3_Name)
	{
		this.RoomId = RoomId;
		this.RoomName = RoomName;
		this.RoomStyle = RoomStyle;
		this.RoomHolder_Name = RoomHolder_Name;
		
		this.Player1_Name = Player1_Name;
		this.Player2_Name = Player2_Name;
		this.Player3_Name = Player3_Name;
	}
	public String getRoomId()
	{
		return RoomId;
	}
	public String getRoomName(){
		return RoomName;
	}
	public String getRoomHolder_Id()
	{
		return RoomHolder_Id;
	}
	public String getRoomHolder_Name()
	{
		return RoomHolder_Name;
	}
	public String getRoomStyle()
	{
		return RoomStyle;
	}
	public String getPlayer1_Id()
	{
		return Player1_Id;
	}
	public String getPlayer2_Id()
	{
		return Player2_Id;
	}
	public String getPlayer3_Id()
	{
		return Player3_Id;
	}
	public String getPlayer1_Name()
	{
		return Player1_Name;
	}
	public String getPlayer2_Name()
	{
		return Player2_Name;
	}
	public String getPlayer3_Name()
	{
		return Player3_Name;
	}
	public void setRoomName(String RoomName)
	{
		this.RoomName = RoomName;
	}
	public void setRoomHolder_Id(String RoomHolder_Id)
	{
		this.RoomHolder_Id = RoomHolder_Id;
	}
	public void setRoomStyle(String RoomStyle)
	{
		this.RoomStyle = RoomStyle;
	}
}
