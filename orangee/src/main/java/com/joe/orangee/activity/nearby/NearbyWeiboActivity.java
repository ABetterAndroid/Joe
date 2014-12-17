package com.joe.orangee.activity.nearby;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.fragment.nearby.NearbyWeiboFragment;
import com.joe.orangee.util.Utils;

public class NearbyWeiboActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_activity);
		
		NearbyWeiboFragment nearbyFragment=new NearbyWeiboFragment(getIntent().getDoubleExtra("latitude", 0.0),
				getIntent().getDoubleExtra("longitude", 0.0));

		getFragmentManager()
		.beginTransaction()
		.replace(R.id.nearby_frame, nearbyFragment)
		.commit();
		
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar(), R.string.status_nearby);
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
