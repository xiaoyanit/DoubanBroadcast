package com.djf.doubanbroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//TODO: refactor this, it's a mess now...
public class BroadcastView extends LinearLayout {
	private JSONObject json;

    public BroadcastView(final MainActivity context, final JSONObject json) {
		super(context);
		this.json = json;

		ImageView avatar = new ImageView(context);
		try {
			context.service.downloadImage(json.getJSONObject("user").getString("small_avatar"), avatar);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		avatar.setPadding(14, 14, 14, 14);

		//TODO: cache avatar.
		addView(avatar);
		addView(makeContentView(context));
		
		this.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(context, ItemActivity.class);
				intent.putExtra("json", json.toString());
				context.startActivity(intent);
			}
		});
	}

    private String stripText(String text) {
    	Log.i("before", text);
    	text = text.replaceFirst("\\[score\\]", " ( ")
    			.replaceFirst("\\[/score\\]", "�� )")
    			.replaceAll("\\r", "")
    			.replaceAll("\\n", "")
    			.replaceAll(" +", " ");
    	Log.i("after", text);
    	return text;
    }

    private LinearLayout makeContentView(Context context) {
    	LinearLayout content = new LinearLayout(context);
    	content.setOrientation(VERTICAL);
    	content.setPadding(0, 12, 25, 12);
		try {
			String text;
			JSONObject reshare = json.has("reshared_status") ? json.getJSONObject("reshared_status") : null;
			JSONArray attachArray = (reshare != null ? reshare : json).getJSONArray("attachments");

			JSONObject attach = (attachArray.length() == 0) ? null : attachArray.getJSONObject(0);

			TextView title = new TextView(context);
			text = (reshare != null ? reshare : json).getJSONObject("user").getString("screen_name") + " "
					+ (reshare != null ? reshare : json).getString("title") + " "
					+ (attach != null ? attach.getString("title") : "");
			title.setText(stripText(text));
			title.setTextColor(0xff404040);
			content.addView(title);

			if (attach != null && !attach.getString("description").equals("")) {
				TextView description = new TextView(context);
				description.setText(stripText(attach.getString("description")));
				description.setBackgroundColor(0xffe0e0e0);
				description.setTextSize(10);
				description.setPadding(3, 3, 3, 3);
				content.addView(description);
			}

			TextView saying = new TextView(context);
			text = (reshare != null ? reshare : json).getString("text");
			if (!text.equals("")) {
				saying.setText("\" "+stripText(text)+" \"");
				saying.setTextSize(12);
				content.addView(saying);
			}

			if (reshare != null) {
				TextView extra = new TextView(context);
				extra.setText("��"+json.getJSONObject("user").getString("screen_name")+"ת��");
				content.addView(extra);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return content;
    }
}