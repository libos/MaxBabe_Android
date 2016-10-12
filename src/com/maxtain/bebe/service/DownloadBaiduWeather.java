package com.maxtain.bebe.service;

import java.net.URLEncoder;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.maxtain.bebe.data.BaiduWeather;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.OneDayWeatherInf;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;
import com.maxtain.bebe.service.IWeatherDownloadListener.WEATHERDOWNLOAD;

public class DownloadBaiduWeather implements Runnable {
	private final String url_baidu = "http://api.map.baidu.com/telematics/v3/weather?";
	private Context context;
	private IWeatherDownloadListener downloadListener;

	public DownloadBaiduWeather(Context context,
			IWeatherDownloadListener downloadListener) {
		this.context = context;
		this.downloadListener = downloadListener;
	}

	@Override
	public void run() {
		CityData citydata = new CityData(this.context);
		citydata.loadCity();
		WeatherData weather = new WeatherData();
		try {
			//
			String request_url = url_baidu + "location="
					// +URLEncoder.encode("台南", "UTF-8")
					+ URLEncoder.encode(citydata.city, "UTF-8")
					+ "&output=json&ak=vVrrq4WM35hdQ17yVDw7I0mB&mcode=A0:7F:1D:12:99:3F:33:01:48:8C:F3:E2:AF:57:69:40:98:F2:68:71;com.maxtain.bebe";
			HttpClient httpClient = new DefaultHttpClient();
			HttpClientParams.setCookiePolicy(httpClient.getParams(),
					CookiePolicy.BROWSER_COMPATIBILITY);
			HttpGet requestGet = new HttpGet(request_url);
			HttpResponse response = null;
			boolean success = false;
			try {
				response = httpClient.execute(requestGet);
				success = true;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
			if (success) {
				HttpEntity entity = response.getEntity();
				String ret_str = EntityUtils.toString(entity, HTTP.UTF_8);
				// Log.e("BAIDU", ret_str);
				BaiduWeather bdw = BaiduWeather.resolveWeatherInf(ret_str);
				OneDayWeatherInf[] daysInfo = bdw.getWeatherInfs();

				weather.state = null;
				weather.updatetime = daysInfo[0].getDate();
				weather.aqi = String.valueOf(bdw.getAQI());
				weather.temp = daysInfo[0].getTempertureNow().replace("℃", "");
				String[] strarr = weather.temp.split("~", 2);

				if (strarr.length == 2) {
					int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
					if (hour >= 10 && hour <= 15) {
						weather.temp = strarr[0].trim();
					} else {
						weather.temp = strarr[1].trim();
					}
				}
				weather.weather = daysInfo[0].getWeather();
				weather.day_weather = "b";
				weather.night_weather = daysInfo[0].getWeather();
				weather.day_temp = "b";
				weather.night_temp = daysInfo[0].getTempertureOfDay();
				weather.next_day_weather = "b";
				weather.next_night_weather = daysInfo[1].getWeather();
				weather.next_day_temp = "b";
				weather.next_night_temp = daysInfo[1].getTempertureOfDay();
				weather.has_alarm = 0;
				weather.saveWeather(this.context);
				downloadListener.weatherDownloadDoneCallback(
						WEATHERDOWNLOAD.DOWNLOAD_FROM_BAIDU, weather,
						SERVICE_STATUS.SERVICE_BAIDU_WEATHER_DATA_STORED);
			}else{
				downloadListener.weatherDownloadDoneCallback(
						WEATHERDOWNLOAD.DOWNLOAD_FROM_BAIDU, weather,
						SERVICE_STATUS.SERVICE_NO_NETWORK_DOWN);
			}

		} catch (Exception e) {
			// Log.e("ERROR FOR DOWNLOAD", "catch exception");
			e.printStackTrace();
		}

		// Log.d("hhh", "adfadfas");
	}
}
