package com.joe.orangee.fragment.nearby;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.orangee.R;
import com.joe.orangee.adapter.OrangeeRecyclerViewAdapter;
import com.joe.orangee.adapter.WeiboStatusAdapter;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.Downloader.WeiboDownloader;

import java.util.List;

@SuppressLint("ValidFragment")
public class NearbyWeiboFragment extends Fragment implements OnRefreshListener{

	private Context context;
	private View view;
	private WeiboStatusAdapter mAdapter;
	private int page=1;
	private SwipeRefreshLayout refreshLayout;
	private List<WeiboStatus> statusList;
	private double currentLat;
	private double currentLng;
	private RecyclerView mRecyclerView;
	private OrangeeRecyclerViewAdapter recyclerAdapter;
    private LinearLayoutManager mLayoutManager;
	
	public NearbyWeiboFragment(double currentLat, double currentLng) {
		super();
		this.currentLat = currentLat;
		this.currentLng = currentLng;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null ) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.weibo_home, container, false);
		context=getActivity();
		
		refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.status_swipe);
		refreshLayout.setColorSchemeResources(R.color.theme_orange);
		refreshLayout.setOnRefreshListener(this);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.weibo_list);
	    mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			
        mRecyclerView.setOnScrollListener(listener);
		
		refreshLayout.setRefreshing(true);
		
		fillData();
		return view;
	}

	private void fillData() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				statusList = new WeiboDownloader(context).getNearbyStatusList(currentLat, currentLng, 20, page);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (statusList!=null) {
					refreshLayout.setRefreshing(false);
					if (recyclerAdapter==null) {
						recyclerAdapter=new OrangeeRecyclerViewAdapter(statusList, context, null);
						mRecyclerView.setAdapter(recyclerAdapter);
					}else {
						recyclerAdapter.addData(statusList);
						recyclerAdapter.notifyDataSetChanged();
					}
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	private RecyclerView.OnScrollListener listener=new RecyclerView.OnScrollListener() {
		
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
	
	@Override
	public void onRefresh() {
		page=1;
		mAdapter.clearData();
		fillData();
		
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
