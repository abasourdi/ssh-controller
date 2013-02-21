package com.pumkin.sshcontroller.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.constants.Values;
import com.pumkin.sshcontroller.display.ControllerDisplay;
import com.pumkin.sshcontroller.object.Template;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class TemplateAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<Template> templates;
	Context context;

	public TemplateAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		templates = Template.getTemplates();
		this.context = context;
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
			holder.template = (TextView) convertView
					.findViewById(R.id.template);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Drawable drawable = ControllerDisplay.getStateListDrawableFromDesign(
				context, templates.get(position).design);
		holder.template.setBackgroundDrawable(drawable);
		// ControllerDisplay.getLayoutParams(context, Values.defaultWidth,
		// Values.defaultWidth)
		holder.template.setMinimumHeight(SshControllerUtils.convertDpToPx(
				context, templates.get(position).design.height));
		holder.template.setMinimumWidth(SshControllerUtils.convertDpToPx(
				context, templates.get(position).design.width));
		// An other one maybe

		LayoutParams layoutParams = ControllerDisplay.getLayoutParams(context,
				0, 0);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		holder.template.setLayoutParams(layoutParams);

		return convertView;
	}

	private class ViewHolder {
		TextView template;
	}
}
