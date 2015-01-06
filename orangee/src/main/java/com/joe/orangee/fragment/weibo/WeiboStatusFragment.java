package com.joe.orangee.fragment.weibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.joe.orangee.R;
import com.joe.orangee.adapter.OrangeeRecyclerViewAdapter;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.Downloader.WeiboDownloader;
import com.joe.orangee.sql.SQLDataGetterUtils;
import com.joe.orangee.sql.StatusesSQLOpenHelper;
import com.joe.orangee.sql.StatusesSQLUtils;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.PreferencesKeeper;

import java.util.List;

@SuppressLint("ValidFragment")
public class WeiboStatusFragment extends Fragment implements OnRefreshListener {

	private List<WeiboStatus> weiboList;
	private Context context;
	private View view;
	private int page=1;
	private SwipeRefreshLayout refreshLayout;
	private String url=Constants.URL_FRIENDS_TIMELINE;
	private long max_id=-1;
//	private View footerView;
	private StatusesSQLOpenHelper mOpenHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private int offsetY;
	private int currentPosition;
	private int positionToScroll;
	private boolean isRefresh;
	private RecyclerView mRecyclerView;
	private OrangeeRecyclerViewAdapter recyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private Toolbar toolbar;
    private int mtoobarHeight = 0;
    private int mTranslationY = 0;

    public WeiboStatusFragment() {
        super();
    }

	public WeiboStatusFragment(Toolbar toolbar) {
		super();
        this.toolbar=toolbar;
	}

	public WeiboStatusFragment(String url) {
		this.url=url;
	}
	
