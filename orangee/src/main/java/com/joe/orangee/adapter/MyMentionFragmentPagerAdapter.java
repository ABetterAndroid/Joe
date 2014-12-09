package com.joe.orangee.adapter;

import com.joe.orangee.fragment.mycomment.MyCommentFragment;
import com.joe.orangee.fragment.weibo.WeiboStatusFragment;
import com.joe.orangee.util.Constants;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyMentionFragmentPagerAdapter extends FragmentPagerAdapter{

	
	public MyMentionFragmentPagerAdapter(FragmentManager fm ) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return position==0? new WeiboStatusFragment(Constants.URL_STATUS_MENTION): new MyCommentFragment(Constants.URL_COMMENT_MENTION, true);
	}
	

	@Override
	public int getCount() {
		return 2;
	}

}
