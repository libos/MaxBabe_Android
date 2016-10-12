package com.maxtain.bebe.setting;

import com.maxtain.bebe.R;
import com.maxtain.bebe.R.id;
import com.maxtain.bebe.R.layout;
import com.maxtain.bebe.account.ReviseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("NewApi")
public class BebeInfoActivity extends Activity implements OnClickListener{

	private ImageView iv_back;
	private TextView bebe_name;
	private TextView revise_name;
	private TextView bebe_gender;
	private TextView revise_gender;
	private TextView bebe_birth;
	private TextView revise_birth;
	private RelativeLayout rl_bebe_name;
	private RelativeLayout rl_bebe_gender;
	private RelativeLayout rl_bebe_birth;
	private static final int NAME_CODE = 0;
	private static final int GENDER_CODE = 1;
	private static final int BIRTH_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebeinfo);
        this.getActionBar().hide();
        initView();
    }
    
    private void initView(){
    	rl_bebe_name = (RelativeLayout) findViewById(R.id.rl_bebe_name);
    	rl_bebe_name.setOnClickListener(this);
    	iv_back = (ImageView) findViewById(R.id.iv_back);
    	iv_back.setOnClickListener(this);
    	bebe_name = (TextView) findViewById(R.id.bebe_name);
    	revise_name = (TextView) findViewById(R.id.revise_name);
    	revise_name.setOnClickListener(this);
    	
    	rl_bebe_gender = (RelativeLayout) findViewById(R.id.rl_bebe_gender);
    	rl_bebe_gender.setOnClickListener(this);
    	bebe_gender = (TextView) findViewById(R.id.bebe_gender);
    	revise_gender = (TextView) findViewById(R.id.revise_gender);
    	revise_gender.setOnClickListener(this);
    	
    	rl_bebe_birth = (RelativeLayout) findViewById(R.id.rl_bebe_birth);
    	rl_bebe_birth.setOnClickListener(this);
    	bebe_birth = (TextView) findViewById(R.id.bebe_birth);
    	revise_birth = (TextView) findViewById(R.id.revise_birth);
    	revise_birth.setOnClickListener(this);
    	
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(),ReviseActivity.class);
		switch(v.getId()){
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_bebe_name:
			
		case R.id.revise_name:
			intent.putExtra("Title", "name");
			startActivityForResult(intent, NAME_CODE);
			break;
		case R.id.rl_bebe_gender:
			
		case R.id.revise_gender:
			intent = new Intent(getBaseContext(),ReviseGenderActivity.class);
			intent.putExtra("Title", "gender");
			startActivityForResult(intent, GENDER_CODE);
			break;
		case R.id.rl_bebe_birth:
			
		case R.id.revise_birth:
			intent = new Intent(getBaseContext(),ReviseBirthActivity.class);
			intent.putExtra("Birth", bebe_birth.getText());
			intent.putExtra("Title", "birth");
			startActivityForResult(intent, BIRTH_CODE);
			break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_CANCELED){
			return;
		}
		
		Bundle b=data.getExtras();
		switch(requestCode){
		case NAME_CODE:
			bebe_name.setText(b.getString("result"));
			break;
		case GENDER_CODE:
			bebe_gender.setText(b.getString("result"));
			break;
		case BIRTH_CODE:
			bebe_birth.setText(b.getString("result"));
			break;
			
		}
		
	}
    
	
    
}
