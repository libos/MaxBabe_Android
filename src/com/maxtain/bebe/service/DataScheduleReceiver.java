package com.maxtain.bebe.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class DataScheduleReceiver extends BroadcastReceiver {
	// restart service every 1 hour
	// 10s for test
	// 20 min 1000 * 60*20 = 1200000
//	private static final long REPEAT_TIME = 1200000;// 10000;// 1000 * 3600;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		AlarmManager service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, DataStartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(
				context.getApplicationContext(), 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// Log.d("aaa", "onReceive: " + intent.getAction());

		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())
				|| "android.intent.action.USER_PRESENT".equals(intent
						.getAction())
				|| "android.intent.action.SCREEN_ON".equals(intent.getAction())
				|| "com.maxtain.bebe.service.startup"
						.equals(intent.getAction())) {

			Calendar cal = Calendar.getInstance();
			// start 30 seconds after boot completed
			cal.add(Calendar.SECOND, 10);
			long now = System.currentTimeMillis();
			// fetch every 30 seconds
			// InexactRepeating allows Android to optimize the energy
			// consumption
			service.setInexactRepeating(AlarmManager.RTC_WAKEUP, now,
					AlarmManager.INTERVAL_FIFTEEN_MINUTES, pending);
			// Just to make sure the intent is registered
			// if (!"android.intent.action.ACTION_SCREEN_ON".equals(intent
			// .getAction())) {
			// IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
			// filter.addAction(Intent.ACTION_SCREEN_OFF);
			// context.registerReceiver(this, filter);
			// }
		}
		// service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// REPEAT_TIME, pending);

		// Dont do it. If you terminate the alarm while there is a valid app
		// running, the log entry will
		// get closed only when the phone wakes up and this will cause a very
		// long session...
		// The log entry will be closed only on the next alarm when the screen
		// is blank and not in call.
		// if (false && "android.intent.action.SCREEN_OFF".equals(intent
		// .getAction())) {
		// // Terminate alarm
		// service.cancel(pending);
		// Log.d("aaa", "Terminate Alarm on SCREEN_OFF");
		// }
	}

}
