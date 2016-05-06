package com.example.room;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
import com.squareup.okhttp.Cache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.room.GameView;

public class InterRoom extends Activity {
	private String[] names0 = new String[]{"张三丰","灭绝师太","空见","岳不群"};
	private List<SinglePlayer> mData = null;
	private Context mContext;
	private SinglePlayerAdapter mAdapter = null;
	private ListView names;
	private String[] StringArray;
	private SingleRoom roomData = null;
	
	private RequestQueue mQueue = null;
	private OkHttpClient client = new OkHttpClient();
	
	private String dstName = "172.18.40.26";
	private int dstPort = 8888;
	
	private Timer timer = new Timer();
	private TimerTask timertask = null;
	private boolean fullRoom = false;
	
	private Handler mHandler = null;
	
	private Button btnStartGame;
	private Button btnExitRoom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.inter_room);
		
		
		mContext = InterRoom.this;
		names = (ListView) findViewById(R.id.names);
		
		
		
		mQueue = Volley.newRequestQueue(mContext);
		
		mData = new LinkedList<SinglePlayer>();
			
		//得Intent中的Bundle对象
		Bundle bundle = this.getIntent().getExtras();
		//取得Bundle对象中的数据 
		StringArray = bundle.getStringArray("roominfo");
		//mData.add(new SinglePlayer(StringArray[1]));//添加房主，房主数据不能为空
		roomData = new SingleRoom(StringArray[0], 
								StringArray[1], 
								StringArray[2],
								StringArray[3],
								StringArray[4],
								StringArray[5],
								StringArray[6]);
		
		for(int i = 3; i < StringArray.length; i++)
		{
			if(StringArray[i].length() != 0)
			{
				
				if(!mData.contains(new SinglePlayer(StringArray[i]))){
					mData.add(new SinglePlayer(StringArray[i])); // 如果玩家名字不为空且未添加到数据中，则添加玩家名字
				}		
			}
		}
		
		Log.e("TimeTask", "TimeTaskDefine");
		timertask = new TimerTask(){
			@Override
			public void run(){
				Log.e("Timetask", String.valueOf(fullRoom));
				if(fullRoom){
					Message message = new Message();
					message.what = 1;
					mHandler.sendMessage(message);
				}
			}
		};
		
		//计数器定时跳转到游戏界面
		mHandler = new Handler(){
			private int count = 10;
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
			
				count = count - 1;
				
				Toast.makeText(mContext, Integer.toString(count), Toast.LENGTH_SHORT).show();
				
				if(count == 0){
					timer.cancel();
					Intent GameViewintent = new Intent();
					GameViewintent.setClass(InterRoom.this, GameView.class);
					InterRoom.this.finish();
					InterRoom.this.startActivity(GameViewintent);
				}
			}
		};
		
		Log.e("Timer", "TimerStart");
		timer.schedule(timertask, 1000, 1000);
		Log.e("Timer", "TimerSetEnd");
		
		//getPlayerInfo();
		Log.e("TAG","CallFunction");
		getPlayerInfo1(dstName, dstPort);
		Log.e("TAG","CallFunctionEnd");

		

		mAdapter = new SinglePlayerAdapter((LinkedList<SinglePlayer>)mData, mContext);
		names.setAdapter(mAdapter);
		
		btnStartGame = (Button)findViewById(R.id.start);
		btnExitRoom = (Button)findViewById(R.id.exit);
		btnStartGame.setOnClickListener(new ClickEvent());
		btnExitRoom.setOnClickListener(new ClickEvent());

	}
	
	@Override
	protected void onStart(){
		super.onStart();

	}
	
	private void getPlayerInfo()
	{
		String url = "http://172.18.40.26:8080/TestDemo/getRoomPlayerList?RoomId=" + StringArray[0] ;
		StringRequest stringRequest = new StringRequest(url, 
				new Response.Listener<String>(){
					@Override
					public void onResponse(String response){
						Log.d("TAG", response);
						if(!response.equals("Failed")){
							String[] row = response.split(";");
							for(int i = 0; i < row.length; i++)
							{
								String[] rowData = row[i].split(",");
								SinglePlayer player = new SinglePlayer( rowData[0] );
								if(!mData.contains( player )){
									mData.add( player ); // 如果玩家名字不为空且未添加到数据中，则添加玩家名字
								}		
							}
						}
		
					}
				},new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError error){
						Log.e("TAG", error.getMessage(), error);
					}
				});

		
		mQueue.add(stringRequest);
	}

	
	//private ReceiveThread mReceiveThread = null;
	private boolean stop = true;
	private int sendSwitch = 1;
	private void getPlayerInfo1(String dstName, int dstPort){

		Log.e("Thread","CreateNewThread");
		stop = false;
		new ReceiveSendThread().execute(dstName, Integer.toString(dstPort), "test", "test", "1");
		Log.e("Thread", "StartUp");
		
	}
	
	private class ReceiveSendThread extends AsyncTask<String, String, String>{
		private Socket clientSocket = null;
		private boolean SocketConnStatus = false;
		private String dstName = null;
		private int dstPort;
		private int timeout = 10000;
		private String NameId = null;
		private String RoomId = null;
		private int RoomStyle;

		private InputStream inStream = null;
		private OutputStream outStream = null;
		private BufferedReader in = null;
		private PrintWriter out = null;
		private byte[] buf;
		private String str = null;

		
		@Override
		protected String doInBackground(String... params){
			dstName = params[0];
			dstPort = Integer.parseInt(params[1]);
			NameId = params[2];
			RoomId = params[3];
			RoomStyle = Integer.parseInt(params[4]);

			Log.e("dstName", dstName);
			Log.e("dstPort", params[1]);
			Log.e("NameId", NameId);
			Log.e("RoomId", RoomId);
			Log.e("RoomStyle", params[4]);
			
			try{
				Log.e("AsyncTask","CreateSocket");
				clientSocket = new Socket(dstName, dstPort);
				//clientSocket.setSoTimeout(timeout);
				Log.e("AsyncTask", "CreateSocketOver");
			}
			catch(SocketTimeoutException e){
				e.printStackTrace();
			}
			catch(UnknownHostException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			if(clientSocket != null)
			{
				SocketConnStatus = clientSocket.isConnected();
				try{
					Log.e("AsyncTask", "getInputStream");
					inStream = clientSocket.getInputStream();
					outStream = clientSocket.getOutputStream();
					in = new BufferedReader(new InputStreamReader(inStream));
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outStream)), true);
					Log.e("AsyncTask", "getInputStreamEnd");
				}catch(IOException e){
					e.printStackTrace();
				}

				while(!stop){
					buf = new byte[512];
					try{
						Log.e("inStream", "readBuf");
						inStream.read(this.buf);
					}catch(IOException e){
						e.printStackTrace();
					}
					
					try{
						Log.e("str", "CreateStr");
						str = new String(this.buf, "GB2312").trim();
						Log.e("AsyncTask", str);
					}catch(UnsupportedEncodingException e){
						e.printStackTrace();
					}
					if(str != null)
					{
						Log.e("publish", str);
						publishProgress(str);
					}


					//第一次进入房间就发送消息
					if(sendSwitch == 1)
					{
						String tmp = "EnterRoom"+RoomId + NameId;
						try{
							buf = tmp.getBytes("UTF-8");
						}catch(UnsupportedEncodingException e){
							e.printStackTrace();
						}
						try{
							outStream.write(buf);
						}catch(IOException e){
							e.printStackTrace();
						}
						
						sendSwitch = 0;
					}
					//离开房间发送消息
					if(sendSwitch == 2){
						Log.e("AsyncTask","SendLeaveRoomStart");
						String tmp = "ExitRoom"+RoomId + NameId;
						
						try{
							buf = tmp.getBytes("UTF-8");
						}catch(UnsupportedEncodingException e){
							e.printStackTrace();
						}
						try{
							outStream.write(buf);
						}catch(IOException e){
							e.printStackTrace();
						}
						
					}		
					
				}
			}
			else{
				Log.e("AsyncTask", "这TM就尴尬了");	
					
			}
			
			
			return "End receive.";
		}
		
		@Override
		protected void onPreExecute(){

		}
		
		@Override
		protected void onProgressUpdate(String... values){
			String response = values[0];
			if(!response.equals("Failed")){
				String[] row = response.split(";");
				for(int i = 0; i < row.length; i++)
				{
					String[] rowData = row[i].split(",");
					if(rowData[0] != null){
						SinglePlayer player = new SinglePlayer( rowData[0] );
						if(!mData.contains( player )){
							mData.add( player ); // 如果玩家名字不为空且未添加到数据中，则添加玩家名字
						}
					}				
							
				}
			}
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Log.e("AsyncTask", "setFullRoom");
			
			//测试跳转，release版注释掉
			fullRoom = true;
			
			if(SocketConnStatus){
				fullRoom = true;
			}else{
				Toast.makeText(mContext, new String("Socket Explosion! Please try it again."), Toast.LENGTH_LONG).show();
			}
						
		}

	}
	
	class ClickEvent implements View.OnClickListener{
		
		@Override
		public void onClick(View v)
		{
			switch(v.getId()){
			case R.id.start:
				Intent GameViewintent = new Intent();
				GameViewintent.setClass(InterRoom.this, GameView.class);
				InterRoom.this.finish();
				InterRoom.this.startActivity(GameViewintent);
				break;
			case R.id.exit:
				sendLeaveRoomInfo(roomData.getRoomId(), "13349076");
				sendSwitch = 2;
				Intent RoomListintent = new Intent();
				RoomListintent.setClass(InterRoom.this, RoomActivity.class);
				InterRoom.this.finish();
				InterRoom.this.startActivity(RoomListintent);
				break;
			}
		}
	}
	
	private void sendLeaveRoomInfo(String RoomId, String Name_Id)
	{
		String JoinRoomURL = "http://172.18.40.26:8080/TestDemo/leaveRoom?RoomId=" + RoomId 
				+ "&NameId=" + Name_Id;

		StringRequest JoinRoomRequest = new StringRequest(Method.GET, JoinRoomURL, 
				new Response.Listener<String>(){
					@Override
					public void onResponse(String response){
						Log.d("TAG", response);

					}
				},new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError error){
						Log.e("TAG", error.getMessage(), error);
					}
				});


		mQueue.add(JoinRoomRequest);
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.room, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		stop = true;
	}

	@Override
	protected void onResume() {
	 /**
	  * 设置为横屏
	  */
	 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	 }
	 super.onResume();
	}
	
}
