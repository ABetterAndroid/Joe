package com.joe.orangee.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.joe.orangee.R;
import com.joe.orangee.fragment.web.WebFragment;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.listener.OrangeeImageLoadingListener.ProgressListener;
import com.joe.orangee.util.Utils;
import com.joe.orangee.view.photoview.PhotoView;
import com.joe.orangee.view.photoview.PhotoViewAttacher.OnViewTapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PicsBrowserAdapter extends PagerAdapter {

	private List<String> picUrlsList;
	private ImageLoader imageLoader;
	private DisplayImageOptions picOptions;
	private OrangeeImageLoadingListener.BrowsePictureLoadingListener loadingListener;
	private Context context;
	private Activity activity;
	
	public PicsBrowserAdapter(Context context, List<String> picUrlsList ) {
		super();
		this.context=context;
		activity = (Activity)context;
		this.picUrlsList = picUrlsList;

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
        view.setTag(position);
		final PhotoView photoView=(PhotoView) view.findViewById(R.id.pic_photo);
        final WebView picWeb= (WebView) view.findViewById(R.id.pic_web);
        picWeb.setVisibility(View.INVISIBLE);
        final ProgressBar progressBar= (ProgressBar) view.findViewById(R.id.pic_progress_bar);
        WebSettings webSettings = picWeb.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);
        picWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    picWeb.setVisibility(View.VISIBLE);
                    photoView.setVisibility(View.GONE);
                    Utils.fadeOut(context, progressBar);
                }
            }
        });


        photoView.setTag(picUrlsList.get(position));
        picWeb.setTag(picUrlsList.get(position));
        picWeb.setWebViewClient(new WebFragment.OrangeeWebViewClient());
        loadingListener = new OrangeeImageLoadingListener.BrowsePictureLoadingListener(photoView, picWeb);

		ProgressListener progressListener = new OrangeeImageLoadingListener.ProgressListener(context, progressBar);
        picWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
		photoView.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				activity.onBackPressed();
			}
		});

		imageLoader.displayImage(picUrlsList.get(position).replace("thumbnail", "bmiddle"), photoView, picOptions, loadingListener, progressListener);

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
