package com.joe.orangee.activity.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.fragment.nearby.NearbyMapFragment;
import com.joe.orangee.util.Utils;

public class NearbyWeiboMapActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_map_activity);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nearby_map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
			
		case R.id.action_map_list:
			Intent intent=new Intent(this, NearbyWeiboActivity.class);
			intent.putExtra("latitude", NearbyMapFragment.currentLat);
			intent.putExtra("longitude", NearbyMapFragment.currentLng);
			startActivity(intent);
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
