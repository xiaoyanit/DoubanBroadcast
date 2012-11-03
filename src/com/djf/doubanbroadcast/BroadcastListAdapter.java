package com.djf.doubanbroadcast;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BroadcastListAdapter extends BaseAdapter {
	private Context context;
	private JSONArray sayings;
	public BroadcastListAdapter(Context context, JSONArray sayings) {
		this.context = context;
		this.sayings = sayings;
	}

	public int getCount() {
		return sayings.length();
	}

	public Object getItem(int position) {
		try {
			return sayings.getJSONObject(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			return new BroadcastView(context, sayings.getJSONObject(position));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}