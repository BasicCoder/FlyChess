package com.example.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RoomActivity extends Activity {
	
	private String[] names = new String[]{"武当派","峨眉派","少林派","华山派","崆峒派","明教"};
	private String[] desc = new String[]{"张三丰","灭绝师太","空见","岳不群","崆峒五老","张无忌"};
	private List<SingleRoom> mData = null;
	private Context mContext;
	private SingleRoomAdapter mAdapter = null;
	private ListView rooms;
	private int mClickPosition = 0;

	private String UserID = "13349076";
	private String UserName = "13349076";
	
	private RequestQueue mQueue = null;
	private RequestQueue mCreateRoomQueue = null;
	
	private String dstName = "172.19.54.9";
	private int dstPort = 8080;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_room);
		
		mContext = RoomActivity.this;
		rooms = (ListView) findViewById(R.id.rooms);
		
		mQueue = Volley.newRequestQueue(mContext);
		mCreateRoomQueue = Volley.newRequestQueue(mContext);
		
		Bundle bundle = this.getIntent().getExtras();
		UserName = bundle.getString("username");
		UserID = UserName;
		Log.e("RoomActivity", UserID);

		mData = new LinkedList<SingleRoom>();
		getRoomData();
		for(int i = 0; i < names.length; i++)
		{
			mData.add(new SingleRoom(Integer.toString(i), names[i], new String("4人局"), desc[i],  
					new String(), new String(), new String()));
		}
		mAdapter = new SingleRoomAdapter((LinkedList<SingleRoom>)mData, mContext);
		rooms.setAdapter(mAdapter);
		rooms.setOnItemClickListener(new ListViewItemClick());
		
		
		/*List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < names.length; i++){
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("names", names[i]);
			listItem.put("desc", desc[i]);
			listItems.add(listItem);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.room_list, new String[]{"names","desc"}, new int []{R.id.name, R.id.desc});
		ListView list = (ListView) findViewById(R.id.room);
		list.setAdapter(simpleAdapter);*/
		
	}
	
	private void getRoomData()
	{
		String url = "http://" + dstName + ":" + Integer.toString(dstPort) +"/TestDemo/getRoomList";
		
		StringRequest stringRequest = new StringRequest(url, 
				new Response.Listener<String>(){
					@Override
					public void onResponse(String response){
						Log.d("GetRoomData", response);
						if(!response.equals("Failed")){
							String[] row = response.split(";");
							for(int i = 0; i < row.length; i++)
							{
								String[] rowData = row[i].split(",");
								
								mData.add(new SingleRoom(rowData[0], rowData[1], rowData[2]+"人局", rowData[3],  
										rowData[4], rowData[5], rowData[6]));
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
	
	//监听用户点击房间列表项
	class ListViewItemClick implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			mAdapter.setSelectItem(position);
			mAdapter.notifyDataSetInvalidated();
			mClickPosition = position;
			Toast.makeText(mContext,"你点击了第" + (position+1) + "项",Toast.LENGTH_SHORT).show();
		}
	}
	
	//Button点击创建房间
	private AlertDialog alert = null;
	private AlertDialog.Builder builder = null;
	private EditText RoomName;
	private Spinner Spin_RoomStyle;
	private String StyleOfRoom = new String("4人局");
	private String CreateRoomId = new String();
	private String NameOfRoom = new String();
	public void createRoom(View sourse){
		LayoutInflater inflater = RoomActivity.this.getLayoutInflater();
		View RegisterRoom = inflater.inflate(R.layout.select_list, null, false);
		//LinearLayout RegisterRoom = (LinearLayout)getLayoutInflater().inflate( R.layout.select_list, null);
		RoomName = (EditText) RegisterRoom.findViewById(R.id.nameofroom);
		
		//Spin监听器设置
		Spin_RoomStyle = (Spinner) RegisterRoom.findViewById(R.id.numbersel);
		Spin_RoomStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,  int position, long id){
				String[] array = getResources().getStringArray(R.array.numroom);
				StyleOfRoom = array[position];
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				Toast.makeText(getApplicationContext(), "没有改变的处理",  Toast.LENGTH_LONG).show();
			}
		});
		
		builder = new AlertDialog.Builder(mContext);
		builder.setView(RegisterRoom).setTitle("创建房间");
		builder.setPositiveButton("确定", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				
					NameOfRoom = RoomName.getText().toString().trim();
					//String StyleOfRoom = new String("4人局");
					if(NameOfRoom.equals(""))
					{
						Toast.makeText(mContext, "Please enter Room Name!", 5000).show();
					}
					else{	
						Intent intent = new Intent();
						intent.setClass(RoomActivity.this, InterRoom.class);
						
						
						sendCreateRoomInfo(NameOfRoom, StyleOfRoom, UserID, intent);

						/*while(CreateRoomId.isEmpty()){

						}*/

						//Log.e("ReceivedRoomId", CreateRoomId);
						
						 
						alert.dismiss();
					}
			}
							
		});
		builder.setNegativeButton("取消", new OnClickListener(){
			@Override 
			public void onClick(DialogInterface dialog, int which){
				alert.dismiss();
			}
		});
		
		alert = builder.create();
		alert.show();
		
		/*
		new AlertDialog.Builder(this).setView(RegisterRoom)
		.setPositiveButton("确定", new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which){
							Intent intent = new Intent();
							intent.setClass(RoomActivity.this, InterRoom.class);
							RoomActivity.this.finish();
							RoomActivity.this.startActivity(intent);  

						}
					})
		.setNegativeButton("取消", new OnClickListener(){
				@Override 
						public void onClick(DialogInterface dialog, int which){
					
					}
			})
		.create()
		.show();
		*/
		
	}
	
	//发送创建房间信息
	private void sendCreateRoomInfo(String NameofRoom_, String StyleOfRoom_, String id_, final Intent intent)
	{
		String createRoomURL = "http://" + dstName + ":" + Integer.toString(dstPort) +"/TestDemo/addRoom?name="
				+ NameofRoom_ + "&type="+StyleOfRoom_.substring(0,1)+"&id="+id_;
		
		StringRequest CreateRoomRequest = new StringRequest(Method.GET,createRoomURL, 
				new Response.Listener<String>(){
					@Override
					public void onResponse(String response){
						Log.e("SendCreatRoom1", response);
						CreateRoomId = response;
						String[] StringArray = new String[]{CreateRoomId,
															NameOfRoom,
															StyleOfRoom,
															UserID,
															new String(),
															new String(),
															new String()
															};
						Bundle bundle = new Bundle();
						bundle.putStringArray("roominfo", StringArray);
						bundle.putString("username", UserName);
						intent.putExtras(bundle);
						RoomActivity.this.finish();
						RoomActivity.this.startActivity(intent); 
						Log.e("SendCreatRoom2", CreateRoomId);
					}
				},new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError error){
						Log.e("TAG", error.getMessage(), error);
					}
				}){
					@Override
					public Priority getPriority(){
						Priority priority = Priority.IMMEDIATE;
						return priority;
					}
				};

		
				mCreateRoomQueue.add(CreateRoomRequest);
	}
	
	//Button点击加入房间信息
	public void joinRoom(View sourse){
		Intent intent = new Intent();
		intent.setClass(RoomActivity.this, InterRoom.class);
		
		 //new一个Bundle对象，并将要传递的数据传入
		SingleRoom singleroominfo = mData.get(mClickPosition);
		String[] StringArray = new String[]{singleroominfo.getRoomId(),
											singleroominfo.getRoomName(), 
											singleroominfo.getRoomStyle(),
											singleroominfo.getRoomHolder_Name(),
											singleroominfo.getPlayer1_Name(),
											singleroominfo.getPlayer2_Name(),
											singleroominfo.getPlayer3_Name()
											};
		sendJoinRoomRequest(singleroominfo.getRoomId(), UserID);
		Bundle bundle = new Bundle();
		bundle.putStringArray("roominfo", StringArray);
		bundle.putString("username", UserName);
		intent.putExtras(bundle);
		
		startActivity(intent);
		RoomActivity.this.finish();//会触发onDestroy();
	}
	
	private void sendJoinRoomRequest(String RoomId, String Name_Id)
	{
		String JoinRoomURL = "http://" + dstName + ":" + Integer.toString(dstPort) +"/TestDemo/joinRoom?RoomId=" + RoomId 
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
