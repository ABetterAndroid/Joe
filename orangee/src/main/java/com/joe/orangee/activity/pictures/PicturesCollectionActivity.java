package com.joe.orangee.activity.pictures;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joe.orangee.R;
import com.joe.orangee.adapter.PicsRecyclerViewAdapter;
import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.sql.PicsDataGetterUtils;
import com.joe.orangee.sql.PicturesSQLOpenHelper;
import com.joe.orangee.sql.PicturesSQLUtils;
import com.joe.orangee.util.Utils;

import java.util.List;

/**
 * Created by qiaorongzhu on 2014/12/22.
 */
public class PicturesCollectionActivity extends ActionBarActivity{

    private PicturesSQLOpenHelper mOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private List<PictureCollection> collectionList;
    private PicsRecyclerViewAdapter mAdapter;
    private RecyclerView picRecyclerView;
    private View floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pics_collection);
        View contentView=findViewById(R.id.content_layout);
        Utils.setTopPadding(this, contentView);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.setActionBarStyle(getSupportActionBar(), R.string.pic_collection);

        floatView=findViewById(R.id.item_layout);
        floatView.setVisibility(View.GONE);

        picRecyclerView= (RecyclerView) findViewById(R.id.pics_col);
        picRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager=new GridLayoutManager(this, 3);
        picRecyclerView.setLayoutManager(layoutManager);

        mOpenHelper = new PicturesSQLOpenHelper(this);
        mSQLiteDatabase = mOpenHelper.getReadableDatabase();
        Cursor mCursor = PicturesSQLUtils.fetchAllData(mSQLiteDatabase);
        if (mCursor != null && mCursor.getCount() !=0) {
            fillDataFromSQL(mCursor);
        }

    }

    private void fillDataFromSQL(final Cursor mCursor) {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                collectionList= PicsDataGetterUtils.getStatuses(mCursor);
                return null;
            }

            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(Void result) {
                mCursor.close();
				mSQLiteDatabase.close();
				mOpenHelper.close();
                mAdapter=new PicsRecyclerViewAdapter(PicturesCollectionActivity.this, collectionList, floatView, picRecyclerView);
                picRecyclerView.setAdapter(mAdapter);
                super.onPostExecute(result);
            }}.execute();
    }
}
