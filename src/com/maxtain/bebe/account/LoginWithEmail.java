package com.maxtain.bebe.account;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxtain.bebe.R;
import com.maxtain.bebe.account.QueryReg.QueryType;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;
import com.umeng.analytics.MobclickAgent;

public class LoginWithEmail extends Activity implements OnClickListener,
		IAccountListener {
	private SharedPreferences _sp;
	private EditText et_username;
	private EditText et_password;
	private RelativeLayout loading;
	private LinearLayout ll_login;
	private TextView warningTip;
	private Button loginbtn;
	private Button cancelbtn;

	private TextView forget;
	private String username;
	private String password;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_email);
		this.getActionBar().hide();

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
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);
		loading = (RelativeLayout) findViewById(R.id.loadingPanel);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);

		forget = (TextView) findViewById(R.id.forget_pwd);
		forget.setOnClickListener(this);
		warningTip = (TextView) findViewById(R.id.warning_tip);
		warningTip.setTranslationY(-warningTip.getHeight());
		warningTip.setAlpha(0.0f);
		loginbtn = (Button) findViewById(R.id.login_btn);
		loginbtn.setOnClickListener(this);
		cancelbtn = (Button) findViewById(R.id.cancel_btn);
		cancelbtn.setOnClickListener(this);
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
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.forget_pwd:
			Intent intent = new Intent(this, ForgetPasswd.class);
			startActivity(intent);
			break;
		case R.id.login_btn:
			this.password = et_password.getText().toString();
			this.username = et_username.getText().toString();
			if (StringUtils.isEmpty(this.password)
					|| StringUtils.isEmpty(this.username)) {
				warningShow("请先填写-_-!");
				return;
			}
			EmailValidator ev = EmailValidator.getInstance();
			if (!ev.isValid(this.username)
					&& !PhoneNumberUtils.isGlobalPhoneNumber(this.username)) {
				warningShow("既不是邮箱也不是手机-_-!");
				return;
			}
			String auth = PhpMD5
					.md5((this.username + ". maxtain . mybabe . login " + this.password)
							.getBytes());
			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			NameValuePair nvp1 = new BasicNameValuePair("email", this.username);
			NameValuePair nvp2 = new BasicNameValuePair("password",
					this.password);
			NameValuePair nvp3 = new BasicNameValuePair("auth", auth);

			paramList.add(nvp1);
			paramList.add(nvp2);
			paramList.add(nvp3);
			ll_login.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			new Thread(new QueryReg(getApplicationContext(), paramList,
					QueryType.LOGIN, this)).start();
			break;
		case R.id.cancel_btn:
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void callback(final ACCOUNT_STATUS as) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				switch (as) {
				case CANCEL:
					warningShow("貌似出问题了Σ(⊙▽⊙\"a等下哈");
					loading.setVisibility(View.GONE);
					ll_login.setVisibility(View.VISIBLE);

					break;
				case NO_NETWORK:

					warningShow("请先联网后注册╥﹏╥");
					loading.setVisibility(View.GONE);
					ll_login.setVisibility(View.VISIBLE);

					break;
				case DATA_ERR:
					warningShow("开小差了，请稍后重试(ಥ_ಥ)");
					loading.setVisibility(View.GONE);
					ll_login.setVisibility(View.VISIBLE);
					break;
				case NO_USER:
					warningShow("用户名或密码错误o(╯□╰)o");
					loading.setVisibility(View.GONE);
					ll_login.setVisibility(View.VISIBLE);

					break;
				case LOGON:
					Toast.makeText(getBaseContext(),
							getResources().getString(R.string.login_suc), 2000)
							.show();
					Intent intent = new Intent(getBaseContext(),
							AccountActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
					break;
				case DUPLICATE:

					break;
				default:
					break;
				}
			}
		});

	}

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
