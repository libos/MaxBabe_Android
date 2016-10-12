package com.maxtain.bebe.add;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.maxtain.bebe.R;

@SuppressLint("NewApi")
public class ConnectAddList extends Activity implements OnItemClickListener {
	private Button mAddNew;
	private Button mUsedAdd;
	private ListView listv;
	private View noDevice;
	private ArrayAdapter<String> adapter;
	private List<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_page);
		this.getActionBar().hide();
		listv = (ListView) findViewById(R.id.id_listview_devices);
		data = new ArrayList<String>();
		// get data array from xxx
		// if darr not empty
		// data.addxxx

		if (data.isEmpty()) {
			data.add("什么是Bebe设备？");
			adapter = new ArrayAdapter<String>(this, R.layout.item_dummy,
					R.id.dummy_text, data);
		} else {
			adapter = new ArrayAdapter<String>(this,
					R.layout.item_added_device, R.id.device_name, data);
		}

		listv.setAdapter(adapter);
		listv.setOnItemClickListener(this);
		adapter.notifyDataSetChanged();

		// if (1 != 3) {
		// } else {
		//
		// }

		mAddNew = (Button) findViewById(R.id.id_add_new_device);
		mAddNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConnectAddList.this, HotAPList.class);
				startActivity(intent);
			}
		});
		mUsedAdd = (Button) findViewById(R.id.id_add_used_device);
		mUsedAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ConnectAddList.this, UsedBind.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(
				this,
				"position:" + position + "  item:"
						+ parent.getItemAtPosition(position).toString(),
				Toast.LENGTH_LONG).show();

	}
}
