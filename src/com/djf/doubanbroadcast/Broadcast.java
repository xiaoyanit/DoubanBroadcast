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
			if (attachs.length() != 0 && !attachs.getJSONObject(0).getString("description").equals("")) attach = attachs.getJSONObject(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasAttach() {
		return (attach != null);
	}

	public String getId() {
		try {
			return origin().getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
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
			String ret = origin().getString("comments_count") + "»Ø¸´  ";
			if (!origin().getString("like_count").equals("0")) ret += (origin().getString("like_count") + "Ï²»¶  ");
			if (!origin().getString("reshared_count").equals("0")) ret += (origin().getString("reshared_count") + "×ª²¥  ");
			return ret;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getLikeCount() {
		try {
			return origin().getString("like_count") + "ÔÞ";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getReshareCount() {
		try {
			return origin().getString("reshared_count") + "×ª²¥";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean Liked() {
		try {
			return obj.getBoolean("liked");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setLiked(boolean b) {
		try {
			obj.remove("liked");
			obj.put("liked", b);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
