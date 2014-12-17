package com.joe.orangee.activity.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.joe.orangee.R;
import com.joe.orangee.activity.weibo.WeiboEditActivity;
import com.joe.orangee.util.PickPhotoUtil;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImagePickActivity extends ActionBarActivity {

	private GridView gridview;
//	TextView group_text, total_text;
	private ProgressDialog mProgressDialog;

	private ImageLoader mImageLoader;

	private HashMap<String, ArrayList<String>> mGruopMap = new HashMap<String, ArrayList<String>>();

	private ArrayList<String> mAllImgs;
	private final static int SCAN_OK = 1;

	private DisplayImageOptions options;

	private int limit_count ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_pick_activity);
		View contentView=findViewById(R.id.content_layout);
		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
        Utils.setActionBarStyle(getSupportActionBar(), R.string.chose_picture);
		initView();
		initData();
		setListener();
	}

	private void setListener() {

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(chooseItem.get(0) == 0 && 0 == position) {
					if(addedPath.size() >= limit_count) {
						Toast.makeText(ImagePickActivity.this, "最多选9张，请取消后再点击拍照", Toast.LENGTH_SHORT).show();
						return;
					}
					
					tempCameraPath = WeiboEditActivity.CAMERA_PATH + "/"
							+ System.currentTimeMillis() + ".jpg";
					PickPhotoUtil.getInstance().takePhoto(
							ImagePickActivity.this, "tempUser", tempCameraPath);
				}else {
					if(addedPath.contains(gridview.getAdapter().getItem(position))) {
						//已经包含这个path了，则干掉
						addedPath.remove(gridview.getAdapter().getItem(position));
						ImageView icon=(ImageView)arg1.findViewById(R.id.grid_img);
						icon.setImageResource(R.drawable.friends_sends_pictures_select_icon_unselected);
					} else {
						//判断大小
						if(addedPath.size() < limit_count) {
							addedPath.add(((GridAdapter)gridview.getAdapter()).getItem(position));
							ImageView icon=(ImageView)arg1.findViewById(R.id.grid_img);
							icon.setImageResource(R.drawable.friends_sends_pictures_select_icon_selected);
							//添加图片，显示出来张数
						}
					}
				}
			}
		});
	}
	
	private String tempCameraPath = "";
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
			case PickPhotoUtil.PickPhotoCode.PICKPHOTO_TAKE:
				
				File fi = new File("");
				PickPhotoUtil.getInstance().takeResult(this,
						data, fi);
				
				//相机的图片
				ArrayList<String> camepaths = new ArrayList<String>();
				camepaths.add(tempCameraPath);
				Intent dataIntent = new Intent();
				Bundle dataBundle = new Bundle();
				dataBundle.putStringArrayList("pic_paths", camepaths);
				dataIntent.putExtras(dataBundle);
				setResult(RESULT_OK, dataIntent);
				ImagePickActivity.this.finish();
				break;

			default:
				break;
			}
		}
	}


	ArrayList<String> nowStrs = new ArrayList<String>();


	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				mProgressDialog.dismiss();
				
				// 获取到mAllImgs；并显示到数据中
				GridAdapter gridAdatper = new GridAdapter();
				gridAdatper.setData(mAllImgs);
				gridview.setAdapter(gridAdatper);
				gridAdatper = null;
				break;
			}
		}

	};

	private void initView() {
		gridview = (GridView) findViewById(R.id.gridview);

	}

	private void initData() {
		// 初始化数据，所有图片应在281张以内
		chooseItem.add(0);
		mImageLoader = ImageLoader.getInstance();

		options = Utils.getCommonDisplayImageOptions();

		mAllImgs = new ArrayList<String>(281);
		addedPath = new ArrayList<String>();
		limit_count = 1- WeiboEditActivity.mPicList.size();
//		total_text.setText("0/"+limit_count+"张");
		getImages();
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}

		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载图片...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = ImagePickActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					// 获取该图片的父路径名
					File pa_file = new File(path).getParentFile();
					String parentName = pa_file.getAbsolutePath();
					if (mAllImgs.size() < 281) {
						mAllImgs.add(path);
					}
					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						ArrayList<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(path);
					}
				}

				mCursor.close();

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);

			}
		}).start();

	}

	private ArrayList<String> addedPath = null;

	// gridview的Adapter
	class GridAdapter extends BaseAdapter {
		// 根据三种不同的布局来应用
		final int VIEW_TYPE = 2;
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;
		LayoutInflater inflater;
		private ArrayList<String> gridStrings;

		public GridAdapter() {
			gridStrings = new ArrayList<String>();
			inflater = LayoutInflater.from(ImagePickActivity.this);
		}

		public void setData(ArrayList<String> strs) {
			if (null != strs) {
				gridStrings.clear();
				gridStrings.addAll(strs);
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gridStrings.size();
		}

		@Override
		public String getItem(int position) {
			if (chooseItem.get(0) == 0) {
				return gridStrings.get(position - 1);
			} else {
				return gridStrings.get(position);
			}
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			if (chooseItem.get(0) == 0) {
				if (position == 0) {
					return TYPE_1;
				} else {
					return TYPE_2;
				}
			} else {
				return TYPE_2;
			}
		}

		@Override
		public int getViewTypeCount() {
			if (chooseItem.get(0) == 0) {
				return VIEW_TYPE;
			} else {
				return 1;
			}
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			GridHolder gridHolder = null  ;
			PhotoHolder photoHodler = null;
			int type = getItemViewType(position);
			if (convertView == null) {
				switch (type) {
				case TYPE_1:
					// 显示拍照
					photoHodler = new PhotoHolder();
					convertView = inflater.inflate(R.layout.take_photo, null);
					convertView.setTag(photoHodler);
					break;
				case TYPE_2:
					convertView = inflater.inflate(R.layout.image_pick_grid_item, null);
					gridHolder = new GridHolder();
					gridHolder.grid_image = (ImageView) convertView
							.findViewById(R.id.grid_image);
					gridHolder.grid_img = (ImageView) convertView
							.findViewById(R.id.grid_img);
					convertView.setTag(gridHolder);
					break;
				default:
					break;
				}
			} else {
				switch (type) {
				case TYPE_1:
					// 显示拍照
					photoHodler = (PhotoHolder) convertView.getTag();
					break;
				case TYPE_2:
					gridHolder = (GridHolder) convertView.getTag();
					break;
				default:
					break;
				}
			}

			if (type == TYPE_2) {
				// 判断是否已经添加
				final ImageView imageView=gridHolder.grid_image;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
					}
				}).start();
				mImageLoader.displayImage("file://" + getItem(position) ,imageView, options);

				if (addedPath.contains(getItem(position))) {
					// 已经添加过了
					gridHolder.grid_img.setImageResource(R.drawable.friends_sends_pictures_select_icon_selected);
				} else {
					gridHolder.grid_img.setImageResource(R.drawable.friends_sends_pictures_select_icon_unselected);
				}
			}

			return convertView;
		}

		class PhotoHolder {

		}

		class GridHolder {
			ImageView grid_image;
			public ImageView grid_img;
		}

	}
	
	@SuppressLint("HandlerLeak")
	Handler mYhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
//				total_text.setText(addedPath.size()+"/"+limit_count+"张");
				break;

			default:
				break;
			}
		}
	};

	private ArrayList<Integer> chooseItem = new ArrayList<Integer>();

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_pick_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_pick_done:
			Intent dataIntent = new Intent();
			Bundle dataBundle = new Bundle();
			dataBundle.putStringArrayList("pic_paths", addedPath);
			dataIntent.putExtras(dataBundle);
			setResult(RESULT_OK, dataIntent);
			ImagePickActivity.this.finish();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
