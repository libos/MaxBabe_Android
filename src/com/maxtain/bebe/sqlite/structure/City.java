package com.maxtain.bebe.sqlite.structure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class City extends Model {
	private SQLiteDatabase db;
	private Context context;
	public String name;
	public String pinyin;
	public String level2;
	public String province;

	public City(Context cxt) {
		super("city", cxt, getDbFromContext(cxt));
		this.context = cxt;
		this.db = getDbFromContext(cxt);
	}

	public City(Context context, SQLiteDatabase db) {
		super("city", context, db);
		this.context = context;
		this.db = db;
	}

	public City(Context context, SQLiteDatabase db, String name, String pinyin,
			String level2, String province) {
		super("city", context, db);
		this.context = context;
		this.db = db;
		this.name = name;
		this.pinyin = pinyin;
		this.level2 = level2;
		this.province = province;
	}

	public static List<City> select(Context context, String filter) {
		SQLiteDatabase tmpdb = getDbFromContext(context);
		Cursor cursor = null;
		List<City> lb = new ArrayList<City>();
		try {
			cursor = tmpdb.rawQuery("select * from city where " + filter, null);

			// Log.e("sqlcity", "select * from city where " + filter);
			while (cursor.moveToNext()) {
				lb.add(new City(context, tmpdb, cursor.getString(1), cursor
						.getString(2), cursor.getString(3), cursor.getString(4)));

			}
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}

		return lb;
	}

	public static List<City> auto(Context context, String part) {
		part = part.replaceAll("'", "").replaceAll("\"", "");
		String likepart = part + "%";
		String filter = String.format(
				"( name like '%s' ) or ( pinyin like '%s' )"
						+ "or ( level2 like '%s' ) or ( province like '%s' ) "
						+ "or ( '%s' like '%s' || name || '%s' ) "
						+ "or ( '%s' like '%s' || pinyin || '%s' ) limit 5",
				likepart, likepart, likepart, likepart, likepart, '%', '%',
				likepart, '%', '%');
		List<City> list = select(context, filter);
		return list;
	}

	public static List<String> auto_complete(Context context, String part) {
		List<City> cities = auto(context, part);
		List<String> citiesList = new ArrayList<String>();
		int city_size = cities.size();
		for (int iter = 0; iter < city_size; iter++) {
			citiesList
					.add(cities.get(iter).name + ":"
							+ cities.get(iter).province + ":"
							+ cities.get(iter).level2);
		}
		return citiesList;
	}
}
