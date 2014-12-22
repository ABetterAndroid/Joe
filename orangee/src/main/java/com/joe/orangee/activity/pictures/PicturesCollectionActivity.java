package com.joe.orangee.activity.pictures;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.joe.orangee.R;
import com.joe.orangee.adapter.PicsRecyclerViewAdapter;
import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.sql.PicsDataGetterUtils;
import com.joe.orangee.sql.PicturesSQLOpenHelper;
import com.joe.orangee.sql.PicturesSQLUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pics_collection);
        picRecyclerView= (RecyclerView) findViewById(R.id.pics_col);
        GridLayoutManager layoutManager=new GridLayoutManager(this, 5);
        picRecyclerView.setLayoutManager(layoutManager);
        mOpenHelper = new PicturesSQLOpenHelper(this);
        mSQLiteDatabase = mOpenHelper.getReadableDatabase();
        Cursor mCursor = PicturesSQLUtils.fetchAllData(mSQLiteDatabase);
        if (mCursor != null && mCursor.getCount() !=0) {
            fillDataFromSQL(mCursor);
        }else {
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
                mAdapter=new PicsRecyclerViewAdapter(PicturesCollectionActivity.this, collectionList);
                picRecyclerView.setAdapter(mAdapter);
                super.onPostExecute(result);
            }}.execute();
    }
}
