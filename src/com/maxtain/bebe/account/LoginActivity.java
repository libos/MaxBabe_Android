package com.maxtain.bebe.account;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maxtain.bebe.R;
import com.maxtain.bebe.util.BabeConst;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private RelativeLayout rl_reg_button;
	private Button b_reg_button;
	private Button b_login_button;
	private RelativeLayout rl_login_now;

	private SharedPreferences _sp;
	private String username;
	private String nickname;
	private String password;
	private String phone;
	private String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.getActionBar().hide();
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		username = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
		nickname = _sp.getString(BabeConst.ACCOUNT_NICKNAME, "");
		phone = _sp.getString(BabeConst.ACCOUNT_PHONE, "");

		if (StringUtils.isNotEmpty(username)
				&& StringUtils.isNotEmpty(nickname)
				&& StringUtils.isNotEmpty(phone)) {
			Intent intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		initView();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
		username = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
		nickname = _sp.getString(BabeConst.ACCOUNT_NICKNAME, "");
		phone = _sp.getString(BabeConst.ACCOUNT_PHONE, "");

		if (StringUtils.isNotEmpty(username)
				&& StringUtils.isNotEmpty(nickname)
				&& StringUtils.isNotEmpty(phone)) {
			finish();
			return;
		}
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		rl_reg_button = (RelativeLayout) findViewById(R.id.rl_reg_button);
		rl_reg_button.setOnClickListener(this);
		b_reg_button = (Button) findViewById(R.id.button_register);
		b_reg_button.setOnClickListener(this);

		b_login_button = (Button) findViewById(R.id.tv_logway2);
		b_login_button.setOnClickListener(this);
		rl_login_now = (RelativeLayout) findViewById(R.id.rl_login_button);
		rl_login_now.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Intent intent = new Intent(getBaseContext(),ReviseActivity.class);
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.button_register:
		case R.id.rl_reg_button:
			Intent reg_activity = new Intent(getBaseContext(),
					RegisterUser.class);
			startActivity(reg_activity);
			break;
		case R.id.tv_logway2:
		case R.id.rl_login_button:
			Intent login_activity = new Intent(getBaseContext(),
					LoginWithEmail.class);
			startActivity(login_activity);
			break;
		default:
			break;
		}
	}

}
