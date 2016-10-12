package com.maxtain.bebe.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;
import com.maxtain.bebe.service.IWeatherDownloadListener.WEATHERDOWNLOAD;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;

import android.content.Context;
import android.util.Log;

public class DownloadServerWeather implements Runnable {
	// private final String url_weather =
	// "http://cdn-babe-api.maxtain.com/get_data.php?";
	private final String url_weather = "http://api.babe.maxtain.com/get_data.php?";
	private Context context;
	private IWeatherDownloadListener listener;
	private final String USER_AGENT = "Mozilla/5.0";

	public DownloadServerWeather(Context context,
			IWeatherDownloadListener listener) {
		this.context = context;
		this.listener = listener;
	}

	@Override
	public void run() {
		HttpClient httpClient = new DefaultHttpClient();

		// 先封装一个 JSON 对象
		WeatherData weather = new WeatherData();
		CityData city = new CityData(this.context);
		city.loadCity();
		String cid = Babe.cleanCity(city.city);// "重庆";//
		String type = "all";
		String auth = null;
		auth = PhpMD5
				.md5((cid + ". maxtain ." + type + ". mybabe ").getBytes());
		Calendar cal = Calendar.getInstance();

		String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String date = String.format("%02d", cal.get(Calendar.DATE));
		String hour = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
		//
		// List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		// NameValuePair pair1 = new BasicNameValuePair("id", cid);
		// NameValuePair pair2 = new BasicNameValuePair("type", "all");
		// NameValuePair pair3 = new BasicNameValuePair("auth", auth);
		// NameValuePair pair4 = new BasicNameValuePair("user", "1");
		// NameValuePair pairmonth = new BasicNameValuePair("month", month);
		// NameValuePair pairdate = new BasicNameValuePair("day", date);
		// NameValuePair pairhour = new BasicNameValuePair("hour", hour);
		//
		// paramList.add(pair1);
		// paramList.add(pair2);
		// paramList.add(pair3);
		// paramList.add(pair4);
		// paramList.add(pairmonth);
		// paramList.add(pairdate);
		// paramList.add(pairhour);
		String url = url_weather + "id=" + cid + "&type=all&auth=" + auth
				+ "&user=1&month=" + month + "&day=" + date + "&hour=" + hour;
		// Log.e("refere", url);
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		request.addHeader("referer", BabeConst.referer);
		try {

			// 发送HttpPost请求，并返回HttpResponse对象
			HttpResponse httpResponse = null;
			boolean success = false;
			try {
				httpResponse = httpClient.execute(request);
				success = true;
			} catch (Exception ex) {
				success = false;
			}
			// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
			// if (httpResponse.getStatusLine().getStatusCode() == 200) {
			// 获取返回结果
			if (success) {
				String ret_str = EntityUtils.toString(httpResponse.getEntity());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					ObjectMapper mapper = new ObjectMapper();
					// Log.e("s", ret_str);
					weather = mapper.readValue(ret_str, WeatherData.class);
					weather.saveWeather(this.context);
					// Log.e("d", "heler");
				}
				this.listener.weatherDownloadDoneCallback(
						WEATHERDOWNLOAD.DOWNLOAD_FROM_SERVER, weather,
						SERVICE_STATUS.SERVICE_WEATHER_DATA_STORED);
			} else {
				this.listener.weatherDownloadDoneCallback(
						WEATHERDOWNLOAD.DOWNLOAD_FROM_SERVER, weather,
						SERVICE_STATUS.SERVICE_NO_NETWORK_DOWN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
