package com.joe.orangee.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joe.orangee.R;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.LoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.ProgressListener;
import com.joe.orangee.view.photoview.PhotoView;
import com.joe.orangee.view.photoview.PhotoViewAttacher.OnViewTapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PicsBrowserAdapter extends PagerAdapter {

	private List<String> picUrlsList;
	private ImageLoader imageLoader;
	private DisplayImageOptions picOptions;
	private LoadingListener loadingListener;
	private Context context;
	private Activity activity;
	
	public PicsBrowserAdapter(Context context, List<String> picUrlsList ) {
		super();
		this.context=context;
		activity = (Activity)context;
		this.picUrlsList = picUrlsList;
		loadingListener = new OrangeeImageLoadingListener.LoadingListener();
		
		imageLoader = ImageLoader.getInstance();
		picOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.pic_default)
		.showImageOnFail(R.drawable.pic_default)
		.showImageOnLoading(R.drawable.pic_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
	}

	@Override
	public int getCount() {
		return picUrlsList.size();
	}

	@SuppressLint("NewApi")
	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view =View.inflate(context, R.layout.photoview_layout, null);
//		PhotoView photoView = new PhotoView(container.getContext());
		PhotoView photoView=(PhotoView) view.findViewById(R.id.pic_photo);
		TextView tvProgress=(TextView) view.findViewById(R.id.pic_progress);
		ProgressListener progressListener = new OrangeeImageLoadingListener.ProgressListener(tvProgress);
		photoView.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				activity.onBackPressed();
			}
		});
//		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		photoView.setLayoutParams(params);
//		photoView.setScaleType(ScaleType.FIT_XY);
		String url=(String) picUrlsList.get(position).replace("thumbnail", "bmiddle");
		imageLoader.displayImage(url, photoView, picOptions, loadingListener, progressListener);

		container.addView(view);

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
