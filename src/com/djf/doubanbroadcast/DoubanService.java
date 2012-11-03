package com.djf.doubanbroadcast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

// TODO: Maybe make this a subclass of Service?
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
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void newPost(String text, Activity activity){
		PostNewBroadcastTask postTask = new PostNewBroadcastTask(activity);
		postTask.execute(text);
	}
	
	public void getPosts(Activity activity) {
		getAllPostsTask tast = new getAllPostsTask(activity);
		tast.execute();
	}

	private class getAllPostsTask extends AsyncTask<String, Void, Boolean> {
		private JSONArray posts;
		private Activity mActivity;
		
		public getAllPostsTask(Activity activity) {
			super();
			this.mActivity = activity;
		}
		
		private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		    public void onItemClick(AdapterView parent, View v, int position, long id) {
		    	mActivity.finish();
		    }
		};

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				HttpGet request =new HttpGet("https://api.douban.com/shuo/v2/statuses/home_timeline");
				request.addHeader("Authorization", "Bearer "+accessToken);

				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				posts = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

	     protected void onPostExecute(Boolean result) {
	    	 ListView view = (ListView) mActivity.findViewById(R.id.wrapper);
	    	 BroadcastListAdapter adapter = new BroadcastListAdapter(mActivity, posts);
	    	 view.setAdapter(adapter);
	    	 view.setOnItemClickListener(mMessageClickedHandler);
	     }
	}

	private class PostNewBroadcastTask extends AsyncTask<String, Void, String> {

		private Activity mActivity;
		public PostNewBroadcastTask(Activity activity) {
			super();
			this.mActivity = activity;
		}

		@Override
		protected String doInBackground(String... text) {
			HttpPost post =new HttpPost("https://api.douban.com/shuo/v2/statuses/");
			post.addHeader("Authorization", "Bearer "+accessToken);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("source", "06dccbf9c6a1907c149663ed53e4b174"));
			params.add(new BasicNameValuePair("text", text[0]));
			params.add(new BasicNameValuePair("access_token", accessToken));
			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(post);
				Log.i("!!!!!!!!!", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
				if (httpResponse.getStatusLine().getStatusCode()>=400) {
					JSONObject retJsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
					Log.i("?????", retJsonObject.getString("code"));
					Log.i("?????", retJsonObject.getString("msg"));
					Log.i("?????", retJsonObject.getString("request"));
					return retJsonObject.getString("msg");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return "You've posted a new broadcast.";
		}

	     protected void onPostExecute(String result) {
    		 Context context = mActivity.getApplicationContext();
    		 Toast toast = Toast.makeText(context, result, Toast.LENGTH_SHORT);
    		 toast.show();
	     }
	}
}
