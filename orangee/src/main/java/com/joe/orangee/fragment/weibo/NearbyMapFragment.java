package com.joe.orangee.fragment.weibo;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.joe.orangee.R;
import com.joe.orangee.activity.weibo.WeiboCommentActivity;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.WeiboDownloader;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class NearbyMapFragment extends Fragment implements AMapLocationListener, OnMarkerClickListener, OnInfoWindowClickListener, LocationSource, InfoWindowAdapter {

	private View view;
	private Context context;
	private AMap aMap;
	private MapView mapView;
	private Marker marker;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Double latitude;
	private Double longitude;
	private List<WeiboStatus> statusList;
	private List<WeiboStatus> statusTotalList=new ArrayList<WeiboStatus>();
	private List<Marker> markers=new ArrayList<Marker>();
	private List<LatLng> points=new ArrayList<LatLng>();
	private ImageLoader imageLoader;
	private int page=1;
	public static double currentLat;
	public static double currentLng;
	
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
		view = inflater.inflate(R.layout.nearby_weibo_map, container, false);
		context=getActivity();
		imageLoader = ImageLoader.getInstance();
		mapView = (MapView) view.findViewById(R.id.weibo_map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		init();
		
		return view;
	}
	
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				

				@Override
				public void onCameraChangeFinish(CameraPosition position) {
					LatLng latLng = null;
					if (latitude!=null && longitude!=null) {
						latLng = new LatLng(latitude, longitude);
					}
					if (!points.contains(latLng)) {
						points.add(latLng);
					}
					for (int i = 0; i < points.size(); i++) {
						float distance=AMapUtils.calculateLineDistance(position.target, points.get(i));
						if (distance<2000) {
							/*if (i==points.size()-1) {
								if (position.zoom>15.5) {
									page+=1;
									getWeiboList();
								}
							}*/
							break;
						}
						if (i==points.size()-1) {
//							page=1;
							latitude=position.target.latitude;
							longitude=position.target.longitude;
							getWeiboList();
						}
					}
				}
				
				@Override
				public void onCameraChange(CameraPosition position) {
					if (marker!=null && marker.isInfoWindowShown()) {
						marker.hideInfoWindow();
					}
				}
			});
			aMap.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng arg0) {
					if (marker!=null && marker.isInfoWindowShown()) {
						marker.hideInfoWindow();
					}
				}
			});
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.icon_marka));
			myLocationStyle.strokeColor(getResources().getColor(R.color.transparent));// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(getResources().getColor(R.color.transparent));// 设置圆形的填充颜色
			aMap.setMyLocationStyle(myLocationStyle);
			aMap.setOnMarkerClickListener(this);
			aMap.setOnInfoWindowClickListener(this);
			aMap.setLocationSource(this);// 设置定位监听
			UiSettings mUiSettings=aMap.getUiSettings();
			mUiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
			mUiSettings.setRotateGesturesEnabled(false);
			mUiSettings.setTiltGesturesEnabled(false);
			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
			//设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			aMap.setInfoWindowAdapter(this);
			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		markers=null;
		mapView.onDestroy();
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		this.marker = marker;
		if (!marker.isInfoWindowShown()) {
//			aMap.animateCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
			marker.showInfoWindow();
		}else {
			marker.hideInfoWindow();
		}
        return true;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		int index=Integer.valueOf(marker.getSnippet());
		WeiboStatus status=statusTotalList.get(index);
		Intent intent=new Intent(context, WeiboCommentActivity.class);
		intent.putExtra("WeiboStatus", status);
		intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
		context.startActivity(intent);
		if (marker!=null && marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 600000, 10, this);
		}
		
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
		
	}

	@SuppressLint("InflateParams")
	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = ((Activity) context).getLayoutInflater().inflate(
				R.layout.nearby_infowindow, null);
		TextView tvTitle=(TextView) infoContent.findViewById(R.id.nearby_marker_text);
		tvTitle.setText(marker.getTitle());
		return infoContent;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
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
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {

			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			currentLat=latitude = aLocation.getLatitude();
			currentLng=longitude = aLocation.getLongitude();
			aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
							new LatLng(latitude, longitude), 15, 0, 0)), 1000, null);
			getWeiboList();
		}
		
	}
	
	private void getWeiboList() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				statusList = new WeiboDownloader(context).getNearbyStatusList(latitude, longitude, 10, page);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (markers.size()>50) {
					for (int i = 0; i < 10; i++) {
						markers.get(i).remove();
						statusTotalList.remove(i);
						markers.remove(i);
					}
				}
				if (statusList!=null) {
					for (int i = 0; i < statusList.size(); i++) {
						WeiboStatus status=statusList.get(i);
						if (!statusTotalList.contains(status)) {
							statusTotalList.add(status);
							LatLng latLng=new LatLng(status.getLatitude(), status.getLongitude());
							String url=status.getUser().getAvatar();
							String text=status.getUser().getName()+":"+status.getPostText();
							imageLoader.loadImage(url, Utils.getCommonDisplayImageOptions(), new MarkerImageLodingListener(latLng, text, statusTotalList.indexOf(status)));
							
						}
					}
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}
	
	private class MarkerImageLodingListener implements ImageLoadingListener{
		
		private LatLng latLng;
		private String title;
		private int id;

		public MarkerImageLodingListener(LatLng latLng, String title, int id) {
			super();
			this.latLng = latLng;
			this.title=title;
			this.id=id;
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			View markerView=View.inflate(context, R.layout.nearby_avatar_img, null);
			ImageView iv=(ImageView) markerView.findViewById(R.id.nearby_img);
			if (loadedImage!=null) {
				iv.setImageBitmap(Utils.toRoundCorner(loadedImage, 10));
				
			}
			
			Marker marker=aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.position(latLng)
					.icon(BitmapDescriptorFactory.fromView(markerView))
					.title(title)
					.snippet(id+""));
			Utils.jumpPoint(aMap, marker, latLng);
			try {
				markers.add(marker);
			} catch (NullPointerException e) {
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
		}} 
	
}
