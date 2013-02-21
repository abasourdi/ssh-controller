package com.pumkin.sshcontroller.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.activity.SshControllerActivity;
import com.pumkin.sshcontroller.object.SshFile;

public class ChooseFileAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;

	public ChooseFileAdapter(Context context, ArrayList<SshFile> sshFiles) {
		inflater = LayoutInflater.from(context);
		this.sshFiles=sshFiles;
	}
	
	ArrayList<SshFile> sshFiles;

	@Override
	public int getCount() {
		return sshFiles.size();
	}

	@Override
	public SshFile getItem(int position) {
		return sshFiles.get(position);
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
			convertView = inflater.inflate(R.layout.item_file, null);
			holder.name = (TextView) convertView.findViewById(R.id.fileName);
			holder.picture = (ImageView) convertView
					.findViewById(R.id.filePicture);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(sshFiles.get(position).name);
		if(getItem(position).isDirectory){
			holder.picture.setImageResource(R.drawable.folder);
		}else if(getItem(position).isExecutable){
			holder.picture.setImageResource(R.drawable.executable_file);
		}else if(getItem(position).isReadable){
			holder.picture.setImageResource(R.drawable.executable_file);
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView picture;
		TextView name;
	}
}
