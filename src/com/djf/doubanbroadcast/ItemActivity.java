package com.djf.doubanbroadcast;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemActivity extends Activity{
	public DoubanService service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.item);
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		service = new DoubanService(preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, null), preference.getString(DoubanUtil.PREF_USER, null));
		
		ImageView avatar = (ImageView) findViewById(R.id.item_avatar);
		avatar.setScaleType(ImageView.ScaleType.FIT_XY);
		TextView user = (TextView) findViewById(R.id.item_user);
		
		JSONObject json;
		try {
			json = new JSONObject(getIntent().getStringExtra("json"));
			service.downloadImage(json.getJSONObject("user").getString("small_avatar"), avatar);
			user.setText(json.getJSONObject("user").getString("screen_name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
