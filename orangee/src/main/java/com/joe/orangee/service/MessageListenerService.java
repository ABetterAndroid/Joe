package com.joe.orangee.service;

import java.util.Timer;
import java.util.TimerTask;

import com.joe.orangee.R;
import com.joe.orangee.activity.home.OrangeeHomeActivity;
import com.joe.orangee.model.UnreadMessage;
import com.joe.orangee.net.CommonDownloader;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MessageListenerService extends Service {

	private Context context;
	private UnreadMessage unreadMessage;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.e("MessageService", "------------service create-----------");
		timer = new Timer();
		messageTask = new GetMessageTask();
		context=this;
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("MessageService", "------------service start-----------");
		messageTask.cancel();
		messageTask=null;
		messageTask=new GetMessageTask();
		timer.scheduleAtFixedRate(messageTask, 1000, 600000);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	Handler showMessageHandler=new Handler(){

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			if (unreadMessage.getFollower()!=0) {
				showNotification("新增"+unreadMessage.getFollower()+"名粉丝");
			}
			if (unreadMessage.getComment()!=0) {
				showNotification(unreadMessage.getComment()+"条新评论");
			}
			if (unreadMessage.getMention_status()!=0 || unreadMessage.getMetion_comment()!=0) {
				showNotification("有人提到您");
			}
			unreadMessage=null;
			super.handleMessage(msg);
		}};
	private Timer timer;
	private GetMessageTask messageTask;
	
	private class GetMessageTask extends TimerTask{

		@Override
		public void run() {
			Log.e("MessageService", "------------service task-----------");
			unreadMessage = new CommonDownloader(context).getUnreadMessage();
			if (unreadMessage!=null) {
				showMessageHandler.sendEmptyMessage(0);
			}
		}}
	private void showNotification(String notificationText){
		NotificationManager notificationManager = (NotificationManager)    
	            this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);   
	          
	        // 定义Notification的各种属性   
	        Notification notification =new Notification(); 
	        notification.icon=R.drawable.noti_icon;
	        notification.tickerText="Orangee";
	        
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
	        notification.defaults = Notification.DEFAULT_LIGHTS; 
	        notification.ledARGB = Color.BLUE;   
	        notification.ledOnMS =5000; //闪光时间，毫秒
	          
	        // 设置通知的事件消息   
	        CharSequence contentTitle =notificationText; // 通知栏标题   
	        CharSequence contentText =""; // 通知栏内容   
	        Intent notificationIntent =new Intent(this, OrangeeHomeActivity.class); // 点击该通知后要跳转的Activity   
	        notification.flags=Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
	        PendingIntent contentItent = PendingIntent.getActivity(this, 0, notificationIntent, 0);   
	        notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);   
	          
	        // 把Notification传递给NotificationManager   
	        notificationManager.notify(0, notification);   
	}
}
