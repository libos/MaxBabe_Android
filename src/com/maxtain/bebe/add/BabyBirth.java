package com.maxtain.bebe.add;

import java.util.Calendar;
import java.util.Date;

import net.kapati.widgets.DatePicker;
import net.kapati.widgets.DatePicker.OnDateSetListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxtain.bebe.MainActivity;
import com.maxtain.bebe.R;

public class BabyBirth extends Activity implements OnDateSetListener {
	private DatePicker datepicker;
	private Button confirm_btn;
	private Button cancel_btn;
	private String babyName;
	private Date birth;
	private TextView timeon;
	private RelativeLayout rlfigure;
	private ImageView figureimg;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_birth);
		this.getActionBar().hide();
		babyName = getIntent().getExtras().get("babyName").toString();
		((TextView) findViewById(R.id.birthday_title)).setText(babyName
				+ "生日是？");

		timeon = (TextView) findViewById(R.id.baby_birth_time_calc);
		rlfigure = (RelativeLayout) findViewById(R.id.static_time_calc);
		figureimg = (ImageView) findViewById(R.id.figure);

		datepicker = ((DatePicker) findViewById(R.id.birth_date_picker));
		datepicker.setDateFormat(DateFormat.getLongDateFormat(this));
		Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
		datepicker.setMinDate(year - 5, 1, 1);
		datepicker.setMaxDate(year, month, day);
		datepicker.setOnDateSetListener(this);
		confirm_btn = (Button) findViewById(R.id.baby_birth_confirm_btn);
		cancel_btn = (Button) findViewById(R.id.baby_birth_cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BabyBirth.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Toast.makeText(this, year + "/" + (month + 1) + "/" + day,
				Toast.LENGTH_LONG).show();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		birth = calendar.getTime();

		Date now = Calendar.getInstance().getTime();

		long diff = now.getTime() - birth.getTime();

		long diffDays = (diff / (60 * 60 * 1000) / 24);
		String tips = "";
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.f_normal05);
		//need to adjust for different image
		if (diffDays < 180) {
			drawable = res.getDrawable(R.drawable.f_normal05);
		} else if (diffDays >= 180 && diffDays < 360) {
			drawable = res.getDrawable(R.drawable.f_normal05);
		} else if (diffDays >= 360) {
			drawable = res.getDrawable(R.drawable.f_normal05);
		}
		if (diffDays < 31) {
			tips = diffDays + "天";
		} else {
			// Need to consider month days
			int diffMonth = (int) (diffDays / 31);
			diffDays = diffDays % 31;
			if (diffDays == 0) {
				tips = diffMonth + "个月整";
			} else {
				tips = diffMonth + "个月零" + diffDays + "天";
			}
		}
		timeon.setText(tips);
		rlfigure.setVisibility(View.VISIBLE);
		figureimg.setVisibility(View.VISIBLE);
		figureimg.setBackground(drawable);
	}
}
