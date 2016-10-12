package com.maxtain.bebe.sqlite.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.util.BabeConst;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Oneword extends Model {
	public int id;
	public String word;

	public String weather;

	public int ge_hour;
	public int le_hour;
	public int ge_week;
	public int le_week;
	public int ge_month;
	public int le_month;
	public int ge_temp;
	public int le_temp;
	public int ge_aqi;
	public int le_aqi;
	public String created;

	private SQLiteDatabase db;
	private Context context;

	public Oneword(Context context) {
		super("oneword", context, getDbFromContext(context));
		this.context = context;
		this.db = getDbFromContext(context);
	}

	public Oneword(Context context, SQLiteDatabase db) {
		super("oneword", context, db);
		this.context = context;
		this.db = db;
	}

	public Oneword(Context context, SQLiteDatabase db, int id, String word,
			String weather) {
		super("oneword", context, db);
		this.context = context;
		this.db = db;
		this.id = id;
		this.word = word;
		this.weather = weather;
	}

	public static List<Oneword> select(Context context, String filter) {
		SQLiteDatabase tmpdb = getDbFromContext(context);
		List<Oneword> lb = new ArrayList<Oneword>();
		Cursor cursor = null;
		try {
			cursor = tmpdb.rawQuery("select * from oneword where " + filter,
					null);

			while (cursor.moveToNext()) {
				lb.add(new Oneword(context, tmpdb, cursor.getInt(0), cursor
						.getString(1), cursor.getString(2)));
			}
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			// tmpdb.close();
		}

		return lb;
	}

	public static List<Oneword> select_test(Context context) {
		SQLiteDatabase tmpdb = getDbFromContext(context);
		List<Oneword> lb = new ArrayList<Oneword>();
		Cursor cursor = null;
		try {
			cursor = tmpdb.rawQuery("select * from oneword ", null);

			while (cursor.moveToNext()) {
				lb.add(new Oneword(context, tmpdb, cursor.getInt(0), cursor
						.getString(1), cursor.getString(2)));
			}
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			// tmpdb.close();
		}

		// Log.e("List", lb.toString());
		// if (lb.size() > 0)
		// 	Log.e("List", lb.get(0).word);
		// Log.e("ListSize", "" + lb.size());

		return lb;
	}

	@Override
	public long save_nofile() { // do it after chech hasSelf()
		ContentValues contentValues = new ContentValues();
		contentValues.put("word", word);
		contentValues.put("weather", weather);
		contentValues.put("ge_hour", ge_hour);
		contentValues.put("le_hour", le_hour);
		contentValues.put("ge_week", ge_week);
		contentValues.put("le_week", le_week);
		contentValues.put("ge_month", ge_month);
		contentValues.put("le_month", le_month);
		contentValues.put("ge_temp", ge_temp);
		contentValues.put("le_temp", le_temp);
		contentValues.put("ge_aqi", ge_aqi);
		contentValues.put("le_aqi", le_aqi);

		return this.db.insert("oneword", null, contentValues);

	}

	@Override
	public long save(int update_flag) {
		// super.save(update_flag);
		if (hasSelf(update_flag)) {
			return 0;
		}

		ContentValues contentValues = new ContentValues();
		contentValues.put("word", word);
		contentValues.put("weather", weather);
		contentValues.put("ge_hour", ge_hour);
		contentValues.put("le_hour", le_hour);
		contentValues.put("ge_week", ge_week);
		contentValues.put("le_week", le_week);
		contentValues.put("ge_month", ge_month);
		contentValues.put("le_month", le_month);
		contentValues.put("ge_temp", ge_temp);
		contentValues.put("le_temp", le_temp);
		contentValues.put("ge_aqi", ge_aqi);
		contentValues.put("le_aqi", le_aqi);
		long ret_id = -1;

		ret_id = this.db.insert("oneword", null, contentValues);
		// Log.e("DB isReadOnly", this.db.isReadOnly() == false ? "FALSE" : "TRUE");
		// Log.e("DB isOpen", this.db.isOpen() == false ? "FALSE" : "TRUE");
		// Log.e("DB ret_id", "" + ret_id);

		return ret_id;
	}

	public boolean hasSelf(int update_flag) {

		switch (update_flag) {
		case SQLITE_UPDATE_FORCE_ADD_FLAG:
			super.delete(String.format("word='%s' and weather='%s' "
					+ "and ge_hour=%d and le_hour=%d "
					+ "and ge_month=%d and le_month=%d "
					+ "and ge_week=%d and le_week=%d "
					+ "and ge_temp=%d and le_temp=%d "
					+ "and ge_aqi = %d and le_aqi = %d", word, weather,
					ge_hour, le_hour, ge_month, le_month, ge_week, le_week,
					ge_temp, le_temp, ge_aqi, le_aqi));

			return false;
		case SQLITE_UPDATE_DELETE_FLAG:
			super.delete(String.format("word='%s' and weather='%s' "
					+ "and ge_hour=%d and le_hour=%d "
					+ "and ge_month=%d and le_month=%d "
					+ "and ge_week=%d and le_week=%d "
					+ "and ge_temp=%d and le_temp=%d "
					+ "and ge_aqi = %d and le_aqi = %d", word, weather,
					ge_hour, le_hour, ge_month, le_month, ge_week, le_week,
					ge_temp, le_temp, ge_aqi, le_aqi));
			return true;
		case SQLITE_UPDATE_NORMAL_FLAG:
			break;
		case SQLITE_UPDATE_UPDATE_OLD_FLAG:
			break;
		default:
			break;
		}
		String tmp_filter = String.format("word='%s' and weather='%s' "
				+ "and ge_hour=%d and le_hour=%d "
				+ "and ge_month=%d and le_month=%d "
				+ "and ge_week=%d and le_week=%d "
				+ "and ge_temp=%d and le_temp=%d "
				+ "and ge_aqi = %d and le_aqi = %d", word, weather, ge_hour,
				le_hour, ge_month, le_month, ge_week, le_week, ge_temp,
				le_temp, ge_aqi, le_aqi);

		if (hasData(tmp_filter)) {
			return true;
		}
		return false;
	}

	public static List<Oneword> selectNowWeahter(Context context) {
		String filter = WeatherData.getNowFilter(context);
		return select(context, filter);
	}

	public static boolean hasModelCurWeather(Context context) {
		String filter = WeatherData.getNowFilter(context);
		SQLiteDatabase db = getDbFromContext(context);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(
					"select count(*) from oneword where " + filter, null);
			if (cursor.moveToNext()) {
				long count = cursor.getLong(0);
				if (count > 0) {
					if (cursor != null && !cursor.isClosed())
						cursor.close();
					// db.close();
					return true;
				}
			}

		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();

			// db.close();
		}

		return false;
	}

	public static Oneword getProper(Context context) {
		List<Oneword> lb = selectNowWeahter(context);
		if (lb.size() > 0) {
			return lb.get(new Random().nextInt(lb.size()));
		}
		return null;
	}

	@Override
	public void use(Context cxt) {
		SharedPreferences _sp = cxt.getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		Editor editor = _sp.edit();
		editor.putString(BabeConst.PIC_ONEWORD, this.word);
		editor.commit();
		// Log.e("PIC_ONEWORD", _sp.getString(BabeConst.PIC_ONEWORD, ""));

		// this.db.close();
	}

}
