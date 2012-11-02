package com.djf.doubanbroadcast;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OAuth2Activity extends Activity {
	public static String AuthenticateURL = "https://www.douban.com/service/auth2/auth?client_id=06dccbf9c6a1907c149663ed53e4b174&redirect_uri=http://myappdjf.com&response_type=code";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
		setContentView(webView);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.startsWith(DoubanService.CALLBACK)) {
					view.stopLoading();
					Uri uri = Uri.parse(url);
					if (uri.getQueryParameter("error") == null) {
						try {
							JSONObject retJsonObject = new JSONObject(DoubanService.getAccessToken(uri.getQueryParameter("code")));
							String token = retJsonObject.getString("access_token");
							String user = retJsonObject.getString("douban_user_id");
							SharedPreferences preference = getSharedPreferences("pref!", MODE_PRIVATE);
							preference.edit().putString("token", token).putString("user", user).commit();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					finish();
				}
			}
		});

		webView.loadUrl(AuthenticateURL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
