package com.maxtain.bebe.service;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxtain.bebe.MainActivity;
import com.maxtain.bebe.R;
import com.maxtain.bebe.data.BaiduWeather;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.OneDayWeatherInf;
import com.maxtain.bebe.data.PicsData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;
import com.maxtain.bebe.service.ServiceConst.*;
import com.maxtain.bebe.sqlite.structure.Background;
import com.maxtain.bebe.sqlite.structure.Figure;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

public class DownloadService extends Service implements
		IWeatherDownloadListener, IImageDownloadListener,
		IPictureJsonDownloadListener {
	private Context appContext;

	private final IBinder mBinder = new DownloadBinder();
	private int result = Activity.RESULT_CANCELED;
	private JSONObject ret_json;

	private String ret_str = "";
	private SharedPreferences _sp;

	private boolean send_push_msg_flag = true;
	private boolean auto_update_flag = true;

//	private String city_id;
	private static String ok_for_download_anytime;
	private static boolean try_once = true;

	public static enum DOWNLOAD_TYPE {
		WEATHER_DATA, PIC_DATA, CITY_DATA
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.appContext = getApplicationContext();
		// Log.d("aaa", "LocalWordService:onStartCommand ");
		_sp = this.appContext.getSharedPreferences(BabeConst.SHAREP_DATABASE,
				Activity.MODE_PRIVATE);

		// get setting data
		auto_update_flag = _sp.getBoolean(BabeConst.SETTING_AUTOUPDATE, true);

		if (!auto_update_flag) {
			return Service.START_NOT_STICKY;
		}

//		city_id = _sp.getString("city_id", "1");
		ok_for_download_anytime = _sp.getString("download_allow", null);
		// if (false)
		new Thread(new DownloadServerWeather(this.appContext, this)).start();
		// new Thread(new DownloadBaiduWeather(this.appContext, this)).start();

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class DownloadBinder extends Binder {
		public DownloadService getService() {
			return DownloadService.this;
		}
	}

	public String getData() {
		return ret_str;
	}

	private void publishResults(SERVICE_STATUS refrsh) {
		Intent intent = new Intent(ServiceConst.NOTIFICATION);
		intent.putExtra(ServiceConst.MRESULT, Activity.RESULT_OK);
		intent.putExtra(ServiceConst.MSTATUS, refrsh);
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
			if (category == DownloadImage.CATEGORY_BACKGROUND) {
				Background.updatefile(this.appContext, id, value, name);
				publishDownloadResults(
						SERVICE_STATUS.SERVICE_PIC_BACKGROUND_DOWNLOAD_COMPLETE,
						value);
			}
			if (category == DownloadImage.CATEGORY_FIGURE) {
				Figure.updatefile(this.appContext, id, value, name);
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
			Babe.makeNotification(this.appContext, weather);
			publishResults(publish_status);
			new Thread(new DownloadPictureJson(this.appContext, this, this))
					.start();
			break;
		case DOWNLOAD_FROM_SERVER:
			if (publish_status != SERVICE_STATUS.SERVICE_NO_NETWORK_DOWN) {
				Babe.makeNotification(this.appContext, weather);
				new Thread(new DownloadPictureJson(this.appContext, this, this))
						.start();
			}
			publishResults(publish_status);
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
