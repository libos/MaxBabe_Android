package com.maxtain.bebe.sqlite.structure;

import java.util.ArrayList;
import java.util.List;

import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.sqlite.SQLiteHelper;
import com.maxtain.bebe.util.BabeConst;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Model {
	public static final int SQLITE_UPDATE_NORMAL_FLAG = 0x00;
	public static final int SQLITE_UPDATE_DELETE_FLAG = 0x01;
	public static final int SQLITE_UPDATE_FORCE_ADD_FLAG = 0x02;
	public static final int SQLITE_UPDATE_UPDATE_OLD_FLAG = 0x03;
	private SQLiteDatabase db;
	private Context context;
	private String tablename;

	public Model() {
	}

	public Model(String tablename, Context context, SQLiteDatabase db) {
		this.tablename = tablename;
		this.context = context;
		this.db = db;
	}

	public boolean update(String setter, String filter) {
		String sql = "update " + this.tablename + " " + setter + " where "
				+ filter;
		this.db.execSQL(sql);
		return true;

	}

	public boolean delete(String filter) {
		this.db.execSQL("delete from " + this.tablename + " where " + filter);
		return true;
	}

	public boolean delete(int id) {
		this.db.execSQL("delete from " + this.tablename + " where id = " + id);
		return true;
	}

	public boolean hasData(String filter) {
		Cursor cursor = null;
		try {
			cursor = this.db.rawQuery("select count(*) from " + this.tablename
					+ " where " + filter, null);
			if (cursor.moveToNext()) {
				long count = cursor.getLong(0);
				if (count > 0) {
					if (cursor != null && !cursor.isClosed())
						cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return false;
	}

	public static SQLiteDatabase getDbFromContext(Context context) {
		SQLiteHelper sqlhelper = SQLiteHelper.getInstance(context);
		;
		SQLiteDatabase tmpdb = sqlhelper.getWritableDatabase();

		return tmpdb;
	}

	public long save(int update_flag) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long save_nofile() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void use(Context cxt) {
		// TODO Auto-generated method stub

	}

}
