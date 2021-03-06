package com.joe.orangee.activity.weibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.joe.orangee.R;
import com.joe.orangee.activity.base.BaseActivity;
import com.joe.orangee.adapter.OrangeeRecyclerViewAdapter.MyViewHolder;
import com.joe.orangee.adapter.WeiboCommentAdapter;
import com.joe.orangee.library.ComputeYListView;
import com.joe.orangee.listener.OrangeeImageLoadingListener.LoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.ParamsChangeLoadingListener;
import com.joe.orangee.model.Comment;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.Downloader.WeiboDownloader;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;
import com.joe.orangee.util.WeiboItemUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class WeiboCommentActivity extends BaseActivity implements OnRefreshListener {

    private ComputeYListView lvComment;
    private Context context;
    private String weiboID;
    private List<Comment> commentList;
    private WeiboCommentAdapter mAdapter;
    private WeiboStatus status;
    private SwipeRefreshLayout refreshLayout;
    private int page = 1;
    public static final String CARD_NAME = "transform_card";
    public static final String IMAGE_NAME = "transform_avatar";
    public static final String TEXT_NAME = "transform_text";
    private Toolbar toolbar;
    private CardView card;
    private LinearLayout commentRetweetLayout;
    private int mtoobarHeight = Constants.TOOLBAR_HEIGHT;
    private int mTranslationY = 0;
    private int scrollY = 0;
    private int scrollItem=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        status = getIntent().getParcelableExtra("WeiboStatus");
        weiboID = status.getWeiboId();
        setContentView(R.layout.weibo_comment);

        RelativeLayout contentView = (RelativeLayout) findViewById(R.id.content_layout);
        Utils.setTopPadding(this, contentView);
        lvComment = (ComputeYListView) findViewById(R.id.lv_weibo_comment);
        lvComment.setOnScrollListener(onScrollListener);
        footerView = View.inflate(context, R.layout.footer_view, null);
        lvComment.addFooterView(footerView);
//        View headerView = View.inflate(context, R.layout.header_blank_view, null);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_blank_view, lvComment, false);

        headerView.getLayoutParams().height = (int) (5 * Constants.DENSITY + 0.5f) + Constants.TOOLBAR_HEIGHT;

        headerView.requestLayout();
        lvComment.addHeaderView(headerView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.setActionBarStyle(getSupportActionBar(), R.string.comment_detail);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.comment_swipe);
        refreshLayout.setColorSchemeResources(R.color.theme_orange);
        refreshLayout.setOnRefreshListener(this);

        initHeader();

        if (isNetworkOK()) {
            fillData();
        }

    }

    @SuppressLint("NewApi")
    private void initHeader() {
        View include = findViewById(R.id.item_layout);
        weiboHeader = View.inflate(context, R.layout.weibo_item, null);
        weiboHeader.setVisibility(View.INVISIBLE);
        lvComment.addHeaderView(weiboHeader);
        weiboHeader.findViewById(R.id.item_layout).setVisibility(View.INVISIBLE);
        card = (CardView) include.findViewById(R.id.card_view);
        card.setTransitionName(CARD_NAME);
        commentRetweetLayout = (LinearLayout) include.findViewById(R.id.comment_repost_layout);

        ImageView avatar = (ImageView) include.findViewById(R.id.weibo_avatar);
        avatar.setTransitionName(IMAGE_NAME);
        headScroll = (ScrollView) findViewById(R.id.head_scroll);
        avatar.setImageResource(R.drawable.ic_launcher);
        MyViewHolder holder = new MyViewHolder(weiboHeader);
        MyViewHolder holder2 = new MyViewHolder(include);

        ImageLoader imageLoader = ImageLoader.getInstance();

        LoadingListener mListener = new LoadingListener();
        ParamsChangeLoadingListener mChangeListener = new ParamsChangeLoadingListener();

        DisplayImageOptions avatarOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                .displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        DisplayImageOptions picOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        WeiboItemUtil.getWeiboItem(context, imageLoader, mListener, mChangeListener, avatarOptions, picOptions, 0, holder, status);
        WeiboItemUtil.getWeiboItem(context, imageLoader, mListener, mChangeListener, avatarOptions, picOptions, 0, holder2, status);

    }

    private OnScrollListener onScrollListener = new OnScrollListener() {

        private int lastItemIndex;//当前ListView中最后一个Item的索引
        //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex > mAdapter.getCount() - 1 - 5) {
                page += 1;
                fillData();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一
            lastItemIndex = firstVisibleItem + visibleItemCount - 1 - 1;

            if (lvComment.getChildAt(0) == null) {
                return;
            }
            if (firstVisibleItem > 0) {

                int deltaY=lvComment.getChildAt(0).getTop()-scrollY;
                int deltaItem = firstVisibleItem - scrollItem;
                if (deltaItem >= 1 || deltaY<0) {
                    toolbar.animate().translationY(-mtoobarHeight).setDuration(300).start();
                } else if(deltaItem <= -1|| deltaY>0){
                    toolbar.animate(). translationY(0).setDuration(300).start();
                }
                scrollItem = firstVisibleItem;
                scrollY=lvComment.getChildAt(0).getTop();

            }
        }
    };
    private View footerView;
    private ScrollView headScroll;
    private View weiboHeader;

    private void fillData() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                commentList = new WeiboDownloader(context).getWeiboComment(weiboID, 0, 0, 20, page, 0);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                weiboHeader.setVisibility(View.VISIBLE);
                weiboHeader.findViewById(R.id.item_layout).setVisibility(View.VISIBLE);
                headScroll.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                if (commentList != null) {
                    if (commentList.size() < 20) {
                        footerView.setVisibility(View.GONE);
                    }
                    if (mAdapter == null) {
                        mAdapter = new WeiboCommentAdapter(commentList, context);
                        lvComment.setAdapter(mAdapter);
                    } else {
                        mAdapter.addData(commentList);
                        mAdapter.notifyDataSetChanged();
                    }

                }
                super.onPostExecute(result);
            }
        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mAdapter.clearDataList();
        fillData();

    }

    @Override
    public void onBackPressed() {
        if (lvComment.getFirstVisiblePosition() > 1) {
            headScroll.setY(-headScroll.getHeight() - getSupportActionBar().getHeight() - Utils.getStatusBarHeight(this));
        } else if (lvComment.getFirstVisiblePosition() == 1) {
            if (lvComment.getChildAt(0) != null) {

                headScroll.setY(lvComment.getChildAt(0).getTop() - 5 * Constants.DENSITY - 0.5f);
            }
        } else {
            if (lvComment.getChildAt(0) != null) {

                headScroll.setY(headScroll.getY() + lvComment.getChildAt(0).getTop());
            }
        }
        headScroll.setVisibility(View.VISIBLE);
        onBack();
    }

    private void onBack() {
        try {
            super.onBackPressed();
        } catch (IllegalStateException e) {
            finish();
        }
    }

}
