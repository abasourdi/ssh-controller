package com.pumkin.sshcontroller.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pumkin.sshcontroller.adapter.TemplateAdapter;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.Design;

public class ChooseTemplateActivity extends SshControllerActivity implements
		OnItemClickListener {

	Button button;
	TemplateAdapter templateAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_template);
		if (getIntent() != null && getIntent().getExtras() != null
				&& getIntent().getExtras().get("uuid") != null) {
			button = controller.getButtonByUuid(getIntent().getExtras()
					.get("uuid").toString());
		} else {
			button = null;
		}

		// Then, we load the buttons
		GridView lvListe = (GridView) findViewById(R.id.templateList);
		templateAdapter = new TemplateAdapter(this);
		lvListe.setAdapter(templateAdapter);
		lvListe.setOnItemClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		Log.i(this.getClass().getName(), "calling onItemClick for view: "
				+ view);
		// Now, we
		Design newDesign = templateAdapter.getItem(position).design;

		if (button == null) {
			Button newButton = new Button(newDesign);
			controller.addButton(newButton);
			Controller.saveControllers();
		} else {
			button.design = newDesign;
		}
		onBackPressed();
	}
}
