package com.maxtain.bebe;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.maxtain.bebe.adapter.CityListAdapter;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.location.BaiLocationListener;
import com.maxtain.bebe.location.LocCallbackListener;
import com.maxtain.bebe.sqlite.structure.City;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;

public class CityActivity extends Activity implements LocCallbackListener {
	private ImageView iv_back;

	private ImageView iv_search;
	private TextView tv_autolocate;
	private ListView list_citys;
	private CityListAdapter cityListAdapter;
	private SharedPreferences _sp;
	public LocationClient mLocationClient = null;
	public BaiLocationListener baiListener = new BaiLocationListener();

	private String result = "";
	private boolean locateflag = false;
	private EditText input_city;
	private ArrayList<String> default_data;
	private RelativeLayout layout_auto;
	private LinearLayout layout_manual;
	private TextView tv_manual;
	private RelativeLayout.LayoutParams lp_manual;
	private RelativeLayout.LayoutParams lp_auto;
	private int old_height = 0;
	private CityData citydata = null;
	private String[] citys;

	private int search_state = 0;
	private InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		this.getActionBar().hide();
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		// AndroidBug5497Workaround.assistActivity(this);
		tv_autolocate = (TextView) findViewById(R.id.tv_autolocate);

		layout_auto = (RelativeLayout) findViewById(R.id.layout_auto);
		layout_manual = (LinearLayout) findViewById(R.id.layout_manual);

		tv_manual = (TextView) findViewById(R.id.tv_manual);
		iv_search = (ImageView) findViewById(R.id.iv_search);

		input_city = (EditText) findViewById(R.id.input_city);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		tv_autolocate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (locateflag && citydata != null) {
					result = citydata.district + ":" + citydata.province + ":"
							+ citydata.city;
					citydata.saveCity(citydata.latitude, citydata.longitude,
							citydata.radis, citydata.district,
							citydata.cityCode, citydata.city, citydata.province);
					close();
				}
			}
		});

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (search_state == 1) {
					imm.hideSoftInputFromWindow(input_city.getWindowToken(), 0);
					layout_auto.setVisibility(View.VISIBLE);
					tv_manual.setVisibility(View.VISIBLE);
					input_city.setVisibility(View.GONE);
					layout_manual.setLayoutParams(lp_auto);
					cityListAdapter.setData(default_data);
					search_state = 0;
					return;
				}
				// TODO Auto-generated method stub
				finish();
			}
		});
		list_citys = (ListView) findViewById(R.id.list_citys);

		if (!getResources().getConfiguration().locale.getCountry().equals("TW")) {
			citys = new String[] { "北京", "上海", "杭州", "深圳", "成都", "香港" };
		} else {
			citys = new String[] { "台北", "高雄", "台南", "台中", "基隆", "嘉义" };
		}

		default_data = new ArrayList<String>();
		for (int i = 0; i < citys.length; i++) {
			default_data.add(Babe.t2s(this, citys[i]));
		}
		cityListAdapter = new CityListAdapter(default_data, this);
		list_citys.setAdapter(cityListAdapter);
		list_citys.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				long mExitTime = _sp.getLong(BabeConst.CITY_SWTICH_INTEVAL, 0);
				// Log.e("city_time1", System.currentTimeMillis() + "");
				// Log.e("city_time2", mExitTime + "");
				// Log.e("city_time2", (System.currentTimeMillis() - mExitTime)
				// + "");
				if ((System.currentTimeMillis() - mExitTime) < 3000) {
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(
									R.string.please_not_switch_too_fast),
							Toast.LENGTH_SHORT).show();
					// finish();
				} else {
					result = (String) ((TextView) view
							.findViewById(R.id.tv_invi_data)).getText();
					_sp.edit()
							.putLong(BabeConst.CITY_SWTICH_INTEVAL,
									System.currentTimeMillis()).commit();
					// Toast.makeText(getApplicationContext(),
					// "您点击的是：" + ((TextView) view).getText(),
					// Toast.LENGTH_SHORT).show();
					close();
				}

			}

		});

		input_city.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1) {
					ArrayList<String> citiesx = (ArrayList<String>) City
							.auto_complete(getApplicationContext(),
									s.toString());
					cityListAdapter.setData(citiesx);
					// list_citys.invalidate();
				} else {

					cityListAdapter.setData(default_data);
					// list_citys.invalidate();
				}
				cityListAdapter.notifyDataSetChanged();

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable text) {

			}
		});

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		baiListener.setCallbackListener(this);
		mLocationClient.registerLocationListener(baiListener); // 注册监听函数
		initLocation();
		lp_manual = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		lp_manual.addRule(RelativeLayout.BELOW, R.id.iv_back);
		lp_manual.setMargins(0, 50, 0, 0);
		lp_auto = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		lp_auto.addRule(RelativeLayout.BELOW, R.id.layout_auto);
		iv_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (search_state == 0) {
					layout_manual.setLayoutParams(lp_manual);
					input_city.setVisibility(View.VISIBLE);
					input_city.requestFocus();

					imm.showSoftInput(input_city,
							InputMethodManager.SHOW_IMPLICIT);
					layout_auto.setVisibility(View.GONE);
					tv_manual.setVisibility(View.GONE);
					search_state = 1;
				} else {
					imm.hideSoftInputFromWindow(input_city.getWindowToken(), 0);
					layout_auto.setVisibility(View.VISIBLE);
					tv_manual.setVisibility(View.VISIBLE);
					input_city.setVisibility(View.GONE);
					layout_manual.setLayoutParams(lp_auto);
					cityListAdapter.setData(default_data);
					search_state = 0;
				}
			}
		});
		//
		// final View activityRootView = findViewById(R.id.city_root_layout);
		// activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
		// new OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// int heightDiff = activityRootView.getRootView()
		// .getHeight() - activityRootView.getHeight();
		// if (old_height != heightDiff) {
		// if (heightDiff > 100) {
		// // layout_manual.setLayoutParams(lp_manual);
		// // layout_auto.setVisibility(View.GONE);
		// // tv_manual.setVisibility(View.GONE);
		// } else {
		// layout_auto.setVisibility(View.VISIBLE);
		// tv_manual.setVisibility(View.VISIBLE);
		// layout_manual.setLayoutParams(lp_auto);
		// input_city.setVisibility(View.GONE);
		// cityListAdapter.setData(default_data);
		// }
		// old_height = heightDiff;
		// }
		// }
		// });
	}

	private void close() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("result", result);
		intent.putExtras(bundle);
		this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		mLocationClient.unRegisterLocationListener(baiListener);
		super.onStop();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息

		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else {
			;
			// Log.d("LocSDK5", "locClient is null or not started");
		}
	}

	@Override
	public void locationCallback(double latitude, double longitude,
			double radius, String district, String citycode, String city,
			String province) {
		tv_autolocate.setText(Babe.t2s(getApplicationContext(), district));
		citydata = new CityData(getApplicationContext());
		citydata.city = city;
		citydata.district = district;
		citydata.cityCode = citycode;
		citydata.province = province;
		citydata.latitude = latitude;
		citydata.longitude = longitude;
		citydata.radis = (float) radius;

		// citydata.saveCity(, longitude, radius, district, citycode,
		// city, province);
		locateflag = true;
	}

}
