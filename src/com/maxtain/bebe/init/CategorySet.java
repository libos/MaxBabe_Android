package com.maxtain.bebe.init;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import com.maxtain.bebe.R;

public class CategorySet {
	// 18种 backgrounds
	public static final int daytime_clear[] = { R.drawable.daytime_clear01 };
	public static final int daytime_cloudy[] = { R.drawable.daytime_cloudy01,
			R.drawable.daytime_cloudy02 };
	public static final int daytime_fog[] = { R.drawable.daytime_fog01 };
	public static final int daytime_haze[] = { R.drawable.daytime_haze01 };
	public static final int daytime_rain[] = { R.drawable.daytime_rain01,
			R.drawable.daytime_rain02, R.drawable.daytime_rain03 };
	public static final int daytime_snow[] = { R.drawable.daytime_snow01 };
	public static final int daytime_sandstorm[] = { R.drawable.daytime_sandstorm01 };

	public static final int nightfall_clear[] = { R.drawable.nightfall_clear01 };
	public static final int nightfall_cloudy[] = { R.drawable.nightfall_cloudy01 };
	public static final int nightfall_rain[] = { R.drawable.nightfall_rain01,
			R.drawable.nightfall_rain02 };

	public static final int midnight_clear[] = { R.drawable.midnight_clear01 };
	public static final int midnight_snow[] = { R.drawable.midnight_snow01 };
	public static final int midnight_cloudy[] = { R.drawable.midnight_cloudy01 };
	public static final int midnight_rain[] = { R.drawable.midnight_rain01,
			R.drawable.midnight_rain02 };

	public static final int night_clear[] = { R.drawable.night_clear01 };
	public static final int night_snow[] = { R.drawable.night_snow01 };
	public static final int night_cloudy[] = { R.drawable.night_cloudy01 };
	public static final int night_rain[] = { R.drawable.night_rain01,
			R.drawable.night_rain02 };
	public static ArrayList<int[]> init_background_list = new ArrayList<int[]>(
			Arrays.asList(daytime_clear, daytime_cloudy, daytime_cloudy,
					daytime_fog, daytime_haze, daytime_rain, daytime_snow,
					daytime_sandstorm, nightfall_clear, nightfall_cloudy,
					nightfall_cloudy, nightfall_rain, midnight_clear,
					midnight_snow, midnight_cloudy, midnight_cloudy,
					midnight_rain, night_clear, night_snow, night_cloudy,
					night_cloudy, night_rain));
	public static String backgroud_weather[] = { "晴", "多云", "阴", "雾", "霾", "雨",
			"雪", "沙尘暴", "晴", "多云", "阴", "雨", "晴", "雪", "多云", "阴", "雨", "晴",
			"雪", "多云", "阴", "雨" };

	public class updown {
		public int ge;
		public int le;

		public updown(int ge, int le) {
			this.ge = ge;
			this.le = le;
		}
	}

	public ArrayList<updown> background_time = new ArrayList<updown>();

	// 17种figure
	public static final int figure_normal[] = { R.drawable.f_normal01,
			R.drawable.f_normal02, R.drawable.f_normal03,
			R.drawable.f_normal04, R.drawable.f_normal05 };
	public static final int figure_rain[] = { R.drawable.f_rain01,
			R.drawable.f_rain02, R.drawable.f_rain03, R.drawable.f_rain04,
			R.drawable.f_rain05 };
	public static final int figure_snow[] = { R.drawable.f_snow01 };
	public static final int figure_fog[] = { R.drawable.f_flog01 };
	public static final int figure_haze[] = { R.drawable.f_haze01 };
	public static final int figure_sand[] = { R.drawable.f_sandstorm01 };
	public static final int figure_hot[] = { R.drawable.f_hot01 };
	public static final int figure_midnight[] = { R.drawable.f_midnight01,
			R.drawable.f_midnight01 };
	public static final int figure_nightfall[] = { R.drawable.f_nightfall01 };

