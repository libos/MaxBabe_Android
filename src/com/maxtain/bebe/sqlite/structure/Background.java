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

public class Background extends Model {
	public int id;
	public String filename;
	public String path;
	public String md5;
	public String weather;
	public int download;
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
	private Context context;
	private SQLiteDatabase db;

	private WeatherData curWeather;

	public Background(Context context) {
		super("background", context, getDbFromContext(context));
		this.context = context;
		this.db = getDbFromContext(context);
	}

	public Background(Context context, SQLiteDatabase db) {
		super("background", context, db);
		this.context = context;
		this.db = db;
	}

	public Background(Context context, SQLiteDatabase db, int id,
			String filename, String path, String md5, String weather) {
		super("background", context, db);
		this.context = context;
		this.db = db;
		this.id = id;
		this.filename = filename;
		this.path = path;
		this.md5 = md5;
		this.weather = weather;
	}

	public static List<Background> select(Context context, String filter) {
		SQLiteDatabase tmpdb = getDbFromContext(context);

		List<Background> lb = new ArrayList<Background>();
		Cursor cursor = null;
		try {
			cursor = tmpdb.rawQuery("select * from background where " + filter,
					null);

			while (cursor.moveToNext()) {
				lb.add(new Background(context, tmpdb, cursor.getInt(0), cursor
						.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(5)));
				// Log.e("slelct", cursor.getInt(4) + ";" + cursor.getInt(6) +
				// ";"
				// + cursor.getInt(7) + ";" + cursor.getInt(8));
			}

		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			// tmpdb.close();
		}

		return lb;
	}

	public static List<Background> select_test(Context context) {
		SQLiteDatabase tmpdb = getDbFromContext(context);

		List<Background> lb = new ArrayList<Background>();
		Cursor cursor = null;
		try {
			cursor = tmpdb.rawQuery("select * from background ", null);
			while (cursor.moveToNext()) {
				lb.add(new Background(context, tmpdb, cursor.getInt(0), cursor
						.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(5)));

			}
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			// tmpdb.close();
		}
		for (Background back : lb) {
			Log.e("List", back.filename);
		}
		Log.e("List", lb.toString());
		if (lb.size() > 0)
			Log.e("List", lb.get(0).path);
		Log.e("ListSize", "" + lb.size());
		return lb;
	}

	@Override
	public long save_nofile() { // do it after chech hasSelf()
		ContentValues contentValues = new ContentValues();
		contentValues.put("filename", filename);
		contentValues.put("md5", md5);
		contentValues.put("path", "");
		contentValues.put("download", 0);
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
		// Log.e("bk_no_file", filename + ";" + md5 + ";" + download + ";"
		// + created + ";" + path + ";" + weather + ";" + ge_hour + ";"
		// + le_hour + ";" + le_temp + ";" + ge_temp + ";" + ge_week + ";"
		// + le_week + ";" + ge_month + ";" + le_month + ";" + ge_aqi
		// + ";" + le_aqi);
		return this.db.insert("background", null, contentValues);

	}

	@Override
	public long save(int update_flag) {
		// super.save(update_flag);
		// Log.e("back", filename + "  " + md5);
		if (hasSelf(update_flag)) {
			return 0;
		}

		ContentValues contentValues = new ContentValues();
		contentValues.put("filename", filename);
		contentValues.put("path", path);
		contentValues.put("md5", md5);
		contentValues.put("download", download);
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

		ret_id = this.db.insert("background", null, contentValues);
		// Log.e("DB isReadOnly", this.db.isReadOnly() == false ? "FALSE" :
		// "TRUE");
		// Log.e("DB isOpen", this.db.isOpen() == false ? "FALSE" : "TRUE");
		// Log.e("DB ret_id", "" + ret_id);
		// this.db.close();
		return ret_id;
	}

