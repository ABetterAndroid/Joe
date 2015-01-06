package com.joe.orangee.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaorongzhu on 2015/1/5.
 */
public class ComputeYListView extends ListView {
    private int mItemCount;
    private List<Integer> mItemOffsetY;
    private boolean scrollIsComputed = false;
    private int mHeight;
    private int count;

    public ComputeYListView(Context context) {
        super(context);
    }

    public ComputeYListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getListHeight() {
        return mHeight;
    }

    public void computeScrollY() {
        mHeight = 0;
        mItemCount = getAdapter().getCount();

        if (mItemOffsetY == null) {
            mItemOffsetY = new ArrayList<Integer>();
        }
        for (int i = count; i < mItemCount; ++i) {
            View view = getAdapter().getView(i, null, this);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mItemOffsetY.add(mHeight);
            mHeight += view.getMeasuredHeight();
            System.out.println(mHeight);
        }
        count=getAdapter().getCount();
        scrollIsComputed = true;
    }

    public boolean scrollYIsComputed() {
        return scrollIsComputed;
    }

    public int getComputedScrollY() {
        int pos, nScrollY, nItemY;
        View view = null;
        pos = getFirstVisiblePosition();
        view = getChildAt(0);
        nItemY = view.getTop();
        nScrollY = mItemOffsetY.get(pos) - nItemY;
        return nScrollY;
    }
}
