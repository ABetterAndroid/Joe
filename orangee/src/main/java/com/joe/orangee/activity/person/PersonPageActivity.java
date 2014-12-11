package com.joe.orangee.activity.person;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joe.orangee.R;
import com.joe.orangee.adapter.OrangeeRecyclerViewAdapter;
import com.joe.orangee.adapter.WeiboStatusAdapter;
import com.joe.orangee.model.User;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.PersonDownloader;
import com.joe.orangee.net.WeiboDownloader;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PersonPageActivity extends ActionBarActivity implements OnRefreshListener {

	private Context context;
	private String name;
	private DisplayImageOptions avatarOptions;
	private DisplayImageOptions coverOptions;
	private ListView lvPersonStatus;
	private ImageView ivCover;
	private ImageView ivAvatar;
	private TextView tvName;
	private TextView tvLocation;
	private TextView tvVerifiedReason;
	private TextView tvDescription;
	private LinearLayout followLayout;
	private TextView tvFollow;
	private LinearLayout followerLayout;
	private TextView tvFollower;
	private LinearLayout statusLayout;
	private TextView tvStatus;
	private ImageLoader imageLoader;
	private List<WeiboStatus> weiboList;
	private WeiboStatusAdapter mAdapter;
	private SwipeRefreshLayout refreshLayout;
	private int page=1;
	private boolean isFollowing;
	private boolean isFollowMe;
	private int followResult;
	private int cancelFollowResult;
	private RecyclerView mRecyclerView;
	private OrangeeRecyclerViewAdapter recyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private View headerView;
    private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.person_page_activity);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar());
		user = getIntent().getParcelableExtra("User");
		name = user.getName();
		
		initViews();
		imageLoader = ImageLoader.getInstance();
		avatarOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.showImageOnLoading(R.drawable.ic_launcher)
		.displayer(new RoundedBitmapDisplayer(1000))
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
	
		coverOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.cover_default)
		.showImageOnFail(R.drawable.cover_default)
		.showImageOnLoading(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
		
		refreshLayout.setRefreshing(true);
		fillData();
	}

	private void initViews() {
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.person_swipe);
		refreshLayout.setColorSchemeResources(R.color.theme_orange);
		refreshLayout.setOnRefreshListener(this);
		mRecyclerView = (RecyclerView) findViewById(R.id.person_status_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		
        mRecyclerView.setOnScrollListener(listener);
		
		headerView=LayoutInflater.from(context).inflate(R.layout.person_page_header, mRecyclerView, false);
		ivCover = (ImageView) headerView.findViewById(R.id.person_cover);
		ivAvatar = (ImageView) headerView.findViewById(R.id.person_avatar);
		tvName = (TextView) headerView.findViewById(R.id.person_name);
		tvLocation = (TextView) headerView.findViewById(R.id.person_location);
		tvVerifiedReason = (TextView) headerView.findViewById(R.id.person_verified_reason);
		tvDescription = (TextView) headerView.findViewById(R.id.person_description);
		followLayout = (LinearLayout) headerView.findViewById(R.id.person_follow_layout);
		tvFollow = (TextView) headerView.findViewById(R.id.person_follow);
		followerLayout = (LinearLayout) headerView.findViewById(R.id.person_follower_layout);
		tvFollower = (TextView) headerView.findViewById(R.id.person_follower);
		statusLayout = (LinearLayout) headerView.findViewById(R.id.person_status_layout);
		tvStatus = (TextView) headerView.findViewById(R.id.person_status);
		
	}

    private android.support.v7.widget.RecyclerView.OnScrollListener listener=new android.support.v7.widget.RecyclerView.OnScrollListener() {
		
		private int lastItemIndex;//当前ListView中最后一个Item的索引
		
		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			 if (newState == RecyclerView.SCROLL_STATE_IDLE  && lastItemIndex > mLayoutManager.getItemCount() - 1-5) {  
	        	 page+=1;
	        	 fillData();
	         }  
			super.onScrollStateChanged(recyclerView, newState);
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
	        lastItemIndex = mLayoutManager.findFirstVisibleItemPosition() + mRecyclerView.getChildCount() - 1;  
			super.onScrolled(recyclerView, dx, dy);
		}
	};
	
	private void fillData() {
		imageLoader.displayImage(user.getCover(), ivCover, coverOptions);
		imageLoader.displayImage(user.getAvatar(), ivAvatar, avatarOptions);
		tvName.setText(user.getName());
		tvLocation.setText(user.getLocation());
		if (user.getVerified_reason() ==null || user.getVerified_reason().equals("")) {
			tvVerifiedReason.setVisibility(View.GONE);
		}else {
			tvVerifiedReason.setText(user.getVerified_reason());
		}
		if (user.getDescription() ==null || user.getDescription().equals("")) {
			tvDescription.setVisibility(View.GONE);
		}else {
			tvDescription.setText(user.getDescription());
		}
		tvFollow.setText(user.getFollowers_count()+"");
		tvFollower.setText(user.getFollowers_count()+"");
		tvStatus.setText(user.getStatuses_count()+"");
		isFollowing = user.isFollowing();
		isFollowMe = user.isFollow_me();
		invalidateOptionsMenu();
					
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				weiboList=new WeiboDownloader(context).getWeiboList(Constants.URL_PERSON_STATUSES, name, 0L, 0L, 20, page, 0, 0, 0, 0);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				refreshLayout.setRefreshing(false);
				if (recyclerAdapter==null) {
					recyclerAdapter=new OrangeeRecyclerViewAdapter(weiboList, context, headerView);
					mRecyclerView.setAdapter(recyclerAdapter);
				}else {
					recyclerAdapter.addData(weiboList);
					recyclerAdapter.notifyDataSetChanged();
				}
				super.onPostExecute(result);
			}}.execute();
		
	}

	@Override
	public void onRefresh() {
		page=1;
		if (mAdapter!=null) {
			mAdapter.clearData();
		}
		fillData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.person_info, menu);
		if (isFollowMe && isFollowing){
			menu.getItem(0).setTitle(R.string.person_each_following);
		}else if (isFollowMe) {
			menu.getItem(0).setTitle(R.string.person_follow);
		}else if (isFollowing) {
			menu.getItem(0).setTitle(R.string.person_following);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id==android.R.id.home) {
			finish();
		}else if (id == R.id.action_follow) {
			if (item.getTitle().toString().equals(getResources().getString(R.string.person_follow))) {
				
				follow(item);
				
			}else {
				
				cancelFollow(item);
				
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void follow(final MenuItem item) {
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				followResult = new PersonDownloader(context).friendshipDeal(name ,true);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (followResult==1) {
					item.setTitle(R.string.person_following);
					
				}
				super.onPostExecute(result);
			}}.execute();
		
	}
	private void cancelFollow(final MenuItem item) {
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				cancelFollowResult = new PersonDownloader(context).friendshipDeal(name ,false);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (cancelFollowResult==1) {
					item.setTitle(R.string.person_follow);
					
				}
				super.onPostExecute(result);
			}}.execute();
		
	}
	
}
