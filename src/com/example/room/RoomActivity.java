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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RoomActivity extends Activity {
	
	private String[] names = new String[]{"�䵱��","��ü��","������","��ɽ��","�����","����"};
	private String[] desc = new String[]{"������","���ʦ̫","�ռ�","����Ⱥ","�������","���޼�"};
	private List<SingleRoom> mData = null;
	private Context mContext;
	private SingleRoomAdapter mAdapter = null;
	private ListView rooms;
	private int mClickPosition = 0;
	
	private RequestQueue mQueue = null;
	
	private String dstName = "172.18.40.26";
	private int dstPort = 8080;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_room);
		
		mContext = RoomActivity.this;
		rooms = (ListView) findViewById(R.id.rooms);
		
		mQueue = Volley.newRequestQueue(mContext);
		
		mData = new LinkedList<SingleRoom>();
		getRoomData();
		for(int i = 0; i < names.length; i++)
		{
			mData.add(new SingleRoom(Integer.toString(i), names[i], new String("4�˾�"), desc[i],  
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
						Log.d("TAG", response);
						if(!response.equals("Failed")){
							String[] row = response.split(";");
							for(int i = 0; i < row.length; i++)
							{
								String[] rowData = row[i].split(",");
								
								mData.add(new SingleRoom(rowData[0], rowData[1], rowData[2]+"�˾�", rowData[3],  
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
	
	//�����û���������б���
	class ListViewItemClick implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			mAdapter.setSelectItem(position);
			mAdapter.notifyDataSetInvalidated();
			mClickPosition = position;
			Toast.makeText(mContext,"�����˵�" + (position+1) + "��",Toast.LENGTH_SHORT).show();
		}
	}
	
	//�����������
	private AlertDialog alert = null;
	private AlertDialog.Builder builder = null;
	private EditText RoomName;
	private Spinner Spin_RoomStyle;
	private String StyleOfRoom = new String("4�˾�");
	private String CreateRoomId;
	public void createRoom(View sourse){
		LayoutInflater inflater = RoomActivity.this.getLayoutInflater();
		View RegisterRoom = inflater.inflate(R.layout.select_list, null, false);
		//LinearLayout RegisterRoom = (LinearLayout)getLayoutInflater().inflate( R.layout.select_list, null);
		RoomName = (EditText) RegisterRoom.findViewById(R.id.nameofroom);
		
		//Spin����������
		Spin_RoomStyle = (Spinner) RegisterRoom.findViewById(R.id.numbersel);
		Spin_RoomStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,  int position, long id){
				String[] array = getResources().getStringArray(R.array.numroom);
				StyleOfRoom = array[position];
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				Toast.makeText(getApplicationContext(), "û�иı�Ĵ���",  Toast.LENGTH_LONG).show();
			}
		});
		
		builder = new AlertDialog.Builder(mContext);
		builder.setView(RegisterRoom).setTitle("��������");
		builder.setPositiveButton("ȷ��", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				
					String NameOfRoom = RoomName.getText().toString().trim();
					//String StyleOfRoom = new String("4�˾�");
					if(NameOfRoom.equals(""))
					{
						Toast.makeText(mContext, "Please enter Room Name!", 5000).show();
					}
					else{	
						Intent intent = new Intent();
						intent.setClass(RoomActivity.this, InterRoom.class);
						
						
						sendCreateRoomInfo(NameOfRoom, StyleOfRoom, "13349076");
						
						String[] StringArray = new String[]{CreateRoomId,
															NameOfRoom,
															StyleOfRoom,
															"13349076",
															};
						Bundle bundle = new Bundle();
						bundle.putStringArray("roominfo", StringArray);
						intent.putExtras(bundle);
						RoomActivity.this.finish();
						RoomActivity.this.startActivity(intent);  
						alert.dismiss();
					}
			}
							
		});
		builder.setNegativeButton("ȡ��", new OnClickListener(){
			@Override 
			public void onClick(DialogInterface dialog, int which){
				alert.dismiss();
			}
		});
		
		alert = builder.create();
		alert.show();
		
		/*
		new AlertDialog.Builder(this).setView(RegisterRoom)
		.setPositiveButton("ȷ��", new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which){
							Intent intent = new Intent();
							intent.setClass(RoomActivity.this, InterRoom.class);
							RoomActivity.this.finish();
							RoomActivity.this.startActivity(intent);  

						}
					})
		.setNegativeButton("ȡ��", new OnClickListener(){
				@Override 
						public void onClick(DialogInterface dialog, int which){
					
					}
			})
		.create()
		.show();
		*/
		
	}
	
	//���ʹ���������Ϣ
	private void sendCreateRoomInfo(String NameofRoom_, String StyleOfRoom_, String id_)
	{
		String createRoomURL = "http://" + dstName + ":" + Integer.toString(dstPort) +"/TestDemo/addRoom?name="
				+ NameofRoom_ + "&type="+StyleOfRoom_.substring(0,1)+"&id="+id_;
		
		StringRequest CreateRoomRequest = new StringRequest(Method.GET,createRoomURL, 
				new Response.Listener<String>(){
					@Override
					public void onResponse(String response){
						Log.d("TAG", response);
						CreateRoomId = response;
					}
				},new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError error){
						Log.e("TAG", error.getMessage(), error);
					}
				});

		
		mQueue.add(CreateRoomRequest);
	}
	
	//������뷿����Ϣ
	public void joinRoom(View sourse){
		Intent intent = new Intent();
		intent.setClass(RoomActivity.this, InterRoom.class);
		
		 //newһ��Bundle���󣬲���Ҫ���ݵ����ݴ���
		SingleRoom singleroominfo = mData.get(mClickPosition);
		String[] StringArray = new String[]{singleroominfo.getRoomId(),
											singleroominfo.getRoomName(), 
											singleroominfo.getRoomStyle(),
											singleroominfo.getRoomHolder_Name(),
											singleroominfo.getPlayer1_Name(),
											singleroominfo.getPlayer2_Name(),
											singleroominfo.getPlayer3_Name()
											};
		sendJoinRoomRequest(singleroominfo.getRoomId(),"13349076");
		Bundle bundle = new Bundle();
		bundle.putStringArray("roominfo", StringArray);
		intent.putExtras(bundle);
		
		startActivity(intent);
		RoomActivity.this.finish();//�ᴥ��onDestroy();
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
	  * ����Ϊ����
	  */
	 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	 }
	 super.onResume();
	}
	
}
