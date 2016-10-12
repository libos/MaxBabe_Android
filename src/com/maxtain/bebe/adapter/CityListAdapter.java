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

public class CityListAdapter extends BaseAdapter {
	private ArrayList<String> data;
	private LayoutInflater mInflater;
	private Context context;

	public CityListAdapter(ArrayList<String> citylist, Context context) {
		data = citylist;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void setData(ArrayList<String> citylist) {
		data = citylist;
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
			view = mInflater.inflate(R.layout.item_city, null);
		} else {
			view = convertView;
		}

		String city_name = (String) data.get(position);
		if (city_name.indexOf(":") != -1) {
			String[] city = city_name.split(":");
			((TextView) view.findViewById(R.id.tv_city)).setText(city[0]);
			if (city.length >= 1) {
				if (!city[0].equals(city[1])) {
					((TextView) view.findViewById(R.id.tv_province))
							.setText(city[1]);
				} else {
					((TextView) view.findViewById(R.id.tv_province))
							.setText("");
				}
				((TextView) view.findViewById(R.id.tv_invi_data))
						.setText(city_name);
			}
		} else {
			((TextView) view.findViewById(R.id.tv_city)).setText(city_name);
			((TextView) view.findViewById(R.id.tv_province)).setText("");
			((TextView) view.findViewById(R.id.tv_invi_data))
					.setText(city_name);
		}
		if ((position & 1) == 1) {
			view.setBackgroundColor(Color.parseColor("#4BB9D2"));
		} else {
			view.setBackgroundColor(Color.parseColor("#5DC0D7"));
		}

		return view;
	}
}
