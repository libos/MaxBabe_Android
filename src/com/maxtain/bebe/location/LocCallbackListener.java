package com.maxtain.bebe.location;

public interface LocCallbackListener {
	public void locationCallback(double latitude, double longitude,
			double radius, String district, String citycode, String city,
			String province);
}
