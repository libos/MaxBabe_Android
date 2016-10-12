package com.maxtain.bebe.monitor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxtain.bebe.R;

public class WearSetItem extends FrameLayout implements View.OnTouchListener{

	private ImageView iv_wear;
	private TextView tv_position;
	private TextView tv_select;
	private RelativeLayout layout_item;

	private List<Integer> initList = new ArrayList<Integer>();
	private List<Integer> clickList = new ArrayList<Integer>();
	private String[] pos = { "��ǰ��", "��", "�ҷ�", "���ɵײ�", "���" };
	private Context context;
	private int id;

	public WearSetItem(Context context, int id) {
		super(context);
		this.context = context;
		this.id = id;
		LayoutInflater.from(context).inflate(R.layout.item_wearset, this);
//		initList.add(R.drawable.wearfront);
//		initList.add(R.drawable.wearleft);
//		initList.add(R.drawable.wearright);
//		initList.add(R.drawable.wearbottom);
//		initList.add(R.drawable.wearback);
//		clickList.add(R.drawable.wearfront_select);
//		clickList.add(R.drawable.wearleft_select);
//		clickList.add(R.drawable.wearright_select);
//		clickList.add(R.drawable.wearbottom_select);
//		clickList.add(R.drawable.wearback_select);

		layout_item = (RelativeLayout) findViewById(R.id.layout_item);
		if ((id & 1) == 0) {
			layout_item.setBackgroundColor(context.getResources().getColor(
					R.color.lightblue));
		}
		iv_wear = (ImageView) findViewById(R.id.iv_wear);

		tv_position = (TextView) findViewById(R.id.tv_position);

		tv_select = (TextView) findViewById(R.id.tv_select);
		tv_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WearSetActivity.clearstate();
				setclick();
			}
		});
		setOnTouchListener(this);
	}

	public void setinit() {
		iv_wear.setImageDrawable(context.getResources().getDrawable(
				initList.get(id)));
		tv_position.setText(pos[id]);
		tv_select.setText("ѡ��");
		tv_select.setTextColor(Color.WHITE);
	}

	public void setclick() {
		iv_wear.setImageDrawable(context.getResources().getDrawable(
				clickList.get(id)));
		tv_position.setText(pos[id]);
		tv_select.setText("��ǰ״̬");
		tv_select.setTextColor(Color.GRAY);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			WearSetActivity.clearstate();
			
			setclick();
		}
		return true;
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View view = inflater.inflate(R.layout.item_wearset, container, false);
	//
	//
	//
	// return view;
	// }
}
