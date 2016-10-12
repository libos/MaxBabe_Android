package com.maxtain.bebe.add;

import com.maxtain.bebe.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BabyName extends Activity implements OnClickListener {
	private Button confirmBtn;
	private Button cancelBtn;
	private EditText babyName;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_name);
		this.getActionBar().hide();

		confirmBtn = (Button) findViewById(R.id.baby_name_confirm_btn);
		cancelBtn = (Button) findViewById(R.id.baby_name_cancel_btn);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		babyName = (EditText) findViewById(R.id.baby_name_input);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.baby_name_confirm_btn:
			Intent intent = new Intent(this, BabyGender.class);
			intent.putExtra("babyName", babyName.getText().toString());
			startActivity(intent);
			break;
		case R.id.baby_name_cancel_btn:
			finish();
			break;
		default:
			break;
		}

	}
}
