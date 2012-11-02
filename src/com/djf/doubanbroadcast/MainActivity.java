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

		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		service = new DoubanService(preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, null), preference.getString(DoubanUtil.PREF_USER, null));
	}

	public void logout(View view) {
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		preference.edit().clear().commit();
		finish();
	}
}
