package com.joe.orangee.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.joe.orangee.R;
import com.joe.orangee.activity.home.OrangeeHomeActivity;
import com.joe.orangee.activity.web.OrangeeWebActivity;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;

public class OrangeeClickabSpan extends ClickableSpan {

	private Context context;
	private String type;
	private String text;
    public static WebView webView;
    private boolean startFlag;

	public OrangeeClickabSpan(Context context, String type, String text) {
	    super();
	    this.context=context;
	    this.type=type;
	    this.text = text;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
	    ds.setColor(ds.linkColor);
	    ds.setUnderlineText(false); //去掉下划线
	}

	@Override
	public void onClick(View widget) {
		switch (type) {
		case "@":
			/*Intent intent=new Intent(context, PersonPageActivity.class);
			intent.putExtra(Constants.PERSON_NAME, text);
			context.startActivity(intent);*/
			break;
		case "#":
			break;
		case "http":
            startFlag=false;
            webView = new WebView(context);
            if (context instanceof OrangeeHomeActivity) {
                Utils.showHotKey(OrangeeHomeActivity.hotKey);
                OrangeeHomeActivity.hotKey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!startFlag) {
                            startWeb();
                            startFlag = true;
                        }
                    }
                });
                /*AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(OrangeeHomeActivity.hotKey, "scaleX", 1f, 1.3f, 1f).setDuration(500),
                        ObjectAnimator.ofFloat(OrangeeHomeActivity.hotKey, "scaleY", 1f, 1.3f, 1f).setDuration(500)
                );
                animatorSet.start();*/

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                webView.setLayoutParams(params);
                webView.setWebViewClient(new OrangeeWebViewClient());
                webView.setWebChromeClient(new OrangeeWebChromeClient());
                webView.loadUrl(text);

            } else {
                startWeb();
            }

			break;
		default:
			break;
		}
		
	}

    private class OrangeeWebViewClient extends WebViewClient {
        @Override

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

        }
    }
    private class OrangeeWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!startFlag){
                startWeb();
                startFlag=true;
            }
        }

    }

    private void startWeb() {
        if (context instanceof OrangeeHomeActivity) {

            Utils.hideHotKey(OrangeeHomeActivity.hotKey);
        }
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent webIntent = new Intent(context, OrangeeWebActivity.class);
                webIntent.putExtra(Constants.URL, text);
                context.startActivity(webIntent);
                ((Activity) context).overridePendingTransition(R.anim.enter_up_down, R.anim.exit);
            }
        }, 300);
    }
}
