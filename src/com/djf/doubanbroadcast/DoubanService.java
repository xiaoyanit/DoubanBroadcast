package com.djf.doubanbroadcast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;
import android.util.Log;

public class DoubanService {
	public static final String CALLBACK = "http://myappdjf.com";
	
	private String accessToken;
	private String user;
	
	public DoubanService(String token, String user) {
		this.accessToken = token;
		this.user = user;
	}

	public static String getAccessToken(String authorization_code) {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork()
				.penaltyLog()
				.build());
			HttpPost post = new HttpPost("https://www.douban.com/service/auth2/token");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("client_id", "06dccbf9c6a1907c149663ed53e4b174"));
			params.add(new BasicNameValuePair("client_secret", "eb595717d2aa329f"));
			params.add(new BasicNameValuePair("redirect_uri", CALLBACK));
			params.add(new BasicNameValuePair("grant_type", "authorization_code"));
			params.add(new BasicNameValuePair("code", authorization_code));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean newPost(String text){
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectNetwork()
		.penaltyLog()
		.build());
		HttpPost post =new HttpPost("https://api.douban.com/shuo/v2/statuses/");
		post.addHeader("Authorization", "Bearer "+accessToken);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("source", "06dccbf9c6a1907c149663ed53e4b174"));
		params.add(new BasicNameValuePair("text", text));
		params.add(new BasicNameValuePair("access_token", accessToken));
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			Log.i("!!!!!!!!!", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
