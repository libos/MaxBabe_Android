package com.maxtain.bebe;

import java.io.File;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxtain.bebe.data.PicsData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.service.DataScheduleReceiver;
import com.maxtain.bebe.service.DownloadService;
import com.maxtain.bebe.service.RefreshService;
import com.maxtain.bebe.service.ServiceConst;
import com.maxtain.bebe.service.ServiceConst.SERVICE_STATUS;
import com.maxtain.bebe.sqlite.structure.Background;
import com.maxtain.bebe.sqlite.structure.Figure;
import com.maxtain.bebe.sqlite.structure.Oneword;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private DownloadService ds;
	private FragmentManager fragmentManager;
	private MainPageBody mbody;
	private MainPageBottom mbottom;
	private MainPageTitle mtitle;

	private DataScheduleReceiver thereciever = new DataScheduleReceiver();
	private ObjectMapper mapper;
	private SharedPreferences _sp;
	private ImageView backimg;
	private ImageView background_image_blur;
	private String reso;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.getActionBar().hide();

		AnalyticsConfig.enableEncrypt(true);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent.setDebugMode(true);

		UmengUpdateAgent.silentUpdate(getApplicationContext());
	UmengUpdateAgent.setUpdateCheckConfig(false);

		_sp = getApplicationContext().getSharedPreferences(
				BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE);
		backimg = (ImageView) findViewById(R.id.background_image);
		background_image_blur = (ImageView) findViewById(R.id.background_image_blur);
		reso = _sp.getString("reso", null);
		if (reso == null) {
			Display mDisplay = getWindowManager().getDefaultDisplay();
			int W = mDisplay.getWidth();
			if (W >= 1070) {
				reso = "xx";
			} else {
				reso = "x";
			}
			Editor _sp_editor = _sp.edit();
			_sp_editor.putString("reso", reso);
			_sp_editor.commit();
		}

		fragmentManager = getFragmentManager();
		mbody = (MainPageBody) fragmentManager
				.findFragmentById(R.id.id_fragment_body);
		mbottom = (MainPageBottom) fragmentManager
				.findFragmentById(R.id.id_fragment_bottom);
		mtitle = (MainPageTitle) fragmentManager
				.findFragmentById(R.id.id_fragment_title);

		mbody.disableAlarm();
		mapper = new ObjectMapper();
		// doBindService();

		IntentFilter download_filter = new IntentFilter(
				ServiceConst.NOTIFICATION);
		// filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(broadreceiver, download_filter);

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		// filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(thereciever, filter);

		Intent intent = new Intent();
		intent.setAction("com.maxtain.bebe.service.startup");
		sendBroadcast(intent);
		refreshForce();
		//
		if (_sp.getBoolean(BabeConst.IS_INIT_PROCESS, true)
				&& _sp.getBoolean(BabeConst.FAST_LOADDATA_MODE, false)) {

			WeatherData weat = WeatherData
					.loadNowWeather(getApplicationContext());
			weather_data_on_process(weat);
			PicsData pics = PicsData.loadNowPics(getApplicationContext());
			pics_data_on_process(pics);
		} else {
			_sp.edit().putBoolean(BabeConst.FAST_LOADDATA_MODE, true).commit();
		}
	}

	public void refreshForce() {
		Intent intent_refresh = new Intent(getApplicationContext(),
				RefreshService.class);

		startService(intent_refresh);
	}

	public void getNewWeather() {
		Intent intent = new Intent();
		intent.setAction("com.maxtain.bebe.service.startup");
		sendBroadcast(intent);

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				PicsData pics = PicsData.loadNowPics(getApplicationContext());
				pics_data_on_process(pics);
				break;
			case 2:
				break;
			default:
				break;
			}
		}
	};
	private long mExitTime;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, getResources().getString(R.string.quit),
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private BroadcastReceiver broadreceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int resultCode = bundle.getInt(ServiceConst.MRESULT);
				if (resultCode == RESULT_OK) {
					SERVICE_STATUS status = (SERVICE_STATUS) bundle
							.get(ServiceConst.MSTATUS);
					switch (status) {
					case SERVICE_CANCELED:
						Toast.makeText(MainActivity.this, "REFRESH FAILED",
								Toast.LENGTH_LONG).show();
						break;
					case SERVICE_WEATHER_DATA_STORED:

						WeatherData wea = WeatherData
								.loadNowWeather(getApplicationContext());
						weather_data_on_process(wea);
						new Thread(new Runnable() {
							@Override
							public void run() {
								Context cxt = getApplicationContext();

								// Background bk;
								try {
									Background.getProper(cxt).use(cxt);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// if (bk != null)
								// bk.use(cxt);
								// Figure fig =
								// Figure fg = null;
								try {
									Figure.getProper(cxt).use(cxt);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// if (fg != null)
								// fg.use(cxt);
								Oneword ow = Oneword.getProper(cxt);
								if (ow != null)
									ow.use(cxt);
								Message msgMessage = new Message();
								msgMessage.arg1 = 1;
								handler.sendMessage(msgMessage);
							}
						}).start();
						break;
					case SERVICE_BAIDU_WEATHER_DATA_STORED:
						WeatherData weax = WeatherData
								.loadNowWeather(getApplicationContext());
						weather_data_on_process(weax);
						break;
					case SERVICE_PICTURE_DATA_STORED:
						// PicsData pics = PicsData
						// .loadNowPics(getApplicationContext());
						// pics_data_on_process(pics);
						break;
					case SERVICE_PIC_BACKGROUND_DOWNLOAD_COMPLETE:
						// String nbackground_path = (String) bundle
						// .get(ServiceConst.MFILEPATH);
						// change_background(nbackground_path);
						// Log.e("cc",
						// "SERVICE_PIC_BACKGROUND_DOWNLOAD_COMPLETE");
						// append_new_background(nbackground_path);
						break;
					case SERVICE_PIC_FIGURE_DOWNLOAD_COMPLETE:
						// String nfigure_path = (String) bundle
						// .get(ServiceConst.MFILEPATH);
						// mbody.change_figure(nfigure_path);
						// append_new_figure(nfigure_path);
						break;
					case SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_BOTH:
						// PicsData pics0 = PicsData
						// .loadNowPics(getApplicationContext());
						// pics_data_on_process(pics0);
						break;
					case SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_FIGURE:
						// PicsData pics1 = PicsData
						// .loadNowPics(getApplicationContext());
						// pics_data_on_process(pics1, true, false);
						break;
					case SERVICE_PICTURE_DATA_STORED_AND_NEED_DOWN_BACKGROUND:
						// PicsData pics2 = PicsData
						// .loadNowPics(getApplicationContext());
						// pics_data_on_process(pics2, false, true);
						break;
					case SERVICE_NO_NETWORK_DOWN:
						Toast.makeText(MainActivity.this,
								Babe.t2s(getApplicationContext(), "请连接网络！"),
								Toast.LENGTH_LONG).show();
						break;
					default:
						break;
					}

				} else {
					Toast.makeText(MainActivity.this, "FAILED",
							Toast.LENGTH_LONG).show();
				}
			}
		}

	};

	private void append_new_figure(String nfigure_path) {
		PicsData pics = PicsData.loadNowPics(getApplicationContext());
		pics.fpath = nfigure_path;
		pics.savePics(getApplicationContext());
	}

	private void append_new_background(String nbackground_path) {
		PicsData pics = PicsData.loadNowPics(getApplicationContext());
		pics.bpath = nbackground_path;
		pics.savePics(getApplicationContext());
	}

	public void pics_data_process(String json) {
		PicsData pics = null;
		try {
			pics = mapper.readValue(json, PicsData.class);
			pics_data_on_process(pics);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void pics_data_on_process(PicsData pics) {
		// Log.d("ERROR", pics.state == null ? "STATE NULL" : "STATE NOT NULL");
		if (pics.state != "err") {
			String bpath = pics.bpath;
			change_background(bpath);
		}
		// if (pics.fgstate != "err") {
		mbody.setPics(pics);
		// }

		mbody.setWord(pics.words);
	}

	private void pics_data_on_process(PicsData pics, boolean background,
			boolean figure) {

		if (pics.state != "err") {
			if (figure)
				mbody.setPics(pics);

			mbody.setWord(pics.words);
			String bpath = pics.bpath;
			String fpath = pics.fpath;
			// Log.e("Bpaht", bpath);

			if (background)
				change_background(bpath);
			// Toast.makeText(
			// MainActivity.this,
			// "Download complete. Download URI: " + " \n" + bpath + " \n"
			// + fpath, Toast.LENGTH_LONG).show();
		} else {
			// Log.e("wocao", "asdf");
		}
	}

	public void change_background(final String path) {

		if (path.startsWith("drawable://")) {
			// backimg.setImageResource(Integer.parseInt(path
			// .substring(11)));
			background_image_blur.setImageResource(Integer.parseInt(path
					.substring(11)));
		} else {
			File imgFile = new File(path);
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				// backimg.setImageBitmap(myBitmap);
				background_image_blur.setImageBitmap(myBitmap);
			}
		}
		background_image_blur.setAlpha(0.0f);
		backimg.setAlpha(1.0f);
		background_image_blur.setVisibility(View.VISIBLE);
		backimg.animate().alpha(0.0f).setDuration(3000).start();
		background_image_blur.animate().alpha(1.0f).setStartDelay(1000)
				.setDuration(3000).setListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator arg0) {

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {
					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						// background_image_blur.setVisibility(View.INVISIBLE);
						if (path.startsWith("drawable://")) {
							backimg.setImageResource(Integer.parseInt(path
									.substring(11)));
						} else {
							File imgFile = new File(path);
							if (imgFile.exists()) {
								Bitmap myBitmap = BitmapFactory
										.decodeFile(imgFile.getAbsolutePath());
								backimg.setImageBitmap(myBitmap);
							}
						}
						backimg.setAlpha(1.0f);
					}

					@Override
					public void onAnimationCancel(Animator arg0) {
					}
				}).start();

	}

	public void weather_data_process(String json) {
		WeatherData weather = null;
		try {
			weather = mapper.readValue(json, WeatherData.class);
			weather_data_on_process(weather);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void weather_data_on_process(WeatherData weather) {
		if (weather.state != "err") {
			if (weather.has_alarm != 1) {
				mbody.disableAlarm();
			} else {
				mbody.setAlarm(weather);
			}
			mbody.setData(weather);
			mbottom.setData(weather);
			mtitle.changePublishTime();
			/*
			 * Toast.makeText(MainActivity.this,
			 * "Download complete. Download URI: " + weather.toString(),
			 * Toast.LENGTH_LONG).show();
			 */
		}
	}

	/*
	 * array('updatetime','temp','fengxiang','fenglevel','humidity','weather',
	 * 'aqi','day_weather','night_weather','day_temp','night_temp',
	 * 'next_day_weather','next_night_weather','next_day_temp',
	 * 'next_night_temp','weather_detail','has_alarm');
	 * 
	 * @see android.app.Activity#onResume()
	 */

	@Override
	protected void onDestroy() {
		// Log.d("aaa", "Reciever Destroyed");
		unregisterReceiver(thereciever);
		unregisterReceiver(broadreceiver);
		super.onDestroy();
	}

	void doBindService() {
		bindService(new Intent(this, DownloadService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			ds = ((DownloadService.DownloadBinder) binder).getService();
			Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			ds = null;
		}

	};

	public void callService() {

		if (ds != null) {
			// String json = ds.getData();
			// mbody.setData(json);
			// mbottom.setData(json);
			// Log.e("M", "ds going");

			// Toast.makeText(MainActivity.this, "" + json,
			// Toast.LENGTH_LONG)
			// .show();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}

}
