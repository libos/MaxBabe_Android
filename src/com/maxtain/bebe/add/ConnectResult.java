package com.maxtain.bebe.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maxtain.bebe.MainActivity;
import com.maxtain.bebe.R;

public class ConnectResult extends Activity implements OnClickListener {
	private Button enterApp;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_result);
		this.getActionBar().hide();

		enterApp = (Button) findViewById(R.id.enter_app);
		enterApp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enter_app:
			Intent intent = new Intent(this, BabyName.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 Intent intent = new Intent(this,MainActivity.class);
			 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			 startActivity(intent);
			 return true; 
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
