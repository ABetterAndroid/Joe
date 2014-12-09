package com.joe.orangee.fragment.mycomment;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.joe.orangee.R;
import com.joe.orangee.adapter.MyCommentAdapter;
import com.joe.orangee.model.Comment;
import com.joe.orangee.net.CommentDownloader;

public class MyCommentFragment extends Fragment implements OnRefreshListener {

	private View view;
	private Context context;
	private SwipeRefreshLayout refreshLayout;
	private ListView lvMyComment;
	private List<Comment> commentList;
	private MyCommentAdapter mAdapter;
	private boolean toMe;
	private int page=1;
	private String url;
	
	public MyCommentFragment(String url, boolean toMe) {
		super();
		this.url=url;
		this.toMe = toMe;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null ) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.my_comment_fragment, container, false);
		refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.my_comment_swipe);
		refreshLayout.setColorSchemeResources(R.color.theme_orange);
		refreshLayout.setOnRefreshListener(this);
		lvMyComment = (ListView) view.findViewById(R.id.my_comment_list);
		View headerView=View.inflate(context, R.layout.header_blank_view, null);
		lvMyComment.addHeaderView(headerView);
		footerView = View.inflate(context, R.layout.footer_view, null);
		lvMyComment.addFooterView(footerView);
		lvMyComment.setOnScrollListener(onScrollListener);
		filter_by_author = toMe? 0:null;
		refreshLayout.setRefreshing(true);
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
	private Integer filter_by_author;
	private View footerView;
	
	private void fillData() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				commentList = new CommentDownloader(context).getMyCommentList(url, 0, 20, page, filter_by_author);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				refreshLayout.setRefreshing(false);
				if (commentList!=null) {
					if (commentList.size()<20) {
						footerView.setVisibility(View.GONE);
					}
					if (mAdapter==null) {
						mAdapter=new MyCommentAdapter(commentList, context);
						lvMyComment.setAdapter(mAdapter);
					}else {
						mAdapter.addData(commentList);
						mAdapter.notifyDataSetChanged();
					}
					
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	@Override
	public void onRefresh() {
		page=1;
		mAdapter.clearDataList();
		fillData();
		
	}

}
