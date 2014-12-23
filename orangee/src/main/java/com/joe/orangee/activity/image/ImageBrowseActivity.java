package com.joe.orangee.activity.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.joe.orangee.R;
import com.joe.orangee.adapter.PicsBrowserAdapter;
import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.sql.PicturesSQLOpenHelper;
import com.joe.orangee.sql.PicturesSQLUtils;
import com.joe.orangee.util.Utils;
import com.joe.orangee.view.photoview.HackyViewPager;

import java.util.ArrayList;

public class ImageBrowseActivity extends ActionBarActivity {

	private Context context;
	private HackyViewPager mViewPager;
	private int currentItem;
    private WeiboStatus status;
    private PicturesSQLOpenHelper mOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image_browse_activity);
		context = this;
        View contentView=findViewById(R.id.pic_browser_layout);
        Utils.setTopPadding(this, contentView);
		currentItem = getIntent().getIntExtra("current", 0);
		ArrayList<String> picList=getIntent().getStringArrayListExtra("imageList");
        status = getIntent().getParcelableExtra("WeiboStatus");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.setActionBarStyle(getSupportActionBar(), 0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		mViewPager = (HackyViewPager) findViewById(R.id.pic_browser_vp);
		mViewPager.setAdapter(new PicsBrowserAdapter(context, picList));
		mViewPager.setCurrentItem(currentItem);
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pic_browse_menu, menu);

        MenuCompat.setShowAsAction(menu.findItem(R.id.action_collect), MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_collect:
                String url=(String)mViewPager.findViewWithTag(mViewPager.getCurrentItem()).findViewById(R.id.pic_photo).getTag();
                Toast.makeText(this, url,Toast.LENGTH_SHORT).show();
                final PictureCollection collection=new PictureCollection(url, status);
                if (mOpenHelper==null){

                    mOpenHelper = new PicturesSQLOpenHelper(context);
                }
                if (mSQLiteDatabase==null){

                    mSQLiteDatabase=mOpenHelper.getWritableDatabase();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PicturesSQLUtils.insertStatusesData(collection, mSQLiteDatabase);
                    }
                }).start();

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (mSQLiteDatabase!=null && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
        if (mOpenHelper!=null) {

            mOpenHelper.close();
        }
        super.onPause();
    }

}