	public boolean hasSelf(int update_flag) {

		switch (update_flag) {
		case SQLITE_UPDATE_FORCE_ADD_FLAG:
			super.delete(String.format("md5='%s' and weather='%s' "
					+ "and ge_hour=%d and le_hour=%d "
					+ "and ge_month=%d and le_month=%d "
					+ "and ge_week=%d and le_week=%d "
					+ "and ge_temp=%d and le_temp=%d "
					+ "and ge_aqi = %d and le_aqi = %d", md5, weather, ge_hour,
					le_hour, ge_month, le_month, ge_week, le_week, ge_temp,
					le_temp, ge_aqi, le_aqi));

			return false;
		case SQLITE_UPDATE_DELETE_FLAG:
			super.delete(String.format("md5='%s' and weather='%s' "
					+ "and ge_hour=%d and le_hour=%d "
					+ "and ge_month=%d and le_month=%d "
					+ "and ge_week=%d and le_week=%d "
					+ "and ge_temp=%d and le_temp=%d "
					+ "and ge_aqi = %d and le_aqi = %d", md5, weather, ge_hour,
					le_hour, ge_month, le_month, ge_week, le_week, ge_temp,
					le_temp, ge_aqi, le_aqi));
			return true;
		case SQLITE_UPDATE_NORMAL_FLAG:
			break;
		case SQLITE_UPDATE_UPDATE_OLD_FLAG:
			break;
		default:
			break;
		}
		String tmp_filter = String.format("md5='%s' and weather='%s' "
				+ "and ge_hour=%d and le_hour=%d "
				+ "and ge_month=%d and le_month=%d "
				+ "and ge_week=%d and le_week=%d "
				+ "and ge_temp=%d and le_temp=%d "
				+ "and ge_aqi = %d and le_aqi = %d and download=1", md5,
				weather, ge_hour, le_hour, ge_month, le_month, ge_week,
				le_week, ge_temp, le_temp, ge_aqi, le_aqi);

		if (hasData(tmp_filter)) {
			return true;
		}
		return false;
	}

	public void getSelf() {
		String tmp_filter = String.format("md5='%s' and weather='%s' "
				+ "and ge_hour=%d and le_hour=%d "
				+ "and ge_month=%d and le_month=%d "
				+ "and ge_week=%d and le_week=%d "
				+ "and ge_temp=%d and le_temp=%d "
				+ "and ge_aqi = %d and le_aqi = %d", md5, weather, ge_hour,
				le_hour, ge_month, le_month, ge_week, le_week, ge_temp,
				le_temp, ge_aqi, le_aqi);
		Cursor cursor = null;
		try {
			cursor = this.db.rawQuery("select * from background where "
					+ tmp_filter, null);
			// Log.e("da", "what hell happend");
			if (cursor.moveToNext()) {
				this.filename = cursor.getString(1);
				this.path = cursor.getString(2);
				// Log.e("da", this.path);
			}
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}

	}

	public static List<Background> selectNowWeahter(Context context) {
		String filter = " (download = 1) and "
				+ WeatherData.getNowFilter(context);
		return select(context, filter);
	}

	public static boolean hasModelCurWeather(Context context) {
		String filter = WeatherData.getNowFilter(context);
		SQLiteDatabase db = getDbFromContext(context);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select count(*) from background where "
					+ filter, null);
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

	public static Background getProper(Context context) {
		List<Background> lb = selectNowWeahter(context);
		if (lb.size() > 0) {
			int tmp = new Random().nextInt(lb.size());
			// Log.e("new Random().nextInt(lb.size())", tmp + "");
			return lb.get(tmp);
		}
		return null;
	}

	@Override
	public void use(Context cxt) {
		SharedPreferences _sp = cxt.getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		Editor editor = _sp.edit();
		editor.putString(BabeConst.PIC_BACKGROUND, this.filename);
		editor.putString(BabeConst.PIC_BACKGROUND_MD5, this.md5);
		editor.putString(BabeConst.PIC_BACKGROUND_PATH, this.path);
		editor.commit();
		// Log.e("PIC_BACKGROUND1", _sp.getString(BabeConst.PIC_BACKGROUND,
		// ""));
		// Log.e("PIC_BACKGROUND2",
		// _sp.getString(BabeConst.PIC_BACKGROUND_MD5, ""));
		// Log.e("PIC_BACKGROUND3",
		// _sp.getString(BabeConst.PIC_BACKGROUND_PATH, "x"));
		// this.db.close();
	}

	public static void updatefile(Context context, long id, String filepath,
			String md5) {
		SQLiteDatabase tmpdb = getDbFromContext(context);
		String sql = "update background set path='" + filepath + "'"
				+ " where id =" + id;
		// Log.e("sql--", sql);
		// try {
		tmpdb.execSQL(sql);

		sql = "update background set download=1 where id =" + id;

		tmpdb.execSQL(sql);

		// List<Background> bk = select(context, "id = " + id);

		// for (Background background : bk) {
		// Log.e("bk", background.filename + ";" + background.md5 + ";"
		// + background.download + ";" + background.created + ";"
		// + background.path + ";" + background.weather + ";"
		// + background.ge_hour + ";" + background.le_hour + ";"
		// + background.le_temp + ";" + background.ge_temp + ";"
		// + background.ge_week + ";" + background.le_week + ";"
		// + background.ge_month + ";" + background.le_month + ";"
		// + background.ge_aqi + ";" + background.le_aqi);
		// }

		// } finally {
		// tmpdb.close();
		// }

	}

}
