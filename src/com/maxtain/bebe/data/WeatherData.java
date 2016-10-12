package com.maxtain.bebe.data;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.maxtain.bebe.util.BabeConst;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/*
 * 'updatetime','temp','fengxiang','fenglevel','humidity','weather',
 * 'aqi','day_weather','night_weather','day_temp','night_temp',
 * 'next_day_weather','next_night_weather','next_day_temp',
 * 'next_night_temp','weather_detail','has_alarm');
 * 'alarm_type','alarm_level','alarm_issuetime','alram_content'
 */
public class WeatherData implements Serializable {

	private static final long serialVersionUID = 8364973986104220742L;
	// this current use data
	private static final String WUPDATETIME = "__weather_updatetime";
	private static final String WTEMP = "__weather_temp";
	private static final String WRTEMP = "__weather_rtemp";
	private static final String WRWEATHER = "__weather_rweather";
	private static final String WFENGXIANG = "__weather_fengxiang";
	private static final String WFENGLEVEL = "__weather_fenglevel";
	private static final String WHUMIDITY = "__weather_humidity";
	private static final String WWEATHER = "__weather_weather";
	private static final String WAQI = "__weather_AQI";
	private static final String WDAYWEATHER = "__weather_day_weather";
	private static final String WNIGHTWEATHER = "__weather_night_weather";
	private static final String WDAYTEMP = "__weather_daytemp";
	private static final String WNIGHTTEMP = "__weather_nighttemp";
	private static final String WNEXTDAYWEATHER = "__weather_next_day_weather";
	private static final String WNEXTNIGHTWEATHER = "__weather_next_night_weather";
	private static final String WNEXTDAYTEMP = "__weather_next_day_temp";
	private static final String WNEXTNIGHTTEMP = "__weather_next_night_temp";
	private static final String WWEATHERDETAIL = "__weather_weather_detail";
	private static final String WHASALARM = "__weather_has_alarm";
	private static final String WALARMTYPE = "__weather_alarm_type";
	private static final String WALARMLEVEL = "__weather_alarm_level";
	private static final String WALARMISSUETIME = "__weather_alarm_issue_time";
	private static final String WALARMCONTENT = "__weather_alarm_content";

	private static final String WTODAY_WEATHER = "__weather_today_weather";
	private static final String WTODAY_TEMP = "__weather_today_temp";
	private static final String WTOMO_TEMP = "__weather_tomo_temp";
	private static final String WTOMO_WEATHER = "__weather_tomo_weather";

	public String rtemp;
	public String rweather;
	public String state;
	public String updatetime;
	public String temp;
	public String fengxiang;
	public String fenglevel;
	public String humidity;
	public String weather;
	public String aqi;
	public String today_temp = "";
	public String today_weather = "";
	public String day_weather;
	public String night_weather;
	public String day_temp;
	public String night_temp;
	public String tomo_weather = "";
	public String tomo_temp = "";
	public String next_day_weather;
	public String next_night_weather;
	public String next_day_temp;
	public String next_night_temp;
	public String weather_detail;
	public int has_alarm;
	public String alarm_type;
	public String alarm_level;
	public String alarm_issuetime;
	public String alram_content;

	@Override
	public String toString() {
		return "State=" + state + " Weather [updatetime=" + updatetime
				+ ", temp=" + temp + ", " + "aqi=" + aqi + "weather=" + weather;
	}

	public String buildFilter() {
		return null;
	}

	public void saveWeather(Context context) {
		SharedPreferences _sp = context.getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		Editor _sp_editor = _sp.edit();
		if (this.state != "err") {
			_sp_editor.putString(WUPDATETIME, this.updatetime);
			_sp_editor.putString(WTEMP, this.temp);
			_sp_editor.putString(WRTEMP, this.rtemp);
			_sp_editor.putString(WRWEATHER, this.rweather);
			_sp_editor.putString(WFENGLEVEL, this.fenglevel);
			_sp_editor.putString(WFENGXIANG, this.fengxiang);
			_sp_editor.putString(WHUMIDITY, this.humidity);
			_sp_editor.putString(WWEATHER, this.weather);
			_sp_editor.putString(WAQI, this.aqi);
			_sp_editor.putString(WDAYWEATHER, this.day_weather);
			_sp_editor.putString(WNIGHTWEATHER, this.night_weather);
			_sp_editor.putString(WDAYTEMP, this.day_temp);
			_sp_editor.putString(WNIGHTTEMP, this.night_temp);
			_sp_editor.putString(WNEXTDAYWEATHER, this.next_day_weather);
			_sp_editor.putString(WNEXTNIGHTWEATHER, this.next_night_weather);
			_sp_editor.putString(WNEXTDAYTEMP, this.next_day_temp);
			_sp_editor.putString(WNEXTNIGHTTEMP, this.next_night_temp);
			_sp_editor.putString(WWEATHERDETAIL, this.weather_detail);
			_sp_editor.putString(WTODAY_WEATHER, this.today_weather);
			_sp_editor.putString(WTODAY_TEMP, this.today_temp);
			_sp_editor.putString(WTOMO_TEMP, this.tomo_temp);
			_sp_editor.putString(WTOMO_WEATHER, this.tomo_weather);

			_sp_editor.putString(WHASALARM, String.valueOf(this.has_alarm));
			if (this.has_alarm == 1) {
				_sp_editor.putString(WALARMTYPE, this.alarm_type);
				_sp_editor.putString(WALARMLEVEL, this.alarm_level);
				_sp_editor.putString(WALARMISSUETIME, this.alarm_issuetime);
				_sp_editor.putString(WALARMCONTENT, this.alram_content);
			} else {
				_sp_editor.putString(WALARMTYPE, "");
				_sp_editor.putString(WALARMLEVEL, "");
				_sp_editor.putString(WALARMISSUETIME, "");
				_sp_editor.putString(WALARMCONTENT, "");
			}
			_sp_editor.commit();
		}
	}

