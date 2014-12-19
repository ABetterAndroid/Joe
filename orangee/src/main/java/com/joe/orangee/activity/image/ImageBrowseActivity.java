package com.joe.orangee.activity.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.joe.orangee.R;
import com.joe.orangee.adapter.PicsBrowserAdapter;
import com.joe.orangee.view.photoview.HackyViewPager;

import java.util.ArrayList;

public class ImageBrowseActivity extends Activity {

	private Context context;
	private HackyViewPager mViewPager;
	private int currentItem;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image_browse_activity);
		context = this;
		currentItem = getIntent().getIntExtra("current", 0);
		ArrayList<String> picList=getIntent().getStringArrayListExtra("imageList");

		mViewPager = (HackyViewPager) findViewById(R.id.pic_browser_vp);
		mViewPager.setAdapter(new PicsBrowserAdapter(context, picList));
		mViewPager.setCurrentItem(currentItem);
		
	}

}
