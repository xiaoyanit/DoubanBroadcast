package com.djf.doubanbroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Broadcast extends JSONObject{
	private JSONObject obj;
	public boolean reshared;
	private JSONObject attach = null;

	public Broadcast(JSONObject object) {
		obj = object;
		reshared = obj.has("reshared_status");
		try {
			JSONArray attachs = origin().getJSONArray("attachments");
			if (attachs.length() != 0) attach = attachs.getJSONObject(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getOriginAvatar() {
		try {
			return origin().getJSONObject("user").getString("small_avatar");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getOriginScreenName() {
		try {
			return DoubanUtil.stripText(origin().getJSONObject("user").getString("screen_name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getOriginTitle() {
		try {
			return DoubanUtil.stripText(origin().getString("title"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getOriginText() {
		try {
			return DoubanUtil.stripText(origin().getString("text").equals("") ? "" : "\" " + origin().getString("text") +" \"");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAttachTitle() {
		try {
			return DoubanUtil.stripText((attach == null) ? "" : attach.getString("title"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAttachDesc() {
		try {
			return DoubanUtil.stripText((attach == null) ? "" : attach.getString("description"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAttachImg() {
		try {
			if (attach == null) return "";
			return attach.getJSONArray("media").getJSONObject(0).getString("src");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	private JSONObject origin() {
		try {
			if (reshared) return obj.getJSONObject("reshared_status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public String getScreenName() {
		try {
			return obj.getJSONObject("user").getString("screen_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getExtra() {
		try {
			return origin().getString("comments_count") + "»Ø¸´ " + origin().getString("like_count") + "Ï²»¶ " + origin().getString("reshared_count") + "×ª²¥ ";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}
