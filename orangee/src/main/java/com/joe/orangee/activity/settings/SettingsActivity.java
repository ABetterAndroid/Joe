package com.joe.orangee.activity.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.util.Utils;

public class SettingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
        Utils.setActionBarStyle(getSupportActionBar(), R.string.settings);
		getFragmentManager().beginTransaction().replace(R.id.settings, new SettingsFragment()).commit();
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
