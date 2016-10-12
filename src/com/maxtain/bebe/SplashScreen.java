package com.maxtain.bebe;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.maxtain.bebe.init.CategorySet;
import com.maxtain.bebe.init.CategorySet.updown;
import com.maxtain.bebe.location.LocCallbackListener;
import com.maxtain.bebe.sqlite.SQLiteHelper;
import com.maxtain.bebe.sqlite.structure.Background;
import com.maxtain.bebe.sqlite.structure.Figure;
import com.maxtain.bebe.sqlite.structure.Oneword;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;

public class SplashScreen extends Activity implements LocCallbackListener {
	private SharedPreferences _sp;

	private SQLiteHelper sqlhelper;

	private Resources res;
	// private LocationClient mLocationClient = null;
	// private BaiLocationListener baiListener = new BaiLocationListener();
	private Context appCxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		// this.getActionBar().hide();
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		appCxt = getApplicationContext();
		boolean is_init = _sp.getBoolean(BabeConst.IS_INIT_PROCESS, false);
		if (!is_init) {
			// mLocationClient = new LocationClient(this.appCxt);
			// baiListener.setCallbackListener(this);
			// mLocationClient.registerLocationListener(baiListener);
			// initLocation();
			// we need start a thread to locate
			res = getResources();
			sqlhelper = SQLiteHelper.getInstance(this.appCxt);
			SQLiteDatabase db = sqlhelper.getWritableDatabase();
			// .openOrCreateDatabase(BabeConst.SQLITE_DATABASE_NAME,Activity.MODE_PRIVATE,
			// null);
			try {

				db.beginTransaction();
				String[] queries = res.getString(R.string.city_sql).split(";");
				for (String query : queries) {
					// Log.e("query", query);
					db.execSQL(query);

				}
				CategorySet cs = new CategorySet();

				for (int idx = 0; idx < CategorySet.init_background_list.size(); idx++) {
					String tmp_wea = CategorySet.backgroud_weather[idx];
					int[] backgound_array = CategorySet.init_background_list
							.get(idx);
					updown time_updown = cs.background_time.get(idx);
					for (int idx_inner = 0; idx_inner < backgound_array.length; idx_inner++) {
						int drawable_id = backgound_array[idx_inner];

						Background bg = new Background(this.appCxt, db);
						bg.filename = res.getResourceEntryName(drawable_id);
						bg.path = "drawable://" + drawable_id;
						InputStream is = res.openRawResource(drawable_id);
						bg.md5 = PhpMD5.md5(is);
						bg.weather = tmp_wea;
						bg.download = 1;
						bg.ge_hour = time_updown.ge;
						bg.le_hour = time_updown.le;
						bg.ge_month = 0;
						bg.le_month = 31;
						bg.ge_week = 0;
						bg.le_week = 6;
						bg.ge_temp = -100;
						bg.le_temp = 100;
						bg.ge_aqi = 0;
						bg.le_aqi = 1000;
						// Log.e("here1", "background");
						bg.save(0);

					}
				}

				for (int idx = 0; idx < CategorySet.init_figure_list.size(); idx++) {
					int[] figure_array = CategorySet.init_figure_list.get(idx);
					String tmp_wea = CategorySet.figure_weather[idx];
					updown time_updown = cs.figure_time.get(idx);

					updown temp_updown = cs.figure_temp.get(idx);
					for (int idx_inner = 0; idx_inner < figure_array.length; idx_inner++) {
						int drawable_id = figure_array[idx_inner];

						Figure fg = new Figure(this.appCxt, db);
						fg.filename = res.getResourceEntryName(drawable_id);

						fg.path = "drawable://" + drawable_id;
						InputStream is = res.openRawResource(drawable_id);
						fg.md5 = PhpMD5.md5(is);
						// Log.e("fg name", fg.filename + "  " + fg.md5);
						fg.weather = tmp_wea;
						fg.ge_hour = time_updown.ge;
						fg.le_hour = time_updown.le;
						fg.download = 1;
						fg.ge_month = 0;
						fg.le_month = 31;
						fg.ge_week = 0;
						fg.le_week = 6;
						fg.ge_temp = temp_updown.ge;
						fg.le_temp = temp_updown.le;
						fg.ge_aqi = 0;
						fg.le_aqi = 1000;
						fg.save(0);
						// Log.e("here2", "figure");
					}
				}

				for (int idx = 0; idx < CategorySet.init_words.length; idx++) {
					String word = CategorySet.init_words[idx];
					updown time_updown = cs.oneword_time.get(idx);

					Oneword ow = new Oneword(this.appCxt, db);
					ow.word = word;
					ow.weather = "*";
					ow.ge_hour = time_updown.ge;
					ow.le_hour = time_updown.le;
					ow.ge_month = 0;
					ow.le_month = 31;
					ow.ge_week = 0;
					ow.le_week = 6;
					ow.ge_temp = -100;
					ow.le_temp = 100;
					ow.ge_aqi = 0;
					ow.le_aqi = 1000;
					ow.save(0);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				// db.close();
			}

			// now create databases
			Editor _sp_editor = _sp.edit();
			_sp_editor.putBoolean(BabeConst.SETTING_AUTOUPDATE, true);
			_sp_editor.putBoolean(BabeConst.SETTING_SEND_PUSH_MSG, true);
			_sp_editor.putBoolean(BabeConst.SETTING_WIFIONLY, false);
			_sp_editor.putBoolean(BabeConst.SETTING_NOTIFICATION, true);
			_sp_editor.putBoolean(BabeConst.SETTING_USE_GPS, false);
			_sp_editor.putBoolean(BabeConst.IS_INIT_PROCESS, true);
			_sp_editor.commit();
		}
		// String city = _sp.getString(BabeConst.LOCATION_CITY, null);
		// while (city != null) {
		// city = _sp.getString(BabeConst.LOCATION_CITY, "");
		// }
		// Background.select_test(getApplicationContext());
		// Figure.select_test(getApplicationContext());
		// Oneword.select_test(getApplicationContext());
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashScreen.this, MainActivity.class));
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				finish();
			}
		}, 2000);
	}

	private void initLocation() {
		// LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Battery_Saving);
		//
		// option.setCoorType("bd09ll");
		// option.setScanSpan(5000);
		// option.setIsNeedAddress(true);
		//
		// mLocationClient.setLocOption(option);
		// mLocationClient.start();
		// if (mLocationClient != null && mLocationClient.isStarted())
		// mLocationClient.requestLocation();
		// else
		// Log.d("LocSDK5", "locClient is null or not started");
	}

	private void createDataSet() {

	}

	@Override
	public void locationCallback(double latitude, double longitude,
			double radius, String district, String citycode, String city,
			String province) {
		if (!_sp.getBoolean(BabeConst.IS_FIRST_LOCATE_PROCESS, false)) {
			// CityData cityx = new CityData(getApplicationContext());
			// cityx.loadCity();

			// // || Babe.cleanCity(cityx.city).equals(Babe.cleanCity(city))
			// CityData citydata = new CityData(getApplicationContext());
			// citydata.saveCity(latitude, longitude, radius, district,
			// citycode,
			// city, province);
		}

	}

}
