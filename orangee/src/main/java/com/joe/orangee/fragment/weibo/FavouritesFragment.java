package com.joe.orangee.fragment.weibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.joe.orangee.R;
import com.joe.orangee.activity.weibo.WeiboCommentActivity;
import com.joe.orangee.adapter.WeiboStatusAdapter;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.Downloader.WeiboDownloader;

import java.util.List;

@SuppressLint("ValidFragment")
public class FavouritesFragment extends Fragment implements OnRefreshListener {

	private ListView lvWeibo;
	private List<WeiboStatus> weiboList;
	private Context context;
	private View view;
	private WeiboStatusAdapter mAdapter;
	private int page=1;
	
	public FavouritesFragment() {
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
		view = inflater.inflate(R.layout.weibo_list_layout, container, false);
		context=getActivity();
		lvWeibo = (ListView) view.findViewById(R.id.weibo_list);
		footerView = View.inflate(context, R.layout.footer_view, null);
		lvWeibo.addFooterView(footerView);
		lvWeibo.setOnScrollListener(onScrollListener);
		
		lvWeibo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WeiboStatus status=(WeiboStatus) lvWeibo.getAdapter().getItem(position);
				Intent intent=new Intent(context, WeiboCommentActivity.class);
				intent.putExtra("ID", status.getWeiboId());
				startActivity(intent);
				
			}
		});
		refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.status_swipe);
		refreshLayout.setColorSchemeResources(R.color.theme_orange);
		refreshLayout.setOnRefreshListener(this);
		fillData();
		return view;
	}

	private OnScrollListener onScrollListener=new OnScrollListener() {

		private int lastItemIndex;//当前ListView中最后一个Item的索引
	     //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
	     @Override
	     public void onScrollStateChanged(AbsListView view, int scrollState) {
	         if (scrollState == OnScrollListener.SCROLL_STATE_IDLE  && lastItemIndex > mAdapter.getCount() - 1-5) {
	        	 page+=1;
	        	 fillData();
	         }
	     }
	     @Override
	     public void onScroll(AbsListView view, int firstVisibleItem,
	             int visibleItemCount, int totalItemCount) {
	         //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一
	         lastItemIndex = firstVisibleItem + visibleItemCount - 1 -1;
	     }
	};
	private SwipeRefreshLayout refreshLayout;
	private View footerView;
	
	private void fillData() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				weiboList=new WeiboDownloader(context).getFavourites(20, page);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				refreshLayout.setRefreshing(false);
				if (weiboList.size()==0) {
					footerView.setVisibility(View.GONE);
				}
				if (mAdapter==null) {
					mAdapter=new WeiboStatusAdapter(weiboList, context);
					lvWeibo.setAdapter(mAdapter);
				}else {
					mAdapter.addData(weiboList);
					mAdapter.notifyDataSetChanged();
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	@Override
	public void onRefresh() {
		page=1;
		if (mAdapter!=null) {
			mAdapter.clearData();
		}
		fillData();
		
	}

}
