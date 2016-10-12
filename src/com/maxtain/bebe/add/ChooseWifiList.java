package com.maxtain.bebe.add;

import java.util.ArrayList;
import java.util.HashMap;

import com.maxtain.bebe.R;
import com.maxtain.bebe.adapter.WifiListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ChooseWifiList extends Activity implements OnItemClickListener {
	private ListView listv;
	private WifiListAdapter adapter;
	private ArrayList<HashMap<String, Object>> data;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_wifi);
		this.getActionBar().hide();

		listv = (ListView) findViewById(R.id.id_choose_wifi_list);
		data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> v1 = new HashMap<String, Object>();
		HashMap<String, Object> v2 = new HashMap<String, Object>();
		v1.put("ssid", "TP-Link_32EF93A");
		v1.put("rssi", 4);
		v2.put("ssid", "xxx");
		v2.put("rssi", 3);
		data.add(v1);
		data.add(v2);
		adapter = new WifiListAdapter(data, this);
		listv.setAdapter(adapter);
		listv.setOnItemClickListener(this);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
/*		Toast.makeText(
				this,
				"position:" + position + "  item:"
						+ parent.getItemAtPosition(position).toString(),
				Toast.LENGTH_LONG).show();*/
		Intent intent = new Intent(this, WifiPassword.class);
		intent.putExtra("ssid", data.get(position).get("ssid").toString());
		startActivity(intent);
	}
}
