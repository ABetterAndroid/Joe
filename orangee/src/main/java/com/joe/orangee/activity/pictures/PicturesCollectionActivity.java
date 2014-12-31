package com.joe.orangee.activity.pictures;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pics_collection);
//        View contentView=findViewById(R.id.content_layout);
//        Utils.setTopPadding(this, contentView);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.setActionBarStyle(getSupportActionBar(), R.string.pic_collection);

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
                mAdapter=new PicsRecyclerViewAdapter(PicturesCollectionActivity.this, collectionList, picRecyclerView);
                picRecyclerView.setAdapter(mAdapter);
                super.onPostExecute(result);
            }}.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pic_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_collection_edit:
                startSupportActionMode(mCallback);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionMode.Callback mCallback = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.pic_collection_action_mode, menu);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            boolean ret = false;
            if (item.getItemId() == R.id.mode_collection_delete) {
                mode.finish();
                ret = true;
            }
            return ret;
        }
    };
}
