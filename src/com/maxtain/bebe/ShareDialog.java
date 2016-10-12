package com.maxtain.bebe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private Context gloabal_context;
	private RelativeLayout canvas;
	private ImageView canvas_img;
	private TextView canvas_text;

	private Button iv_share;
	private Button iv_cancel;
	private SharedPreferences _sp;

	public ShareDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public ShareDialog(Context context, int theme, Context ctx) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.gloabal_context = ctx;
		_sp = ctx.getSharedPreferences(BabeConst.SHAREP_DATABASE,
				Activity.MODE_PRIVATE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_share);
		iv_share = (Button) findViewById(R.id.tv_share_confirm);
		iv_share.setOnClickListener(this);
		iv_cancel = (Button) findViewById(R.id.tv_share_cancel);
		iv_cancel.setOnClickListener(this);
		canvas = (RelativeLayout) findViewById(R.id.canvas);
		canvas_img = (ImageView) findViewById(R.id.canvas_img);
		canvas_text = (TextView) findViewById(R.id.canvas_text);

		Log.d("as", "Asdfas");
		String path = _sp.getString(BabeConst.PIC_FIGURE_PATH, "");
		Bitmap bm = null;
		if (path.startsWith("drawable://")) {
			bm = BitmapFactory.decodeResource(
					this.gloabal_context.getResources(),
					Integer.parseInt(path.substring(11)));
		} else {
			File imgFile = new File(path);
			if (imgFile.exists()) {
				bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			}
		}
		canvas_img.setImageBitmap(bm);
		canvas_text.setText(Babe.t2s(context,
				_sp.getString(BabeConst.PIC_ONEWORD, "")));

		canvas.setDrawingCacheEnabled(true);

		// Bitmap bitmap = canvas.getDrawingCache();
		// File file, f = null;
		// if (android.os.Environment.getExternalStorageState().equals(
		// android.os.Environment.MEDIA_MOUNTED)) {
		// file = new File(
		// android.os.Environment.getExternalStorageDirectory(),
		// "TTImages_cache");
		// if (!file.exists()) {
		// file.mkdirs();
		//
		// }
		// f = new File(file.getAbsolutePath() + file.separator + "filename"
		// + ".png");
		// }
		// FileOutputStream ostream;
		// try {
		// ostream = new FileOutputStream(f);
		// bitmap.compress(CompressFormat.PNG, 10, ostream);
		// ostream.close();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_share_confirm:
			break;
		case R.id.tv_share_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}

}
