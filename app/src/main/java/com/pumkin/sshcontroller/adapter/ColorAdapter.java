package com.pumkin.sshcontroller.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.activity.SshControllerActivity;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.SshFile;

public class ColorAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;

	public ColorAdapter(Context context, int[] colors) {
		inflater = LayoutInflater.from(context);
		this.context=context;
		this.colors=colors;
	}

	int[] colors;

	@Override
	public int getCount() {
		return colors.length+1;
	}

	@Override
	public Integer getItem(int position) {
		if(position==colors.length){
			return null;
		}else{
			return colors[position];
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_color, null);
			holder.name = (TextView) convertView.findViewById(R.id.colorName);
			holder.color = (TextView) convertView
					.findViewById(R.id.color);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position==colors.length){
			holder.color.setVisibility(View.GONE);
			holder.name.setText(context.getString(R.string.add_new_color));
		}else{
			holder.color.setBackgroundColor(colors[position]);
			holder.name.setText("toto todo"+position);
		}
				
		return convertView;
	}

	private class ViewHolder {
		TextView color;
		TextView name;
	}
}
