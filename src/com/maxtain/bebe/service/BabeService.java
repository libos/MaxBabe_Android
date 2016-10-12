package com.maxtain.bebe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BabeService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
