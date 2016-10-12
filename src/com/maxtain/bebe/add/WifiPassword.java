package com.maxtain.bebe.add;

import com.maxtain.bebe.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class WifiPassword extends Activity {
	private Button cancel_btn;
	private Button connect_btn;
	private CheckBox display_passcode;
	private EditText wifi_passcode;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_password);
		this.getActionBar().hide();

		String ssid = getIntent().getExtras().getString("ssid");
		((TextView) findViewById(R.id.pass_wifi_name)).setText(ssid);
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		connect_btn = (Button) findViewById(R.id.connect_btn);
		display_passcode = (CheckBox) findViewById(R.id.display_passcode);
		wifi_passcode = (EditText) findViewById(R.id.wifi_passcode);
		display_passcode
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {

						if (isChecked) {
							wifi_passcode
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
						} else {
							wifi_passcode
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}

					}
				});
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		connect_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WifiPassword.this,
						ConnectResult.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});

	}

}
