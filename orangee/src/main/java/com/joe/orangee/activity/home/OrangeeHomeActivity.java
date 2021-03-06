package com.joe.orangee.activity.home;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.joe.orangee.R;
import com.joe.orangee.activity.weibo.WeiboEditActivity;
import com.joe.orangee.fragment.drawer.NavigationDrawerFragment;
import com.joe.orangee.fragment.weibo.WeiboStatusFragment;
import com.joe.orangee.service.MessageListenerService;
import com.joe.orangee.util.Utils;

public class OrangeeHomeActivity extends ActionBarActivity {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;
	private DrawerLayout mDrawerLayout;
	private WeiboStatusFragment statusFragment;
    public static View hotKey;
    public static ImageView ivKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		Intent intent=new Intent(this, MessageListenerService.class);
		startService(intent);

		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		/*mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);*/
		mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment=new NavigationDrawerFragment(this);
        mNavigationDrawerFragment.setUp(mDrawerLayout, toolbar);
        statusFragment = new WeiboStatusFragment(toolbar);
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.container, statusFragment)
		.replace(R.id.drawer, mNavigationDrawerFragment)
		.commit();

        hotKey = findViewById(R.id.hot_key);
        ivKey= (ImageView) findViewById(R.id.key_img);
        hotKey.setVisibility(View.GONE);
        /*hotKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusFragment.onRefresh();
            }
        });*/
        registerReceiver(new FinishReceiver(), new IntentFilter("com.joe.orangee.finish"));
    }

	@SuppressWarnings("deprecation")
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.orangee_home_menu, menu);

	    MenuCompat.setShowAsAction(menu.findItem(R.id.action_home_refresh), MenuItem.SHOW_AS_ACTION_ALWAYS);
	    MenuCompat.setShowAsAction(menu.findItem(R.id.action_home_edit), MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_home_refresh:
			statusFragment.onRefresh();
			break;
		case R.id.action_home_edit:
			startActivity(new Intent(this, WeiboEditActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
			mDrawerLayout.closeDrawer(Gravity.START);
		}else {
			super.onBackPressed();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        if (hotKey.getVisibility()==View.INVISIBLE) {
            Utils.showHotKey(hotKey);
        }
    }

    class FinishReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "com.joe.orangee.finish") {
                finish();
            }
        }
    }

}
