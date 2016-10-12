package com.maxtain.bebe.data;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BaiduWeather {
	private OneDayWeatherInf[] weatherInfs;
	private String dressAdvise;// 穿衣建议
	private String washCarAdvise;// 洗车建议
	private String coldAdvise;// 感冒建议
	private String sportsAdvise;// 运动建议
	private String ultravioletRaysAdvise;// 紫外线建议
	private int aqi;

	public BaiduWeather() {
		dressAdvise = "";
		washCarAdvise = "";
		coldAdvise = "";
		sportsAdvise = "";
		ultravioletRaysAdvise = "";
	}

	public void printInf() {
		System.out.println(dressAdvise);
		System.out.println(washCarAdvise);
		System.out.println(coldAdvise);
		System.out.println(sportsAdvise);
		System.out.println(ultravioletRaysAdvise);
		for (int i = 0; i < weatherInfs.length; i++) {
			System.out.println(weatherInfs[i]);
		}

	}

	public OneDayWeatherInf[] getWeatherInfs() {
		return weatherInfs;
	}

	public void setWeatherInfs(OneDayWeatherInf[] weatherInfs) {
		this.weatherInfs = weatherInfs;
	}

	public String getDressAdvise() {
		return dressAdvise;
	}

	public void setDressAdvise(String dressAdvise) {
		this.dressAdvise = dressAdvise;
	}

	public String getWashCarAdvise() {
		return washCarAdvise;
	}

	public void setWashCarAdvise(String washCarAdvise) {
		this.washCarAdvise = washCarAdvise;
	}

	public String getColdAdvise() {
		return coldAdvise;
	}

	public void setColdAdvise(String coldAdvise) {
		this.coldAdvise = coldAdvise;
	}

	public void setAQI(int aqi) {
		this.aqi = aqi;
	}

	public int getAQI() {
		return this.aqi;
	}

	public String getSportsAdvise() {
		return sportsAdvise;
	}

	public void setSportsAdvise(String sportsAdvise) {
		this.sportsAdvise = sportsAdvise;
	}

	public String getUltravioletRaysAdvise() {
		return ultravioletRaysAdvise;
	}

	public void setUltravioletRaysAdvise(String ultravioletRaysAdvise) {
		this.ultravioletRaysAdvise = ultravioletRaysAdvise;
	}

	public static BaiduWeather resolveWeatherInf(String strPar)
			throws JSONException {

		JSONObject dataOfJson = new JSONObject(strPar);
				if (!dataOfJson.optString("status").startsWith("success")) {
			return null;
		}

		if (dataOfJson.optInt("error") != 0) {
			return null;
		}

		// 保存全部的天气信息。
		BaiduWeather weatherInf = new BaiduWeather();

		String date = dataOfJson.optString("date");
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		int day = Integer.parseInt(date.substring(8, 10));
		Date today = new Date(year - 1900, month - 1, day);

		JSONArray results = dataOfJson.getJSONArray("results");
		JSONObject results0 = results.getJSONObject(0);

		String location = results0.optString("currentCity");
		int pmTwoPointFive;
				if (results0.optString("pm25").isEmpty()) {
			pmTwoPointFive = 0;
		} else {
			pmTwoPointFive = results0.optInt("pm25");
		}
		weatherInf.setAQI(pmTwoPointFive);
		// System.out.println(results0.get("pm25").toString()+"11");

		JSONArray index = results0.optJSONArray("index");
		if (index.length() > 0) {
			try {
				JSONObject index0 = index.optJSONObject(0);// 穿衣
				JSONObject index1 = index.optJSONObject(1);// 洗车
				JSONObject index2 = index.optJSONObject(2);// 感冒
				JSONObject index3 = index.optJSONObject(3);// 运动
				JSONObject index4 = index.optJSONObject(4);// 紫外线强度

				String dressAdvise = index0.optString("des");// 穿衣建议
				String washCarAdvise = index1.optString("des");// 洗车建议
				String coldAdvise = index2.optString("des");// 感冒建议
				String sportsAdvise = index3.optString("des");// 运动建议
				String ultravioletRaysAdvise = index4.optString("des");// 紫外线建议

				weatherInf.setDressAdvise(dressAdvise);
				weatherInf.setWashCarAdvise(washCarAdvise);
				weatherInf.setColdAdvise(coldAdvise);
				weatherInf.setSportsAdvise(sportsAdvise);
				weatherInf.setUltravioletRaysAdvise(ultravioletRaysAdvise);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONArray weather_data = results0.optJSONArray("weather_data");// weather_data中有四项�?
				// OneDayWeatherInf是自己定义的承载某一天的天气信息的实体类，详细定义见后面。
		OneDayWeatherInf[] oneDayWeatherInfS = new OneDayWeatherInf[4];
		for (int i = 0; i < 4; i++) {
			oneDayWeatherInfS[i] = new OneDayWeatherInf();
		}
				for (int i = 0; i < weather_data.length(); i++) {

			JSONObject OneDayWeatherinfo = weather_data.optJSONObject(i);
			String dayData = OneDayWeatherinfo.optString("date");
			OneDayWeatherInf oneDayWeatherInf = new OneDayWeatherInf();

			oneDayWeatherInf.setDate((today.getYear() + 1900) + "."
					+ (today.getMonth() + 1) + "." + today.getDate());
			today.setDate(today.getDate() + 1);// 增加一天

			oneDayWeatherInf.setLocation(location);
			oneDayWeatherInf.setPmTwoPointFive(pmTwoPointFive);

						if (i == 0) {// 第一个，也就是当天的天气，在date字段中最后包含了实时天气
				int beginIndex = dayData.indexOf("：");
				int endIndex = dayData.indexOf(")");

								oneDayWeatherInf.setTempertureNow(OneDayWeatherinfo
						.optString("temperature"));
				oneDayWeatherInf.setWeek(OneDayWeatherinfo.optString("date")
						.substring(0, 2));
				if (beginIndex > -1) {
										oneDayWeatherInf.setTempertureNow(dayData.substring(
							beginIndex + 1, endIndex));
				}

			} else {

								oneDayWeatherInf.setWeek(OneDayWeatherinfo.optString("date"));
			}
			
						oneDayWeatherInf.setTempertureOfDay(OneDayWeatherinfo
					.optString("temperature"));
			oneDayWeatherInf.setWeather(OneDayWeatherinfo.optString("weather"));
			oneDayWeatherInf.setWind(OneDayWeatherinfo.optString("wind"));

			oneDayWeatherInfS[i] = oneDayWeatherInf;
		}

		weatherInf.setWeatherInfs(oneDayWeatherInfS);

		return weatherInf;
	}
}
