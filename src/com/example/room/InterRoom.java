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
import java.net.InetSocketAddress;
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

	private List<SinglePlayer> mData = null;
	private Context mContext;
	private SinglePlayerAdapter mAdapter = null;
	private ListView names;
	private String[] StringArray;
	private SingleRoom roomData = null;

	private RequestQueue mQueue = null;
	private OkHttpClient client = new OkHttpClient();

	private String dstName = "192.168.23.3";
	private int dstPort = 8888;


	private String UserID = "13349076";
	private String UserName = "13349076";
	private String InRoomSerial = "0";

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

		// 
		Bundle bundle = this.getIntent().getExtras();
		// 
		StringArray = bundle.getStringArray("roominfo");
		UserID = bundle.getString("userid");
		UserName = UserID;
		// mData.add(new
		// SinglePlayer(StringArray[1]));//Create Room Infomation.
		roomData = new SingleRoom(StringArray[0], StringArray[1], StringArray[2], StringArray[3], StringArray[4],
				StringArray[5], StringArray[6]);

		Log.e("InterRoom", StringArray[3]);
		Log.e("InterRoom", StringArray[4]);
		Log.e("InterRoom", StringArray[5]);
		Log.e("InterRoom", StringArray[6]);
		for (int i = 3; i < StringArray.length; i++) {
			if (StringArray[i].length() != 0 && !StringArray[i].equals("null") ) {

				if (!mData.contains(new SinglePlayer(StringArray[i]))) {
					mData.add(new SinglePlayer(StringArray[i])); // 
				}
			}
		}

		Log.e("TimeTask", "TimeTaskDefine");
		timertask = new TimerTask() {
			@Override
			public void run() {
				// Log.e("Timetask", String.valueOf(fullRoom));
				if (fullRoom) {
					Message message = new Message();
					message.what = 1;
					mHandler.sendMessage(message);
				}
			}
		};

		// Deal with timer message, when count == 10, start a new Intent.
		mHandler = new Handler() {
			private int count = 10;

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				count = count - 1;

				Toast.makeText(mContext, Integer.toString(count), Toast.LENGTH_SHORT).show();

				if (count == 0) {
					timer.cancel();
					Intent GameViewintent = new Intent();
					GameViewintent.setClass(InterRoom.this, GameView.class);
					String[] stringArray = new String[]{
							roomData.getRoomId(),
							InRoomSerial
					};

					Bundle bundle = new Bundle();
					bundle.putStringArray("roominfo", StringArray);
					bundle.putString("username", UserName);
					GameViewintent.putExtras(bundle);
					
					InterRoom.this.finish();
					InterRoom.this.startActivity(GameViewintent);
				}
			}
		};

		Log.e("Timer", "TimerStart");
		timer.schedule(timertask, 1000, 1000);
		Log.e("Timer", "TimerSetEnd");

		// getPlayerInfo();
		Log.e("TAG", "CallFunction");
		getPlayerInfo1(dstName, dstPort);
		Log.e("TAG", "CallFunctionEnd");

		mAdapter = new SinglePlayerAdapter((LinkedList<SinglePlayer>) mData, mContext);
		names.setAdapter(mAdapter);

		btnStartGame = (Button) findViewById(R.id.start);
		btnExitRoom = (Button) findViewById(R.id.exit);
		btnStartGame.setOnClickListener(new ClickEvent());
		btnExitRoom.setOnClickListener(new ClickEvent());

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void getPlayerInfo() {
		String url = "http://172.18.40.26:8080/TestDemo/getRoomPlayerList?RoomId=" + StringArray[0];
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("TAG", response);
				if (!response.equals("Failed")) {
					String[] row = response.split(";");
					for (int i = 0; i < row.length; i++) {
						String[] rowData = row[i].split(",");
						SinglePlayer player = new SinglePlayer(rowData[0]);
						if (!mData.contains(player)) {
							mData.add(player); // add player infomation in mData.
						}
					}
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});

		mQueue.add(stringRequest);
	}

	// private ReceiveThread mReceiveThread = null;
	private boolean stop = true;
	private int sendSwitch = 1;

	private void getPlayerInfo1(String dstName, int dstPort) {

		Log.e("Thread", "CreateNewThread");
		stop = false;
		new ReceiveSendThread().execute(dstName, Integer.toString(dstPort), UserID, roomData.getRoomId(),
				roomData.getRoomStyle().substring(0, 1));
		Log.e("Thread", "StartUp");

	}

	private class ReceiveSendThread extends AsyncTask<String, String, String> {
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
		protected String doInBackground(String... params) {
			dstName = params[0];
			dstPort = Integer.parseInt(params[1]);
			NameId = params[2];
			RoomId = params[3];
			RoomStyle = Integer.parseInt(params[4]);

			ApplicationUtil appUtil = (ApplicationUtil) InterRoom.this.getApplication();

			Log.e("dstName", dstName);
			Log.e("dstPort", params[1]);
			Log.e("NameId", NameId);
			Log.e("RoomId", RoomId);
			Log.e("RoomStyle", params[4]);

			try {
				Log.e("AsyncTask", "CreateSocket");
				// clientSocket = new Socket(dstName, dstPort);

				appUtil.init();

				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				clientSocket = appUtil.getSocket();

				// clientSocket.setSoTimeout(timeout);

				Log.e("AsyncTask", "CreateSocketOver");
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (clientSocket != null) {
				SocketConnStatus = clientSocket.isConnected();
				try {
					Log.e("AsyncTask", "getInputStream");
					// inStream = clientSocket.getInputStream();
					// outStream = clientSocket.getOutputStream();

					inStream = appUtil.getDis();
					outStream = appUtil.getDos();

					in = new BufferedReader(new InputStreamReader(inStream));
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outStream)), true);
					Log.e("AsyncTask", "getInputStreamEnd");
				} catch (Exception e) {
					e.printStackTrace();
				}

				//This Thread deamon send LeaveRoom Message.
				new Thread(new Runnable() {
					@Override
					public void run() {

						while (true) {
							if (sendSwitch == 2) {
								byte[] Exitbuf = new byte[512];
								Log.e("ExitRoomThread", "SendLeaveRoomStart");
								String tmp = "ExitRoom:" + RoomId + "," + NameId;

								try {
									Exitbuf = tmp.getBytes("UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								try {
									outStream.write(Exitbuf);
									outStream.flush();
									Log.e("sendExitRoom", tmp);
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								sendSwitch = 0; //Close Send Switch.
								try{
									clientSocket.close();
								} catch(IOException e){
									e.printStackTrace();
								}
								
							}

						}
						//

					}
				}).start();

				while (!stop) {
					// malloc a buffer to get socket message.
					buf = new byte[512];

					// First come in room, Send a message(flag) to get a Serial Number in Room.
 					if (sendSwitch == 1) {
						byte[] Enterbuf = new byte[512];
						Log.e("AsyncTask", "SendEnterRoomStart");
						String tmp = "EnterRoom:" + RoomId + "," + NameId;

						try {
							Enterbuf = tmp.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						try {
							outStream.write(Enterbuf);
							outStream.flush();
							Log.e("sendEnterRoom", tmp);
						} catch (IOException e) {
							e.printStackTrace();
						}

						sendSwitch = 0;
					}

					buf = new byte[512];
					try {
						Log.e("inStream", "readBuf");
						inStream.read(this.buf);
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						Log.e("str", "CreateStr");
						str = new String(this.buf, "UTF-8").trim();
						Log.e("AsyncTask", str);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (str != null) {
						Log.e("publish", str);
						publishProgress(str);
					}

				}
			} else {
				Log.e("AsyncTask", "TM explosion");

			}

			return "End receive.";
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(String... values) {
			String response = values[0];
			if (!response.equals("Failed")) {

				String[] row = response.split(":");
				if (row[0].equals("Join")) {

					if (row[1] != null) {
						
						Log.e("AsyncTask_Join_Name", row[1]);
						Log.e("AsyncTask_Join_Id", row[2]);
						SinglePlayer player = new SinglePlayer(row[1], Integer.parseInt(row[2]));
						if(row[1].equals(UserName)){
							InRoomSerial = row[2]; // Save Current User Serial in Room from Server;
						}
						if (!mData.contains(player)) {
							Log.e("AsyncTask_New_Player_Name", player.getPlayerName());
							mData.add(player); 
							mAdapter.notifyDataSetChanged();
						}
					}
				}

				if (row[0].equals("Leave")) {
					if (row[1] != null) {
						Log.e("AysncTask_Leave", row[1]);
						SinglePlayer player = new SinglePlayer(row[1]);
						if (mData.contains(player)) {
							mData.remove(player);
							mAdapter.notifyDataSetChanged();
						}
					}
				}
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.e("AsyncTask", "setFullRoom");

			// In Release Version Comment It.
			// fullRoom = true;

			if (SocketConnStatus) {
				if(mData.size() == RoomStyle){
					fullRoom = true;
				}
				
			} else {
				Toast.makeText(mContext, new String("Socket Explosion! Please try it again."), Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	class ClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.start:
				Intent GameViewintent = new Intent();
				GameViewintent.setClass(InterRoom.this, GameView.class);
				InterRoom.this.finish();
				InterRoom.this.startActivity(GameViewintent);
				break;
			case R.id.exit:
				// sendLeaveRoomInfo(roomData.getRoomId(), "13349076");
				sendSwitch = 2;
				Intent RoomListintent = new Intent();
				RoomListintent.setClass(InterRoom.this, RoomActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("username", UserName); //Avoid NullPointerException in RoomActivity onCreate() get username
				RoomListintent.putExtras(bundle);

				InterRoom.this.finish();
				InterRoom.this.startActivity(RoomListintent);
				break;
			}
		}
	}

	private void sendLeaveRoomInfo(String RoomId, String Name_Id) {
		String JoinRoomURL = "http://172.18.40.26:8080/TestDemo/leaveRoom?RoomId=" + RoomId + "&NameId=" + Name_Id;

		StringRequest JoinRoomRequest = new StringRequest(Method.GET, JoinRoomURL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("TAG", response);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});

		mQueue.add(JoinRoomRequest);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.room, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	@Override
	public void onDestroy() {
		super.onDestroy();
		stop = true;
		timer.cancel();
	}

	@Override
	protected void onResume() {
		/**
		 * 鑴欓垾鎳婂厽顭嬪啠锟芥拋鍏ゆ崡鍏ｇ煫鍋�㈠伄濂藉厽鈭跺啠锟界妴鍏燂拷
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

}
