package com.maxtain.bebe.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
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

public class ReviseActivity extends Activity implements OnClickListener,
		IAccountListener {

	private TextView tv_revise_title;
	private TextView tv_new_title;
	private TextView tv_password;

	private EditText tv_revise_new;
	private EditText ed_old_password;
	private Button cancel_btn;
	private Button confirm_btn;
	private LinearLayout ll_sex;
	private LinearLayout ll_revise;
	private RelativeLayout loading;
	private TextView warningTip;

	private Map<String, String> titleMap = new HashMap<String, String>();
	private String key;
	private String password;
	private Resources res;
	private String values;
	private SharedPreferences _sp;
	private RadioGroup rg_sex;
	private RadioButton rb_sex;
	private RadioButton rb_male;
	private RadioButton rb_female;

	private String sp_sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_revise);
		this.getActionBar().hide();
		res = getResources();

		tv_password = (TextView) findViewById(R.id.tv_password);
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

		ll_sex = (LinearLayout) findViewById(R.id.sex_ll);
		ll_sex.setVisibility(View.GONE);
		ll_revise = (LinearLayout) findViewById(R.id.ll_revise);
		loading = (RelativeLayout) findViewById(R.id.loadingPanel);
		warningTip = (TextView) findViewById(R.id.warning_tip);
		warningTip.setAlpha(0.0f);
		warningTip.setTranslationY(-warningTip.getHeight());

		titleMap.put("nick", res.getString(R.string.reg_nickname));
		titleMap.put("sex", res.getString(R.string.reg_sex));
		titleMap.put("new_password", res.getString(R.string.reg_password));
		titleMap.put("phone", res.getString(R.string.reg_phone));

		Intent intent = getIntent();
		key = intent.getStringExtra("Title");

		initView();
	}

	private void initView() {
		tv_revise_title = (TextView) findViewById(R.id.tv_revise_title);
		tv_revise_title.setText("修改" + titleMap.get(key));
		tv_new_title = (TextView) findViewById(R.id.tv_new_title);
		tv_new_title.setText("新" + titleMap.get(key));
		tv_revise_new = (EditText) findViewById(R.id.tv_revise_new);
		ed_old_password = (EditText) findViewById(R.id.ed_password);
		tv_new_title.setVisibility(View.VISIBLE);
		tv_revise_new.setVisibility(View.VISIBLE);

		rg_sex = (RadioGroup) findViewById(R.id.sex);

		if (key.equals("new_password")) {
			tv_new_title.setText(res.getString(R.string.old_password));
			tv_password.setText("新" + titleMap.get(key));
			tv_revise_new.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		if (key.equals("sex")) {
			sp_sex = _sp.getString(BabeConst.ACCOUNT_SEX, "女");
			rb_male = (RadioButton) findViewById(R.id.customer_add_radio_sex_male);
			rb_female = (RadioButton) findViewById(R.id.customer_add_radio_sex_female);
			if (sp_sex.equals("男")) {
				rb_male.setChecked(true);
				rb_female.setChecked(false);
			} else {
				rb_male.setChecked(false);
				rb_female.setChecked(true);
			}

			tv_new_title.setVisibility(View.GONE);
			tv_revise_new.setVisibility(View.GONE);
			ll_sex.setVisibility(View.VISIBLE);

		}
		if (key.equals("phone")) {
			tv_revise_new.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(this);
		confirm_btn = (Button) findViewById(R.id.confirm_btn);
		confirm_btn.setOnClickListener(this);
	}

	@Override
	public void callback(final ACCOUNT_STATUS as) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				switch (as) {
				case CANCEL:
					warningShow("貌似出问题了Σ(⊙▽⊙\"a");
					loading.setVisibility(View.GONE);
					ll_revise.setVisibility(View.VISIBLE);
					break;
				case NO_NETWORK:
					warningShow("请先联网后注册╥﹏╥");
					loading.setVisibility(View.GONE);
					ll_revise.setVisibility(View.VISIBLE);
					break;
				case DATA_ERR:
					warningShow("开小差了，请稍后重试(ಥ_ಥ)");
					loading.setVisibility(View.GONE);
					ll_revise.setVisibility(View.VISIBLE);
					break;
				case DUPLICATE:
					warningShow("手机号已有人在使用(｡•ˇ‸ˇ•｡)");
					loading.setVisibility(View.GONE);
					ll_revise.setVisibility(View.VISIBLE);
					break;
				case NO_USER:
					warningShow("密码错误o(╯□╰)o");
					loading.setVisibility(View.GONE);
					ll_revise.setVisibility(View.VISIBLE);
					break;
				case UPDATE_DONE:
					Toast.makeText(getBaseContext(), "修改成功", 2000).show();

					Editor ed = _sp.edit();
					String account = "_a";
					if (key.equals("nick")) {
						account = BabeConst.ACCOUNT_NICKNAME;
					} else if (key.equals("phone")) {
						account = BabeConst.ACCOUNT_PHONE;
					} else if (key.equals("new_password")) {
						account = BabeConst.ACCOUNT_PASSWORD;
					} else if (key.equals("sex")) {
						account = BabeConst.ACCOUNT_SEX;
						values = values.equals("1") ? "男" : "女";
					} else {
						return;
					}

					ed.putString(account, values);
					ed.commit();

					Intent intent = new Intent();

					ReviseActivity.this.setResult(RESULT_OK, intent);
					finish();

					break;

				default:
					break;
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName() + ":" + this.key);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName() + ":" + this.key);
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_btn:

			values = tv_revise_new.getText().toString();
			password = ed_old_password.getText().toString();
			if (key.equals("new_password")) {
				password = tv_revise_new.getText().toString();
				values = ed_old_password.getText().toString();
			}
			if (StringUtils.isEmpty(password)) {
				warningShow("先把密码填了-.-");
				return;
			}
			int old_ps_len = password.length();

			if (old_ps_len < 6 || old_ps_len > 36) {
				warningShow("密码至少6位，少于36位(づ′▽`)づ");
				return;
			}
			String email = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
			EmailValidator ev = EmailValidator.getInstance();
			if (!ev.isValid(email)) {
				warningShow(getResources().getString(R.string.sorry_no_login));
				return;
			}
			if (key.equals("sex")) {
				rb_sex = (RadioButton) findViewById(rg_sex
						.getCheckedRadioButtonId());
				values = rb_sex.getTag().toString();
			}
			if (key.equals("new_password")) {
				int ps_len = values.length();
				if (ps_len < 6 || ps_len > 36) {
					warningShow("密码至少6位，少于36位(づ′▽`)づ");
					return;
				}
				if (values.equals(password)) {
					warningShow("新旧密码一样===(￣▽￣*)b");
					return;
				}
			}
			String auth = PhpMD5
					.md5((email + ". maxtain . mybabe . update " + password)
							.getBytes());

			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			NameValuePair nvp1 = new BasicNameValuePair("email", email);
			NameValuePair nvp2 = new BasicNameValuePair("password", password);
			NameValuePair nvp3 = new BasicNameValuePair("auth", auth);
			NameValuePair nvp4 = new BasicNameValuePair("field_name", key);
			NameValuePair nvp5 = new BasicNameValuePair("field_value", values);
			paramList.add(nvp1);
			paramList.add(nvp2);
			paramList.add(nvp3);
			paramList.add(nvp4);
			paramList.add(nvp5);
			loading.setVisibility(View.VISIBLE);
			ll_revise.setVisibility(View.GONE);
			new Thread(new QueryReg(getApplicationContext(), paramList,
					QueryType.UPDATE, this)).start();

			break;
		case R.id.cancel_btn:
			finish();
			break;
		default:
			break;
		}

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
