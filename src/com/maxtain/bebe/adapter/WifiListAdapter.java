package com.maxtain.bebe.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.maxtain.bebe.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

public class WifiListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater mInflater;
	private Context context;

	public WifiListAdapter(ArrayList<HashMap<String, Object>> wifilist,
			Context context) {
		data = wifilist;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_wifi, null);
		} else {
			view = convertView;
		}
		
		String wifi_name = (String)data.get(position).get("ssid");
		int wifi_rssi = (Integer) data.get(position).get("rssi");
		((TextView) view.findViewById(R.id.choose_wifi_name)).setText(wifi_name);
		Drawable wifi_icon;
		if (wifi_rssi == 4) {
			wifi_icon = context.getResources().getDrawable(
					R.drawable.icon_list_wifi_level1);
		} else {
			wifi_icon = context.getResources().getDrawable(
					R.drawable.icon_list_wifi_level2);
		}

		((ImageView) view.findViewById(R.id.wifi_rssi))
				.setImageDrawable(wifi_icon);
		if ((position & 1) == 1) {
			view.setBackgroundColor(Color.parseColor("#4BB9D2"));
		} else {
			view.setBackgroundColor(Color.parseColor("#5DC0D7"));
		}

		return view;
	}
}
