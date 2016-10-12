package com.maxtain.bebe.service;

public class ServiceConst {
	public static final String NOTIFICATION = "com.maxtain.bebe.service.receiver";

	public static enum SERVICE_STATUS {
		SERVICE_CANCELED, SERVICE_WEATHER_DATA_STORED, SERVICE_BAIDU_WEATHER_DATA_STORED, SERVICE_PICTURE_DATA_STORED, SERVICE_PIC_BACKGROUND_DOWNLOAD_COMPLETE, SERVICE_PIC_FIGURE_DOWNLOAD_COMPLETE, SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_BOTH, SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_BACKGROUND, SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_FIGURE, SERVICE_NO_NETWORK_DOWN
	};

	public static final String MSTATUS = "__service_status";
	public static final String MFILEPATH = "__service_filepath";
	public static final String MRESULT = "__service_result";
}
