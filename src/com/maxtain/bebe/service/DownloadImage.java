package com.maxtain.bebe.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

class DownloadImage implements Runnable {
	private Context context;
	private String url;
	private String md5;
	private long id;
	private IImageDownloadListener mListener;
	private String category;
	public static final String CATEGORY_BACKGROUND = "_background_";
	public static final String CATEGORY_FIGURE = "_figure_";
	public static final String CATEGORY_SPLASH = "_splash_";

	public DownloadImage(Context context, String url, String md5, long id) {
		this.context = context;
		this.url = url;
		this.md5 = md5;
		this.id = id;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setListener(IImageDownloadListener listener) {
		mListener = listener;
	}

	Drawable DownloadDrawable(String url, String src_name)
			throws java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);
	}

	public String DownloadFile(String imageURL, String fileName)
			throws IOException {
		URL url = new URL(imageURL);

		File file = new File(this.context.getFilesDir(), fileName);

		long startTime = System.currentTimeMillis();
		// Log.d("DownloadFile", "Begin Download URL: " + url + " Filename: "
				// + fileName);
		URLConnection ucon = url.openConnection();
		// ucon.setRequestProperty("Referer", "referer.maxtain.com");
		InputStream is = ucon.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayBuffer baf = new ByteArrayBuffer(1024);
		int current = 0;
		while ((current = bis.read()) != -1)
			baf.append((byte) current);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(baf.toByteArray());
		fos.close();
		// Log.d("DownloadFile",
		// 		"File was downloaded in: "
		// 				+ ((System.currentTimeMillis() - startTime) / 1000)
		// 				+ "s");
		return file.getAbsolutePath();
	}

	@Override
	public void run() {
		String ret = "0";
		try {
			ret = DownloadFile(this.url, "_" + this.md5 + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		mListener.imageDownloadDoneCallback(new BasicNameValuePair(this.md5,
				ret), this.id, this.category);

	}

}