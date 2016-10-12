package com.maxtain.bebe.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.maxtain.bebe.util.BabeConst;

public class CityData {
	private SharedPreferences _sp;

	public String district;
	public String city;
	public String cityCode;
	public String province;
	public double latitude;
	public double longitude;
	public float radis;

	public CityData(Context context) {
		_sp = context.getSharedPreferences(BabeConst.SHAREP_DATABASE,
				Activity.MODE_PRIVATE);
	}

	public void loadCity() {
		this.city = _sp.getString(BabeConst.LOCATION_CITY, "");
		this.cityCode = _sp.getString(BabeConst.LOCATION_CITYCODE, "");
		this.province = _sp.getString(BabeConst.LOCATION_PROVINCE, "");
		this.district = _sp.getString(BabeConst.LOCATION_DISTRICT, "");
		this.latitude = Double.parseDouble(_sp.getString(
				BabeConst.LOCATION_LATITUDE, "0.0"));
		this.longitude = Double.parseDouble(_sp.getString(
				BabeConst.LOCATION_LONGITUDE, "0.0"));
		this.radis = Float.parseFloat(_sp.getString(BabeConst.LOCATION_RADIUS,
				"0.0"));
	}

	public void saveCity(double latitude, double longitude, double radius,
			String district, String citycode, String city, String province) {
		Editor _sp_editor = _sp.edit();
		_sp_editor.putString(BabeConst.LOCATION_PROVINCE, province);
		_sp_editor.putString(BabeConst.LOCATION_CITY, city);
		_sp_editor.putString(BabeConst.LOCATION_CITYCODE, citycode);
		_sp_editor.putString(BabeConst.LOCATION_DISTRICT, district);
		_sp_editor.putString(BabeConst.LOCATION_LATITUDE,
				String.valueOf(latitude));
		_sp_editor.putString(BabeConst.LOCATION_LONGITUDE,
				String.valueOf(longitude));
		_sp_editor.putString(BabeConst.LOCATION_RADIUS, String.valueOf(radius));
		_sp_editor.commit();
	}


}
