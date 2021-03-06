package com.joe.orangee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.joe.orangee.fragment.mycomment.MyCommentFragment;
import com.joe.orangee.util.Constants;

public class OrangeeFragmentPagerAdapter extends FragmentPagerAdapter{

    private String[] titleArray;
	
	public OrangeeFragmentPagerAdapter(FragmentManager fm, String[] titleArray) {

        super(fm);
        this.titleArray=titleArray;

	}

	@Override
	public Fragment getItem(int position) {
		if (position==0) {
			return MyCommentFragment.newInstance(Constants.URL_COMMENTS_TO_ME, true);
		}else {
			return MyCommentFragment.newInstance(Constants.URL_COMMENTS_BY_ME, false);
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

    @Override
    public CharSequence getPageTitle(int position) {
        return titleArray[position];
    }

}
