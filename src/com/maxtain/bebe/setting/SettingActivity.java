package com.maxtain.bebe.setting;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxtain.bebe.AboutUSActivity;
import com.maxtain.bebe.R;
import com.maxtain.bebe.WeatherSetActivity;
import com.maxtain.bebe.account.AccountActivity;
import com.maxtain.bebe.account.LoginActivity;
import com.maxtain.bebe.util.BabeConst;

@SuppressLint("NewApi")
public class SettingActivity extends Activity implements OnClickListener {

	private ImageView iv_close;
	private RelativeLayout rl_login;
	private TextView tv_account;

	private TextView tv_weatherset;
	private TextView tv_aboutus;

	private TextView tv_monitoradd;
	private TextView tv_bebeinfo;

	private SharedPreferences _sp;
	private String username;
	private String nickname;
	private String password;
	private String phone;
	private String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		this.getActionBar().hide();

		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

		initView();
	}

	private void initView() {
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_close.setOnClickListener(this);
		rl_login = (RelativeLayout) findViewById(R.id.rl_login);
		rl_login.setOnClickListener(this);

		tv_account = (TextView) findViewById(R.id.tv_account);
		tv_account.setOnClickListener(this);

		tv_weatherset = (TextView) findViewById(R.id.tv_weatherset);
		tv_weatherset.setOnClickListener(this);

		tv_aboutus = (TextView) findViewById(R.id.tv_aboutus);
		tv_aboutus.setOnClickListener(this);

		tv_monitoradd = (TextView) findViewById(R.id.tv_monitoradd);
		tv_monitoradd.setOnClickListener(this);
		tv_bebeinfo = (TextView) findViewById(R.id.tv_bebeinfo);
		tv_bebeinfo.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		username = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
		nickname = _sp.getString(BabeConst.ACCOUNT_NICKNAME, "");
		password = _sp.getString(BabeConst.ACCOUNT_PASSWORD, "");
		phone = _sp.getString(BabeConst.ACCOUNT_PHONE, "");

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(nickname)
				|| StringUtils.isEmpty(phone)) {
			rl_login.setVisibility(View.VISIBLE);
			tv_account.setVisibility(View.GONE);
		}
		if (StringUtils.isNotEmpty(username)
				&& StringUtils.isNotEmpty(nickname)
				&& StringUtils.isNotEmpty(phone)) {
			tv_account.setText(nickname);

			rl_login.setVisibility(View.GONE);
			tv_account.setVisibility(View.VISIBLE);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_close:
			finish();
			break;
		case R.id.rl_login:
			intent = new Intent(getBaseContext(), LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_account:
			intent = new Intent(getBaseContext(), AccountActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_weatherset:
			intent = new Intent(getBaseContext(), WeatherSetActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_aboutus:
			intent = new Intent(getBaseContext(), AboutUSActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		// case R.id.tv_monitoradd:
		// if (true) {
		// intent = new Intent(getBaseContext(), ConnectAddList.class);
		// startActivity(intent);
		// } else {
		// intent = new Intent(getBaseContext(), MonitorSetActivity.class);
		// startActivity(intent);
		// }
		//
		// break;
		// case R.id.tv_bebeinfo:
		// intent = new Intent(getBaseContext(), BebeInfoActivity.class);
		// startActivity(intent);
		// break;
		}
	}
}
