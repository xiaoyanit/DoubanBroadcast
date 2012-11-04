package com.djf.doubanbroadcast;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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

    public BroadcastView(final AbstractActivity context, final JSONObject json) {
		super(context);
		this.item = new Broadcast(json);

		ImageView avatar = new ImageView(context);
		avatar.setMinimumHeight(50);
		avatar.setMinimumWidth(50);
		context.service.downloadImage(item.getOriginAvatar(), avatar);

		avatar.setPadding(14, 14, 14, 14);

		//TODO: cache avatar.
		addView(avatar);
		addView(makeContentView(context));

		//TODO: use fragment.
		if (context instanceof MainActivity) this.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(context, ItemActivity.class);
				intent.putExtra("json", json.toString());
				context.startActivity(intent);
			}
		});
	}

    //TODO: refactor this, user a template
    private LinearLayout makeContentView(Context context) {
    	LinearLayout content = new LinearLayout(context);
    	content.setOrientation(VERTICAL);
    	content.setPadding(0, 12, 25, 12);

    	TextView title = new TextView(context);
		String text = item.getOriginScreenName() + " "	+ item.getOriginTitle() + " " + item.getAttachTitle();
		title.setText(text);
		title.setTextColor(0xff606060);
		content.addView(title);

		if (!(text = item.getAttachDesc()).equals("")) {
			TextView description = new TextView(context);
			description.setText(DoubanUtil.stripText(text));
			description.setBackgroundColor(0xffe0e0e0);
			description.setTextSize(10);
			description.setPadding(3, 3, 3, 3);
			content.addView(description);
		}

		TextView saying = new TextView(context);
		text = item.getOriginText();
		if (!text.equals("")) {
			saying.setText(DoubanUtil.stripText(text));
			saying.setTextSize(12);
			content.addView(saying);
		}

		TextView extra = new TextView(context);
		text = item.getExtra();
		if (item.reshared) text += ("  ÓÉ" + item.getScreenName() + "×ª²¥");
		extra.setText(text);
		extra.setTextColor(0xff888888);
		content.addView(extra);
    	return content;
    }
}