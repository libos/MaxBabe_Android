package com.maxtain.bebe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DataStartServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		WakeLocker.acquire(context);
		// Log.d("aaa", "MyStartServiceReceiver: ");
		Intent service = new Intent(context.getApplicationContext(),
				DownloadService.class);
		context.startService(service);
		WakeLocker.release();
	}

}
