package com.maxtain.bebe.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxtain.bebe.R;
import com.maxtain.bebe.data.BaiduWeather;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.OneDayWeatherInf;
import com.maxtain.bebe.data.PicsData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.DownloadService.DOWNLOAD_TYPE;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;
import com.maxtain.bebe.service.ServiceConst.*;
import com.maxtain.bebe.sqlite.structure.Background;
import com.maxtain.bebe.sqlite.structure.Figure;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.RemoteViews;

public class RefreshService extends IntentService implements
		IWeatherDownloadListener, IImageDownloadListener,
		IPictureJsonDownloadListener {

	private String now_city_id;

	// public static final String NOTIFICATION =
	// "com.maxtain.bebe.service.refresh_receiver";

	private String ret_str = "";

	private SharedPreferences _sp;
	private String reso;

	private SERVICE_STATUS result = SERVICE_STATUS.SERVICE_CANCELED;

	public RefreshService() {
		super("RefreshService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

		this.reso = _sp.getString("reso", null);
		CityData city = new CityData(getApplicationContext());
		city.loadCity();

		this.now_city_id = Babe.cleanCity(city.city);

		// if (false) {
		new Thread(new DownloadServerWeather(getApplicationContext(), this))
				.start();
		// }
		// new Thread(new DownloadBaiduWeather(getApplicationContext(), this))
		// .start();

	}

	private void publishResults(SERVICE_STATUS result) {
		Intent intent = new Intent(ServiceConst.NOTIFICATION);
		intent.putExtra(ServiceConst.MRESULT, Activity.RESULT_OK);
		intent.putExtra(ServiceConst.MSTATUS, result);
		sendBroadcast(intent);
	}

	private void publishDownloadResults(SERVICE_STATUS result, String fpath) {
		Intent intent = new Intent(ServiceConst.NOTIFICATION);
		intent.putExtra(ServiceConst.MRESULT, Activity.RESULT_OK);
		intent.putExtra(ServiceConst.MSTATUS, result);
		intent.putExtra(ServiceConst.MFILEPATH, fpath);
		sendBroadcast(intent);
	}

	@Override
	public void imageDownloadDoneCallback(NameValuePair result, long id,
			String category) {
		String name = result.getName();
		String value = result.getValue();

		if (value != "0") {
			// Editor _sp_editor = _sp.edit();
			// _sp_editor.putString(name, value);
			// _sp_editor.commit();
			if (category == DownloadImage.CATEGORY_BACKGROUND) {
				Background.updatefile(getApplicationContext(), id, value, name);
				publishDownloadResults(
						SERVICE_STATUS.SERVICE_PIC_BACKGROUND_DOWNLOAD_COMPLETE,
						value);
			}
			if (category == DownloadImage.CATEGORY_FIGURE) {
				Figure.updatefile(getApplicationContext(), id, value, name);
				publishDownloadResults(
						SERVICE_STATUS.SERVICE_PIC_FIGURE_DOWNLOAD_COMPLETE,
						value);
			}

		}

	}

	@Override
	public void weatherDownloadDoneCallback(WEATHERDOWNLOAD from,
			WeatherData weather, SERVICE_STATUS publish_status) {
		switch (from) {
		case DOWNLOAD_FROM_BAIDU:
			new Thread(new DownloadPictureJson(getApplicationContext(), this,
					this));
			Babe.makeNotification(getApplicationContext(), weather);
			publishResults(publish_status);

			break;
		case DOWNLOAD_FROM_SERVER:
			Babe.makeNotification(getApplicationContext(), weather);
			publishResults(publish_status);
			new Thread(new DownloadPictureJson(getApplicationContext(), this,
					this));

			break;
		default:
			break;
		}
	}

	@Override
	public void pictureJsonDoneCallback(SERVICE_STATUS publish_status) {
		publishResults(publish_status);
	}

}
