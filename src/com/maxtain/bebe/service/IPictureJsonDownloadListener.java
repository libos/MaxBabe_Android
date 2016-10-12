package com.maxtain.bebe.service;

import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;

public interface IPictureJsonDownloadListener {
	public void pictureJsonDoneCallback(SERVICE_STATUS publish_status);
}
