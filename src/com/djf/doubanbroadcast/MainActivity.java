package com.djf.doubanbroadcast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	public DoubanService service;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

		SharedPreferences preference = this.getSharedPreferences("pref!", MODE_PRIVATE);
		service = new DoubanService(preference.getString("token", null), preference.getString("user", null));
		service.newPost("test api [From my iToilet]");
	}

	public void logout(View view) {
		SharedPreferences preference = this.getSharedPreferences("pref!", MODE_PRIVATE);
		preference.edit().clear().commit();
		finish();
	}
}
