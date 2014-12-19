package com.joe.orangee.fragment.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.joe.orangee.R;
import com.joe.orangee.library.OrangeeClickabSpan;

public class WebFragment extends Fragment {
    private static final String ARG_PARAM = "url";

    private ProgressBar progressBar;
    private String url;
    private View view;
    private Context context;
    private static Toolbar toolbar;
    private WebView webView;

    public static WebFragment newInstance(String url, Toolbar mToolbar) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, url);
        fragment.setArguments(args);
        toolbar=mToolbar;
        return fragment;
    }

    public WebFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.url = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null ) {
                parent.removeView(view);
            }
            return view;
        }
        context=getActivity();
        view=inflater.inflate(R.layout.fragment_web, container, false);
        LinearLayout webLayout= (LinearLayout) view.findViewById(R.id.web_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (webView==null){
            webView=new WebView(context);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            webView.setLayoutParams(params);
        }

        webLayout.addView(webView);
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
        return view;
    }

    private class OrangeeWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
            Log.i("progress", newProgress + "");
            if (newProgress == 100) {
                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                progressBar.startAnimation(fadeOut);
                progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (toolbar!=null){
                toolbar.setTitle(title);
            }
            super.onReceivedTitle(view, title);
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

    @Override
    public void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        OrangeeClickabSpan.webView=null;
        super.onDestroy();
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnWebLoadingListener) activity;
        } catch (ClassCastException e) {
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

}
