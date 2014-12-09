package com.joe.orangee.activity.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.joe.orangee.R;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;

public class OrangeeWebActivity extends ActionBarActivity {

	private String url;
	private ProgressBar progressBar;
	private Context context;
	private WebView webView;
	private Toolbar toolbar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		context=this;
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar());
		toolbar.setTitle("");
		url = getIntent().getStringExtra(Constants.URL);
		
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		
		webView = (WebView) findViewById(R.id.web);
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setDisplayZoomControls(false);
		
		webView.setWebViewClient(new OrangeeWebViewClient());
		webView.setWebChromeClient(new OrangeeWebChromeClient());
		webView.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.orangee_web_menu, menu);
		return true;
	}

	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.enter_sec, R.anim.exit_sec_up_down);
			break;
		case R.id.action_web_go:
			Uri uri = Uri.parse(url);  
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
			startActivity(intent);
			break;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_sec, R.anim.exit_sec_up_down);
	}

	private class OrangeeWebChromeClient extends WebChromeClient{

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(newProgress);
			Log.i("progress", newProgress+"");
			if (newProgress==100) {
				Animation fadeOut=AnimationUtils.loadAnimation(context, R.anim.fade_out);
				progressBar.startAnimation(fadeOut);
				progressBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			toolbar.setTitle(title);
			super.onReceivedTitle(view, title);
		}
		
	}
	
	private class OrangeeWebViewClient extends WebViewClient{@Override
		
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}}

	@Override
	protected void onResume() {
		webView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		webView.onPause();
		super.onPause();
	}
		
}
