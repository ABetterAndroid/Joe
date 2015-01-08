package com.joe.orangee.activity.base;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.androidplus.net.NetworkUtil;
import com.joe.orangee.R;

/**
 * Created by qiaorongzhu on 2014/12/17.
 */
public class BaseActivity extends ActionBarActivity {

    public boolean isNetworkOK(){
        if (NetworkUtil.getInstance(this).getNetworkType() == -1) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
