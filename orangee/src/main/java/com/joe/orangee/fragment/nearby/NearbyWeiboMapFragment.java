package com.joe.orangee.fragment.nearby;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.joe.orangee.R;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("ValidFragment")
public class NearbyWeiboMapFragment extends Fragment implements AMapLocationListener, View.OnClickListener {

    private LocationManagerProxy locationManager;
    private View view;
    private ImageView ivMap;
    int screenWidth;
    int screenHeigh;
    View hotKey;
    int cx;
    int cy;
    int finalRadius;
    AnimatorSet animatorSet;
    AnimatorSet outAnimatorSet;
    Animator smallerAnim;
    Animator biggerAnim;
    FrameLayout statusLayout;

    public NearbyWeiboMapFragment() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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

        view = inflater.inflate(R.layout.fragment_nearby_weibo_map, container, false);
        statusLayout = (FrameLayout) view.findViewById(R.id.status_list_container);
        ivMap= (ImageView) view.findViewById(R.id.static_map);

        hotKey=view.findViewById(R.id.hot_key);
        Utils.hideHotKey(hotKey);
        hotKey.setVisibility(View.INVISIBLE);
        prepare();

        hotKey.setOnClickListener(this);


        locationManager = LocationManagerProxy.getInstance(getActivity());
        locationManager.setGpsEnable(true);
        // API定位采用GPS定位方式，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
        locationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 1000000, 10, this);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void prepare() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;

        screenHeigh = dm.heightPixels;

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(hotKey, "scaleX", 0, 0.5f, 0.8f, 1f, 1.1f, 1f).setDuration(300),
                ObjectAnimator.ofFloat(hotKey, "scaleY", 0, 0.5f, 0.8f, 1f, 1.1f, 1f).setDuration(300)
        );


        outAnimatorSet = new AnimatorSet();
        outAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(hotKey, "scaleX", 1f, 1.1f, 1f, 0.8f, 0.5f, 0f).setDuration(300),
                ObjectAnimator.ofFloat(hotKey, "scaleY", 1f, 1.1f, 1f, 0.8f, 0.5f, 0f).setDuration(300)
        );
        outAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                biggerAnim.start();
                ivMap.setVisibility(View.VISIBLE);
                hotKey.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        final String mapImageUrl= Constants.STATIC_MAP_URL+"&markers=mid,,:"+aMapLocation.getLongitude()+","+aMapLocation.getLatitude();
        ImageLoader.getInstance().displayImage(mapImageUrl, ivMap, Utils.getNoDefaultImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap != null) {
                    ImageView imageView = (ImageView) view;
                    FadeInBitmapDisplayer.animate(imageView, 800);
                }
                view.postDelayed(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {

                        statusLayout.setVisibility(View.VISIBLE);
                        cx = (hotKey.getLeft() + hotKey.getRight()) / 2;
                        cy = (hotKey.getTop() + hotKey.getBottom()) / 2;

                        finalRadius = Math.max(ivMap.getWidth(), ivMap.getHeight());

                        smallerAnim = ViewAnimationUtils.createCircularReveal(ivMap, cx, cy, finalRadius, 0);

                        smallerAnim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ivMap.setVisibility(View.GONE);
                                hotKey.setVisibility(View.VISIBLE);
                                animatorSet.start();
                            }
                        });
                        smallerAnim.start();
                    }
                }, 2000);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.status_list_container, new NearbyWeiboFragment(aMapLocation.getLatitude(), aMapLocation.getLongitude()))
                .commit();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager.destory();
        }
        locationManager = null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        biggerAnim = ViewAnimationUtils.createCircularReveal(ivMap, cx, cy, 0, finalRadius);
        outAnimatorSet.start();
    }
}
