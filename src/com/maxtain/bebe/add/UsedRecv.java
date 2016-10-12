package com.maxtain.bebe.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maxtain.bebe.MainActivity;
import com.maxtain.bebe.R;

public class UsedRecv extends Activity {
	private Button agree_btn;
	private Button reject_btn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_used_recv);
		this.getActionBar().hide();

		agree_btn = (Button) findViewById(R.id.btn_recv_agree);
		reject_btn = (Button) findViewById(R.id.btn_recv_reject);

		agree_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UsedRecv.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		reject_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UsedRecv.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

	}
}