	@SuppressLint("NewApi")
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
		mRecyclerView.setDrawingCacheEnabled(true);
		mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		mRecyclerView.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE
				| ViewGroup.PERSISTENT_SCROLLING_CACHE);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		
        mRecyclerView.setOnScrollListener(listener);
		
		refreshLayout.setRefreshing(true);
		
		offsetY=PreferencesKeeper.readOffsetY(context);
		currentPosition=positionToScroll=PreferencesKeeper.readPosition(context);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Constants.TOOLBAR_HEIGHT = mtoobarHeight = toolbar.getHeight();
                refreshLayout.setProgressViewOffset(false, 0, (int) (mtoobarHeight * 1.2));
                refreshLayout.invalidate();

                if (url==Constants.URL_FRIENDS_TIMELINE) {
                    mOpenHelper = new StatusesSQLOpenHelper(context, Constants.TABLE_NAME_FRIENDS_TIMELINE);
                    mSQLiteDatabase = mOpenHelper.getReadableDatabase();
                    Cursor mCursor = StatusesSQLUtils.fetchAllData(mSQLiteDatabase);
                    if (mCursor != null && mCursor.getCount() !=0) {
                        fillDataFromSQL(mCursor);
                    }else {
                        max_id=0L;
                        fillData();
                    }
                }else {
                    max_id=0L;
                    fillData();
                }

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

		return view;
	}

	private void fillDataFromSQL(final Cursor mCursor) {
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				weiboList=SQLDataGetterUtils.getStatuses(mCursor);
				return null;
			}

			@SuppressLint("NewApi")
			@Override
			protected void onPostExecute(Void result) {
				mCursor.close();
				refreshLayout.setRefreshing(false);
				max_id=Long.valueOf(weiboList.get(weiboList.size()-1).getWeiboId())-1;
				recyclerAdapter=new OrangeeRecyclerViewAdapter(weiboList, context, null, toolbar);
				mRecyclerView.setAdapter(recyclerAdapter);
				mLayoutManager.scrollToPositionWithOffset(positionToScroll, offsetY);

				super.onPostExecute(result);
			}}.execute();
	}
	
	private android.support.v7.widget.RecyclerView.OnScrollListener listener=new android.support.v7.widget.RecyclerView.OnScrollListener() {
		
		private int lastItemIndex;//当前ListView中最后一个Item的索引
		
		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
	    		 ViewGroup item = (ViewGroup) recyclerView.getChildAt(0);//此处必须为0
                if (item!=null){

                    offsetY=item.getTop();
                }
			}
			 if (newState == RecyclerView.SCROLL_STATE_IDLE  && lastItemIndex > mLayoutManager.getItemCount() - 1-5) {  
	        	 page+=1;
	        	 fillData();
	         }  
			super.onScrollStateChanged(recyclerView, newState);
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			/*visibleItemCount = mRecyclerView.getChildCount();
	        totalItemCount = mLayoutManager.getItemCount();
	        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();*/
	        lastItemIndex = mLayoutManager.findFirstVisibleItemPosition() + mRecyclerView.getChildCount() - 1;  
	        currentPosition=mLayoutManager.findFirstVisibleItemPosition();

            int deltaY = -dy;
            if ((mTranslationY > -mtoobarHeight && deltaY < 0) || (mTranslationY < 0 && deltaY > 0)) {

                mTranslationY += deltaY;
            }

            if (mTranslationY < -mtoobarHeight) {
                mTranslationY = -mtoobarHeight;
            } else if (mTranslationY > 0) {
                mTranslationY = 0;
            }
            toolbar.setTranslationY(mTranslationY);

			super.onScrolled(recyclerView, dx, dy);
		}
	};
	
	
	private void fillData() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (max_id==0L) {
					weiboList=new WeiboDownloader(context).getWeiboList(url, null, 0L, 0L, 20, page, 0, 0, 0, 0);
					
				}else {
					
					weiboList=new WeiboDownloader(context).getWeiboList(url, null, 0L, max_id, 20, 1, 0, 0, 0, 0);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				refreshLayout.setRefreshing(false);
				if (weiboList.size()==0) {
//					footerView.setVisibility(View.GONE);
				}else {
					if (max_id!=0L) {
						max_id=Long.valueOf(weiboList.get(weiboList.size()-1).getWeiboId())-1;
						
					}
				}
				if (isRefresh) {
					recyclerAdapter.clearData();
					isRefresh=false;
					
				}
				if (recyclerAdapter==null) {
					recyclerAdapter=new OrangeeRecyclerViewAdapter(weiboList, context, null, toolbar);
					mRecyclerView.setAdapter(recyclerAdapter);
				}else {
					recyclerAdapter.addData(weiboList);
					recyclerAdapter.notifyDataSetChanged();
				}
				if (url==Constants.URL_FRIENDS_TIMELINE && page==1) {
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
                        mOpenHelper = new StatusesSQLOpenHelper(context, Constants.TABLE_NAME_FRIENDS_TIMELINE);
                        mSQLiteDatabase=mOpenHelper.getWritableDatabase();
							/*if (page==1 && max_id==0L) {
								SQLUtils.deleteTableData(mSQLiteDatabase);
							}
							SQLUtils.insertStatusesData(weiboList, mSQLiteDatabase);*/
						Cursor mCursor = StatusesSQLUtils.fetchAllData(mSQLiteDatabase);
						if (mCursor != null && mCursor.getCount() != 0) {
							StatusesSQLUtils.updateStatusesData(weiboList, mSQLiteDatabase);
						}else {
							StatusesSQLUtils.insertStatusesData(weiboList, mSQLiteDatabase);
						}
						mCursor.close();
						}
					}).start();
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	@Override
	public void onRefresh() {
		mLayoutManager.scrollToPositionWithOffset(0, 0);
		max_id=0L;
		page=1;
		isRefresh=true;
		fillData();
	}

	@Override
	public void onPause() {
		PreferencesKeeper.writeListViewPosition(context, offsetY, currentPosition);
		if (mSQLiteDatabase!=null && mSQLiteDatabase.isOpen()) {
			mSQLiteDatabase.close();
		}
		if (mOpenHelper!=null) {
			
			mOpenHelper.close();
		}
		super.onPause();
	}

}
