package com.maxtain.bebe;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.location.BaiLocationListener;
import com.maxtain.bebe.location.LocCallbackListener;
import com.maxtain.bebe.setting.SettingActivity;
import com.maxtain.bebe.share.ShareActivity;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class MainPageTitle extends Fragment implements LocCallbackListener {

	private ImageView iv_share;
	private TextView tv_city;
	private TextView tv_refresh;
	private ImageView iv_setting;
	private LinearLayout iv_hotpot;

	private boolean use_gps_flag = false;
	private static final int REQUESTCODE_CITY = 1000;
	private SharedPreferences _sp;

	public LocationClient mLocationClient = null;
	public BaiLocationListener baiListener = new BaiLocationListener();
	private Context ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ctx = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.fragment_main_page_title,
				container, false);
		iv_share = (ImageView) view.findViewById(R.id.iv_share);
		tv_city = (TextView) view.findViewById(R.id.tv_city);
		iv_hotpot = (LinearLayout) view.findViewById(R.id.iv_hotpot);

		iv_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// ((MainActivity) getActivity()).callService();
				Intent intent = new Intent(getActivity(), ShareActivity.class);
				startActivity(intent);
				// ShareDialog shareDialog = new ShareDialog(getActivity(),
				// R.style.Share);
				// shareDialog.show();
			}
		});
		iv_hotpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CityActivity.class);
				startActivityForResult(intent, REQUESTCODE_CITY);
			}
		});
		_sp = ctx.getSharedPreferences(BabeConst.SHAREP_DATABASE,
				Activity.MODE_PRIVATE);

		use_gps_flag = _sp.getBoolean(BabeConst.SETTING_USE_GPS, false);
		CityData city_store = new CityData(getActivity()
				.getApplicationContext());
		city_store.loadCity();
		if (StringUtils.isNotBlank(city_store.district)) {
			tv_city.setText(Babe.t2s(ctx, city_store.district));
		} else if (StringUtils.isNotEmpty(city_store.city)) {
			tv_city.setText(Babe.t2s(ctx, city_store.city));
		} else {
			tv_city.setText(getResources().getString(R.string.locating));
		}
		tv_refresh = (TextView) view.findViewById(R.id.tv_refresh);
		iv_setting = (ImageView) view.findViewById(R.id.iv_setting);

		iv_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				startActivityForResult(intent, 200);
			}
		});
		if (!_sp.getBoolean(BabeConst.IS_FIRST_LOCATE_PROCESS, false)) {
			mLocationClient = new LocationClient(getActivity()
					.getApplicationContext());
			baiListener.setCallbackListener(this);
			mLocationClient.registerLocationListener(baiListener);
			initLocation();
		}

		return view;
	}

	@Override
	public void onStop() {
		if (!_sp.getBoolean(BabeConst.IS_FIRST_LOCATE_PROCESS, false)) {
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(baiListener);
		}
		super.onStop();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);
		if (use_gps_flag) {
			option.setLocationMode(LocationMode.Hight_Accuracy);
		}
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.setIsNeedAddress(true);

		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else {
			// Log.d("LocSDK5", "locClient is null or not started");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}

		// if (resultCode == Activity.RESULT_OK) {
		Bundle b = data.getExtras();
		switch (requestCode) {
		case REQUESTCODE_CITY:
			String ret_city = b.getString("result");
			CityData city = new CityData(getActivity().getApplicationContext());
			if (ret_city.indexOf(":") != -1) {
				String[] citi = ret_city.split(":");

				city.saveCity(0, 0, 0, citi[0], "0", citi[2], citi[1]);
				tv_city.setText(Babe.t2s(ctx, citi[0]));
			} else {

				city.saveCity(0, 0, 0, ret_city, "0", ret_city, ret_city);
				tv_city.setText(Babe.t2s(ctx, ret_city));
			}
			//
			// _sp.edit()
			// .putString(BabeConst.LOCATION_CITY, b.getString("result"))
			// .commit();
			((MainActivity) getActivity()).refreshForce();
			((MainActivity) getActivity()).getNewWeather();
			break;
		default:
			break;
		}
		// }
	}

	public void changePublishTime() {
		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);
		if (minutes >= 30) {
			minutes = minutes - 30;
		}
		String tip = "";
		Resources res = getResources();
		if (minutes <= 3) {
			tip = res.getString(R.string.just_now);
		} else {
			tip = minutes + res.getString(R.string.minutes_ago);
		}
		tv_refresh.setText(tip);
	}

	@Override
	public void locationCallback(double latitude, double longitude,
			double radius, String district, String citycode, String city,
			String province) {
		if (!_sp.getBoolean(BabeConst.IS_FIRST_LOCATE_PROCESS, false)) {
			CityData cityx = new CityData(getActivity().getApplicationContext());
			cityx.loadCity();
			tv_city.setText(Babe.t2s(ctx, district));
			CityData citydata = new CityData(getActivity()
					.getApplicationContext());
			citydata.saveCity(latitude, longitude, radius, district, citycode,
					city, province);
			_sp.edit().putBoolean(BabeConst.IS_FIRST_LOCATE_PROCESS, true)
					.commit();
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName()); // 统计时长
		if (!_sp.getBoolean(BabeConst.HAS_GET_INTO_SHARE, false)) {
			iv_share.setImageResource(R.drawable.icon_camera_reddot);
		} else {
			iv_share.setImageResource(R.drawable.icon_camera);
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}
}
