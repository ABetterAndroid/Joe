package com.joe.orangee.activity.weibo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.activity.base.BaseActivity;
import com.joe.orangee.fragment.weibo.FavouritesFragment;
import com.joe.orangee.util.Utils;

public class WeiboCollectionActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_collection_activity);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar(), R.string.my_favourite);
        if (isNetworkOK()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.collection_container, new FavouritesFragment())
                    .commit();
        }
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
