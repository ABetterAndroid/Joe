package com.joe.orangee.fragment.weibo;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.joe.orangee.R;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NearbyWeiboMapFragment extends Fragment implements AMapLocationListener {

    private LocationManagerProxy locationManager;
    private View view;
    private ImageView ivMap;

    public NearbyWeiboMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null ) {
                parent.removeView(view);
            }
            ivMap= (ImageView) parent.findViewById(R.id.static_map);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_nearby_weibo_map, container, false);
        locationManager = LocationManagerProxy
                .getInstance(getActivity());
        // API定位采用GPS定位方式，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
        locationManager.requestLocationUpdates(
                LocationManagerProxy.GPS_PROVIDER, 10000000, 10, this);
        return view;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        String mapImageUrl= Constants.STATIC_MAP_URL+"&markers=mid,,:"+aMapLocation.getLongitude()+","+aMapLocation.getLatitude();
        ImageLoader.getInstance().displayImage(mapImageUrl, ivMap, Utils.getCommonDisplayImageOptions(), new OrangeeImageLoadingListener.LoadingListener());
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
}
