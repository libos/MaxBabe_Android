package com.maxtain.bebe;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.umeng.analytics.MobclickAgent;

public class WeatherSetActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private ImageView iv_back;

	private RelativeLayout layout_send;
	private ToggleButton iv_send;
	private RelativeLayout layout_auto;
	private ToggleButton iv_auto;
	private RelativeLayout layout_wifi;
	private ToggleButton iv_wifi;
	private RelativeLayout layout_notificate;
	private ToggleButton iv_notification;
	private RelativeLayout layout_gps;
	private ToggleButton iv_gps;
	private SharedPreferences _sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weatherset);
		this.getActionBar().hide();
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		layout_send = (RelativeLayout) findViewById(R.id.layout_send);
		layout_send.setOnClickListener(this);
		layout_auto = (RelativeLayout) findViewById(R.id.layout_auto);
		layout_auto.setOnClickListener(this);
		layout_wifi = (RelativeLayout) findViewById(R.id.layout_wifi);
		layout_wifi.setOnClickListener(this);
		layout_notificate = (RelativeLayout) findViewById(R.id.layout_notificate);
		layout_notificate.setOnClickListener(this);
		layout_gps = (RelativeLayout) findViewById(R.id.layout_gps);
		layout_gps.setOnClickListener(this);

		iv_send = (ToggleButton) findViewById(R.id.iv_send);
		iv_auto = (ToggleButton) findViewById(R.id.iv_auto);
		iv_wifi = (ToggleButton) findViewById(R.id.iv_wifi);
		iv_notification = (ToggleButton) findViewById(R.id.iv_notificate);
		iv_gps = (ToggleButton) findViewById(R.id.iv_gps);

		iv_send.setChecked(_sp
				.getBoolean(BabeConst.SETTING_SEND_PUSH_MSG, true));
		iv_auto.setChecked(_sp.getBoolean(BabeConst.SETTING_AUTOUPDATE, true));
		iv_wifi.setChecked(_sp.getBoolean(BabeConst.SETTING_WIFIONLY, false));
		iv_gps.setChecked(_sp.getBoolean(BabeConst.SETTING_USE_GPS, false));
		iv_notification.setChecked(_sp.getBoolean(
				BabeConst.SETTING_NOTIFICATION, true));

		iv_send.setOnCheckedChangeListener(this);
		iv_auto.setOnCheckedChangeListener(this);
		iv_wifi.setOnCheckedChangeListener(this);
		iv_gps.setOnCheckedChangeListener(this);
		iv_notification.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_send:
			iv_send.toggle();
			break;
		case R.id.layout_auto:
			iv_auto.toggle();
			break;
		case R.id.layout_wifi:
			iv_wifi.toggle();
			break;
		case R.id.layout_notificate:
			iv_notification.toggle();
			break;
		case R.id.layout_gps:
			iv_gps.toggle();
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean checked) {
		String set_what = null;
		switch (button.getId()) {
		case R.id.iv_send:
			set_what = BabeConst.SETTING_SEND_PUSH_MSG;
			break;
		case R.id.iv_auto:
			set_what = BabeConst.SETTING_AUTOUPDATE;
			break;
		case R.id.iv_wifi:
			set_what = BabeConst.SETTING_WIFIONLY;
			break;
		case R.id.iv_notificate:
			set_what = BabeConst.SETTING_NOTIFICATION;
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			if (!checked) {
				nm.cancel(0);
			} else {
				_sp.edit().putBoolean(set_what, true).commit();
				Babe.makeNotification(getApplicationContext(),
						WeatherData.loadNowWeather(this));
			}
			break;
		case R.id.iv_gps:
			set_what = BabeConst.SETTING_USE_GPS;
			break;
		default:
			break;
		}

		Editor _sp_editor = _sp.edit();
		_sp_editor.putBoolean(set_what, checked);
		_sp_editor.commit();

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}
}
