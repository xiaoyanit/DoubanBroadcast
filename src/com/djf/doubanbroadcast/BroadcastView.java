package com.djf.doubanbroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BroadcastView extends LinearLayout {
	private TextView title;
	private TextView content;
	private TextView description;
	private TextView extra;

    public BroadcastView(Context context, JSONObject json) {
		super(context);
		
		setOrientation(VERTICAL);

		try {
			String text;
			JSONObject reshare = json.has("reshared_status") ? json.getJSONObject("reshared_status") : null;
			JSONArray attachArray = (reshare != null ? reshare : json).getJSONArray("attachments");

			JSONObject attach = (attachArray.length() == 0) ? null : attachArray.getJSONObject(0);

			title = new TextView(context);
			text = (reshare != null ? reshare : json).getJSONObject("user").getString("screen_name") + " "
					+ (reshare != null ? reshare : json).getString("title") + " "
					+ (attach != null ? attach.getString("title") : "");
			title.setText(stripText(text));
			addView(title);

			content = new TextView(context);
			text = (reshare != null ? reshare : json).getString("text");
			content.setText(stripText(text));
			addView(content);

			if (attach != null && !attach.getString("description").equals("")) {
				description = new TextView(context);
				description.setText(stripText(attach.getString("description")));
				addView(description);
			}

			if (reshare != null) {
				extra = new TextView(context);
				extra.setText("ÓÉ"+json.getJSONObject("user").getString("screen_name")+"×ª²¥");
				addView(extra);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
}