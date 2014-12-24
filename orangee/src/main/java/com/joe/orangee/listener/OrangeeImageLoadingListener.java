package com.joe.orangee.listener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;
import com.joe.orangee.view.photoview.PhotoView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class OrangeeImageLoadingListener {

    public static class BrowsePictureLoadingListener implements ImageLoadingListener{

        private PhotoView photoView;
        private WebView webView;

        public BrowsePictureLoadingListener(PhotoView photoView, WebView webView) {
            this.photoView = photoView;
            this.webView = webView;
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLoadingComplete(String url, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                if (loadedImage.getHeight()/loadedImage.getWidth() > 3 || url.endsWith("gif")){
                    webView.loadUrl(url);

                }else{
                    webView.setVisibility(View.GONE);
                }
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
                    if (loadedImage.getHeight()>3000){
                        width=(int)(100*Constants.DENSITY+0.5f);

                        int i=2;

                        while (loadedImage.getHeight()/i > 3000){
                            i+=1;
                        }

                        imageView.setImageBitmap(Bitmap.createScaledBitmap(loadedImage, loadedImage.getWidth()/i, loadedImage.getHeight()/i,true));

                    }
					imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
				}
				
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
	
	public static class ProgressListener implements ImageLoadingProgressListener{

		private ProgressBar progressBar;
        private Context context;

		public ProgressListener(Context context, ProgressBar progressBar) {
			super();
			this.progressBar = progressBar;
            this.context=context;

		}
		@Override
		public void onProgressUpdate(String imageUri, View view, int current, int total) {

			if (progressBar!=null) {
				if (progressBar.getVisibility()==View.INVISIBLE) {
                    Utils.fadeIn(context, progressBar);
				}
				float progress =(float)current/total*100;
                progressBar.setProgress((int) progress);
				if ((int)progress==100) {
                    Utils.fadeOut(context, progressBar);
				}
				
			}
	}}

}