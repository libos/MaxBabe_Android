package com.maxtain.bebe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxtain.bebe.account.LoginActivity;
import com.maxtain.bebe.data.CityData;
import com.maxtain.bebe.data.PicsData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.maxtain.bebe.wxapi.ShareBoard;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareActivity extends Activity implements OnClickListener {

	private Button iv_share;
	private ImageView iv_cancel;
	private RelativeLayout iv_thumb;

	private ImageView canvas_bg;
	private ImageView canvas_fg;
	private TextView canvas_city;
	private TextView canvas_date;
	private TextView canvas_temp;
	private TextView canvas_oooo;
	private TextView canvas_weather;
	private TextView canvas_oneword;
	private TextView canvas_username;

	private UMSocialService mController = null;
	private UMImage resImage;
	private Bitmap bitmap;

	private String username;
	private String nickname;
	private String phone;
	private String sex;
	private SharedPreferences _sp;

	// private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		this.getActionBar().hide();
		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);

		mController = UMServiceFactory.getUMSocialService("com.umeng.share");

		configPlatforms();

		iv_thumb = (RelativeLayout) findViewById(R.id.canvas);
		canvas_bg = (ImageView) findViewById(R.id.canvas_background_img);
		canvas_fg = (ImageView) findViewById(R.id.canvas_figure_img);
		canvas_city = (TextView) findViewById(R.id.canvas_city);
		canvas_date = (TextView) findViewById(R.id.canvas_date);

		canvas_temp = (TextView) findViewById(R.id.canvas_temp);

		canvas_oooo = (TextView) findViewById(R.id.canvas_oooo);
		canvas_weather = (TextView) findViewById(R.id.canvas_weather);
		canvas_oneword = (TextView) findViewById(R.id.canvas_oneword);
		canvas_username = (TextView) findViewById(R.id.canvas_username);

		PicsData pics = PicsData.loadNowPics(getApplicationContext());
		change_image(pics.bpath, canvas_bg);
		change_image(pics.fpath, canvas_fg);
		canvas_bg.invalidate();
		canvas_fg.invalidate();
		Log.e("change image 1", pics.bpath);
		Log.e("change image 2", pics.fpath);
		canvas_oneword.setText(Babe.t2s(this, pics.words));
		CityData city = new CityData(getApplicationContext());
		city.loadCity();
		canvas_city.setText(Babe.t2s(this, city.city));

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);
		canvas_date.setText(Babe.t2s(this, year + "年" + month + "月" + date
				+ "日"));
		WeatherData wea = WeatherData.loadNowWeather(getApplicationContext());
		if (!StringUtils.isEmpty(wea.temp) && !wea.temp.equals("false")) {
			canvas_temp.setText(wea.temp);
		} else {
			canvas_temp.setText(wea.rtemp);
		}
		canvas_weather.setText(Babe.t2s(this, wea.weather));

		iv_share = (Button) findViewById(R.id.tv_share_btn);
		iv_share.setOnClickListener(this);

		iv_cancel = (ImageView) findViewById(R.id.iv_share_close);
		iv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View btn) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		username = _sp.getString(BabeConst.ACCOUNT_EMAIL, "");
		nickname = _sp.getString(BabeConst.ACCOUNT_NICKNAME, "");
		phone = _sp.getString(BabeConst.ACCOUNT_PHONE, "");
		canvas_username.setText("@"
				+ (StringUtils.isEmpty(this.nickname) ? "who" : this.nickname));
	}

	private void configPlatforms() {
		// mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// RenrenSsoHandler renrenSsoHandler = new
		// RenrenSsoHandler(getActivity(),
		// "201874", "28401c0964f04a72a14c812d6132fcef",
		// "3bf66e42db1e4fa9829b955cc300b737");
		// mController.getConfig().setSsoHandler(renrenSsoHandler);

		addWXPlatform();
		addQQQZonePlatform();

	}

	private void setShareContent() {

		// mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareImage(resImage);
		mController.setShareMedia(weixinContent);

		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareImage(resImage);
		mController.setShareMedia(circleMedia);

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareImage(resImage);
		mController.setShareMedia(qqShareContent);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareImage(resImage);
		mController.setShareMedia(sinaContent);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_share_btn:
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(nickname)
					|| StringUtils.isEmpty(phone)) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				return;
			}
			iv_thumb.setDrawingCacheEnabled(true);
			if (bitmap == null) {
				bitmap = iv_thumb.getDrawingCache();
				resImage = new UMImage(this, bitmap);
			}
			setShareContent();
			File file,
			f = null;
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				file = new File(
						android.os.Environment.getExternalStorageDirectory(),
						BabeConst.GALLERY_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				f = new File(file.getAbsolutePath() + file.separator
						+ "filename" + ".png");
			}
			try {
				FileOutputStream ostream = new FileOutputStream(f);
				bitmap.compress(CompressFormat.PNG, 60, ostream);
				ostream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ShareBoard shareBoard = new ShareBoard(this);
			shareBoard
					.setAnimationStyle(R.style.umeng_socialize_dialog_animations);
			shareBoard.showAtLocation(getWindow().getDecorView(),
					Gravity.BOTTOM, 0, 0);
			break;
		default:
			break;
		}
	}

	private void change_image(String path, ImageView img) {
		if (path.startsWith("drawable://")) {
			img.setImageResource(Integer.parseInt(path.substring(11)));
		} else {
			File imgFile = new File(path);
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());

				img.setImageBitmap(myBitmap);
			}
		}
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1104567255";
		String appKey = "5rCyMo4cwkA3qvXx";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");// filename
		qqSsoHandler.addToSocialSDK();

		// // 添加QZone平台
		// QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
		// appKey);
		// qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx1d3399ae82a092e5";
		String appSecret = "724bb1d966c5f256dfd135290e660dfd";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.showCompressToast(true);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
