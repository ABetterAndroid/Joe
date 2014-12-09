package com.joe.orangee.activity.common;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplus.util.StringUtil;
import com.joe.orangee.R;
import com.joe.orangee.activity.home.OrangeeHomeActivity;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.PreferencesKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class WelcomeActivity extends Activity {

	private Context context;
	private TextView tvWelcome;
	private TextView tvAuth;
	private SsoHandler mSsoHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		WeiboAuth mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler= new SsoHandler(this, mWeiboAuth);
		setContentView(R.layout.welcome_activity);
		tvWelcome = (TextView) findViewById(R.id.welcome_tv);
		tvAuth = (TextView) findViewById(R.id.auth_tv);
		tvAuth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSsoHandler.authorize(new AuthListener());
				
			}
		});
		jumpToNext();
	}

	private void jumpToNext() {
		Oauth2AccessToken mAccessToken = PreferencesKeeper.readAccessToken(this);
		if (!StringUtil.isNullOrEmpty(mAccessToken.getToken()) && System.currentTimeMillis()<PreferencesKeeper.readAccessToken(context).getExpiresTime()) {
			startActivity(new Intent(WelcomeActivity.this, OrangeeHomeActivity.class));
			WelcomeActivity.this.finish();
		}else {
			final AnimatorSet animatorSet=new AnimatorSet();
			Animator animator=ObjectAnimator.ofFloat(tvWelcome, "translationY", 0, -200, -300, -400).setDuration(1000);
			animator.setStartDelay(1000);
			animatorSet.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					tvAuth.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
					tvAuth.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					
				}
			});
			animatorSet.playTogether(ObjectAnimator.ofFloat(tvWelcome, "rotationY", 0, 180, 360).setDuration(1000), animator);
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					animatorSet.start();
					
				}
			}, 2000);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
		mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			Toast.makeText(WelcomeActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
			Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values); 
			if (mAccessToken.isSessionValid()) {
			PreferencesKeeper.writeAccessToken(WelcomeActivity.this, mAccessToken);
			startActivity(new Intent(WelcomeActivity.this, OrangeeHomeActivity.class));
			WelcomeActivity.this.finish();
			} else {
				// 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
//			String code = values.getString("code", "");
	//		.........
			}
		}
//		.........

		@Override
		public void onCancel() {
			Toast.makeText(WelcomeActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(WelcomeActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
			
		}
	}
	
}
