package com.joe.orangee.activity.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.fragment.web.WebFragment;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;

public class OrangeeWebActivity extends ActionBarActivity {

	private String url;
	private Toolbar toolbar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar(), 0);
		url = getIntent().getStringExtra(Constants.URL);
		getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.web_container, WebFragment.newInstance(url, toolbar))
                .commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.orangee_web_menu, menu);
		return true;
	}

	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.enter_sec, R.anim.exit_sec_up_down);
			break;
		case R.id.action_web_go:
			Uri uri = Uri.parse(url);  
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
			startActivity(intent);
			break;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_sec, R.anim.exit_sec_up_down);
	}


}
