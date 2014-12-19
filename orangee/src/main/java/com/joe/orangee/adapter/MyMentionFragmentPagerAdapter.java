package com.joe.orangee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.joe.orangee.fragment.mycomment.MyCommentFragment;
import com.joe.orangee.fragment.weibo.WeiboStatusFragment;
import com.joe.orangee.util.Constants;

public class MyMentionFragmentPagerAdapter extends FragmentPagerAdapter{

	
	public MyMentionFragmentPagerAdapter(FragmentManager fm ) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return position==0? new WeiboStatusFragment(Constants.URL_STATUS_MENTION): MyCommentFragment.newInstance(Constants.URL_COMMENT_MENTION, true);
	}
	

	@Override
	public int getCount() {
		return 2;
	}

}
