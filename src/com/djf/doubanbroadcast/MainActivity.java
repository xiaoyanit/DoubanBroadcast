package com.djf.doubanbroadcast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AbstractActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		service = new DoubanService(preference.getString(DoubanUtil.PREF_ACCESS_TOKEN, null), preference.getString(DoubanUtil.PREF_USER, null), this);
		service.getPosts();
	}

	public void logout(View view) {
		SharedPreferences preference = this.getSharedPreferences(DoubanUtil.PREF, MODE_PRIVATE);
		preference.edit().clear().commit();
		finish();
	}

	public void postNewBroadcast(View view) {
		EditText editText = (EditText) findViewById(R.id.newBroadcastText);
		service.newPost(editText.getText().toString());
	}
}
 