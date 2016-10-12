package com.maxtain.bebe.add;

import java.util.Calendar;
import java.util.Date;

import net.kapati.widgets.DatePicker;
import net.kapati.widgets.DatePicker.OnDateSetListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ParcelFormatException;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxtain.bebe.R;

public class BirthFragment extends Fragment implements OnDateSetListener{

	private DatePicker datepicker;
	private Date birth;
	private String initbirth;
	
	public String getInitbirth() {
		return initbirth;
	}

	public void setInitbirth(String initbirth) {
		this.initbirth = initbirth;
		int year = 0 ;
		int month = 0 ;
		int day = 0 ;
		try{
			year = Integer.parseInt(initbirth.split("年")[0]);
			month = Integer.parseInt((initbirth.split("年")[1]).split("月")[0]);
			day = Integer.parseInt((initbirth.split("月")[1]).split("日")[0]);
		// }catch(ParcelFormatException e){
			// Log.i("TAG", "ParcelFormatException", e);
		}catch(Exception e){
			// Log.i("TAG", "Exception", e);
		}
		
		
		
		datepicker.setMinDate(year - 5, 1, 1);
		datepicker.setMaxDate(year, month, day);
		datepicker.setDate(year, month-1, day);
//		datepicker.setOnDateSetListener(this);
		
	}

	private MyInterface myInterface;

	public interface MyInterface {

               public void sendMsg(String string);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		myInterface=(MyInterface) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_birth, container, false);

		datepicker = ((DatePicker) view.findViewById(R.id.birth_date_picker));
		datepicker.setDateFormat(DateFormat.getLongDateFormat(getActivity()));
		
//		Toast.makeText(getActivity(), initbirth+"",
//				Toast.LENGTH_LONG).show();
//		initbirth.split("年");
		Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
//		try{
//			year = Integer.parseInt(initbirth.split("年")[0]);
//			month = Integer.parseInt(initbirth.split("年")[1]);
//			day = Integer.parseInt(initbirth.split("年")[2]);
//		}catch(ParcelFormatException e){
//			Log.i("TAG", "ParcelFormatException", e);
//		}catch(Exception e){
//			Log.i("TAG", "Exception", e);
//			
//		}
//		datepicker.setMinDate(year - 5, 1, 1);
//		datepicker.setMaxDate(year, month, day);
		datepicker.setOnDateSetListener(this);
		
		return view;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// TODO Auto-generated method stub
	
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		birth = calendar.getTime();

		myInterface.sendMsg(year + "年" + (month + 1) + "月" + day +"日");
		
		Toast.makeText(getActivity(), birth.toString(),
				Toast.LENGTH_LONG).show();
		
//		Date now = Calendar.getInstance().getTime();
//
//		long diff = now.getTime() - birth.getTime();
//
//		long diffDays = (diff / (60 * 60 * 1000) / 24);
//		String tips = "";
//		Resources res = getResources();
//		Drawable drawable = res.getDrawable(R.drawable.figure0);
//		if (diffDays < 180) {
//			drawable = res.getDrawable(R.drawable.figure0);
//		} else if (diffDays >= 180 && diffDays < 360) {
//			drawable = res.getDrawable(R.drawable.figure1);
//		} else if (diffDays >= 360) {
//			drawable = res.getDrawable(R.drawable.figure2);
//		}
//		if (diffDays < 31) {
//			tips = diffDays + "天";
//		} else {
//			// Need to consider month days
//			int diffMonth = (int) (diffDays / 31);
//			diffDays = diffDays % 31;
//			if (diffDays == 0) {
//				tips = diffMonth + "个月整";
//			} else {
//				tips = diffMonth + "个月零" + diffDays + "天";
//			}
//		}
//		timeon.setText(tips);
//		rlfigure.setVisibility(View.VISIBLE);
//		figureimg.setVisibility(View.VISIBLE);
//		figureimg.setBackground(drawable);
	}
}
