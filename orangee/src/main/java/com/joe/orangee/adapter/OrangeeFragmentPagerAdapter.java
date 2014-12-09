package com.joe.orangee.adapter;

import com.joe.orangee.fragment.mycomment.MyCommentFragment;
import com.joe.orangee.util.Constants;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrangeeFragmentPagerAdapter extends FragmentPagerAdapter{

	
	public OrangeeFragmentPagerAdapter(FragmentManager fm ) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position==0) {
			return new MyCommentFragment(Constants.URL_COMMENTS_TO_ME, true);
		}else {
			return new MyCommentFragment(Constants.URL_COMMENTS_BY_ME, false);
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

}
