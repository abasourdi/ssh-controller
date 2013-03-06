package com.pumkin.sshcontroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;

public class ControllerAdapter extends BaseAdapter {

	LayoutInflater inflater;

	public ControllerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Controller.controllers.size();
	}

	@Override
	public Controller getItem(int position) {
		return Controller.controllers.get(position);
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
			convertView = inflater.inflate(R.layout.item_controller, null);
			holder.name = (TextView) convertView.findViewById(R.id.controllerName);
			holder.status = (TextView) convertView
					.findViewById(R.id.controllerStatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(Controller.controllers.get(position).name);
		switch(Controller.controllers.get(position).state){
		case Controller._CONNECTED:
			holder.status.setBackgroundDrawable(CurrentConfiguration.instance.getResources().getDrawable(R.drawable.controller_state_online));
			break;
		case Controller._DISCONNECTED:
			holder.status.setBackgroundDrawable(CurrentConfiguration.instance.getResources().getDrawable(R.drawable.controller_state_offline));
			break;
		case Controller._UNKNOWN:
			holder.status.setBackgroundDrawable(CurrentConfiguration.instance.getResources().getDrawable(R.drawable.controller_state_unknown));
			break;
		}
		
		return convertView;

	}

	private class ViewHolder {
		TextView status;
		TextView name;
	}
}
