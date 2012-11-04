package com.djf.doubanbroadcast;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemActivity extends AbstractActivity{
	private Broadcast item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.item);
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		service = new DoubanService(preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, null), preference.getString(DoubanUtil.PREF_USER, null), this);

		ImageView avatar = (ImageView) findViewById(R.id.item_avatar);
		ImageView media = (ImageView) findViewById(R.id.item_attach_img);
		TextView user = (TextView) findViewById(R.id.item_user);
		TextView action = (TextView) findViewById(R.id.item_user_action);
		TextView desc = (TextView) findViewById(R.id.item_attach_desc);
		TextView saying = (TextView) findViewById(R.id.item_saying);
		TextView likes = (TextView) findViewById(R.id.item_liked);
		TextView reshares = (TextView) findViewById(R.id.item_reshared);
		Button post = (Button) findViewById(R.id.item_new_button);

		try {
			item = new Broadcast(new JSONObject(getIntent().getStringExtra("json")));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		likes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (item.Liked()) service.unlike(item);
					else service.like(item);
			}
		});
		
		reshares.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				service.reshare(item);
			}
		});

		service.downloadImage(item.getOriginAvatar(), avatar);
		user.setText(item.getOriginScreenName());
		action.setText(item.getOriginTitle());

		if (item.hasAttach()) {
			String attachImage = item.getAttachImg();
			if (!attachImage.equals("")) {
				service.downloadImage(attachImage, media);
			}
			desc.setText(item.getAttachDesc());
		} else {
			((LinearLayout) findViewById(R.id.item_attach)).setBackgroundColor(0xffffffff);
		}
		saying.setText(item.getOriginText());

		likes.setText(item.getLikeCount());
		reshares.setText(item.getReshareCount());

		service.getComments(item.getId());
	}

	public void postNewComment(View view) {
		EditText newcomment = (EditText) findViewById(R.id.item_new_comment);
		service.postComment(item.getId(), newcomment.getText().toString());
	}
}
