package com.joe.orangee.listener;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.joe.orangee.util.Constants;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class OrangeeImageLoadingListener {

	public static class LoadingListener implements ImageLoadingListener{

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingComplete(String arg0, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				FadeInBitmapDisplayer.animate(imageView, 800);
			}
			
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}}
	
	public static class ParamsChangeLoadingListener implements ImageLoadingListener{

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingComplete(String arg0, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				if (loadedImage.getHeight()>500 || loadedImage.getWidth()>600) {
					int width=LayoutParams.WRAP_CONTENT;
					int height=LayoutParams.WRAP_CONTENT;
					if (loadedImage.getHeight()>500) {
						height=(int)(180*Constants.DENSITY+0.5f);
					}
					if (loadedImage.getWidth()>600) {
						width=(int)(220*Constants.DENSITY+0.5f);
					}
					imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
				}
				
				FadeInBitmapDisplayer.animate(imageView, 300);
			}
			
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}}
	
	public static class ProgressListener implements ImageLoadingProgressListener{

		private ProgressBar progressBar;
		
		public ProgressListener(ProgressBar progressBar) {
			super();
			this.progressBar = progressBar;
			
		}
		@Override
		public void onProgressUpdate(String imageUri, View view, int current, int total) {
            Log.e("progress", (int)((float)current/total*100)+"");
			if (progressBar!=null) {
				if (progressBar.getVisibility()!=View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
				}
				float progress =(float)current/total*100;
//                int progressSet= Integer.valueOf(progress.split(".")[0]);
                progressBar.setProgress(100);
//                progressBar.setProgress((int)progress);
				if ((int)progress==100) {
//                    progressBar.setVisibility(View.GONE);
				}
				
			}
	}}

}