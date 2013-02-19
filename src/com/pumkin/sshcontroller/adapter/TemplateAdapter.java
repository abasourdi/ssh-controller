package com.pumkin.sshcontroller.adapter;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.pumkin.sshcontroller.activity.ChooseTemplateActivity;
import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.display.ControllerDisplay;
import com.pumkin.sshcontroller.object.Template;

public class TemplateAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<Template> templates;
	ChooseTemplateActivity chooseTemplateActivity;

	public TemplateAdapter(ChooseTemplateActivity chooseTemplateActivity) {
		inflater = LayoutInflater.from(chooseTemplateActivity);
		templates = Template.getTemplates();
		this.chooseTemplateActivity = chooseTemplateActivity;
	}

	@Override
	public int getCount() {
		return templates.size();
	}

	@Override
	public Template getItem(int position) {
		return templates.get(position);
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
			convertView = inflater.inflate(R.layout.item_template, null);
			holder.template = (Button) convertView.findViewById(R.id.template);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Set the background of the template + the name
		com.pumkin.sshcontroller.object.Button button = templates.get(position)
				.generateButtonFromTemplate();
		Drawable drawable = ControllerDisplay.getStateListDrawableFromType(
				chooseTemplateActivity, button);
		holder.template.setBackgroundDrawable(drawable);

		holder.template.setLayoutParams(ControllerDisplay.getLayoutParams(
				chooseTemplateActivity, button));
		
		holder.template.setOnClickListener(chooseTemplateActivity);
		return convertView;
	}

	private class ViewHolder {
		Button template;
	}
}
