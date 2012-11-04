package com.djf.doubanbroadcast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Maybe make this a subclass of Service?
public class DoubanService {
	public static final String CALLBACK = "http://myappdjf.com";

	private String accessToken;
	private String user;
	private AbstractActivity activity;

	public DoubanService(String token, String user, AbstractActivity activity) {
		this.accessToken = token;
		this.user = user;
		this.activity = activity;
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

	public void downloadImage(String url, ImageView view) {
		DownloadImageTask task = new DownloadImageTask(view);
		task.execute(url);
	}

	public void newPost(String text){
		PostNewBroadcastTask postTask = new PostNewBroadcastTask(activity);
		postTask.execute(text);
	}

	public void getPosts() {
		GetAllPostsTask task = new GetAllPostsTask(activity);
		task.execute();
	}

	public void getComments(String postId) {
		GetAllCommentsTask task = new GetAllCommentsTask(activity);
		task.execute(postId);
	}

	public void postComment(String postId, String text) {
		PostNewCommentTask task = new PostNewCommentTask(activity);
		task.execute(postId, text);
	}

	public void like(Broadcast item) {
		if (!item.Liked()) {
			LikeTask task = new LikeTask(activity, item);
			task.execute(item.getId());
		}
	}

	public void unlike(Broadcast item) {
		if (item.Liked()) {
			UnlikeTask task = new UnlikeTask(activity, item);
			task.execute(item.getId());
		}
	}

	public void reshare(Broadcast item) {
		ReshareTask task = new ReshareTask(activity, item);
		task.execute(item.getId());
	}

	public class ReshareTask extends AsyncTask<String, Void, Boolean> {
		private AbstractActivity mActivity;
		private Broadcast item;

		public ReshareTask(AbstractActivity activity, Broadcast item) {
			super();
			this.mActivity = activity;
			this.item = item;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			HttpPost request = new HttpPost("https://api.douban.com/shuo/v2/statuses/" + params[0] + "/reshare");
			request.addHeader("Authorization", "Bearer "+accessToken);
			try {
				HttpResponse response = new DefaultHttpClient().execute(request);
				if (response.getStatusLine().getStatusCode() < 400) return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
	    		Toast toast = Toast.makeText(mActivity, "You've reshared it.", Toast.LENGTH_SHORT);
	    		toast.show();
			}
		}
	}

	public class LikeTask extends AsyncTask<String, Void, Boolean> {
		private AbstractActivity mActivity;
		private Broadcast item;

		public LikeTask(AbstractActivity activity, Broadcast item) {
			super();
			this.mActivity = activity;
			this.item = item;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			HttpPost request = new HttpPost("https://api.douban.com/shuo/v2/statuses/" + params[0] + "/like");
			request.addHeader("Authorization", "Bearer "+accessToken);
			try {
				HttpResponse response = new DefaultHttpClient().execute(request);
				if (response.getStatusLine().getStatusCode()<400) return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
	    		Toast toast = Toast.makeText(mActivity, "You've liked it.", Toast.LENGTH_SHORT);
	    		toast.show();
	 			item.setLiked(true);

	 			TextView view = (TextView)activity.findViewById(R.id.item_liked);
	 			view.setText("рятч");
			}
		}
	}

	public class UnlikeTask extends AsyncTask<String, Void, Boolean> {
		private AbstractActivity mActivity;
		private Broadcast item;

		public UnlikeTask(AbstractActivity activity, Broadcast item) {
			super();
			this.mActivity = activity;
			this.item = item;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			HttpDelete request = new HttpDelete("https://api.douban.com/shuo/v2/statuses/" + params[0] + "/like");
			request.addHeader("Authorization", "Bearer "+accessToken);
			try {
				HttpResponse response = new DefaultHttpClient().execute(request);
				if (response.getStatusLine().getStatusCode()<400) return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast toast = Toast.makeText(mActivity, "You've unliked it.", Toast.LENGTH_SHORT);
				toast.show();
				item.setLiked(false);

	 			TextView view = (TextView)activity.findViewById(R.id.item_liked);
	 			view.setText(item.getLikeCount());
			}
		}
	}

	public class DownloadImageTask extends AsyncTask<String, Void, Boolean> {
		private Bitmap bitmap;
		private ImageView target;

		public DownloadImageTask(ImageView view) {
			super();
			target = view;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			HttpGet request = new HttpGet(params[0]);
			HttpResponse response;
			try {
				response = new DefaultHttpClient().execute(request);
				bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
				Log.i("why",response.getStatusLine().getStatusCode()+"");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			target.setImageBitmap(bitmap);
		}
		
	}

	private class GetAllPostsTask extends AsyncTask<String, Void, Boolean> {
		private JSONArray posts;
		private AbstractActivity mActivity;

		public GetAllPostsTask(AbstractActivity activity) {
			super();
			this.mActivity = activity;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				HttpGet request = new HttpGet("https://api.douban.com/shuo/v2/statuses/home_timeline");
				request.addHeader("Authorization", "Bearer "+accessToken);

				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				posts = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
	     protected void onPostExecute(Boolean result) {
	    	 ListView view = (ListView) mActivity.findViewById(R.id.wrapper);
	    	 DoubanListAdapter adapter = new DoubanListAdapter(mActivity, posts);
	    	 view.setAdapter(adapter);
	     }
	}

	private class GetAllCommentsTask extends AsyncTask<String, Void, Boolean> {
		private JSONArray comments;
		private AbstractActivity mActivity;

		public GetAllCommentsTask(AbstractActivity activity) {
			super();
			this.mActivity = activity;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				HttpGet request = new HttpGet("https://api.douban.com/shuo/v2/statuses/" + params[0] + "/comments");
				request.addHeader("Authorization", "Bearer "+accessToken);

				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				comments = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));
				Log.i("comment count", comments.length()+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
	     protected void onPostExecute(Boolean result) {
	    	 ListView view = (ListView) mActivity.findViewById(R.id.comments);
	    	 DoubanListAdapter adapter = new DoubanListAdapter(mActivity, comments);
	    	 view.setAdapter(adapter);
	     }
	}

	private class PostNewCommentTask extends AsyncTask<String, Void, Boolean> {
		private JSONArray comments;
		private AbstractActivity mActivity;
		private String id;

		public PostNewCommentTask(AbstractActivity activity) {
			super();
			this.mActivity = activity;
		}

		@Override
		protected Boolean doInBackground(String... text) {
			try {
				id = text[0];
				HttpPost request = new HttpPost("https://api.douban.com/shuo/v2/statuses/" + id + "/comments");
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("source", "06dccbf9c6a1907c149663ed53e4b174"));
				params.add(new BasicNameValuePair("text", text[1]));
				params.add(new BasicNameValuePair("access_token", accessToken));
				request.addHeader("Authorization", "Bearer "+accessToken);

				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse response = new DefaultHttpClient().execute(request);
				if (response.getStatusLine().getStatusCode()<400) return true;
				Log.i("comment count", comments.length()+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
	     protected void onPostExecute(Boolean result) {
			if (!result) return;

	    	Toast toast = Toast.makeText(mActivity, "Successfully posted new Comment.", Toast.LENGTH_SHORT);
	    	toast.show();
	    	getComments(id);
	     }
	}

	private class PostNewBroadcastTask extends AsyncTask<String, Void, String> {

		private AbstractActivity mActivity;
		public PostNewBroadcastTask(AbstractActivity activity) {
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
				if (httpResponse.getStatusLine().getStatusCode()>=400) {
					JSONObject retJsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
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

		@Override
	     protected void onPostExecute(String result) {
    		 Toast toast = Toast.makeText(mActivity, result, Toast.LENGTH_SHORT);
    		 toast.show();
    		 getPosts();
	     }
	}
}
