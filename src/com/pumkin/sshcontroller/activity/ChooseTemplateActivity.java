package com.pumkin.sshcontroller.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pumkin.sshcontroller.adapter.ControllerAdapter;
import com.pumkin.sshcontroller.adapter.TemplateAdapter;
import com.pumkin.sshcontroller.object.Button;

public class ChooseTemplateActivity extends SshControllerActivity implements
OnClickListener {

	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_template);
		if (getIntent().getExtras().get("uuid") != null) {
			button = controller.getButtonByUuid(getIntent().getExtras()
					.get("uuid").toString());
		} else {
			button = null;
		}

		// Then, we load the buttons
		ListView lvListe = (ListView) findViewById(R.id.templateList);
		TemplateAdapter templateAdapter = new TemplateAdapter(this);
		lvListe.setAdapter(templateAdapter);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getName(), "calling onItemClick for view: "+view);
		// If the button does not exist, we create it
		if (button == null) {

		} else {
			// Else, we just update it with the new colors
		}
		onBackPressed();
	}
}