	public static WeatherData loadNowWeather(Context context) {
		WeatherData tmp = new WeatherData();
		SharedPreferences _sp = context.getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		tmp.updatetime = _sp.getString(WUPDATETIME, null);
		if (tmp.updatetime == null) {
			tmp.state = "err";
		} else {
			tmp.rtemp = _sp.getString(WRTEMP, null);
			tmp.rweather = _sp.getString(WRWEATHER, null);
			tmp.temp = _sp.getString(WTEMP, null);
			tmp.fenglevel = _sp.getString(WFENGLEVEL, null);
			tmp.fengxiang = _sp.getString(WFENGXIANG, null);
			tmp.humidity = _sp.getString(WHUMIDITY, null);
			tmp.weather = _sp.getString(WWEATHER, null);
			tmp.aqi = _sp.getString(WAQI, null);
			tmp.day_weather = _sp.getString(WDAYWEATHER, null);
			tmp.night_weather = _sp.getString(WNIGHTWEATHER, null);
			tmp.day_temp = _sp.getString(WDAYTEMP, null);
			tmp.night_temp = _sp.getString(WNIGHTTEMP, null);
			tmp.next_day_weather = _sp.getString(WNEXTDAYWEATHER, null);
			tmp.next_night_weather = _sp.getString(WNEXTNIGHTWEATHER, null);
			tmp.next_day_temp = _sp.getString(WNEXTDAYTEMP, null);
			tmp.next_night_temp = _sp.getString(WNEXTNIGHTTEMP, null);
			tmp.weather_detail = _sp.getString(WWEATHERDETAIL, null);
			tmp.today_weather = _sp.getString(WTODAY_WEATHER, null);
			tmp.today_temp = _sp.getString(WTODAY_TEMP, null);
			tmp.tomo_temp = _sp.getString(WTOMO_TEMP, null);
			tmp.tomo_weather = _sp.getString(WTOMO_WEATHER, null);
			tmp.has_alarm = Integer.parseInt(_sp.getString(WHASALARM, null));
			if (tmp.has_alarm != 0) {
				tmp.alarm_type = _sp.getString(WALARMTYPE, null);
				tmp.alarm_level = _sp.getString(WALARMLEVEL, null);
				tmp.alarm_issuetime = _sp.getString(WALARMISSUETIME, null);
				tmp.alram_content = _sp.getString(WALARMCONTENT, null);
			}
		}

		return tmp;
	}

	public static String getNowFilter(Context context) {
		WeatherData wea = loadNowWeather(context);
		Calendar cl = Calendar.getInstance();

		int nowhour = cl.get(Calendar.HOUR_OF_DAY);
		int nowdate = cl.get(Calendar.DAY_OF_WEEK) - 1;
		int nowmonth = cl.get(Calendar.DAY_OF_MONTH);
		String hour_filter = String.format(" and ("
				+ "(ge_hour <= %d and le_hour >= %d and (ge_hour <= le_hour))"
				+ " or ((ge_hour > le_hour) "
				+ "and ((ge_hour <= %d and %d < 25) "
				+ "or (%d >= 0 and le_hour >= %d))" + "))", nowhour, nowhour,
				nowhour, nowhour, nowhour, nowhour);
		String week_filter = String.format(" and ( "
				+ "(ge_week <= %d and le_week >= %d and (ge_week <= le_week)) "
				+ "or ((ge_week > le_week) "
				+ "and ((ge_week <= %d and %d <= 7) "
				+ "or (le_week >= %d and %d >= 0))" + "))", nowdate, nowdate,
				nowdate, nowdate, nowdate, nowdate);
		String month_filter = String
				.format(" and ( "
						+ "(ge_month <= %d and le_month >= %d and (ge_month <= le_month)) "
						+ "or ((ge_month > le_month) "
						+ "and ((ge_month <= %d and %d >= 31) "
						+ "or (le_month >=%d and %d >=0 ))" + "))", nowmonth,
						nowmonth, nowmonth, nowmonth, nowmonth, nowmonth);

		String weather_filter = "";
		String nowweather = StringUtils.isNotEmpty(wea.today_weather)
				&& StringUtils.isNotBlank(wea.today_weather) ? wea.today_weather
				: wea.weather;
		if (StringUtils.isNotEmpty(nowweather)
				&& StringUtils.isNotBlank(nowweather)) {
			try {
				weather_filter = String.format(" ((weather = '*') "
						+ "or (weather = '任意天气') or (weather like '%s') "
						+ "or ('%s' like '%s' || weather || '%s'))", "%"
						+ nowweather + "%", nowweather, "%", "%");
			} catch (NumberFormatException e) {
				weather_filter = "";

			}
		}

		String temp_filter = "";
		try {
			int temp_inte = Integer.parseInt(wea.temp);
			temp_filter = String.format(
					" and (ge_temp <= %d and le_temp >= %d)", temp_inte,
					temp_inte);
		} catch (NumberFormatException e) {
			temp_filter = "";
		}

		String aqi_filter = "";
		if (!StringUtils.isEmpty(wea.aqi) && NumberUtils.isNumber(wea.aqi)) {

			int aqi_int = Integer.parseInt(wea.aqi);
			aqi_filter = String.format(" and (ge_aqi <=%d and le_aqi >= %d)",
					aqi_int, aqi_int);
		} else {
			aqi_filter = "";
		}

		String filter = "" + weather_filter + hour_filter + week_filter
				+ month_filter + temp_filter + aqi_filter;

		return filter;
	}
}
