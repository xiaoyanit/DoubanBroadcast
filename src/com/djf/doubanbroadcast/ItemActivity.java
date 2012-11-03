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
	private Broadcast item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.item);
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		service = new DoubanService(preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, null), preference.getString(DoubanUtil.PREF_USER, null));
		
		ImageView avatar = (ImageView) findViewById(R.id.item_avatar);
		ImageView media = (ImageView) findViewById(R.id.item_attach_img);
		TextView user = (TextView) findViewById(R.id.item_user);
		TextView action = (TextView) findViewById(R.id.item_user_action);
		TextView desc = (TextView) findViewById(R.id.item_attach_desc);

		try {
			item = new Broadcast(new JSONObject(getIntent().getStringExtra("json")));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		service.downloadImage(item.getOriginAvatar(), avatar);
		user.setText(item.getOriginScreenName());
		action.setText(item.getOriginTitle());

		String attachImage = item.getAttachImg();
		if (!attachImage.equals("")) {
			service.downloadImage(attachImage, media);
		}
		desc.setText(item.getAttachDesc());
		
	}
}
