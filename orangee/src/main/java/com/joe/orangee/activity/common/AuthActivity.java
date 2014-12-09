package com.joe.orangee.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.joe.orangee.R;
import com.joe.orangee.activity.home.OrangeeHomeActivity;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.PreferencesKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class AuthActivity extends Activity {

	private SsoHandler mSsoHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_activity);
		WeiboAuth mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler= new SsoHandler(this, mWeiboAuth);
		
		findViewById(R.id.bt_auth).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mSsoHandler.authorize(new AuthListener());
				
			}
		});
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
			Toast.makeText(AuthActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
			Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values); 
			if (mAccessToken.isSessionValid()) {
			PreferencesKeeper.writeAccessToken(AuthActivity.this, mAccessToken);
			startActivity(new Intent(AuthActivity.this, OrangeeHomeActivity.class));
			AuthActivity.this.finish();
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
			Toast.makeText(AuthActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(AuthActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
			
		}
	}
	
}
