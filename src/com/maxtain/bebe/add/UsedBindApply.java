package com.maxtain.bebe.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maxtain.bebe.R;

public class UsedBindApply extends Activity {
	private Button return_home;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_used_bind_apply);
		this.getActionBar().hide();
		return_home = (Button)findViewById(R.id.return_home);
		return_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UsedBindApply.this,UsedApplyError.class);
				startActivity(intent);
			}
		});
	}
}
