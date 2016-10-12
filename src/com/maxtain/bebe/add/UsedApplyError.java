package com.maxtain.bebe.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maxtain.bebe.R;

public class UsedApplyError extends Activity {
	private Button confirm_known;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_used_apply_error);
		this.getActionBar().hide();
		confirm_known = (Button) findViewById(R.id.used_confirm_known);
		confirm_known.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UsedApplyError.this, UsedRecv.class);
				startActivity(intent);

			}
		});
	}
}
