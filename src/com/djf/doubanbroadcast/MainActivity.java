package com.djf.doubanbroadcast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	public DoubanService service;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		service = new DoubanService(preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, null), preference.getString(DoubanUtil.PREF_USER, null));
		service.getPosts(this);
	}

	public void logout(View view) {
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		preference.edit().clear().commit();
		finish();
	}
	
	public void postNewBroadcast(View view) {
		EditText editText = (EditText) findViewById(R.id.newBroadcastText);
		service.newPost(editText.getText().toString(), this);
	}
}
