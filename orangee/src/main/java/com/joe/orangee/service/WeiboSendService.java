package com.joe.orangee.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.Toast;
import com.joe.orangee.R;
import com.joe.orangee.callbacks.WeiboSendListener;
import com.joe.orangee.net.WeiboDownloader;

public class WeiboSendService extends IntentService implements WeiboSendListener{


	private Handler handler;
	private Bitmap bitmap;
	private String status;
	private int times;

	public WeiboSendService() {
		super("WeiboSendService");
	}

	 @Override  
    public void onCreate() {  
        super.onCreate();
        handler = new Handler();
    } 
	 
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String path=intent.getStringExtra("pic");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		bitmap = BitmapFactory.decodeFile(path,options);
		status = intent.getStringExtra("status");
		new WeiboDownloader(this).updateStatus(status, bitmap, this);
		
	}

	@Override
	public void onSuccess() {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), R.string.weibo_send_success, Toast.LENGTH_SHORT).show();
				
			}
		});
		stopSelf();
	}

	@Override
	public void onError() {
		
		times+=1;
		if (times<5) {
			new WeiboDownloader(this).updateStatus(status, bitmap, this);
		}else {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), R.string.weibo_send_fail, Toast.LENGTH_SHORT).show();
					
				}
			});
			stopSelf();
		}
		
	}

	@Override
	public void onNoNetword() {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
				
			}
		});
		stopSelf();
	}

}
