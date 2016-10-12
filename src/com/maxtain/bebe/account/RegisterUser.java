package com.maxtain.bebe.account;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxtain.bebe.R;
import com.maxtain.bebe.account.QueryReg.QueryType;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;
import com.umeng.analytics.MobclickAgent;

public class RegisterUser extends Activity implements OnClickListener,
		IAccountListener {
	private SharedPreferences _sp;

	private Button confirm_btn;
	private Button cancel_btn;
	private TextView warningTip;
	private RelativeLayout loading;
	private LinearLayout ll_step_1;
	private LinearLayout ll_step_2;

	// step 1
	private EditText et_username;
	private EditText et_password;

	// step 2
	private EditText et_phone;
	private EditText et_nickname;
	private RadioGroup rg_sex;
	private RadioButton rb_sex;

	private int state_flag;

	private String email;
	private String password;
	private String phone;
	private String nickname;
	private String sex;

	//
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		this.getActionBar().hide();
		// SMSSDK.initSDK(this, "78621ebb882c",
		// "7c1b832c395a9bf0744571470aaf0a3e");
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

		String _username = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
		String _nickname = _sp.getString(BabeConst.ACCOUNT_NICKNAME, "");
		String _phone = _sp.getString(BabeConst.ACCOUNT_PHONE, "");
		if (StringUtils.isNotEmpty(_username)
				&& StringUtils.isNotEmpty(_nickname)
				&& StringUtils.isNotEmpty(_phone)) {
			Intent intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		state_flag = 0;
		confirm_btn = (Button) findViewById(R.id.confirm_btn);
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		loading = (RelativeLayout) findViewById(R.id.loadingPanel);
		warningTip = (TextView) findViewById(R.id.warning_tip);
		warningTip.setAlpha(0.0f);
		warningTip.setTranslationY(-warningTip.getHeight());

		ll_step_1 = (LinearLayout) findViewById(R.id.ll_step_1);
		ll_step_2 = (LinearLayout) findViewById(R.id.ll_step_2);

		// step 1
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);

		// step 2
		et_phone = (EditText) findViewById(R.id.phone);
		et_nickname = (EditText) findViewById(R.id.nickname);
		rg_sex = (RadioGroup) findViewById(R.id.sex);

		confirm_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_btn:
			if (state_flag == 0) {
				this.email = et_username.getText().toString();
				this.password = et_password.getText().toString();
				if (StringUtils.isEmpty(this.email)
						|| StringUtils.isEmpty(this.password)) {
					warningShow("请先填写<(￣︶￣)>");
					return;
				}
				EmailValidator ev = EmailValidator.getInstance();
				if (!ev.isValid(email)) {
					warningShow("Email格式错误<(￣︶￣)>");
					return;
				}
				int ps_len = this.password.length();
				if (ps_len < 6 || ps_len > 36) {
					warningShow("密码至少6位，少于36位(づ′▽`)づ");
					return;
				}
				// validate
				// visibility go and progress show
				loading.setVisibility(View.VISIBLE);
				ll_step_1.setVisibility(View.INVISIBLE);

				String auth = PhpMD5
						.md5((email + ". maxtain . mybabe . unique").getBytes());
				ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
				NameValuePair nvp1 = new BasicNameValuePair("email", email);
				NameValuePair nvp2 = new BasicNameValuePair("auth", auth);
				paramList.add(nvp1);
				paramList.add(nvp2);
				new Thread(new QueryReg(getApplicationContext(), paramList,
						QueryType.UNIQUE, this)).start();
				// ask for unique
				// handler disable prograss then next or show window
				// state_flag = 0 or state flag = 1
			} else if (state_flag == 1) {
				// validate
				this.phone = et_phone.getText().toString();
				this.nickname = et_nickname.getText().toString();
				rb_sex = (RadioButton) findViewById(rg_sex
						.getCheckedRadioButtonId());
				this.sex = rb_sex.getTag().toString();
				if (!PhoneNumberUtils.isGlobalPhoneNumber(this.phone)) {
					warningShow("您的手机号不正确(◡‿◡)");
					return;
				}
				int nick_len = this.nickname.length();
				if (nick_len < 2 || nick_len > 24) {
					warningShow("昵称长度需要大于1位，小于24位(◡‿◡)");
					return;
				}
				String auth = PhpMD5
						.md5((this.email + ". maxtain . mybabe " + this.password)
								.getBytes());
				ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
				NameValuePair nvp1 = new BasicNameValuePair("email", this.email);
				NameValuePair nvp2 = new BasicNameValuePair("phone", this.phone);
				NameValuePair nvp3 = new BasicNameValuePair("password",
						this.password);
				NameValuePair nvp4 = new BasicNameValuePair("nick",
						this.nickname);
				NameValuePair nvp5 = new BasicNameValuePair("sex", this.sex);
				NameValuePair nvp6 = new BasicNameValuePair("auth", auth);
				paramList.add(nvp1);
				paramList.add(nvp2);
				paramList.add(nvp3);
				paramList.add(nvp4);
				paramList.add(nvp5);
				paramList.add(nvp6);
				loading.setVisibility(View.GONE);
				ll_step_2.setVisibility(View.INVISIBLE);
				new Thread(new QueryReg(getApplicationContext(), paramList,
						QueryType.REGISTER, this)).start();
				// ask_for register
				// register done
				// register error
			}
			break;
		case R.id.cancel_btn:
			if (state_flag == 0) {
				finish();
			} else if (state_flag == 1) {
				state_flag = 0;
				ll_step_2.setVisibility(View.GONE);
				ll_step_1.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	public void callback(final ACCOUNT_STATUS as) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				switch (as) {
				case CANCEL:
					if (state_flag == 0) {
						warningShow("貌似出问题了Σ(⊙▽⊙\"a");
						loading.setVisibility(View.GONE);
						ll_step_1.setVisibility(View.VISIBLE);
					} else if (state_flag == 1) {
						warningShow("貌似出问题了Σ(⊙▽⊙\"a");
						loading.setVisibility(View.GONE);
						ll_step_2.setVisibility(View.VISIBLE);
					}
					break;
				case NO_NETWORK:
					if (state_flag == 0) {
						warningShow("请先联网后注册╥﹏╥");
						loading.setVisibility(View.GONE);
						ll_step_1.setVisibility(View.VISIBLE);
					} else if (state_flag == 1) {
						warningShow("请先联网后注册╥﹏╥");
						loading.setVisibility(View.GONE);
						ll_step_2.setVisibility(View.VISIBLE);
					}

					break;
				case DATA_ERR:
					if (state_flag == 0) {
						warningShow("开小差了，请稍后重试(ಥ_ಥ)");
						loading.setVisibility(View.GONE);
						ll_step_1.setVisibility(View.VISIBLE);
					} else if (state_flag == 1) {
						warningShow("开小差了，请稍后重试(ಥ_ಥ)");
						loading.setVisibility(View.GONE);
						ll_step_2.setVisibility(View.VISIBLE);
					}
					break;
				case DATA_OK:
					if (state_flag == 0) {
						state_flag = 1;
						loading.setVisibility(View.GONE);
						ll_step_1.setVisibility(View.GONE);
						ll_step_2.setVisibility(View.VISIBLE);
					} else if (state_flag == 1) {
						Toast.makeText(getBaseContext(), "注册成功", 2000).show();
						Intent intent = new Intent(getBaseContext(),
								AccountActivity.class);
						Editor ed = _sp.edit();
						ed.putString(BabeConst.ACCOUNT_EMAIL, email);
						ed.putString(BabeConst.ACCOUNT_PASSWORD, password);
						ed.putString(BabeConst.ACCOUNT_PHONE, phone);
						ed.putString(BabeConst.ACCOUNT_NICKNAME, nickname);
						ed.putString(BabeConst.ACCOUNT_SEX,
								sex.equals("1") ? "男" : "女");
						ed.commit();
						startActivity(intent);
						finish();
					}
					break;
				case DUPLICATE:
					if (state_flag == 0) {
						warningShow("该用户名已经存在(T_T)，请重新输入");
						loading.setVisibility(View.GONE);
						ll_step_1.setVisibility(View.VISIBLE);
					} else if (state_flag == 1) {

						warningShow("该手机号已经存在(T_T)，请重新输入");
						loading.setVisibility(View.GONE);
						ll_step_2.setVisibility(View.VISIBLE);
					}
					break;
				default:
					break;
				}
			}
		});

	};

	private void warningShow(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				warningTip.setText(Babe.t2s(getApplicationContext(), text));
				warningTip.animate().translationY(0).alpha(1.0f);
				warningTip.postDelayed(new Runnable() {

					@Override
					public void run() {
						warningHide();

					}
				}, 2000);

			}
		});

	}

	private void warningHide() {
		warningTip.animate().translationY(-warningTip.getHeight()).alpha(0.0f);
	}
}
