package com.joe.orangee.activity.my;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.adapter.OrangeeFragmentPagerAdapter;
import com.joe.orangee.util.Utils;

public class MyCommentActivity extends ActionBarActivity {

	private ViewPager vpMyComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_comment_activity);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
        Utils.setActionBarStyle(getSupportActionBar());
		vpMyComment = (ViewPager) findViewById(R.id.my_comment_vp);
		vpMyComment.setAdapter(new OrangeeFragmentPagerAdapter(getSupportFragmentManager()));
		vpMyComment.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				invalidateOptionsMenu();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_comment_menu, menu);
		menu.getItem(0).setTitle(vpMyComment.getCurrentItem()==0? R.string.my_comment_to_me: R.string.my_comment_by_me);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_my:
			if (vpMyComment.getCurrentItem()==0) {
				vpMyComment.setCurrentItem(1);
				item.setTitle(R.string.my_comment_by_me);
			}else {
				vpMyComment.setCurrentItem(0);
				item.setTitle(R.string.my_comment_to_me);
			}
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
