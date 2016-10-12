package com.maxtain.bebe.setting;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.maxtain.bebe.R;

public class ReviseGenderActivity extends Activity implements
		OnCheckedChangeListener {

	private ToggleButton boy_toggle;
	private ToggleButton girl_toggle;

	private TextView tv_revise_title;
	private Button cancel_btn;
	private Button confirm_btn;

	private Map<String, String> titleMap = new HashMap<String, String>();
	private String key;
	private String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.revise_gender);
		this.getActionBar().hide();

		titleMap.put("gender", "性别");

		Intent intent = getIntent();
		key = intent.getStringExtra("Title");

		boy_toggle = (ToggleButton) findViewById(R.id.boy_toggle_btn);
		girl_toggle = (ToggleButton) findViewById(R.id.girl_toggle_btn);
		// boy_toggle.setOnCheckedChangeListener(this);
		// girl_toggle.setOnCheckedChangeListener(this);
		girl_toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				boy_toggle.setChecked(false);
				girl_toggle.setChecked(true);
			}
		});
		boy_toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boy_toggle.setChecked(true);
				girl_toggle.setChecked(false);
			}
		});

		initView();
	}

	private void initView() {
		tv_revise_title = (TextView) findViewById(R.id.tv_revise_title);
		tv_revise_title.setText(titleMap.get(key));

		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		confirm_btn = (Button) findViewById(R.id.confirm_btn);
		confirm_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if(boy_toggle.isChecked()){
				 result = "男";
				 }else{
				 result = "女";
				 }
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", result);
				intent.putExtras(bundle);
				ReviseGenderActivity.this.setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
		// TODO Auto-generated method stub

		if (btn.getId() == R.id.girl_toggle_btn) {

			result = "女";
		}
		if (btn.getId() == R.id.boy_toggle_btn) {
			if (isChecked) {
				girl_toggle.setChecked(false);
			} else {
				boy_toggle.setChecked(true);
			}
			result = "男";
		}

	}

}
