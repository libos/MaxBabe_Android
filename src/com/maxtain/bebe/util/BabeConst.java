package com.maxtain.bebe.util;


public class BabeConst {
	public static final String SHAREP_DATABASE = "Database";

	public static final int SQLITE_DATABASE_VERSION = 6;
	public static final int SHARE_TEXT_LIMIT = 45;
	public static final String GALLERY_PATH = "MaxBabe_Gallery";
	public static final String referer = "http://referer.maxtain.com/";

	public static final String IS_INIT_PROCESS = "_is_init";
	public static final String IS_FIRST_LOCATE_PROCESS = "__is_first_locate";
	public static final String FAST_LOADDATA_MODE = "_fast_mode";
	public static final String HAS_GET_INTO_SHARE = "_HAS_GET_INTO_SHARE";
	
	public static final String SETTING_USE_GPS = "_weather_setting_gps";
	public static final String SETTING_WIFIONLY = "_weather_setting_wifi";
	public static final String SETTING_AUTOUPDATE = "_weather_setting_auto_update";
	public static final String SETTING_SEND_PUSH_MSG = "_weather_setting_send_and_receive_push_message";
	public static final String SETTING_NOTIFICATION = "_weather_notification";

	public static final String LOCATION_PROVINCE = "_location_province";
	public static final String LOCATION_CITY = "_location_city";
	public static final String LOCATION_CITYCODE = "_location_citycode";
	public static final String LOCATION_DISTRICT = "_location_district";
	public static final String LOCATION_LATITUDE = "_location_latitude";
	public static final String LOCATION_LONGITUDE = "_location_longitude";
	public static final String LOCATION_RADIUS = "_location_radius";

	public static final String PIC_BACKGROUND = "__pics_background";
	public static final String PIC_BACKGROUND_MD5 = "__pics_background_md5";
	public static final String PIC_BACKGROUND_PATH = "__pics_background_path";

	public static final String PIC_FIGURE = "__pics_figure";
	public static final String PIC_FIGURE_MD5 = "__pics_figure_md5";
	public static final String PIC_FIGURE_PATH = "__pics_figure_path";

	public static final String PIC_ONEWORD = "__pics_one_word";
	public static final String ACCOUNT_EMAIL = "__account_email_shared_pref";
	public static final String ACCOUNT_PASSWORD = "__account_password_shared_pref";
	public static final String ACCOUNT_PHONE = "__account_phone_shared_pref";
	public static final String ACCOUNT_NICKNAME = "__account_nickname_shared_pref";
	public static final String ACCOUNT_SEX = "__account_sex_shared_pref";

	public static final String CITY_SWTICH_INTEVAL = "__city_switch_last_time_time";
	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G和3G以上网络，或统称为快速网络 */
	public static final int NETWORKTYPE_3G = 3;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 4;

	public static final String SQLITE_DATABASE_NAME = "maxbabe";

	public static final String create_background_table = "CREATE TABLE IF NOT EXISTS `background` ( `id` integer primary key autoincrement , `filename` varchar(255) NOT NULL, `path` varchar(255) NOT NULL, `md5` varchar(255) NOT NULL, `download` integer default 0, `weather` varchar(255) NOT NULL, `ge_hour` integer default 0, `le_hour` integer default 24, `ge_week` integer default 0, `le_week` integer default 6, `ge_month` integer default 0, `le_month` integer default 31, `ge_temp`integer default -100, `le_temp` integer default 100,  `ge_aqi` integer default 0, `le_aqi` integer default 1000, `created` datetime default (datetime('now','localtime')));";
	public static final String create_figure_table = "CREATE TABLE IF NOT EXISTS `figure` ( `id` integer primary key autoincrement , `filename` varchar(255) NOT NULL, `path` varchar(255) NOT NULL, `md5` varchar(255) NOT NULL, `download` integer default 0, `weather` varchar(255) NOT NULL, `ge_hour` integer default 0, `le_hour` integer default 24, `ge_week` integer default 0, `le_week` integer default 6, `ge_month` integer default 0, `le_month` integer default 31,  `ge_temp`integer default -100, `le_temp` integer default 100, `ge_aqi` integer default 0, `le_aqi` integer default 1000, `created` datetime default (datetime('now','localtime')));";
	public static final String create_oneword_table = "CREATE TABLE IF NOT EXISTS `oneword` ( `id` integer primary key autoincrement , `word` text NOT NULL, `weather` varchar(255) NOT NULL, `ge_hour` integer default 0, `le_hour` integer default 24, `ge_week` integer default 0, `le_week` integer default 6, `ge_month` integer default 0, `le_month` integer default 31, `ge_temp`integer default -100, `le_temp` integer default 100, `ge_aqi` integer default 0, `le_aqi` integer default 1000, `created` datetime default (datetime('now','localtime')));";
	public static final String create_splash_table = "CREATE TABLE IF NOT EXISTS `splash` ( `id` integer primary key autoincrement , `filename` varchar(255) NOT NULL, `path` varchar(255) NOT NULL, `md5` varchar(255) NOT NULL, `download` integer default 0, `created` datetime default (datetime('now','localtime')));";
	public static final String create_city_table = "CREATE TABLE IF NOT EXISTS `city` ( `id` integer primary key , `name` varchar(255) NOT NULL, `pinyin` varchar(255) NOT NULL, `level2` varchar(255) NOT NULL, `province` varchar(255) NOT NULL);";
}
