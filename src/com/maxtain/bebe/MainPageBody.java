package com.maxtain.bebe;

import java.io.File;

import android.R.anim;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxtain.bebe.data.PicsData;
import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.pulltorefresh.PtrFrameLayout;
import com.maxtain.bebe.pulltorefresh.PtrHandler;
import com.maxtain.bebe.pulltorefresh.header.MaterialHeader;
import com.maxtain.bebe.pulltorefresh.util.PtrLocalDisplay;
import com.maxtain.bebe.util.AnimationSound;
import com.maxtain.bebe.util.Babe;
import com.maxtain.bebe.util.BabeConst;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class MainPageBody extends Fragment {
	private ImageView iv_mascot;
	private RelativeLayout alarm_info;

	private RelativeLayout iv_mascot_layout;
	private SharedPreferences _sp;
	protected PtrFrameLayout mPtrFrameLayout;
	private long mStartLoadingTime = -1;
	private boolean mImageHasLoaded = false;
	private ImageView iv_mascot_anim;
	private AnimationSound pu_audio;
	private TextView tv_tip;
	private Context cxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_page_body,
				container, false);
		this.cxt = getActivity().getApplicationContext();
		_sp = this.cxt.getSharedPreferences(BabeConst.SHAREP_DATABASE,
				Activity.MODE_PRIVATE);
		// boolean hasPermanentMenuKey = ViewConfiguration.get(this.cxt)
		// .hasPermanentMenuKey();
		// boolean hasVirtualKeys = !hasPermanentMenuKey;
		// String message = hasVirtualKeys ? "This device has virtual menu keys"
		// : "This device has physical keys";

		animimg = (ImageView) view.findViewById(R.id.iv_mascot_anim);

		iv_mascot = (ImageView) view.findViewById(R.id.iv_mascot);
		alarm_info = (RelativeLayout) view.findViewById(R.id.layout_info);
		iv_mascot_layout = (RelativeLayout) view
				.findViewById(R.id.iv_mascot_layout);

		tv_tip = (TextView) view.findViewById(R.id.tv_tip);
		mPtrFrameLayout = (PtrFrameLayout) view
				.findViewById(R.id.material_style_ptr_frame);
		// if (hasPermanentMenuKey) {
		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.setMargins(0, 20, 0, 0);
		// iv_mascot_layout.setLayoutParams(lp);
		// }
		// header
		mPtrFrameLayout.post(new Runnable() {

			@Override
			public void run() {
				final MaterialHeader header = new MaterialHeader(getActivity());
				int[] colors = getResources()
						.getIntArray(R.array.google_colors);
				header.setColorSchemeColors(colors);
				header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
				header.setPadding(0, PtrLocalDisplay.dp2px(15), 0,
						PtrLocalDisplay.dp2px(10));
				header.setPtrFrameLayout(mPtrFrameLayout);

				mPtrFrameLayout.setLoadingMinTime(1000);
				mPtrFrameLayout.setDurationToCloseHeader(1500);
				mPtrFrameLayout.setHeaderView(header);
				mPtrFrameLayout.addPtrUIHandler(header);
				mPtrFrameLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPtrFrameLayout.autoRefresh(false);
					}
				}, 100);
				final Handler handler = new Handler();
				mPtrFrameLayout.setPtrHandler(new PtrHandler() {
					@Override
					public boolean checkCanDoRefresh(PtrFrameLayout frame,
							View content, View header) {
						return true;
					}

					@Override
					public void onRefreshBegin(final PtrFrameLayout frame) {
						if (mImageHasLoaded) {

							long delay = (long) (1000 + Math.random() * 2000);
							delay = Math.max(0, delay);
							delay = 0;
							// Log.e("pull", "1");
							try {
								((MainActivity) getActivity()).refreshForce();
								((MainActivity) getActivity()).getNewWeather();
							} catch (Exception e) {
								e.printStackTrace();
							}
							frame.postDelayed(new Runnable() {
								@Override
								public void run() {

									frame.refreshComplete();
								}
							}, delay);
						} else {// 別刷了，我都來不及變身了
							// Toast.makeText(getActivity(), "等等等...",
							// Toast.LENGTH_LONG)
							// .show();
							// Log.e("pull", "2");

							mStartLoadingTime = System.currentTimeMillis();

							// imageView.loadImage(imageLoader, mUrl);
						}
					}
				});
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						long delay = Math.max(
								0,
								1000 - (System.currentTimeMillis() - mStartLoadingTime));
						delay = 0;
						// Log.e("pull", "3");
						mImageHasLoaded = true;
						mPtrFrameLayout.postDelayed(new Runnable() {
							@Override
							public void run() {
								mPtrFrameLayout.refreshComplete();
							}
						}, delay);
					}
				}, 1000);

			}
		});

		animimg.post(new Runnable() {

			@Override
			public void run() {
				frameAnimation = (AnimationDrawable) animimg.getDrawable();
			}
		});
		return view;
	}

	public void setData(WeatherData data) {

	}

	public void setAlarm(WeatherData weather) {

	}

	public void disableAlarm() {
		alarm_info.setVisibility(View.INVISIBLE);

	}

	private int stop_fg = 0;

	public void setWord(final String words) {
		if (words != null) {
			if (stop_fg == 0) {
				stop_fg = 1;
				tv_tip.animate().alpha(0.0f).translationY(100.0f)
						.setDuration(600).setListener(new AnimatorListener() {

							@Override
							public void onAnimationStart(Animator arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animator arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animator arg0) {
								tv_tip.animate().alpha(1.0f).translationY(0.0f)
										.setDuration(600)
										.setListener(new AnimatorListener() {

											@Override
											public void onAnimationStart(
													Animator arg0) {
												tv_tip.setText(Babe.t2s(cxt,
														words).replace("<br>",
														"\n"));

											}

											@Override
											public void onAnimationRepeat(
													Animator arg0) {

											}

											@Override
											public void onAnimationEnd(
													Animator arg0) {
												stop_fg = 2;
											}

											@Override
											public void onAnimationCancel(
													Animator arg0) {
												// TODO Auto-generated method
												// stub

											}
										}).start();

							}

							@Override
							public void onAnimationCancel(Animator arg0) {
								// TODO Auto-generated method stub

							}
						}).start();

			}
		}
	}

	private AlphaAnimation change_Alpha;
	private AlphaAnimation showNew_Alpha;
	private AlphaAnimation bomb_dismiss_Alpha;
	private ImageView animimg;
	private AnimationDrawable frameAnimation;

	private void setImage(final String path, final ImageView img) {
		if (stop_fg != 2)
			return;
		if (frameAnimation == null)
			return;

		// if (animimg != null) {
		// ((AnimationDrawable) animimg.getDrawable()).().recycle();
		// }

		// if (frameAnimation != null) {
		// frameAnimation.stop();
		// for (int i = 0; i < frameAnimation.getNumberOfFrames(); ++i) {
		// Drawable frame = frameAnimation.getFrame(i);
		// if (frame instanceof BitmapDrawable) {
		// ((BitmapDrawable) frame).getBitmap().recycle();
		// }
		// frame.setCallback(null);
		// }
		// frameAnimation.setCallback(null);
		// }
		animimg.setVisibility(View.VISIBLE);
		// animimg.setImageResource(R.anim.bome2);

		stop_fg = 0;
		// audio
		// pu_audio = new AnimationSound(getActivity().getApplicationContext(),
		// R.raw.pu);

		frameAnimation.setOneShot(true);
		frameAnimation.setVisible(true, true);
		// frameAnimation.setCallback(animimg);

		change_Alpha = new AlphaAnimation(1.0f, 0.0f);
		change_Alpha.setDuration(50);
		change_Alpha.setStartOffset(0);
		showNew_Alpha = new AlphaAnimation(0.0f, 1.0f);

		showNew_Alpha.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
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

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub

			}
		});
		showNew_Alpha.setDuration(10);
		showNew_Alpha.setStartOffset(500);
		AnimationSet mainimage_sxa = new AnimationSet(false);
		mainimage_sxa.addAnimation(change_Alpha);
		mainimage_sxa.addAnimation(showNew_Alpha);
		AnimationSet bome_sxa = new AnimationSet(false);
		// bomb_open_Alpha = new AlphaAnimation(0.0f, 1.0f);
		bomb_dismiss_Alpha = new AlphaAnimation(1.0f, 0.0f);
		bomb_dismiss_Alpha.setDuration(50);
		bomb_dismiss_Alpha.setStartOffset(1100);
		bomb_dismiss_Alpha.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				animimg.setVisibility(View.INVISIBLE);
				// pu_audio.stopsound();
			}
		});
		frameAnimation.start();
		// pu_audio.startsound(false);
		bome_sxa.addAnimation(bomb_dismiss_Alpha);
		animimg.startAnimation(bome_sxa);
		img.startAnimation(mainimage_sxa);

	}

	public void change_figure(String fpath) {
		if (fpath != null) {
			setImage(fpath, iv_mascot);
		}
	}

	public void setPics(PicsData pics) {
		String fpath = pics.fpath;// _sp.getString(pics.fmd5, null);
		if (fpath != null) {
			setImage(fpath, iv_mascot);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName()); // 统计时长

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		System.gc();
	}
}
