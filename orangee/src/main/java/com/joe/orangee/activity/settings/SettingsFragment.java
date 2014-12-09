package com.joe.orangee.activity.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;
import com.joe.orangee.R;
import com.joe.orangee.net.CommonDownloader;
import com.joe.orangee.util.PreferencesKeeper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class SettingsFragment extends PreferenceFragment implements OnPreferenceClickListener {

	private Preference logout;
	private Preference clearCache;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		logout = findPreference("logout");
		clearCache = findPreference("clear_cache");
		logout.setOnPreferenceClickListener(this);
		clearCache.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference==logout) {
			Oauth2AccessToken mAccessToken = PreferencesKeeper.readAccessToken(getActivity());
			if (mAccessToken != null && mAccessToken.isSessionValid()) {
				logout();
            } else {
				Toast.makeText(getActivity(), R.string.logout_fail, Toast.LENGTH_SHORT).show();
            }
		}else if (preference==clearCache) {
			clearCache();
		}
		return false;
	}

	private void clearCache() {
		
		new AsyncTask<Void, Void, Void>() {


			@Override
			protected Void doInBackground(Void... params) {
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.clearDiskCache();
				imageLoader.clearMemoryCache();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(getActivity(), R.string.clear_cache_success, Toast.LENGTH_SHORT).show();
				super.onPostExecute(result);
			}
		}.execute();
	}

	private void logout() {
		new AsyncTask<Void, Void, Void>() {

			private int logoutResult;

			@Override
			protected Void doInBackground(Void... params) {
				logoutResult = new CommonDownloader(getActivity()).logout();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (logoutResult==0) {
					Toast.makeText(getActivity(), R.string.logout_fail, Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(getActivity(), R.string.logout_success, Toast.LENGTH_SHORT).show();
					PreferencesKeeper.clearToken(getActivity());
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id==android.R.id.home) {
			getActivity().finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
