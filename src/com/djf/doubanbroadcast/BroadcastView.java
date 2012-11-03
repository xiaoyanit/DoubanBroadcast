package com.djf.doubanbroadcast;

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
	private Broadcast item;
	
	public BroadcastView(Context context) {
		super(context);
	}

    public BroadcastView(final MainActivity context, final JSONObject json) {
		super(context);
		this.item = new Broadcast(json);

		ImageView avatar = new ImageView(context);
		context.service.downloadImage(item.getOriginAvatar(), avatar);

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
    			.replaceFirst("\\[/score\\]", "ÐÇ )")
    			.replaceAll("\\r", "")
    			.replaceAll("\\n", "")
    			.replaceAll(" +", " ");
    	Log.i("after", text);
    	return text;
    }

    //TODO: refactor this, user a template
    private LinearLayout makeContentView(Context context) {
    	LinearLayout content = new LinearLayout(context);
    	content.setOrientation(VERTICAL);
    	content.setPadding(0, 12, 25, 12);
		
    	TextView title = new TextView(context);
		String text = item.getOriginScreenName() + " "	+ item.getOriginTitle() + " " + item.getAttachTitle();
		title.setText(stripText(text));
		title.setTextColor(0xff606060);
		content.addView(title);

		if ((text = item.getAttachDesc()) != "") {
			TextView description = new TextView(context);
			description.setText(stripText(text));
			description.setBackgroundColor(0xffe0e0e0);
			description.setTextSize(10);
			description.setPadding(3, 3, 3, 3);
			content.addView(description);
		}

		TextView saying = new TextView(context);
		text = item.getOriginText();
		if (!text.equals("")) {
			saying.setText("\" "+stripText(text)+" \"");
			saying.setTextSize(12);
			content.addView(saying);
		}

		if (item.reshared) {
			TextView extra = new TextView(context);
			extra.setText("ÓÉ" + item.getScreenName() + "×ª²¥");
			content.addView(extra);
		}
    	return content;
    }
}