	public static ArrayList<int[]> init_figure_list = new ArrayList<int[]>(
			Arrays.asList(figure_normal, figure_rain, figure_snow, figure_fog,
					figure_haze, figure_sand, figure_hot, figure_midnight,
					figure_nightfall));

	public static String figure_weather[] = { "*", "雨", "雪", "雾", "霾", "沙尘暴",
			"*", "*", "*" };
	public ArrayList<updown> figure_time = new ArrayList<updown>();
	public ArrayList<updown> figure_temp = new ArrayList<updown>();

	// 14 种alarm
	public static final String alarm_list[] = { "baoyu"/* 暴雨 */,
			"bingbao"/* 冰雹 */, "dafeng"/* 大风 */, "dry"/* 干旱 */,
			"taifeng"/* 台风 */, "fog"/* 雾 */, "hightemp"/* 高温 */,
			"lei"/* 雷电 */, "lowtemp"/* 低温 */, "mai"/* 霾 */, "rain"/* 雷雨大风 */,
			"road"/* 道路结冰 */, "sand"/* 沙尘暴 */, "snow"/* 暴雪 */};

	// 6 种雾霾
	// main
	public static final int haze_level[] = { R.drawable.icon_haze_level1,
			R.drawable.icon_haze_level2, R.drawable.icon_haze_level3,
			R.drawable.icon_haze_level4, R.drawable.icon_haze_level5,
			R.drawable.icon_haze_level6 };
	// notification
	public static final int noti_haze_level_begin[] = {
			R.drawable.notification_haze_level1_left,
			R.drawable.notification_haze_level2_left,
			R.drawable.notification_haze_level3_left,
			R.drawable.notification_haze_level4_left,
			R.drawable.notification_haze_level5_left,
			R.drawable.notification_haze_level6_left };
	public static final int noti_haze_level_middle[] = {
			R.drawable.notification_haze_level1_middle,
			R.drawable.notification_haze_level2_middle,
			R.drawable.notification_haze_level3_middle,
			R.drawable.notification_haze_level4_middle,
			R.drawable.notification_haze_level5_middle,
			R.drawable.notification_haze_level6_middle };
	public static final int noti_haze_level_end[] = {
			R.drawable.notification_haze_level1_right,
			R.drawable.notification_haze_level2_right,
			R.drawable.notification_haze_level3_right,
			R.drawable.notification_haze_level4_right,
			R.drawable.notification_haze_level5_right,
			R.drawable.notification_haze_level6_right };
	// 很多话
	public static final String[] init_words = { "新的一天又开始了，该起床尿尿了……",
			"你知道什么叫萌吗，看看我呀～", "什么一场说走就走的旅行<br>我就连一场说走就走的下班都不行T_T",
			"你又不缺什么，只是得不到想要的而已", "你要是喜欢我的话，那就好好工作挣好多好多钱<br>等我结婚时包个大红包给我吧",
			"哪有什么寂寞，不就是闲的吗！", "做坏事早晚都会被发现，所以中午做", "青春很短暂，再疯狂我们就都毁了", "我们做好朋友吧",
			"今天你一定过得很开心，因为我也是", "人生贵知心，定交无暮早", "我爱你。", "世界那么大，我只想去你心里",
			"不想过完今天，主要没脸面对明天" };
	public ArrayList<updown> oneword_time = new ArrayList<updown>();
	public static int noti_wicon_night_start_idx = 26;
	public static int noti_wicon_cloudy_up = 1;
	public static int noti_wicon_cloudy_down = 2;
	public static int noti_wicon_rain_up = 3;
	public static int noti_wicon_rain_down = 12;
	public static int noti_wicon_snow_up = 13;
	public static int noti_wicon_snow_down = 17;
	public static int noti_icon_night_clear_idx = 26;
	public static int noti_icon_night_cloudy_idx = 27;
	public static int noti_icon_night_rain_idx = 28;
	public static int noti_icon_night_snow_idx = 29;
	public static int noti_icon_daytime_snow_idx = 13;

