package com.maxtain.bebe;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxtain.bebe.data.WeatherData;
import com.maxtain.bebe.init.CategorySet;
import com.maxtain.bebe.util.AqiDetail;
import com.maxtain.bebe.util.Babe;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class MainPageBottom extends Fragment {

	private ImageView id_mai_icon;
	private TextView id_mai_aqi;
	private TextView tv_weather;
	private TextView tv_weather_temp;

	private LinearLayout ll_last_level;
	private TextView tv_tempreture;
	private Context ctx;
	private RelativeLayout layout_info;
	private RelativeLayout rl_temp;
	private TextView tv_hightemp;
	private TextView tv_lowtemp;

	private TextView humi_level;
	private TextView wind_level;

	private TextView tv_commo;
	private LinearLayout ll_third_level;
	private LinearLayout ll_tomo_updown;
	private LinearLayout wind_ll;
	private LinearLayout humi_ll;

	private LinearLayout tv_linear;

	private ImageView weather_image;

	private int old_temperature = 0;
	private int temperature = 0;
	private int state = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ctx = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.fragment_main_page_bottom,
				container, false);

		layout_info = (RelativeLayout) view.findViewById(R.id.layout_info);
		layout_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AqiDetail.class);
				startActivity(intent);
			}
		});
		id_mai_icon = (ImageView) view.findViewById(R.id.id_mai_icon);
		id_mai_aqi = (TextView) view.findViewById(R.id.id_mai_aqi);

		tv_tempreture = (TextView) view.findViewById(R.id.tv_tempreture);
		rl_temp = (RelativeLayout) view.findViewById(R.id.rl_temp);
		ll_last_level = (LinearLayout) view.findViewById(R.id.last_level);

		tv_weather_temp = (TextView) view.findViewById(R.id.tv_weatherTomoTemp);
		tv_hightemp = (TextView) view.findViewById(R.id.tv_highTemp);
		tv_lowtemp = (TextView) view.findViewById(R.id.tv_lowTemp);
		tv_weather = (TextView) view.findViewById(R.id.tv_weather);

		humi_level = (TextView) view.findViewById(R.id.humi_level);
		wind_level = (TextView) view.findViewById(R.id.wind_level);

		ll_third_level = (LinearLayout) view.findViewById(R.id.third_level);
		humi_ll = (LinearLayout) view.findViewById(R.id.humi_ll);
		wind_ll = (LinearLayout) view.findViewById(R.id.wind_ll);
		tv_linear = (LinearLayout) view.findViewById(R.id.tv_linear);

		ll_tomo_updown = (LinearLayout) view.findViewById(R.id.ll_tomo_updown);
		weather_image = (ImageView) view.findViewById(R.id.weather_image);
		animInit();
		return view;
	}

	private void animInit() {

		animItFromRight(ll_third_level, 200, 1000);
		animItFromRight(rl_temp, 800, 1000);
		animItFromRight(ll_last_level, 1400, 1000);
		animItFromRight(tv_weather_temp, 1800, 1000);
		animItFromRight(ll_tomo_updown, 2200, 1000);
		animItFromRight(layout_info, 2800, 600);
		// layout_info.setAlpha(0.0f);
		// layout_info.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// float w = (float) (layout_info.getMeasuredWidth() / 2.0);
		// layout_info.setTranslationX(w);
		// layout_info.animate().alpha(1.0f).setDuration(600)
		// .setStartDelay(2800).start();
		// layout_info.animate().translationX(0.0f).setDuration(600)
		// .setStartDelay(2800)
		// .setListener(new AnimatorListener() {
		// @Override
		// public void onAnimationStart(Animator arg0) {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animator arg0) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animator arg0) {
		// try {
		// ((MainActivity) getActivity())
		// .refreshForce();
		// ((MainActivity) getActivity())
		// .getNewWeather();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public void onAnimationCancel(Animator arg0) {
		//
		// }
		// }).start();
		// }
		// });
	}

	private void animItFromRight(final View v, final int startTime,
			final int duration) {
		v.setAlpha(0.0f);
		v.post(new Runnable() {

			@Override
			public void run() {
				float w = (float) (v.getMeasuredWidth() / 2.0);
				v.setTranslationX(w);
				v.animate().alpha(1.0f).setDuration(duration)
						.setStartDelay(startTime).start();
				v.animate().translationX(0.0f).setDuration(duration)
						.setStartDelay(startTime).start();
			}
		});
	}

	/**
	 * @param data
	 */
	public void setData(WeatherData data) {
		if (state != 0) {
			return;
		}
		if (data.aqi != null && NumberUtils.isNumber(data.aqi)
				&& !StringUtils.isEmpty(data.aqi)) {
			layout_info.setVisibility(View.VISIBLE);
			int aqi = Integer.parseInt(data.aqi);
			String aqi_msg = "48 深呼吸";
			int haze_level = 0;
			// Log.e("err", data.toString());
			if (aqi <= 50) {
				aqi_msg = aqi + " 深呼吸";
				haze_level = 0;
			} else if (aqi > 50 && aqi <= 100) {
				aqi_msg = aqi + " 还不错";
				haze_level = 1;
			} else if (aqi > 100 && aqi <= 150) {
				aqi_msg = aqi + " 有点差哦";
				haze_level = 2;
			} else if (aqi > 150 && aqi <= 200) {
				aqi_msg = aqi + " 蛮差的";
				haze_level = 3;
			} else if (aqi > 200 && aqi <= 300) {
				aqi_msg = aqi + " 别出门了";
				haze_level = 4;
			} else if (aqi > 300) {
				aqi_msg = aqi + " 已爆表";
				haze_level = 5;
			}
			id_mai_icon.setImageResource(CategorySet.haze_level[haze_level]);
			id_mai_aqi.setText(Babe.t2s(ctx, aqi_msg));
			tv_linear.removeAllViews();
			tv_linear.refreshDrawableState();
			tv_linear.addView(id_mai_aqi);
			tv_linear.invalidate();
			// tv_linear.postInvalidate();
			// getActivity().findViewById(android.R.id.content).invalidate();

		} else {
			layout_info.setVisibility(View.INVISIBLE);
		}

		if (!StringUtils.isEmpty(data.temp) && !data.temp.equals("false")) {
			// tv_tempreture.setText(data.temp);
			temperature = Integer.parseInt(data.temp);
		} else if (!StringUtils.isEmpty(data.rtemp)
				&& !data.rtemp.equals("false")) {
			temperature = Integer.parseInt(data.rtemp);
			// tv_tempreture.setText(data.rtemp);
		}
		if (old_temperature != temperature) {

			ValueAnimator animator = new ValueAnimator();
			animator.setObjectValues(old_temperature, temperature);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				public void onAnimationUpdate(ValueAnimator animation) {
					tv_tempreture.setText(String.valueOf(animation
							.getAnimatedValue()));
				}
			});
			animator.setEvaluator(new TypeEvaluator<Integer>() {
				public Integer evaluate(float fraction, Integer startValue,
						Integer endValue) {

					return (startValue + Math.round((endValue - startValue)
							* fraction));

				}
			});
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator arg0) {
					state = 1;
				}

				@Override
				public void onAnimationRepeat(Animator arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animator arg0) {
					old_temperature = temperature;
					state = 0;
				}

				@Override
				public void onAnimationCancel(Animator arg0) {
				}
			});
			animator.setDuration(3000);
			animator.start();
		} else {
			tv_tempreture.setText(String.valueOf(temperature));
		}
		if (StringUtils.isNotEmpty(data.today_weather)
				&& StringUtils.isNotBlank(data.today_temp)) {
			tv_weather.setText(Babe.t2s(ctx, data.today_weather));
			setIcon(data.today_weather);
		} else {
			tv_weather.setText(Babe.t2s(ctx, data.weather));
			setIcon(data.weather);
		}

		if (StringUtils.isNotEmpty(data.tomo_weather)) {
			tv_weather_temp.setText(Babe.t2s(ctx, "明天，" + data.tomo_weather));
		} else {
			if (data.next_day_weather != null && data.next_day_weather != "b"
					&& data.next_night_weather != null) {
				if (data.next_day_weather.equals(data.next_night_weather)) {
					tv_weather_temp.setText(Babe.t2s(ctx, "明天，"
							+ data.next_day_weather));
				} else {
					tv_weather_temp.setText(Babe.t2s(ctx, "明天，"
							+ data.next_day_weather + "转"
							+ data.next_night_weather));
				}
			}
			if (data.next_day_weather.equals("b")) {
				tv_weather_temp.setText(Babe.t2s(ctx, "明天，"
						+ data.next_night_weather));
			}
		}

		if (StringUtils.isNotEmpty(data.tomo_temp)) {
			String[] tmp = data.tomo_temp.replaceAll("℃", "").split("~");
			try {
				int t1 = Integer.parseInt(tmp[0].trim());
				int t2 = Integer.parseInt(tmp[1].trim());
				int high, low;
				if (t1 >= t2) {
					high = t1;
					low = t2;
				} else {
					high = t2;
					low = t1;
				}
				tv_lowtemp.setText(String.valueOf(low) + "°");
				tv_hightemp.setText(String.valueOf(high) + "°");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (data.next_night_temp != null && data.next_day_temp != "b"
					&& data.next_day_temp != null) {
				int high, low;
				try {
					int t1 = Integer.parseInt(data.next_day_temp.replaceAll(
							"℃", "").trim());
					int t2 = Integer.parseInt(data.next_night_temp.replaceAll(
							"℃", "").trim());
					if (t1 >= t2) {
						high = t1;
						low = t2;
					} else {
						high = t2;
						low = t1;
					}
					tv_lowtemp.setText(String.valueOf(low) + "°");
					tv_hightemp.setText(String.valueOf(high) + "°");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
		if (StringUtils.isEmpty(data.humidity)) {
			humi_ll.setVisibility(View.INVISIBLE);
		} else {
			humi_ll.setVisibility(View.VISIBLE);
		}
		humi_level.setText(data.humidity);
		if (StringUtils.isEmpty(data.fenglevel)) {
			wind_ll.setVisibility(View.INVISIBLE);
		} else {
			wind_ll.setVisibility(View.VISIBLE);
		}
		try {
			String level = getWindDisplay(data.fenglevel);
			wind_level.setText(level);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void setIcon(String weather) {
		String w_1 = "notpossible";
		String w_2 = "notpossible";
		String w_3 = "notpossible";
		Calendar cal = Calendar.getInstance();
		int len = weather.length();
		w_1 = weather.substring(0, 1);
		if (len >= 2)
			w_2 = weather.substring(0, 2);
		if (len >= 3)
			w_3 = weather.substring(0, 3);
		List<String> w_arr = Arrays.asList(CategorySet.weather_icon_noti_text);

		boolean b_1 = w_arr.contains(w_1);
		boolean b_2 = w_arr.contains(w_2);
		boolean b_3 = w_arr.contains(w_3);

		int w_idx = 0;

		if (b_1 || b_2 || b_3) {
			if (b_3) {
				w_idx = w_arr.indexOf(w_3);
			} else if (b_2) {
				w_idx = w_arr.indexOf(w_2);
			} else if (b_1) {
				w_idx = w_arr.indexOf(w_1);
			}
		}

		int hournow = cal.get(Calendar.HOUR_OF_DAY);
		if (w_idx >= CategorySet.noti_wicon_night_start_idx) {
			if (hournow > 19 || hournow < 8) {

			} else {
				if (w_idx == CategorySet.noti_icon_night_snow_idx) {
					w_idx = CategorySet.noti_icon_daytime_snow_idx;
				}
			}
		}

		if (hournow > 19 || hournow < 8) {
			if (w_idx == 0) {
				w_idx = CategorySet.noti_icon_night_clear_idx;
			}
			if (w_idx >= CategorySet.noti_wicon_cloudy_up
					&& w_idx <= CategorySet.noti_wicon_cloudy_down) {
				w_idx = CategorySet.noti_icon_night_cloudy_idx;
			}
			if (w_idx >= CategorySet.noti_wicon_rain_up
					&& w_idx <= CategorySet.noti_wicon_rain_down) {
				w_idx = CategorySet.noti_icon_night_rain_idx;
			}
			if (w_idx >= CategorySet.noti_wicon_snow_up
					&& w_idx <= CategorySet.noti_wicon_snow_down) {
				w_idx = CategorySet.noti_icon_night_snow_idx;
			}

		}

		weather_image.setImageResource(CategorySet.weather_icon_home[w_idx]);
	}

	private String getWindDisplay(String winddata) {
		if (StringUtils.isEmpty(winddata)) {
			return "";
		}
		int level = Integer.parseInt(winddata.replaceAll("级", "").trim());
		String ret;
		switch (level) {
		case 0:
			ret = "静悄悄";
			break;
		case 1:
			ret = "没啥风";
			break;
		case 2:
			ret = "风儿轻轻吹";
			break;
		case 3:
			ret = "小微风";
			break;
		case 4:
			ret = "温柔的风";
			break;
		case 5:
			ret = "小清风";
			break;
		case 6:
			ret = "刮风了";
			break;
		case 7:
			ret = "刮大风了";
			break;
		case 8:
			ret = "风好大呀";
			break;
		case 9:
			ret = "树都挂歪了";
			break;
		case 10:
		case 11:
			ret = "不能出门了";
			break;
		case 12:
		case 13:
		case 14:
		case 15:
		default:
			ret = "龙~卷~风~";
			break;
		}
		return Babe.t2s(getActivity().getApplicationContext(), ret);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName()); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

}
