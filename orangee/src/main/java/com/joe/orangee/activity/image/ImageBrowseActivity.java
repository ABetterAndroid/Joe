package com.joe.orangee.activity.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.joe.orangee.R;
import com.joe.orangee.activity.base.BaseActivity;
import com.joe.orangee.adapter.PicsBrowserAdapter;
import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.sql.PicturesSQLOpenHelper;
import com.joe.orangee.sql.PicturesSQLUtils;
import com.joe.orangee.util.Utils;
import com.joe.orangee.view.photoview.HackyViewPager;

import java.util.ArrayList;

public class ImageBrowseActivity extends BaseActivity {

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

        isNetworkOK();

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
        mViewPager.setPageTransformer(false, new ParallaxPageTransformer());
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
                Toast.makeText(this, "ͼƬ�ղسɹ�",Toast.LENGTH_SHORT).show();
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

    public class ParallaxPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.8f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
//            ImageView dummyImageView= (ImageView) view.findViewById(R.id.pic_photo);
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(1);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//                dummyImageView.setTranslationX(-position * (pageWidth/2));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(1);
            }
        }
    }

}
