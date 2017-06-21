package mobi.checkapp.epoc.chat.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

import mobi.checkapp.epoc.R;

public class MainPrefs extends PreferenceActivity {
	private final String TAG = this.getClass().getName();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.mainprefs);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setTitle(getResources().getString(R.string.title_activity_prefschat));
		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Log.d(TAG, "Back button clicked");
			super.onBackPressed();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
