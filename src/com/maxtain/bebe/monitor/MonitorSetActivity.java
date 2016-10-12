package com.maxtain.bebe.monitor;

import com.maxtain.bebe.R;
import com.maxtain.bebe.R.id;
import com.maxtain.bebe.R.layout;
import com.maxtain.bebe.account.ReviseActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("NewApi")
public class MonitorSetActivity extends Activity implements OnClickListener{

	private ImageView iv_back;
	private TextView tv_wearset;
	private TextView tv_devicemanage;
	private TextView tv_bebeintroduce;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitorset);
        this.getActionBar().hide();
        initView();
    }
    private void initView(){
    	iv_back = (ImageView)findViewById(R.id.iv_back);
    	iv_back.setOnClickListener(this);
    	tv_wearset = (TextView)findViewById(R.id.tv_wearset);
    	tv_wearset.setOnClickListener(this);
    	tv_devicemanage = (TextView)findViewById(R.id.tv_devicemanage);
    	tv_devicemanage.setOnClickListener(this);
    	tv_bebeintroduce = (TextView)findViewById(R.id.tv_bebeintroduce);
    	tv_bebeintroduce.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_wearset:
			Intent intent = new Intent(getBaseContext(),WearSetActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_devicemanage:
			break;
		case R.id.tv_bebeintroduce:
			break;
		}
	}
	
	
}
