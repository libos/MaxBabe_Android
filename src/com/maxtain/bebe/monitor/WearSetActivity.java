package com.maxtain.bebe.monitor;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maxtain.bebe.R;

@SuppressLint("NewApi")
public class WearSetActivity extends Activity {

//	private ListView list_wearset;
	private String[] pos = {"正前方","左方","右方","裤裆底部","正后方"};
//	private WearListAdapter wearListAdapter;
	private static List<WearSetItem> wearList = new ArrayList<WearSetItem>();
	private LinearLayout layout_wearlist;
	private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearset);
        this.getActionBar().hide();
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        layout_wearlist = (LinearLayout)findViewById(R.id.layout_wearlist);
        
        for (int i = 0; i < pos.length; i++) {
			WearSetItem item = new WearSetItem(this,i);
			item.setinit();
			wearList.add(item);
			layout_wearlist.addView(item);
		}
        
    }
  
    
	public static void clearstate(){
		for (int i = 0; i < wearList.size(); i++) {
			wearList.get(i).setinit();
		}
	}
	
}
