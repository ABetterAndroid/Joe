package com.joe.orangee.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.joe.orangee.R;
import com.joe.orangee.activity.weibo.WeiboCommentActivity;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.LoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.ParamsChangeLoadingListener;
import com.joe.orangee.model.WeiboStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("UseSparseArrays")
public class WeiboStatusAdapter extends BaseAdapter {

	private List<WeiboStatus> dataList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions avatarOptions;
	private DisplayImageOptions picOptions;
	private LoadingListener mListener;
	private ParamsChangeLoadingListener mChangeListener;
	
	public WeiboStatusAdapter(List<WeiboStatus> dataList, Context context) {
		super();
		imageLoader = ImageLoader.getInstance();
		this.dataList = dataList;
		this.context=context;
		
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
		
		/*ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).memoryCacheExtraOptions(500, 200)
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.build();*/
		
	}

	public void addData(List<WeiboStatus> addList){
		dataList.addAll(addList);
	}
	
	public void clearData(){
		dataList.clear();
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view;
		final ViewHolder holder;
		if (convertView!=null && convertView instanceof RelativeLayout) {
			view=convertView;
			holder=(ViewHolder)view.getTag();
		}else {
			view=View.inflate(context, R.layout.weibo_item, null);
			holder=new ViewHolder();
			holder.avatar=(ImageView) view.findViewById(R.id.weibo_avatar);
			holder.name=(TextView) view.findViewById(R.id.weibo_auther);
			holder.time=(TextView) view.findViewById(R.id.weibo_time);
			holder.source=(TextView) view.findViewById(R.id.weibo_source);
			holder.postText=(TextView) view.findViewById(R.id.weibo_text);
			holder.weibo_pics=(LinearLayout) view.findViewById(R.id.weibo_pics);
			holder.retweet_weibo_pics=(LinearLayout) view.findViewById(R.id.retweet_weibo_pics);
			holder.retweet_layout=(LinearLayout) view.findViewById(R.id.retweet_layout);
			holder.retweet_postText=(TextView) view.findViewById(R.id.retweet_text);
			holder.retweet_time=(TextView) view.findViewById(R.id.retweet_time);
			holder.retweet_source=(TextView) view.findViewById(R.id.retweet_source);
			holder.picList.add((ImageView) view.findViewById(R.id.pic1));
			holder.picList.add((ImageView) view.findViewById(R.id.pic2));
			holder.picList.add((ImageView) view.findViewById(R.id.pic3));
			holder.picList.add((ImageView) view.findViewById(R.id.pic4));
			holder.picList.add((ImageView) view.findViewById(R.id.pic5));
			holder.picList.add((ImageView) view.findViewById(R.id.pic6));
			holder.picList.add((ImageView) view.findViewById(R.id.pic7));
			holder.picList.add((ImageView) view.findViewById(R.id.pic8));
			holder.picList.add((ImageView) view.findViewById(R.id.pic9));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic1));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic2));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic3));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic4));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic5));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic6));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic7));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic8));
			holder.retweet_picList.add((ImageView) view.findViewById(R.id.retweet_pic9));
			holder.btComment=(LinearLayout) view.findViewById(R.id.comment_btn_layout);
			holder.btRetweet=(LinearLayout) view.findViewById(R.id.retweet_btn_layout);
			holder.comment_count=(TextView) view.findViewById(R.id.comment_bt_text);
			holder.repost_count=(TextView) view.findViewById(R.id.repost_bt_text);
			holder.retweet_comment_count=(TextView) view.findViewById(R.id.retweet_comment);
			holder.retweet_repost_count=(TextView) view.findViewById(R.id.retweet_repost);
			holder.locationLayout=(LinearLayout) view.findViewById(R.id.status_location_layout);
			holder.location=(TextView) view.findViewById(R.id.status_location);
			
			view.setTag(holder);
		}
		
		WeiboStatus weiboStatus=dataList.get(position);
		
		/*WeiboItemUtil.getWeiboItem(context, picBrowserLayout, mViewPager, imageLoader, mListener, mChangeListener,  avatarOptions, 
				picOptions, position, holder, weiboStatus);*/
		
		holder.retweet_postText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiboStatus status=(WeiboStatus) getItem(position);
				Intent intent=new Intent(context, WeiboCommentActivity.class);
				intent.putExtra("WeiboStatus", status);
				intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
				context.startActivity(intent);
				
			}
		});
		holder.postText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiboStatus status=(WeiboStatus) getItem(position);
				Intent intent=new Intent(context, WeiboCommentActivity.class);
				intent.putExtra("WeiboStatus", status);
				intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
				context.startActivity(intent);
				
			}
		});
		
		view.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				WeiboStatus status=(WeiboStatus) getItem(position);
				Intent intent=new Intent(context, WeiboCommentActivity.class);
				intent.putExtra("WeiboStatus", status);
				intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
				context.startActivity(intent);
				
			}
		});
		/*AnimatorSet animatorSet=new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationY", 300, 100, 0).setDuration(500),
												ObjectAnimator.ofFloat(view, "rotationX", 20, 0));
		animatorSet.start();*/
		return view;
	}

	public static class ViewHolder{
		public ImageView avatar;
		public TextView name;
		public TextView time;
		public TextView source;
		public TextView postText;
		public LinearLayout weibo_pics;
		public LinearLayout retweet_layout;
		public TextView retweet_time;
		public TextView retweet_source;
		public TextView retweet_postText;
		public LinearLayout retweet_weibo_pics;
		public LinearLayout btComment;
		public LinearLayout btRetweet;
		public List<ImageView> picList=new ArrayList<ImageView>();
		public List<ImageView> retweet_picList=new ArrayList<ImageView>();
		public LinearLayout locationLayout;
		public TextView location;
		public TextView comment_count;
		public TextView repost_count;
		public TextView retweet_comment_count;
		public TextView retweet_repost_count;
	}
	
}
