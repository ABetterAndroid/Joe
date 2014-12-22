package com.joe.orangee.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.androidplus.util.StringUtil;
import com.joe.orangee.R;
import com.joe.orangee.activity.image.ImageBrowseActivity;
import com.joe.orangee.activity.weibo.WeiboCommentRetweetActivity;
import com.joe.orangee.adapter.OrangeeRecyclerViewAdapter;
import com.joe.orangee.adapter.OrangeeRecyclerViewAdapter.MyViewHolder;
import com.joe.orangee.listener.OrangeeImageLoadingListener.LoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.ParamsChangeLoadingListener;
import com.joe.orangee.model.WeiboStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class WeiboItemUtil {

	public static void getWeiboItem(final Context context, ImageLoader imageLoader, LoadingListener mListener, ParamsChangeLoadingListener mChangeListener, DisplayImageOptions avatarOptions,
			DisplayImageOptions picOptions,final int position, final ViewHolder viewHolder, final WeiboStatus weiboStatus) {
		
		final OrangeeRecyclerViewAdapter.MyViewHolder holder=(MyViewHolder) viewHolder;
		imageLoader.displayImage(weiboStatus.getUser().getAvatar(), holder.avatar, avatarOptions, mListener);
		/*holder.avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, PersonPageActivity.class);
                intent.putExtra("User", weiboStatus.getUser());
                intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
				context.startActivity(intent);

			}
		});*/
		String text=weiboStatus.getPostText();
		SpannableString postText = Utils.spanText(context, text, false);
		
		holder.postText.setText(postText);
		holder.postText.setMovementMethod(LinkMovementMethod.getInstance());
		holder.name.setText(weiboStatus.getUser().getName());
		holder.time.setText(weiboStatus.getTime());
		holder.source.setText(weiboStatus.getSource());
		Resources res=context.getResources();
		int commentCount=weiboStatus.getComment_count();
		int repostCount=weiboStatus.getRepost_count();
		String commentText=commentCount==0? res.getString(R.string.comment):(res.getString(R.string.comment)+"·"+commentCount);
		String repostText=repostCount==0? res.getString(R.string.retweet):(res.getString(R.string.retweet)+"·"+repostCount);
		holder.comment_count.setText(commentText);
		holder.repost_count.setText(repostText);
		if (StringUtil.isNullOrEmpty(weiboStatus.getLocation())) {
			holder.locationLayout.setVisibility(View.GONE);
		}else {
			holder.location.setText(weiboStatus.getLocation());
			
		}
		final ArrayList<String> picList=weiboStatus.getPicList();
		if (picList.size()==0) {
			holder.weibo_pics.setVisibility(View.GONE);
		}else {
			holder.weibo_pics.setVisibility(View.VISIBLE);
			if (picList.size()<9) {
				for (int i = picList.size(); i < 9; i++) {
					holder.picList.get(i).setVisibility(View.GONE);
				}
			}
			for (int i = 0; i < picList.size(); i++) {
				final int current=i;
				holder.picList.get(i).setVisibility(View.VISIBLE);
				ImageView imageView=holder.picList.get(i);
				imageView.setOnClickListener(new OnClickListener() {
					
					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(context, ImageBrowseActivity.class);
						intent.putStringArrayListExtra("imageList", picList);
						intent.putExtra("current", current);
                        intent.putExtra("WeiboStatus", weiboStatus);
                        intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());

				        context.startActivity(intent);
						
					}
				});
				if (picList.size()==1) {
					RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					imageView.setLayoutParams(params);
					imageView.setMaxHeight(200);
					imageView.setMaxWidth(300);
					imageLoader.displayImage(picList.get(i), imageView, picOptions, mChangeListener);
				}else {
					if (i==0) {
						holder.picList.get(0).setLayoutParams(new RelativeLayout.LayoutParams((int)(60*Constants.DENSITY+0.5f), 
								(int)(60*Constants.DENSITY+0.5f)));
					}
					imageLoader.displayImage(picList.get(i), imageView, picOptions, mListener);
				}
			}
		}
		
		final WeiboStatus retweetStatus=weiboStatus.getWeiboStatus();
		if (retweetStatus!=null) {
			holder.retweet_layout.setVisibility(View.VISIBLE);
			SpannableString retweetPostText = Utils.spanText(context, retweetStatus.getUser().getName()+":"+retweetStatus.getPostText(), true);
			holder.retweet_postText.setText(retweetPostText);
			holder.retweet_postText.setMovementMethod(LinkMovementMethod.getInstance());
			holder.retweet_time.setText(retweetStatus.getTime());
			holder.retweet_source.setText(retweetStatus.getSource());
			holder.retweet_comment_count.setText(res.getString(R.string.comment)+" "+retweetStatus.getComment_count());
			holder.retweet_repost_count.setText(res.getString(R.string.retweet)+" "+retweetStatus.getRepost_count());
			final ArrayList<String> retweetPicList=retweetStatus.getPicList();
			if (retweetPicList.size()==0) {
				holder.retweet_weibo_pics.setVisibility(View.GONE);
			}else {
				holder.retweet_weibo_pics.setVisibility(View.VISIBLE);
				if (retweetPicList.size()<9) {
					for (int i = retweetPicList.size(); i < 9; i++) {
						holder.retweet_picList.get(i).setVisibility(View.GONE);
					}
				}
				for (int i = 0; i < retweetPicList.size(); i++) {
					final int current=i;
					holder.retweet_picList.get(i).setVisibility(View.VISIBLE);
					ImageView imageView=holder.retweet_picList.get(i);
					imageView.setOnClickListener(new OnClickListener() {
						
						@SuppressLint("NewApi")
						@Override
						public void onClick(View v) {
							
							Intent intent=new Intent(context, ImageBrowseActivity.class);
							intent.putStringArrayListExtra("imageList", retweetPicList);
							intent.putExtra("current", current);
                            intent.putExtra("WeiboStatus", weiboStatus);
                            intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());

					        context.startActivity(intent);
							
							/*mViewPager.setAdapter(new PicsBrowserAdapter(context, retweetPicList, picBrowserLayout));
							mViewPager.setCurrentItem(current);
							AnimatorSet animatorSet=new AnimatorSet();
							animatorSet.playTogether(ObjectAnimator.ofFloat(picBrowserLayout, "scaleX", 0.1f, 0.6f, 1.2f, 1.0f).setDuration(500),
																	ObjectAnimator.ofFloat(picBrowserLayout, "scaleY", 0.1f, 0.6f, 1.2f,  1.0f).setDuration(500),
																	ObjectAnimator.ofFloat(picBrowserLayout, "alpha", 0.1f, 0.5f, 1.0f).setDuration(500));
							picBrowserLayout.setVisibility(View.VISIBLE);
							animatorSet.start();*/
							
						}
					});
					if (retweetPicList.size()==1) {
						RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						imageView.setLayoutParams(params);
						imageView.setMaxHeight(200);
						imageView.setMaxWidth(300);
						imageLoader.displayImage(retweetPicList.get(i), imageView, picOptions, mChangeListener);
					}else {
						if (i==0) {
							holder.retweet_picList.get(0).setLayoutParams(new RelativeLayout.LayoutParams((int)(60*Constants.DENSITY+0.5f), 
									(int)(60*Constants.DENSITY+0.5f)));
						}
						imageLoader.displayImage(retweetPicList.get(i), imageView, picOptions, mListener);
					}
				}
			}
			
		}else {
			holder.retweet_layout.setVisibility(View.GONE);
		}
		
		holder.btComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, WeiboCommentRetweetActivity.class);
				intent.putExtra("IDstr", weiboStatus.getWeiboId());
				intent.putExtra("type", 0);
				context.startActivity(intent);
			}
		});
		holder.btRetweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, WeiboCommentRetweetActivity.class);
				intent.putExtra("IDstr", weiboStatus.getWeiboId());
				intent.putExtra("type", 1);
				context.startActivity(intent);
			}
		});
	}
	
}
