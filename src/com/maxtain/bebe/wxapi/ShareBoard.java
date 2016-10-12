package com.maxtain.bebe.wxapi;

import com.maxtain.bebe.R;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.*;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ShareBoard extends PopupWindow implements OnClickListener {
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private Activity mActivity;

	private static final String TAG = "ShareBoard";

	public ShareBoard(Activity activity) {
		super(activity);
		this.mActivity = activity;
		initView(activity);
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.share_board, null);
		rootView.findViewById(R.id.wechat).setOnClickListener(this);
		rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
		rootView.findViewById(R.id.qq).setOnClickListener(this);
		rootView.findViewById(R.id.weibo).setOnClickListener(this);
		rootView.findViewById(R.id.facebook).setOnClickListener(this);
		rootView.findViewById(R.id.instagram).setOnClickListener(this);
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.wechat:
			performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.wechat_circle:
			// Log.i(TAG, TAG + "wechat_circle");
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.qq:
			performShare(SHARE_MEDIA.QQ);
			break;
		case R.id.weibo:
			performShare(SHARE_MEDIA.SINA);
			break;
		case R.id.instagram:
			performShare(SHARE_MEDIA.INSTAGRAM);
			break;
		case R.id.facebook:
			performShare(SHARE_MEDIA.FACEBOOK);
			break;
		default:
			break;
		}
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "分享成功";
				} else {
					showText += "分享失败";
				}
				// Toast.makeText(mActivity, showText,
				// Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}

}
