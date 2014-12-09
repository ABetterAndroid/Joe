package com.joe.orangee.activity.weibo;

import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.joe.orangee.R;
import com.joe.orangee.fragment.weibo.WeiboEditFragment;
import com.joe.orangee.service.WeiboSendService;
import com.joe.orangee.util.Utils;

public class WeiboEditActivity extends ActionBarActivity {

	public static ArrayList<String> mPicList;
	public static String CAMERA_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/weibo_photo";
	private WeiboEditFragment editFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_edit_activity);
//		View contentView=findViewById(R.id.content_layout);
//		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);
		Utils.setActionBarStyle(getSupportActionBar());
		editFragment = (WeiboEditFragment) getSupportFragmentManager().findFragmentById(R.id.edit_fragment);
		initData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			if(100 == requestCode) {
				//判断返回的数据
				ArrayList<String> pic_filse = data.getExtras().getStringArrayList("pic_paths");
				//显示到页面上
				if(!pic_filse.isEmpty()) {
					mPicList.addAll(pic_filse);
					editFragment.setImages(pic_filse);
					
				}
			}
		}
	}

	private void initData() {
		mPicList = new ArrayList<String>();
		File dir_camera = new File(CAMERA_PATH);
		if (!dir_camera.exists() && !dir_camera.isDirectory()) {
			dir_camera.mkdir();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.weibo_edit_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_send_weibo:
			Intent intent=new Intent(this, WeiboSendService.class);
			if (mPicList.size()>0) {
				intent.putExtra("pic", mPicList.get(0));
				
			}
			intent.putExtra("status", editFragment.etWeibo.getText().toString());
			startService(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
