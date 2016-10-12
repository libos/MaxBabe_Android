package com.maxtain.bebe.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.PicsData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class DownloadPictureJson implements Runnable {
	private Context context;
	private IPictureJsonDownloadListener listener;
	private IImageDownloadListener imageCallbackListener;

	private SharedPreferences _sp;

	private static String reso;
	private final String url_pic_info = "http://api.babe.maxtain.com/pic_info.php";
	private final String url_base_url = "http://cdn-babe-img.maxtain.com";
	private boolean wifi_only_flag = false;

	public DownloadPictureJson(Context context,
			IPictureJsonDownloadListener listener,
			IImageDownloadListener imageCallbackListener) {
		this.context = context;
		this.listener = listener;
		this.imageCallbackListener = imageCallbackListener;
		_sp = this.context.getSharedPreferences(BabeConst.SHAREP_DATABASE,
				Activity.MODE_PRIVATE);

		wifi_only_flag = _sp.getBoolean(BabeConst.SETTING_WIFIONLY, true);
		reso = _sp.getString("reso", "xx");
	}

	@Override
	public void run() {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url_pic_info);
		// request.addHeader("referer", BabeConst.referer);
		CityData city = new CityData(this.context);

		city.loadCity();

		WeatherData weather = WeatherData.loadNowWeather(this.context);
		String cid = Babe.cleanCity(city.city);

		String auth = PhpMD5.md5((cid + ". maxtain . mybabe ").getBytes());
		Calendar cl = Calendar.getInstance();
		int nowhour = cl.get(Calendar.HOUR_OF_DAY);
		int nowdate = cl.get(Calendar.DAY_OF_WEEK) - 1;
		int nowmonth = cl.get(Calendar.DAY_OF_MONTH);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		NameValuePair pair1 = new BasicNameValuePair("id", cid);
		NameValuePair pair2 = new BasicNameValuePair("reso", reso);
		NameValuePair pair3 = new BasicNameValuePair("auth", auth);
		NameValuePair pair4 = new BasicNameValuePair("user", "1");
		NameValuePair pair5 = new BasicNameValuePair("weather", weather.weather);
		NameValuePair pair6 = new BasicNameValuePair("temp", weather.temp);
		NameValuePair pair7 = new BasicNameValuePair("aqi", weather.aqi);

		NameValuePair pair8 = new BasicNameValuePair("week", nowdate + "");
		NameValuePair pair9 = new BasicNameValuePair("hour", nowhour + "");
		NameValuePair pair10 = new BasicNameValuePair("month", nowmonth + "");

		paramList.add(pair1);
		paramList.add(pair2);
		paramList.add(pair3);
		paramList.add(pair4);
		paramList.add(pair5);
		paramList.add(pair6);
		paramList.add(pair7);
		paramList.add(pair8);
		paramList.add(pair9);
		paramList.add(pair10);
		SERVICE_STATUS ret = SERVICE_STATUS.SERVICE_CANCELED;

		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(paramList,
					HTTP.UTF_8);

			request.setEntity(requestHttpEntity);
			HttpResponse httpResponse = null;
			boolean success = false;
			try {
				httpResponse = httpClient.execute(request);
				success = true;
			} catch (Exception ex) {
				success = false;
				ex.printStackTrace();
			}
			if (success) {
				String pic_str = EntityUtils.toString(httpResponse.getEntity());

				ObjectMapper mapper = new ObjectMapper();
				// try_once = false;

				PicsData pics = mapper.readValue(pic_str, PicsData.class);
				pics.savePics(this.context);

				// Log.e("PIC_STR", pic_str);
				if (wifi_only_flag) {
					if (getNetWorkType(this.context) != BabeConst.NETWORKTYPE_WIFI) {
						listener.pictureJsonDoneCallback(SERVICE_STATUS.SERVICE_PICTURE_DATA_STORED);
						return;
					}
				}

				ret = SERVICE_STATUS.SERVICE_PICTURE_DATA_STORED;

				if (pics.state != "err") {
					if (pics.needDownBackground) {
						ret = SERVICE_STATUS.SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_BACKGROUND;
						// Task open download background
						DownloadImage r = new DownloadImage(this.context,
								url_base_url + pics.background, pics.bmd5,
								pics.bstore_id);
						r.setListener(this.imageCallbackListener);
						r.setCategory(DownloadImage.CATEGORY_BACKGROUND);
						new Thread(r).start();
					}
					if (pics.needDownFigure) {
						ret = SERVICE_STATUS.SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_FIGURE;
						if (_sp.getString(pics.fmd5, null) == null) {
							DownloadImage r = new DownloadImage(this.context,
									url_base_url + pics.figure, pics.fmd5,
									pics.fstore_id);
							r.setListener(this.imageCallbackListener);
							r.setCategory(DownloadImage.CATEGORY_FIGURE);
							new Thread(r).start();
						}
						if (pics.needDownBackground) {
							ret = SERVICE_STATUS.SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_BOTH;
						}
					}
					listener.pictureJsonDoneCallback(ret);
				}
			} else {
				listener.pictureJsonDoneCallback(SERVICE_STATUS.SERVICE_NO_NETWORK_DOWN);
			}
		} catch (Exception e) {
			// Log.e("Error", "PIC ERROR");
			e.printStackTrace();
		}

	}

	public static int getNetWorkType(Context context) {
		int mNetWorkType = BabeConst.NETWORKTYPE_INVALID;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();
			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = BabeConst.NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				mNetWorkType = TextUtils.isEmpty(proxyHost) ? ((isFastMobileNetwork(context) ? BabeConst.NETWORKTYPE_3G
						: BabeConst.NETWORKTYPE_2G))
						: BabeConst.NETWORKTYPE_WAP;
			}
		} else {
			mNetWorkType = BabeConst.NETWORKTYPE_INVALID;
		}
		return mNetWorkType;
	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50�\100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14�\64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50�\100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400�\1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600�\1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2�\14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700�\1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 400�\7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1�\2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10�\20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}
}
