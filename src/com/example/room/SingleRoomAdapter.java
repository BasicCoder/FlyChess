package com.example.room;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SingleRoomAdapter extends BaseAdapter{
	private LinkedList<SingleRoom> mData;
	private Context mContext;
	int selectItem = -1;
	public SingleRoomAdapter(LinkedList<SingleRoom> mData, Context mContext)
	{
		this.mData = mData;
		this.mContext = mContext;
	}
	public void add(SingleRoom data)
	{
		if(mData == null){
			mData = new LinkedList<SingleRoom>();
		}
		mData.add(data);
		notifyDataSetChanged();
	}
	public void add(int position, SingleRoom data)
	{
		if(mData == null){
			mData = new LinkedList<SingleRoom>();
		}
		mData.add(position, data);
		notifyDataSetChanged();
	}
	
	public void remove(SingleRoom data)
	{
		if(mData != null)
		{
			mData.remove(data);
		}
		notifyDataSetChanged();
	}
	public void remove(int position){
		if(mData != null) {
	        mData.remove(position);
	    }
	    notifyDataSetChanged();
	}
	public void setSelectItem(int position)
	{
		this.selectItem = position;
	}
	@Override
	public int getCount(){
		return mData.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return null;
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.room_list, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.roomholder = (TextView) convertView.findViewById(R.id.desc);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		/*TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView desc = (TextView) convertView.findViewById(R.id.desc);
		TextView number = (TextView) convertView.findViewById(R.id.number);*/
		
		holder.name.setText(mData.get(position).getRoomName());
		holder.roomholder.setText(mData.get(position).getRoomHolder_Name());
		holder.number.setText(mData.get(position).getRoomStyle());
		
		Drawable background = convertView.getBackground();;
		if(position == selectItem){
			convertView.setBackgroundColor(Color.BLUE); 
		}
		else{
			//convertView.setBackgroundColor(Color.TRANSPARENT); 
			convertView.setBackgroundDrawable(background);
		}
		return convertView;
	}
	class ViewHolder{
		TextView name;
		TextView roomholder;
		TextView number;
	}
}
