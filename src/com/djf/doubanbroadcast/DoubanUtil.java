package com.djf.doubanbroadcast;

import android.util.Log;

public class DoubanUtil {
	public static final String PREF = "PrefDouban";
	public static final String API_KEY = "06dccbf9c6a1907c149663ed53e4b174";
	public static final String SECRET = "eb595717d2aa329f";
	public static final String CALLBACK = "http://myappdjf.com";
	public static final String PREF_ACCESS_TOKEN = "access_token";
	public static final String PREF_USER = "user";

    public static String stripText(String text) {
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
