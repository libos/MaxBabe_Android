package com.maxtain.bebe.add;

import com.maxtain.bebe.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UsedBind extends Activity {
	private Button confirm_btn;
	private Button cancel_btn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_used_bind);
		this.getActionBar().hide();
		confirm_btn = (Button) findViewById(R.id.bind_confirm_btn);
		cancel_btn = (Button) findViewById(R.id.bind_cancel_btn);

		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UsedBind.this, UsedBindApply.class);
				startActivity(intent);
			}
		});
	}

}
