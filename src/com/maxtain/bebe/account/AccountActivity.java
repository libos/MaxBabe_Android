package com.maxtain.bebe.account;

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

import com.maxtain.bebe.R;
import com.maxtain.bebe.util.BabeConst;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class AccountActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_username;
	private TextView tv_revise_password;
	private TextView tv_revise_phone;
	private TextView tv_sex;
	private TextView tv_email;
	private RelativeLayout rl_username;
	private RelativeLayout rl_revise_password;
	private RelativeLayout rl_revise_phone;
	private RelativeLayout rl_sex;

	private TextView tv_logout;
	private static final int NICKNAME_CODE = 1;
	private static final int SEX_CODE = 2;
	private static final int PASSWORD_CODE = 3;
	private static final int PHONE_CODE = 4;
	private SharedPreferences _sp;
	private String username;
	private String nickname;
	private String password;
	private String phone;
	private String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountinfo);
		this.getActionBar().hide();
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

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
		username = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
		nickname = _sp.getString(BabeConst.ACCOUNT_NICKNAME, "");
		phone = _sp.getString(BabeConst.ACCOUNT_PHONE, "");
		sex = _sp.getString(BabeConst.ACCOUNT_SEX, "男");
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(nickname)
				|| StringUtils.isEmpty(phone)) {
			Intent intent = new Intent(this, RegisterUser.class);
			startActivity(intent);
			finish();
			return;
		}
		tv_sex.setText(sex);
		tv_username.setText(nickname);
		tv_email.setText(username);
		tv_revise_phone.setText(phone.substring(0, 3) + "****"
				+ phone.substring(7));

		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_revise_password = (TextView) findViewById(R.id.tv_revise_password);
		tv_revise_phone = (TextView) findViewById(R.id.tv_revise_phone);
		tv_logout = (TextView) findViewById(R.id.tv_logout);

		rl_username = (RelativeLayout) findViewById(R.id.rl_username);
		rl_revise_password = (RelativeLayout) findViewById(R.id.rl_revise_password);
		rl_revise_phone = (RelativeLayout) findViewById(R.id.rl_revise_phone);
		rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);

		rl_username.setOnClickListener(this);
		rl_revise_password.setOnClickListener(this);
		rl_revise_phone.setOnClickListener(this);
		rl_sex.setOnClickListener(this);

		tv_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(), ReviseActivity.class);
		switch (v.getId()) {
		case R.id.iv_back:

			finish();
			break;
		case R.id.rl_username:
			intent.putExtra("Title", "nick");
			startActivityForResult(intent, NICKNAME_CODE);
			break;
		case R.id.rl_sex:
			intent.putExtra("Title", "sex");
			startActivityForResult(intent, SEX_CODE);
			break;
		case R.id.rl_revise_password:
			intent.putExtra("Title", "new_password");
			startActivityForResult(intent, PASSWORD_CODE);
			break;
		case R.id.rl_revise_phone:
			intent.putExtra("Title", "phone");
			startActivityForResult(intent, PHONE_CODE);
			break;
		case R.id.tv_logout:
			_sp.edit().remove(BabeConst.ACCOUNT_EMAIL)
					.remove(BabeConst.ACCOUNT_NICKNAME)
					.remove(BabeConst.ACCOUNT_SEX)
					.remove(BabeConst.ACCOUNT_PASSWORD)
					.remove(BabeConst.ACCOUNT_PHONE).commit();
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_CANCELED) {
			return;
		}

		switch (requestCode) {
		case RESULT_OK:

			break;
		case NICKNAME_CODE:

			break;
		case SEX_CODE:
			break;
		case PASSWORD_CODE:
			Bundle b = data.getExtras();
			break;
		case PHONE_CODE:

			break;
		default:
			break;

		}
	}

}
