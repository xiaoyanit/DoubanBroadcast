package com.djf.doubanbroadcast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class EntryActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		if (preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, "") != "") {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	public void authenticate(View view) {
		Intent intent = new Intent(this, OAuth2Activity.class);
		startActivity(intent);
	}
}
