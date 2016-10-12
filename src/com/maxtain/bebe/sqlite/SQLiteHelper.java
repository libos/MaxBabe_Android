package com.maxtain.bebe.sqlite;

import com.maxtain.bebe.util.BabeConst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	private SQLiteHelper(Context ctx) {
		super(ctx, BabeConst.SQLITE_DATABASE_NAME, null,
				BabeConst.SQLITE_DATABASE_VERSION);
	}

	private static SQLiteHelper mInstance = null;

	public static SQLiteHelper getInstance(Context ctx) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (mInstance == null) {
			mInstance = new SQLiteHelper(ctx.getApplicationContext());
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BabeConst.create_background_table);
		db.execSQL(BabeConst.create_figure_table);
		db.execSQL(BabeConst.create_oneword_table);
		db.execSQL(BabeConst.create_splash_table);
		db.execSQL(BabeConst.create_city_table);
//		Log.e("create", "创建数据库");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists background;");
		db.execSQL("drop table if exists figure;");
		db.execSQL("drop table if exists oneword;");
		db.execSQL("drop table if exists splash;");
		db.execSQL("drop table if exists city;");
		onCreate(db);
	}

}
