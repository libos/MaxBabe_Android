package com.maxtain.bebe.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import taobe.tec.jcc.JChineseConvertor;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.maxtain.bebe.MainActivity;
import com.maxtain.bebe.R;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.init.CategorySet;

public class Babe {

	public static void makeNotification(Context context, WeatherData weather) {
		SharedPreferences _sp = context.getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		boolean send_push_msg_flag = _sp.getBoolean(
				BabeConst.SETTING_SEND_PUSH_MSG, true);
		if (!send_push_msg_flag) {
			return;
		}

		String appname = context.getResources().getString(R.string.app_name);
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(context)
				.setContentTitle(appname)
				.setContentText(appname)
				.setContentIntent(pendingIntent)
				.setOngoing(true)
				.setSmallIcon(R.drawable.icon_statusbar_48)
				.setLargeIcon(
						BitmapFactory.decodeResource(context.getResources(),
								R.drawable.icon_statusbar_72));
		Notification noti;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			noti = builder.build();
		} else {
			noti = builder.getNotification();
		}
		CityData cd = new CityData(context);
		cd.loadCity();
		noti.flags |= Notification.FLAG_NO_CLEAR
				| Notification.FLAG_ONGOING_EVENT;
		noti.icon = R.drawable.notification_appicon;
		noti.tickerText = appname;
		noti.when = System.currentTimeMillis(); // 立即发生此通知
		// 1、创建一个自定义的消息布局 notification.xml
		// 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.notification);
		if (NumberUtils.isNumber(weather.aqi)
				&& !StringUtils.isEmpty(weather.aqi)) {
			remoteView.setViewVisibility(R.id.noti_haze_text, View.VISIBLE);
			remoteView.setViewVisibility(R.id.noti_weather_rel, View.VISIBLE);
			// remoteView.setViewVisibility(R.id.noti_haze_back, View.VISIBLE);
			int aqi = Integer.parseInt(weather.aqi);
			String aqi_msg = "48 深呼吸";
			int haze_level = 0;
			if (aqi <= 50) {
				aqi_msg = aqi + " 深呼吸";
				haze_level = 0;
			} else if (aqi > 50 && aqi <= 100) {
				aqi_msg = aqi + " 还不错";
				haze_level = 1;
			} else if (aqi > 100 && aqi <= 150) {
				aqi_msg = aqi + " 有点差哦";
				haze_level = 2;
			} else if (aqi > 150 && aqi <= 200) {
				aqi_msg = aqi + " 蛮差的";
				haze_level = 3;
			} else if (aqi > 200 && aqi <= 300) {
				aqi_msg = aqi + " 别出门了";
				haze_level = 4;
			} else if (aqi > 300) {
				aqi_msg = aqi + " 已爆表";
				haze_level = 5;
			}
			remoteView.setTextViewText(R.id.noti_haze_text,
					t2s(context, aqi_msg));
			remoteView.setImageViewResource(R.id.iv_bg_begin,
					CategorySet.noti_haze_level_begin[haze_level]);
			remoteView.setInt(R.id.tv_linear, "setBackgroundResource",
					CategorySet.noti_haze_level_middle[haze_level]);
			
			// remoteView.removeAllViews(R.id.tv_linear);
			// remoteView.
			remoteView.setImageViewResource(R.id.iv_bg_end,
					CategorySet.noti_haze_level_end[haze_level]);

		} else {
			remoteView.setViewVisibility(R.id.noti_haze_text, View.INVISIBLE);
			remoteView.setViewVisibility(R.id.noti_weather_rel, View.INVISIBLE);
		}
		String weatherx = StringUtils.isNotEmpty(weather.today_weather)
				&& StringUtils.isNotBlank(weather.today_weather) ? weather.today_weather
				: weather.weather;

		int len = weatherx.length() >= 3 ? 3 : weatherx.length();
		String weather_part = weatherx.substring(0, len);
		int weather_part_idx = weather_part.indexOf("转");
		// Log.e("wired ", "" + weather_part_idx);
		if (weather_part_idx != -1) {
			weather_part = weather_part.substring(0, weather_part_idx);
		}

