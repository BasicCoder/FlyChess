package com.example.room;

import java.io.InputStream;

import android.os.Handler;
import android.os.Message;

public class GetThread extends Thread{
	private InputStream dis = null;
	private ApplicationUtil appUtil;
	private Handler handler;
	
	public GetThread(ApplicationUtil app, Handler mainHandler){
		super();
		appUtil = app;
	    dis = appUtil.getDis();
	    handler = mainHandler;
	}
	
	@Override 
	public void run(){
		try{
			System.out.println("����Ϣ "+Control.progressCnt+" color="+Control.getColorTurn());
			byte[] readIn = new byte[1024];
			dis.read(readIn);
			
			String line =  new String(readIn,"utf-8").trim(); 
			String[] row = line.split(";");
			
			Message msg = new Message();
			msg.what = 10;
			MsgInfo tempMsg = new MsgInfo(Integer.parseInt(row[0]),Integer.parseInt(row[1]),Integer.parseInt(row[2]),Integer.parseInt(row[3]));
			msg.obj = tempMsg;
			
			handler.sendMessage(msg);
			
			System.out.println(line);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