	public static final int[] weather_icon_noti = {
			R.drawable.icon_weather_daytime_clear01,
			R.drawable.icon_weather_daytime_cloudy01,
			R.drawable.icon_weather_daytime_cloudy02,
			R.drawable.icon_weather_daytime_rain01,
			R.drawable.icon_weather_daytime_rain02,
			R.drawable.icon_weather_daytime_rain03,
			R.drawable.icon_weather_daytime_rain04,
			R.drawable.icon_weather_daytime_rain05,
			R.drawable.icon_weather_daytime_rain06,
			R.drawable.icon_weather_daytime_rain07,
			R.drawable.icon_weather_daytime_rain08,
			R.drawable.icon_weather_daytime_rain09,
			R.drawable.icon_weather_daytime_rain09,
			R.drawable.icon_weather_daytime_snow04,
			R.drawable.icon_weather_daytime_snow01,
			R.drawable.icon_weather_daytime_snow02,
			R.drawable.icon_weather_daytime_snow03,
			R.drawable.icon_weather_daytime_snow04,
			R.drawable.icon_weather_daytime_sandstorm01,
			R.drawable.icon_weather_daytime_sandstorm02,
			R.drawable.icon_weather_daytime_sandstorm03,
			R.drawable.icon_weather_daytime_sandstorm04,
			R.drawable.icon_weather_daytime_fog01,
			R.drawable.icon_weather_daytime_fog02,
			R.drawable.icon_weather_daytime_fog01,
			R.drawable.icon_weather_daytime_fog02,
			R.drawable.icon_weather_night_clear,
			R.drawable.icon_weather_night_cloudy,
			R.drawable.icon_weather_night_rain,
			R.drawable.icon_weather_night_snow };
	public static final String[] weather_icon_noti_text = { "晴", "多云", "阴",
			"阵雨", "雷阵雨", "冰雹", "雨夹雪", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴",
			"阵雪", "小雪", "中雪", "大雪", "暴雪", "浮尘", "扬沙", "沙尘暴", "强沙尘", "特强沙", "雾",
			"浓雾", "强浓雾", "晴", "阴", "雨", "雪" };

	public static final int[] weather_icon_home = {
			R.drawable.icon_home_weather_daytime_clear01,
			R.drawable.icon_home_weather_daytime_cloudy01,
			R.drawable.icon_home_weather_daytime_cloudy02,
			R.drawable.icon_home_weather_daytime_rain01,
			R.drawable.icon_home_weather_daytime_rain02,
			R.drawable.icon_home_weather_daytime_rain03,
			R.drawable.icon_home_weather_daytime_rain04,
			R.drawable.icon_home_weather_daytime_rain05,
			R.drawable.icon_home_weather_daytime_rain06,
			R.drawable.icon_home_weather_daytime_rain07,
			R.drawable.icon_home_weather_daytime_rain08,
			R.drawable.icon_home_weather_daytime_rain09,
			R.drawable.icon_home_weather_daytime_rain09,
			R.drawable.icon_home_weather_daytime_snow04,
			R.drawable.icon_home_weather_daytime_snow01,
			R.drawable.icon_home_weather_daytime_snow02,
			R.drawable.icon_home_weather_daytime_snow03,
			R.drawable.icon_home_weather_daytime_snow04,
			R.drawable.icon_home_weather_daytime_sandstorm01,
			R.drawable.icon_home_weather_daytime_sandstorm02,
			R.drawable.icon_home_weather_daytime_sandstorm03,
			R.drawable.icon_home_weather_daytime_sandstorm04,
			R.drawable.icon_home_weather_daytime_fog01,
			R.drawable.icon_home_weather_daytime_fog02,
			R.drawable.icon_home_weather_daytime_fog01,
			R.drawable.icon_home_weather_daytime_fog02,
			R.drawable.icon_home_weather_night_clear,
			R.drawable.icon_home_weather_night_cloudy,
			R.drawable.icon_home_weather_night_rain,
			R.drawable.icon_home_weather_night_snow };