		if (!StringUtils.isEmpty(weather.temp) && !weather.temp.equals("false")) {
			remoteView.setTextViewText(R.id.noti_weather_text, weather.temp
					+ "℃  " + t2s(context, weather_part));
		} else {
			remoteView.setTextViewText(R.id.noti_weather_text, weather.rtemp
					+ "℃  " + t2s(context, weather_part));
		}
		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);
		if (minutes >= 30) {
			minutes = minutes - 30;
		}
		String tip = "";
		Resources res = context.getResources();
		if (minutes <= 3) {
			tip = res.getString(R.string.just_now);
		} else {
			tip = minutes + res.getString(R.string.minutes_ago);
		}

		remoteView.setTextViewText(R.id.noti_issue_text,
				t2s(context, cd.district + "  " + tip));

		String w_1 = "notpossible";
		String w_2 = "notpossible";
		String w_3 = "notpossible";

		w_1 = weatherx.substring(0, 1);
		if (len >= 2)
			w_2 = weatherx.substring(0, 2);
		if (len >= 3)
			w_3 = weatherx.substring(0, 3);
		List<String> w_arr = Arrays.asList(CategorySet.weather_icon_noti_text);

		boolean b_1 = w_arr.contains(w_1);
		boolean b_2 = w_arr.contains(w_2);
		boolean b_3 = w_arr.contains(w_3);

		int w_idx = 0;

		if (b_1 || b_2 || b_3) {
			if (b_3) {
				w_idx = w_arr.indexOf(w_3);
			} else if (b_2) {
				w_idx = w_arr.indexOf(w_2);
			} else if (b_1) {
				w_idx = w_arr.indexOf(w_1);
			}
		}

		int hournow = cal.get(Calendar.HOUR_OF_DAY);
		if (w_idx >= CategorySet.noti_wicon_night_start_idx) {
			if (hournow > 19 || hournow < 8) {

			} else {
				if (w_idx == CategorySet.noti_icon_night_snow_idx) {
					w_idx = CategorySet.noti_icon_daytime_snow_idx;
				}
			}
		}

		if (hournow > 19 || hournow < 8) {
			if (w_idx == 0) {
				w_idx = CategorySet.noti_icon_night_clear_idx;
			}
			if (w_idx >= CategorySet.noti_wicon_cloudy_up
					&& w_idx <= CategorySet.noti_wicon_cloudy_down) {
				w_idx = CategorySet.noti_icon_night_cloudy_idx;
			}
			if (w_idx >= CategorySet.noti_wicon_rain_up
					&& w_idx <= CategorySet.noti_wicon_rain_down) {
				w_idx = CategorySet.noti_icon_night_rain_idx;
			}
			if (w_idx >= CategorySet.noti_wicon_snow_up
					&& w_idx <= CategorySet.noti_wicon_snow_down) {
				w_idx = CategorySet.noti_icon_night_snow_idx;
			}

		}

		// Log.e("d", "" + w_idx);
		remoteView.setImageViewResource(R.id.noti_icon,
				CategorySet.weather_icon_noti[w_idx]);
		noti.contentView = remoteView;
		// 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)

		// 这儿点击后简答启动Settings模块
		// PendingIntent contentIntent = PendingIntent.getActivity(
		// D.this, 0, new Intent("android.settings.SETTINGS"),
		// 0);

		// 设置setLatestEventInfo方法,如果不设置会App报错异常
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 注册此通知
		// 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等
		mNotificationManager.notify(0, noti);

	}

	public static String t2s(Context context, String changeText) {
		if (!context.getResources().getConfiguration().locale.getCountry()
				.equals("TW")) {
			return changeText;
		}
		if (StringUtils.isEmpty(changeText) || StringUtils.isBlank(changeText)) {
			return "";
		}
		try {
			JChineseConvertor jChineseConvertor = JChineseConvertor
					.getInstance();
			changeText = jChineseConvertor.s2t(changeText);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return changeText;
	}

	public static String cleanCity(String cityname) {
		if (cityname == null || StringUtils.isBlank(cityname)) {
			return "";
		}
		String[] stopwords = { "市", "自治州", "盟", "旗", "地区", "特别行政区" };
		for (String wd : stopwords) {
			cityname = cityname.replaceAll(wd, "");
		}
		return cityname;
	}

}
