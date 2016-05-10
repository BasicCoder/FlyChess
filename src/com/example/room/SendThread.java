package com.example.room;

import java.io.InputStream;
import java.io.OutputStream;

public class SendThread extends Thread{
	private int roomNum;
	private int gameNum;
	private int countNum;
	private int planeNum;
	private int lenNum;
	private ApplicationUtil appUtil;
	private OutputStream dos = null;
	
	
	public SendThread(int room, int game, int count, int plane, int len, ApplicationUtil app){
		super();
		roomNum = room;
		gameNum = game;
		countNum = count;
		planeNum = plane;
		lenNum = len;
		appUtil = app;
	    dos = appUtil.getDos();
	}
	@Override
	public void run(){
		try {
			String str = "room" + roomNum + "count" + countNum + "game" + gameNum + "plane" + planeNum + "len" + lenNum;
			byte[] b = str.getBytes("utf-8");
			//if(dos == null)
				//Log.v("vvv","xxxx");
			dos.write(b);
			dos.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
