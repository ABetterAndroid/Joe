package com.joe.orangee.adapter;

import com.joe.orangee.fragment.weibo.NearbyWeiboFragment;
import com.joe.orangee.fragment.weibo.WeiboStatusFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter{

	private WeiboStatusFragment statusFragment;
	private NearbyWeiboFragment nearbyFragment;
	
	public HomeFragmentPagerAdapter(FragmentManager fm, WeiboStatusFragment statusFragment, NearbyWeiboFragment nearbyFragment) {
		super(fm);
		this.statusFragment=statusFragment;
		this.nearbyFragment=nearbyFragment;
	}

	@Override
	public Fragment getItem(int position) {
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
