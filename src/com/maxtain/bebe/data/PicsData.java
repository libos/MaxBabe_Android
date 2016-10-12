package com.maxtain.bebe.data;

import com.maxtain.bebe.sqlite.structure.Background;
import com.maxtain.bebe.sqlite.structure.Figure;
import com.maxtain.bebe.sqlite.structure.Oneword;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.util.PhpMD5;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PicsData {

	private Context context;

	public String state;
	public long bstore_id = 0;
	public long fstore_id = 0;

	public String background;
	public String bmd5;
	public String bpath;
	public String bweather = "";
	public int bgehour;
	public int blehour;
	public int bgemonth;
	public int blemonth;
	public int bgeweek;
	public int bleweek;
	public int bgetemp;
	public int bletemp;
	public int bgeaqi;
	public int bleaqi;
	public int bupdate;

	public String figure;
	public String fmd5;
	public String fweather = "";
	public String fpath;
	public int fgehour;
	public int flehour;
	public int fgemonth;
	public int flemonth;
	public int fgeweek;
	public int fleweek;
	public int fgetemp;
	public int fletemp;
	public int fgeaqi;
	public int fleaqi;
	public int fupdate;

	public String words;
	public String oweather = "";
	public int ogehour;
	public int olehour;
	public int ogemonth;
	public int olemonth;
	public int ogeweek;
	public int oleweek;
	public int ogetemp;
	public int oletemp;
	public int ogeaqi;
	public int oleaqi;
	public int oupdate;

	public boolean needDownBackground = false;
	public boolean needDownFigure = false;
	public boolean needUpdateOneword = false;
	public boolean needUpdateSplash = false;

	public String fgstate;

	public void xsetContext(Context context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "State=" + state + " Pic [background=" + background
				+ ", figure=" + figure + ", " + "words" + words + "bmd5="
				+ bmd5 + "fmd5=" + fmd5;
	}

	public void savePics(Context context) {
		this.context = context;
		Background background = buildBackground();
		Figure figure = buildFigure();
		Oneword oneword = buildOneword();
		needDownBackground = !background.hasSelf(bupdate);
		needDownFigure = !figure.hasSelf(fupdate);
		needUpdateOneword = !oneword.hasSelf(oupdate);

		if (this.background == null) {
			needDownBackground = false;
		}
		if (this.figure == null) {
			needDownFigure = false;
		}
		if (this.words == null) {
			needUpdateOneword = false;
		}
		// Log.e("data", needDownBackground == false ? "FALSE" : "TRUE");
		// needUpdateSplash = checkSplash();
		if (needDownBackground) {

			bstore_id = background.save_nofile();
			// background = Background.getProper(context);
			// if (background != null) {
			// background.use();
			// }
		} else {
			// if (this.background != null) {
			// // Log.e("data", "here we are");
			// background.getSelf();
			// background.use();
			// } else {
			// background = Background.getProper(context);
			// if (background != null) {
			// background.use();
			// }
			// }

		}
		if (needDownFigure) {
			// Log.e("data", "hereFigure");
			fstore_id = figure.save_nofile();
			// figure = Figure.getProper(context);
			// if (figure != null) {
			// figure.use();
			// }
		} else {
			// if (this.figure != null) {
			// figure.getSelf();
			// figure.use();
			// } else {
			// figure = Figure.getProper(context);
			// if (figure != null) {
			// figure.use();
			// }
			// }
		}

		if (needUpdateOneword) {
			oneword.save(0);
		} else {
			// if (this.words == null) {
			// oneword = Oneword.getProper(context);
			// } else {
			// Log.e("s", "asdf33   " + oneword.word);
			// }
		}
		// oneword.use();
		// SharedPreferences _sp = context.getSharedPreferences(
		// BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		// Editor _sp_editor = _sp.edit();
		// _sp_editor.putString(BabeConst.PIC_BACKGROUND, background.filename);
		// _sp_editor.putString(BabeConst.PIC_BACKGROUND_MD5, background.md5);
		// _sp_editor.putString(BabeConst.PIC_BACKGROUND_PATH, background.path);
		//
		// _sp_editor.putString(BabeConst.PIC_FIGURE, figure.filename);
		// _sp_editor.putString(BabeConst.PIC_FIGURE_MD5, figure.md5);
		// _sp_editor.putString(BabeConst.PIC_FIGURE_PATH, figure.path);
		//
		// _sp_editor.putString(BabeConst.PIC_ONEWORD, oneword.word);
		//
		// _sp_editor.commit();

	}

	public static PicsData loadNowPics(Context context) {
		PicsData tmp = new PicsData();
		SharedPreferences _sp = context.getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		tmp.background = _sp.getString(BabeConst.PIC_BACKGROUND, null);
		tmp.figure = _sp.getString(BabeConst.PIC_FIGURE, null);
		if (tmp.background == null) {
			tmp.state = "err";
		}
		if (tmp.figure == null) {
			tmp.fgstate = "err";
		}
		tmp.bmd5 = _sp.getString(BabeConst.PIC_BACKGROUND_MD5, null);
		tmp.bpath = _sp.getString(BabeConst.PIC_BACKGROUND_PATH, null);

		tmp.fmd5 = _sp.getString(BabeConst.PIC_FIGURE_MD5, null);
		tmp.fpath = _sp.getString(BabeConst.PIC_FIGURE_PATH, null);
		tmp.words = _sp.getString(BabeConst.PIC_ONEWORD, null);

		// Log.e("A LOTOF DATA", tmp.words == null ? "tur" : "fla");
		return tmp;
	}

	public Background buildBackground() {
		Background bg = new Background(this.context);
		bg.filename = this.background;
		// bg.path = "drawable://" + drawable_id;
		// bg.download = 0;
		bg.md5 = this.bmd5;
		bg.weather = this.bweather;
		bg.ge_hour = this.bgehour;
		bg.le_hour = this.blehour;
		bg.ge_month = this.bgemonth;
		bg.le_month = this.blemonth;
		bg.ge_week = this.bgeweek;
		bg.le_week = this.bleweek;
		bg.ge_temp = this.bgetemp;
		bg.le_temp = this.bletemp;
		bg.ge_aqi = this.bgeaqi;
		bg.le_aqi = this.bleaqi;
		return bg;
	}

	public Figure buildFigure() {
		Figure fg = new Figure(this.context);
		fg.filename = this.figure;
		// fg.path = "drawable://" + drawable_id;
		// fg.download = 0;
		fg.md5 = this.fmd5;
		fg.weather = this.fweather;
		fg.ge_hour = this.fgehour;
		fg.le_hour = this.flehour;
		fg.ge_month = this.fgemonth;
		fg.le_month = this.flemonth;
		fg.ge_week = this.fgeweek;
		fg.le_week = this.fleweek;
		fg.ge_temp = this.fgetemp;
		fg.le_temp = this.fletemp;
		fg.ge_aqi = this.fgeaqi;
		fg.le_aqi = this.fleaqi;
		return fg;
	}

	public Oneword buildOneword() {
		Oneword ow = new Oneword(this.context);
		ow.word = this.words.trim();
		ow.weather = this.oweather;
		ow.ge_hour = this.ogehour;
		ow.le_hour = this.olehour;
		ow.ge_month = this.ogemonth;
		ow.le_month = this.olemonth;
		ow.ge_week = this.ogeweek;
		ow.le_week = this.oleweek;
		ow.ge_temp = this.ogetemp;
		ow.le_temp = this.oletemp;
		ow.ge_aqi = this.ogeaqi;
		ow.le_aqi = this.oleaqi;
		return ow;
	}
}
