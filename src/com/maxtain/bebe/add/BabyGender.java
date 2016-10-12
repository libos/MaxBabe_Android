package com.maxtain.bebe.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.maxtain.bebe.R;

public class BabyGender extends Activity implements OnCheckedChangeListener,
		OnClickListener {
	private ToggleButton boy_toggle;
	private ToggleButton girl_toggle;
	private Button confirm_btn;
	private Button cancel_btn;
	private String babyName;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_gender);
		this.getActionBar().hide();
		babyName = getIntent().getExtras().get("babyName").toString();
		((TextView) findViewById(R.id.baby_gender_title)).setText(babyName
				+ "ÊÇÎ»£¿");

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
		confirm_btn = (Button) findViewById(R.id.baby_gender_confirm_btn);
		cancel_btn = (Button) findViewById(R.id.baby_gender_cancel_btn);
		confirm_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);

	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
		if (btn.getId() != R.id.girl_toggle_btn) {
			girl_toggle.setChecked(!isChecked);
			boy_toggle.setChecked(isChecked);
		}
		if (btn.getId() != R.id.boy_toggle_btn) {
			girl_toggle.setChecked(isChecked);
			boy_toggle.setChecked(!isChecked);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.baby_gender_confirm_btn:
			Intent intent = new Intent(this, BabyBirth.class);
			intent.putExtra("babyName", babyName);
			startActivity(intent);
			break;
		case R.id.baby_gender_cancel_btn:
			finish();
			break;
		default:
			break;
		}

	}
}
