package com.maxtain.bebe.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class BaiLocationListener implements BDLocationListener {
	private LocCallbackListener callbackListener;

	public void setCallbackListener(LocCallbackListener listener) {
		this.callbackListener = listener;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;

		String locTime = location.getTime();
		String province = "";
		String addr = "";
		String city = "";
		String district = "";
		String citycode = "";
		int errcode = location.getLocType();
		double lati = location.getLatitude();
		double longi = location.getLongitude();
		double radius = location.getRadius();

		if (errcode != BDLocation.TypeNetWorkException
				&& errcode != BDLocation.TypeServerError
				&& errcode != BDLocation.TypeOffLineLocationNetworkFail
				&& errcode != BDLocation.TypeOffLineLocationFail
				&& errcode != BDLocation.TypeCriteriaException) {
			addr = location.getAddrStr();
			province = location.getProvince();
			city = location.getCity();
			district = location.getDistrict();
			citycode = location.getCityCode();

			this.callbackListener.locationCallback(lati, longi, radius,
					district, citycode, city, province);
		}
	}
}