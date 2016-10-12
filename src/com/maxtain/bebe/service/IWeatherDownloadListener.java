package com.maxtain.bebe.service;

import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;

public interface IWeatherDownloadListener {
	public static enum WEATHERDOWNLOAD {
		DOWNLOAD_FROM_SERVER, DOWNLOAD_FROM_BAIDU
	};

	public void weatherDownloadDoneCallback(WEATHERDOWNLOAD from,
			WeatherData weather, SERVICE_STATUS publish_status);
}
