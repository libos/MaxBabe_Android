package com.maxtain.bebe.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxtain.bebe.account.IAccountListener.ACCOUNT_STATUS;
import com.maxtain.bebe.util.BabeConst;

public class QueryReg implements Runnable {

	private String query_url;
	private Context ctx;
	private ArrayList<NameValuePair> paramList;
	private IAccountListener ial;
	private static final String base_url = "http://api.babe.maxtain.com/users/";
	private String[] QueryURL = { "unique_username.php", "register.php",
			"login.php", "update.php","forget_password.php" };

	public static enum QueryType {
		UNIQUE, REGISTER, LOGIN, UPDATE, FORGET
	};

	public QueryReg(Context cxt, ArrayList<NameValuePair> paramList,
			QueryType query_type, IAccountListener ial) {
		this.ctx = cxt;
		this.paramList = paramList;
		switch (query_type) {
		case UNIQUE:
			this.query_url = this.base_url + this.QueryURL[0];
			break;
		case REGISTER:
			this.query_url = this.base_url + this.QueryURL[1];
			break;
		case LOGIN:
			this.query_url = this.base_url + this.QueryURL[2];
			break;
		case UPDATE:
			this.query_url = this.base_url + this.QueryURL[3];
			break;
		case FORGET:
			this.query_url = this.base_url + this.QueryURL[4];
			break;
		default:
			break;
		}

		this.ial = ial;
	}

	@Override
	public void run() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(this.query_url);

		ACCOUNT_STATUS as = ACCOUNT_STATUS.CANCEL;

		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
					this.paramList, HTTP.UTF_8);

			request.setEntity(requestHttpEntity);
			HttpResponse httpResponse = null;
			boolean success = false;
			try {
				httpResponse = httpClient.execute(request);
				success = true;
			} catch (Exception ex) {
				success = false;
				as = ACCOUNT_STATUS.NO_NETWORK;
				ex.printStackTrace();
			}
			if (success) {
				String ret = EntityUtils.toString(httpResponse.getEntity());
				Map<String, String> map = new HashMap<String, String>();
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(ret,
						new TypeReference<HashMap<String, String>>() {
						});
				String state = map.get("state");
				if (state.startsWith("err")) {
					as = ACCOUNT_STATUS.DATA_ERR;
				}
				if (state.startsWith("duplicate")) {
					as = ACCOUNT_STATUS.DUPLICATE;
				}
				if (state.equals("ok")) {
					as = ACCOUNT_STATUS.DATA_OK;
				}
				if (state.equals("no_user")) {
					as = ACCOUNT_STATUS.NO_USER;
				}
				if (state.equals("login")) {
					Editor ed = this.ctx.getSharedPreferences(
							BabeConst.SHAREP_DATABASE, Activity.MODE_PRIVATE)
							.edit();
					ed.putString(BabeConst.ACCOUNT_EMAIL, map.get("email"));
					ed.putString(BabeConst.ACCOUNT_PHONE, map.get("phone"));
					ed.putString(BabeConst.ACCOUNT_NICKNAME,
							map.get("nickname"));
					ed.putString(BabeConst.ACCOUNT_SEX,
							map.get("sex").equals("1") ? "男" : "女");
					ed.commit();
					as = ACCOUNT_STATUS.LOGON;
				}
				if (state.equals("done")) {
					as = ACCOUNT_STATUS.UPDATE_DONE;
				}

			} else {
				as = ACCOUNT_STATUS.NO_NETWORK;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.ial.callback(as);
	}

}
