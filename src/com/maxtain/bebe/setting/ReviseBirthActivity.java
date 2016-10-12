package com.maxtain.bebe.setting;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maxtain.bebe.R;
import com.maxtain.bebe.add.BirthFragment;
import com.maxtain.bebe.add.BirthFragment.MyInterface;

public class ReviseBirthActivity extends Activity implements MyInterface{

	private TextView tv_revise_title;
	private TextView tv_new_title;
	private Button cancel_btn;
	private Button confirm_btn;

	private Map<String, String> titleMap = new HashMap<String, String>();
	private String key;
	private String birth;
	private String result = "";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		key = intent.getStringExtra("Title");
		birth = intent.getStringExtra("Birth");
		
		
		
		setContentView(R.layout.revise_birth);
		this.getActionBar().hide();

		FragmentManager fragmentManager = getFragmentManager();
		BirthFragment fragment = (BirthFragment) fragmentManager.findFragmentById(R.id.birth_revise_new);
		fragment.setInitbirth(birth);
		
		titleMap.put("birth", "生日");
		
		initView();
	}

	private void initView() {
		tv_revise_title = (TextView) findViewById(R.id.tv_revise_title);
		tv_revise_title.setText("修改"+titleMap.get(key));
		tv_new_title = (TextView) findViewById(R.id.tv_new_title);
		tv_new_title.setText("新"+titleMap.get(key));

		
		
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		confirm_btn = (Button) findViewById(R.id.confirm_btn);
		confirm_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				
				bundle.putString("result", result);
				intent.putExtras(bundle);
				ReviseBirthActivity.this.setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public void sendMsg(String result) {
		// TODO Auto-generated method stub
		this.result = result;
	}

}
