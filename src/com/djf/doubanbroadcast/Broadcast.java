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
			JSONArray attachs = object.getJSONArray("attachments");
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
			return origin().getJSONObject("user").getString("screen_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getOriginTitle() {
		try {
			return origin().getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getOriginText() {
		try {
			return origin().getString("text");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAttachTitle() {
		try {
			return (attach == null) ? "" : attach.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAttachDesc() {
		try {
			return (attach == null) ? "" : attach.getString("description");
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
}
