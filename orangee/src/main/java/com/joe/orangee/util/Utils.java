package com.joe.orangee.util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.joe.orangee.R;
import com.joe.orangee.library.OrangeeClickabSpan;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class Utils {

	
	public static byte[] Bitmap2Bytes(Bitmap bm) {
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	     return baos.toByteArray();
	 }
	/**
	 * 设置actionnbar
	 * @since 2014-11-4
	 * @param actionBar
	 */
	public static void setActionBarStyle(ActionBar actionBar) {
		actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.back);
	}
	
	public static HashMap<String, String> getParamMap(Context context){
		
		HashMap<String, String> paramMap=new HashMap<String, String>();
		Oauth2AccessToken mAccessToken = PreferencesKeeper.readAccessToken(context);
		paramMap.put("access_token", mAccessToken.getToken());
		
		return paramMap;
		
	}

    public static DisplayImageOptions getNoDefaultImageOptions(){
        DisplayImageOptions picOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
        return picOptions;
    }

	/**
	 * 获取
	 * @return
	 */
	public static DisplayImageOptions getCommonDisplayImageOptions(){
		
		DisplayImageOptions picOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.pic_default)
			.showImageOnFail(R.drawable.pic_default)
			.showImageOnLoading(R.drawable.pic_default)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.build();
		return picOptions;
	}
	
	public static DisplayImageOptions getRoundedPicDisplayImageOptions(){
		
		DisplayImageOptions  options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.pic_default)
		.showImageOnFail(R.drawable.pic_default)
		.displayer(new RoundedBitmapDisplayer(10000))
		.showImageOnLoading(R.drawable.pic_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
		return options;
	}
	
	/**
	 * 动画隐藏浏览图片
	 * @param picBrowserLayout
	 */
	public static void hidePicLayout(final View view) {
//		OrangeeHomeActivity.vpHome.setScrollable(true);
		AnimatorSet animatorSet=new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.3f, 1.5f, 1.8f).setDuration(300),
												ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.3f, 1.5f, 1.8f).setDuration(300),
												ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f, 0.1f, 0f).setDuration(500));
		animatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(View.GONE);
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		animatorSet.start();
	}
	
	/**
	 * 返回过滤话题 @ 超链接后的文本内容
	 * @param context
	 * @param text
	 * @param isRetweet
	 * @return
	 */
	public static SpannableString spanText(Context context, String text, boolean isRetweet) {
		
		SpannableString postText=new SpannableString(text);
		
		if (postText.length()<3) {
			return postText;
		}
		
		if (text.contains("@")) {
			String[] names=text.split("@");
			int formerIndex=0;
			for (int i = 0; i < names.length; i++) {
				if (i > 0) {
					String nameRaw=names[i];
					String name;
					int blankIndex=nameRaw.indexOf(' ');
					int bracketsDoubleIndex=nameRaw.indexOf("）");
					int bracketsSingleIndex=nameRaw.indexOf(")");
					int doubleColonIndex=nameRaw.indexOf("：");
					int singleColonIndex=nameRaw.indexOf(":");
					int colonIndex = -1;
					if ((bracketsDoubleIndex<bracketsSingleIndex && bracketsDoubleIndex<blankIndex) || (bracketsSingleIndex==-1 && blankIndex==-1)) {
						blankIndex=bracketsDoubleIndex;
					}else if ((bracketsSingleIndex<blankIndex && bracketsSingleIndex<bracketsDoubleIndex) || (blankIndex==1&&bracketsDoubleIndex==-1)) {
						blankIndex=bracketsSingleIndex;
					}
					if (doubleColonIndex != -1 && singleColonIndex!= -1) {
						colonIndex=singleColonIndex<doubleColonIndex? singleColonIndex: doubleColonIndex;
					}else {
						colonIndex=singleColonIndex!=-1? singleColonIndex: doubleColonIndex;
					}
					if ((blankIndex< colonIndex && blankIndex!=-1) || (blankIndex!=-1 && colonIndex==-1) ) {
						name = "@"+nameRaw.substring(0, blankIndex);
					}else if ((colonIndex< blankIndex && colonIndex!=-1) || (colonIndex!=-1 && blankIndex==-1) ) {
						if (doubleColonIndex != -1 && singleColonIndex!= -1) {
							String name1 = "@"+nameRaw.substring(0, doubleColonIndex);
							String name2 = "@"+nameRaw.substring(0, singleColonIndex);
							name=name1.length()>name2.length()? name2:name1;
						}else {
							name=singleColonIndex!=-1? "@"+nameRaw.substring(0, singleColonIndex): "@"+nameRaw.substring(0, doubleColonIndex);
						}
					}else {
						name = "@"+nameRaw.substring(0, nameRaw.length());
					}
					int start=text.indexOf(name, formerIndex);
					int end=text.indexOf(name, formerIndex)+name.length();
//					postText.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					postText.setSpan(new OrangeeClickabSpan(context, "@", text.substring(start+1, end)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					formerIndex=text.indexOf(name, formerIndex)+name.length();
				}
			}
		}
		if (isRetweet) {
//			postText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.indexOf(":"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			postText.setSpan(new OrangeeClickabSpan(context, "@", text.substring(0, text.indexOf(":"))), 0, text.indexOf(":"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (text.contains("http")) {
			String[] urlArray=text.split("t.cn/");
			for (int i = 0; i < urlArray.length; i++) {
				if (i > 0) {
					int index;
					try {
						index = text.indexOf("http://t.cn/"+urlArray[i].substring(0, 7));
						postText.setSpan(new OrangeeClickabSpan(context, "http", text.substring(index, index+19)), index, index+19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					} catch (Exception e) {
						index = text.indexOf("http://t.cn/"+urlArray[i].substring(0, 5));
						postText.setSpan(new OrangeeClickabSpan(context, "http", text.substring(index, index+17)), index, index+17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					}
//					postText.setSpan(new ForegroundColorSpan(Color.BLUE), index, index+19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					
				}
			}
		}
		if (text.contains("#")) {
			int topicRaw=text.split("#").length;
			if (topicRaw > 2) {
				int offsetIndex=0;
				for (int i = 0; i < (int)((topicRaw-1)/2); i++) {
					int start=text.indexOf("#", offsetIndex);
					int end=text.indexOf("#", start+1)+1;
//					postText.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					postText.setSpan(new OrangeeClickabSpan(context, "#", text.substring(start+1, end-1)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					offsetIndex=text.indexOf("#", start+1)+1;
				}
			}
			String doText=text.subSequence(0, text.length()-2).toString();
			if (text.endsWith("#") && doText.contains("#") && topicRaw%2==0) {
				int index=doText.lastIndexOf("#");
//				postText.setSpan(new ForegroundColorSpan(Color.BLUE), index, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
				postText.setSpan(new OrangeeClickabSpan(context, "#", text.substring(index+1, text.length()-1)), index, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
			}
		}
		return postText;
	}
	
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	/**
	 * AMap Marker跳动
	 * @user qiaorongzhu
	 * @since 2014-11-25
	 * @param aMap
	 * @param marker
	 * @param targetLatLng
	 */
	public static void jumpPoint(AMap aMap, final Marker marker, final LatLng targetLatLng) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(targetLatLng);
		startPoint.offset(0, -1000);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 3000;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * targetLatLng.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * targetLatLng.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}
	/**
	 * 获取状态栏高度
	 * @user qiaorongzhu
	 * @since 2014-12-2
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
	       int result = 0;
	       int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
	       if (resourceId > 0) {
	           result = context.getResources().getDimensionPixelSize(resourceId);
	       }
	       return result;
	 }
	
	/**
	 * 设置Padding状态栏高度
	 * @user qiaorongzhu
	 * @since 2014-12-5
	 * @param context
	 * @param view
	 */
	public static void setTopPadding(Context context, View view){
		view.setPadding(0, getStatusBarHeight(context), 0, 0);
	}

    /**
     * 显示hotkey
     * @param hotKey
     */
    public static void showHotKey(View hotKey){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(hotKey, "scaleX", 0, 0.5f, 0.8f, 1f, 1.1f, 1f).setDuration(300),
                ObjectAnimator.ofFloat(hotKey, "scaleY", 0, 0.5f, 0.8f, 1f, 1.1f, 1f).setDuration(300)
        );
        animatorSet.start();
        hotKey.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏hotkey
     * @param hotKey
     */
    public static void hideHotKey(final View hotKey){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(hotKey, "scaleX", 1f, 1.1f, 1f, 0.8f, 0.5f, 0f).setDuration(300),
                ObjectAnimator.ofFloat(hotKey, "scaleY", 1f, 1.1f, 1f, 0.8f, 0.5f, 0f).setDuration(300)
        );
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hotKey.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

}
