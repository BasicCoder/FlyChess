package com.example.room;

import java.io.InputStream;
import java.io.OutputStream;

public class SendThread extends Thread{
	private int roomNum;
	private int gameNum;
	private int colorNum;
	private int planeSeq;
	private int lenNum;
	private ApplicationUtil appUtil;
	private OutputStream dos = null;
	
	
	public SendThread(int room, int game, int color, int plane, int len, ApplicationUtil app){
		super();
		roomNum = room;
		gameNum = game; // game progress count
		colorNum = color;
		planeSeq = plane;
		lenNum = len;
		appUtil = app;
	    dos = appUtil.getDos();
	}
	@Override
	public void run(){
		try {
			String str = "Game:" + roomNum + ";" + gameNum + ";" + colorNum + ";" + planeSeq + ";" + lenNum;
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
