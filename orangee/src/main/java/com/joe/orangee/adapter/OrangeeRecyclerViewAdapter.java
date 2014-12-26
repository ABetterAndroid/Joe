package com.joe.orangee.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joe.orangee.R;
import com.joe.orangee.activity.home.OrangeeHomeActivity;
import com.joe.orangee.activity.weibo.WeiboCommentActivity;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.LoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.ParamsChangeLoadingListener;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.util.Utils;
import com.joe.orangee.util.WeiboItemUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class OrangeeRecyclerViewAdapter extends Adapter<ViewHolder> {

	private static final int TYPE_LIST=0;
    private static final int TYPE_HEADER=1;
	private static final int TYPE_FOOTER=2;
	private List<WeiboStatus> dataList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions avatarOptions;
	private DisplayImageOptions picOptions;
	private LoadingListener mListener;
	private ParamsChangeLoadingListener mChangeListener;
	private View extendView;
	
	public OrangeeRecyclerViewAdapter(List<WeiboStatus> dataList, Context context, View extendView) {
		super();
		imageLoader = ImageLoader.getInstance();
		this.dataList = dataList;
		this.context=context;
		this.extendView=extendView;
		
		mListener = new OrangeeImageLoadingListener.LoadingListener();
		mChangeListener = new OrangeeImageLoadingListener.ParamsChangeLoadingListener();
		
		avatarOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.pic_default)
			.showImageOnFail(R.drawable.pic_default)
			.showImageOnLoading(R.drawable.pic_default)
			.displayer(new RoundedBitmapDisplayer(1000))
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.build();
		
		picOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.pic_default)
			.showImageOnFail(R.drawable.pic_default)
			.showImageOnLoading(R.drawable.pic_default)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.build();
	}

	public void addData(List<WeiboStatus> addList){
		dataList.addAll(addList);
	}
	
	public void clearData(){
		dataList.clear();
	}
	
	@Override
	public int getItemCount() {
        if (extendView!=null){
            return dataList.size()+2;
        }else{
            return dataList.size()+1;
        }
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		if (holder instanceof MyViewHolder) {
			final WeiboStatus weiboStatus=dataList.get(position);
			WeiboItemUtil.getWeiboItem(context, imageLoader, mListener, mChangeListener,  avatarOptions, 
					picOptions, position, holder, weiboStatus);
			OnClickListener listener=new OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
                    Utils.hideHotKey(OrangeeHomeActivity.hotKey);
					Intent intent=new Intent(context, WeiboCommentActivity.class);
					intent.putExtra("WeiboStatus", weiboStatus);
					intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
					

				        // BEGIN_INCLUDE(start_activity)
				        /**
				         * Now create an {@link android.app.ActivityOptions} instance using the
				         * {@link android.app.ActivityOptions#makeSceneTransitionAnimation(android.app.Activity, android.util.Pair[])} factory method.
				         */
				        @SuppressWarnings("unchecked")
						ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
				                (Activity)context,

				                new Pair<View, String>(((MyViewHolder) holder).card, WeiboCommentActivity.CARD_NAME)
				                        
				        );

				        context.startActivity(intent, activityOptions.toBundle());
				}
			};
			OnLongClickListener retweetListener=new OnLongClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public boolean onLongClick(View v) {
					Intent intent=new Intent(context, WeiboCommentActivity.class);
					intent.putExtra("WeiboStatus", weiboStatus.getWeiboStatus());
					intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
					

				        /**
				         * Now create an {@link android.app.ActivityOptions} instance using the
				         * {@link android.app.ActivityOptions#makeSceneTransitionAnimation(android.app.Activity, android.util.Pair[])} factory method.
				         */
				        @SuppressWarnings("unchecked")
						ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
				                (Activity)context,

				                new Pair<View, String>(((MyViewHolder) holder).retweetCard, WeiboCommentActivity.CARD_NAME),
				                new Pair<View, String>(((MyViewHolder) holder).avatarMock, WeiboCommentActivity.IMAGE_NAME)
				                        
				        );

				        context.startActivity(intent, activityOptions.toBundle());
					return false;
				}
			};
			((MyViewHolder)holder).card.setOnClickListener(listener);
			((MyViewHolder)holder).retweet_layout.setOnClickListener(listener);
			((MyViewHolder)holder).retweet_layout.setOnLongClickListener(retweetListener);
			/*AnimatorSet animatorSet=new AnimatorSet();
			animatorSet.playTogether(ObjectAnimator.ofFloat(((MyViewHolder) holder).view, "translationY", 300, 50, 0).setDuration(600),
													ObjectAnimator.ofFloat(((MyViewHolder) holder).view, "rotationX", 20, 5, 0).setDuration(600));
			animatorSet.start();*/
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ViewHolder vh = null;
		switch (viewType) {
		case TYPE_LIST:
			 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weibo_item, parent, false);
			 vh = new MyViewHolder(view);
			break;
		case TYPE_HEADER:
			if (extendView!=null) {
				vh = new AddViewHolder(extendView);
			}else {
				View headerView=LayoutInflater.from(parent.getContext()).inflate(R.layout.header_blank_view, parent, false);
				vh = new AddViewHolder(headerView);
			}
			break;
		case TYPE_FOOTER:
			View footerView=LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
			 vh = new AddViewHolder(footerView);
			break;
		default:
			break;
		}
		
		 return vh;
	}

	@Override
	public int getItemViewType(int position) {
        if (extendView!=null){
            if (position==0){
                return TYPE_HEADER;
            }else if (position==getItemCount()-1) {
                return TYPE_FOOTER;
            }else {
                return TYPE_LIST;
            }
        }else {
            if (position==getItemCount()-1) {
                return TYPE_FOOTER;
            }else {
                return TYPE_LIST;
            }
        }

	}

	class AddViewHolder extends ViewHolder{

		public AddViewHolder(View view) {
			super(view);
		}}

	public static class MyViewHolder extends ViewHolder{  
		  
		public View view;
	    public ImageView avatar;
		public TextView name;
		public TextView time;
		public TextView source;
		public TextView postText;
		public LinearLayout weibo_pics;
		public LinearLayout retweet_weibo_pics;
		public LinearLayout retweet_layout;
		public TextView retweet_postText;
		public TextView retweet_time;
		public TextView retweet_source;
		public List<ImageView> picList;
		public List<ImageView> retweet_picList;
		public LinearLayout btComment;
		public LinearLayout btRetweet;
		public TextView comment_count;
		public TextView repost_count;
		public TextView retweet_comment_count;
		public TextView retweet_repost_count;
		public LinearLayout locationLayout;
		public TextView location;
		public CardView card;
		public CardView retweetCard;
		public ImageView avatarMock;
        public LinearLayout comment_retweet_layout;

		public MyViewHolder(View view) {  
	        super(view); 
	        this.view=view;
	        card = (CardView) view.findViewById(R.id.card_view);
	        avatar = (ImageView) view.findViewById(R.id.weibo_avatar);
	        name = (TextView) view.findViewById(R.id.weibo_auther);
	        time = (TextView) view.findViewById(R.id.weibo_time);
	        source = (TextView) view.findViewById(R.id.weibo_source);
	        postText = (TextView) view.findViewById(R.id.weibo_text);
	        weibo_pics = (LinearLayout) view.findViewById(R.id.weibo_pics);
	        retweet_weibo_pics = (LinearLayout) view.findViewById(R.id.retweet_weibo_pics);
	        retweetCard = (CardView) view.findViewById(R.id.retweet_card_view);
	        retweet_layout = (LinearLayout) view.findViewById(R.id.retweet_layout);
	        avatarMock = (ImageView) view.findViewById(R.id.retweet_avatar_mock);
	        retweet_postText = (TextView) view.findViewById(R.id.retweet_text);
	        retweet_time = (TextView) view.findViewById(R.id.retweet_time);
	        retweet_source = (TextView) view.findViewById(R.id.retweet_source);
	        picList = new ArrayList<ImageView>();
	        picList.add((ImageView) view.findViewById(R.id.pic1));
			picList.add((ImageView) view.findViewById(R.id.pic2));
			picList.add((ImageView) view.findViewById(R.id.pic3));
			picList.add((ImageView) view.findViewById(R.id.pic4));
			picList.add((ImageView) view.findViewById(R.id.pic5));
			picList.add((ImageView) view.findViewById(R.id.pic6));
			picList.add((ImageView) view.findViewById(R.id.pic7));
			picList.add((ImageView) view.findViewById(R.id.pic8));
			picList.add((ImageView) view.findViewById(R.id.pic9));
			retweet_picList = new ArrayList<ImageView>();
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic1));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic2));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic3));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic4));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic5));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic6));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic7));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic8));
			retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic9));
			btComment = (LinearLayout) view.findViewById(R.id.comment_btn_layout);
			btRetweet = (LinearLayout) view.findViewById(R.id.retweet_btn_layout);
			comment_count = (TextView) view.findViewById(R.id.comment_bt_text);
			repost_count = (TextView) view.findViewById(R.id.repost_bt_text);
			retweet_comment_count = (TextView) view.findViewById(R.id.retweet_comment);
			retweet_repost_count = (TextView) view.findViewById(R.id.retweet_repost);
			locationLayout = (LinearLayout) view.findViewById(R.id.status_location_layout);
			location = (TextView) view.findViewById(R.id.status_location);
            comment_retweet_layout= (LinearLayout) view.findViewById(R.id.comment_repost_layout);
	    }  
	  
	}  
	
}
