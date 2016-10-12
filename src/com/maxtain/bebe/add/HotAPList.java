package com.maxtain.bebe.add;

import java.util.ArrayList;
import java.util.List;

import com.maxtain.bebe.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class HotAPList extends Activity implements OnItemClickListener {
	private ListView listv;
	private List<String> data;
	private ArrayAdapter<String> adapter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_ap);
		this.getActionBar().hide();

		listv = (ListView) findViewById(R.id.id_listview_devices);
		data = new ArrayList<String>();
		data.add("Monitor AP");
		adapter = new ArrayAdapter<String>(this, R.layout.item_hotap,
				R.id.hotap_name, data);
		adapter.notifyDataSetChanged();
		listv.setAdapter(adapter);
		listv.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this,ChooseWifiList.class);
		startActivity(intent);
	}

}
