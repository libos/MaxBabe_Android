package com.maxtain.bebe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.maxtain.bebe.util.BabeConst;
import com.umeng.fb.FeedbackAgent;

//import com.umeng.fb.FeedbackAgent;

public class AboutUSActivity extends Activity implements OnClickListener {
//	private SharedPreferences _sp;
	private ImageView iv_back;
	private Button feedback;
	private FeedbackAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		this.getActionBar().hide();
		// _sp = getApplicationContext().getSharedPreferences(
		// BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		feedback = (Button) findViewById(R.id.button_feedback);
		feedback.setOnClickListener(this);
		agent = new FeedbackAgent(this);
		agent.sync();
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_feedback:
			agent.startFeedbackActivity();
			break;

		default:
			break;
		}
	}
}
