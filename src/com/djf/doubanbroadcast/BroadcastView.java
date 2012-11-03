package com.djf.doubanbroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//TODO: refactor this, it's a mess now...
public class BroadcastView extends LinearLayout {
	private JSONObject json;

    public BroadcastView(MainActivity context, JSONObject json) {
		super(context);
		this.json = json;

		ImageView avatar = new ImageView(context);
		try {
			context.service.downloadImage(json.getJSONObject("user").getString("small_avatar"), avatar);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		avatar.setPadding(10, 10, 10, 10);

		addView(avatar);
		addView(makeContentView(context));
	}

    private String stripText(String text) {
    	Log.i("before", text);
    	text = text.replaceFirst("\\[score\\]", " ( ")
    			.replaceFirst("\\[/score\\]", "ÐÇ )")
    			.replaceAll("\\r", "")
    			.replaceAll("\\n", "")
    			.replaceAll(" +", " ");
    	Log.i("after", text);
    	return text;
    }

    private LinearLayout makeContentView(Context context) {
    	LinearLayout content = new LinearLayout(context);
    	content.setOrientation(VERTICAL);
    	content.setPadding(0, 10, 20, 0);
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
			title.setTextColor(0xff202020);
			content.addView(title);

			if (attach != null && !attach.getString("description").equals("")) {
				TextView description = new TextView(context);
				description.setText(stripText(attach.getString("description")));
				description.setBackgroundColor(0xffdddddd);
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
				extra.setText("ÓÉ"+json.getJSONObject("user").getString("screen_name")+"×ª²¥");
				content.addView(extra);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return content;
    }
}