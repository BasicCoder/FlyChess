package com.example.room;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SinglePlayerAdapter extends BaseAdapter{
	private LinkedList<SinglePlayer> mData;
	private Context mContext;
	
	public SinglePlayerAdapter(LinkedList<SinglePlayer> mData, Context mContext)
	{
		this.mData = mData;
		this.mContext = mContext;
	}
	public void add(SinglePlayer data)
	{
		if(mData == null){
			mData = new LinkedList<SinglePlayer>();
		}
		mData.add(data);
		notifyDataSetChanged();
	}
	public void add(int position, SinglePlayer data)
	{
		if(mData == null){
			mData = new LinkedList<SinglePlayer>();
		}
		mData.add(position, data);
		notifyDataSetChanged();
	}
	
	public void remove(SinglePlayer data)
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
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.name_list, parent, false);
			holder = new ViewHolder();
			holder.PlayerName = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.PlayerName.setText(mData.get(position).getPlayerName());
		return convertView;
	}
	
	class ViewHolder{
		TextView PlayerName;
	}
}