	public CategorySet() {
		updown allday = new updown(0, 24);
		updown daytime = new updown(7, 16);
		updown nightfall = new updown(17, 18);
		updown night = new updown(19, 22);
		updown midnight = new updown(23, 6);

		updown word_morning = new updown(5, 9);
		updown word_noon = new updown(10, 14);
		updown word_night = new updown(16, 20);
		updown word_allnight = new updown(21, 4);

		updown alltemp = new updown(-100, 100);
		updown hottemp = new updown(32, 100);

		oneword_time.add(word_morning);// "新的一天又开始了，该起床尿尿了……",
		oneword_time.add(allday);// "你知道什么叫萌吗，看看我呀～",
		oneword_time.add(allday);// "多照照镜子，很多事情你就明白原因了",
		oneword_time.add(allday);// "你又不缺什么，只是得不到想要的而已",
		oneword_time.add(word_noon);// "哪有什么孤独，不就是没人爱吗！",
		oneword_time.add(word_noon);// "哪有什么寂寞，不就是闲的吗！"
		oneword_time.add(word_noon);// "哪有什么绝望，不就是穷的吗！",
		oneword_time.add(word_night);// "好好吃饭吧，你的问题又不是因为胖",
		oneword_time.add(word_night);// "我们做好朋友吧"
		oneword_time.add(word_allnight);// "没有钱包的充实，哪来内心的宁静",
		oneword_time.add(word_allnight);// "我们一起睡觉吧",
		oneword_time.add(word_allnight);// "我爱你。"
		oneword_time.add(word_allnight);// "世界那么大，我只想去你心里",
		oneword_time.add(word_allnight); // "不想过完今天，主要没脸面对明天"

		// figure
		figure_time.add(allday);// figure_normal
		figure_time.add(allday);// figure_rain
		figure_time.add(allday);// figure_snow
		figure_time.add(allday);// figure_fog
		figure_time.add(allday);// figure_haze
		figure_time.add(allday);// figure_sand
		figure_time.add(allday);// figure_hot
		figure_time.add(night);// figure_nightfall
		figure_time.add(midnight);// figure_midnight

		// temperature
		figure_temp.add(alltemp);// figure_normal
		figure_temp.add(alltemp);// figure_rain
		figure_temp.add(alltemp);// figure_snow
		figure_temp.add(alltemp);// figure_fog
		figure_temp.add(alltemp);// figure_haze
		figure_temp.add(alltemp);// figure_sand
		figure_temp.add(hottemp);// figure_hot
		figure_temp.add(alltemp);// figure_midnight
		figure_temp.add(alltemp);// figure_nightfall

		// day time
		background_time.add(daytime);// daytime_clear
		background_time.add(daytime);// daytime_cloudy
		background_time.add(daytime);// daytime_yin
		background_time.add(daytime);// daytime_fog
		background_time.add(daytime);// daytime_haze
		background_time.add(daytime);// daytime_rain
		background_time.add(daytime);// daytime_snow
		background_time.add(daytime);// daytime_sandstorm
		// night fall
		background_time.add(nightfall);// nightfall_clear
		background_time.add(nightfall);// nightfall_cloudy
		background_time.add(nightfall);// nightfall_yin
		background_time.add(nightfall);// nightfall_rain

		// mid night
		background_time.add(midnight);// midnight_clear
		background_time.add(midnight);// midnight_snow
		background_time.add(midnight);// midnight_cloudy
		background_time.add(midnight);// midnight_yin
		background_time.add(midnight);// midnight_rain
		// night
		background_time.add(night);// night_clear
		background_time.add(night);// night_snow
		background_time.add(night);// night_cloudy
		background_time.add(night);// night_yin
		background_time.add(night);// night_rain
	}
}
