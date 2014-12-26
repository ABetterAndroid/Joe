package com.joe.orangee.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.joe.orangee.R;
import com.joe.orangee.activity.image.ImageBrowseActivity;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.sql.PicturesSQLUtils;
import com.joe.orangee.util.WeiboItemUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PicsRecyclerViewAdapter extends Adapter<ViewHolder> {

	private List<PictureCollection> dataList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions picOptions;
    View floatView;
    RecyclerView mRecyclerView;
    private float floatViewX;
    private float floatViewY;
    OrangeeImageLoadingListener.LoadingListener mListener;

	public PicsRecyclerViewAdapter(Context context, List<PictureCollection> dataList, View floatView, RecyclerView mRecyclerView) {
		super();
		imageLoader = ImageLoader.getInstance();
		this.dataList = dataList;
		this.context=context;
        this.floatView=floatView;
        this.floatViewX=floatView.getX();
        this.floatViewY=floatView.getY();
        this.mRecyclerView=mRecyclerView;
        mListener=new OrangeeImageLoadingListener.LoadingListener();

		picOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.pic_default)
			.showImageOnFail(R.drawable.pic_default)
			.showImageOnLoading(R.drawable.pic_default)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.build();
	}


	@Override
	public int getItemCount() {
        return dataList.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		if (holder instanceof MyViewHolder) {
			final PictureCollection collection=dataList.get(position);
            imageLoader.displayImage(collection.getUrl(), ((MyViewHolder) holder).ivCollection, picOptions, mListener);
            ((MyViewHolder) holder).ivDelete.setTag(collection);
            ((MyViewHolder) holder).ivCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<String> url=new ArrayList<String>();
                    for (PictureCollection collection: dataList){
                        url.add(collection.getUrl());
                    }
                    Intent intent=new Intent(context, ImageBrowseActivity.class);
                    intent.putStringArrayListExtra("imageList", url);
                    intent.putExtra("current", dataList.indexOf(collection));
                    intent.putExtra("WeiboStatus", collection.getStatus());
                    intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
                    context.startActivity(intent);

                }
            });

            ((MyViewHolder) holder).ivCollection.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    View iv=mRecyclerView.getChildAt(dataList.indexOf(collection));
                    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    int height = windowManager.getDefaultDisplay().getHeight();
                    int width = windowManager.getDefaultDisplay().getWidth();

                    int collectionWidth = iv.getWidth();
                    int collectionHeight = iv.getHeight();
                    floatView.setVisibility(View.VISIBLE);
                    OrangeeRecyclerViewAdapter.MyViewHolder holder = new OrangeeRecyclerViewAdapter.MyViewHolder(floatView);
                    WeiboItemUtil.getWeiboItem(context, imageLoader, mListener, new OrangeeImageLoadingListener.ParamsChangeLoadingListener(), picOptions, picOptions, 0, holder, collection.getStatus());



                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofFloat(floatView, "translationX", 0.0f, floatViewX).setDuration(300),
                            ObjectAnimator.ofFloat(floatView, "translationY", 0.0f, floatViewY).setDuration(300),
                            ObjectAnimator.ofFloat(floatView, "scaleX", 0.0f, 0.3f ).setDuration(500),
                            ObjectAnimator.ofFloat(floatView, "scaleY", 0.0f, 0.3f ).setDuration(500));
                    animatorSet.start();






                    /*Intent intent=new Intent(context, WeiboCommentActivity.class);
                    intent.putExtra("WeiboStatus", collection.getStatus());

                    intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());

                    context.startActivity(intent);*/
                    return true;
                }
            });
            ((MyViewHolder) holder).ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected void onPreExecute() {
                            ((MyViewHolder) holder).ivDelete.setClickable(false);
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            PicturesSQLUtils.deleteOneData(context, ((PictureCollection) (((MyViewHolder) holder).ivDelete.getTag())).getId());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            ((MyViewHolder) holder).ivDelete.setClickable(false);
                            PictureCollection pc= (PictureCollection) ((MyViewHolder) holder).ivDelete.getTag();
                            PicsRecyclerViewAdapter.this.notifyItemRemoved(dataList.indexOf(pc));
                            dataList.remove(pc);
                        }
                    }.execute();
                }
            });
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_col, parent, false);
        ViewHolder vh = new MyViewHolder(view);

        return vh;
	}

    private static class MyViewHolder extends ViewHolder{

        private ImageView ivCollection;
        private ImageView ivDelete;

		public MyViewHolder(View view) {
	        super(view);
            ivCollection= (ImageView) view.findViewById(R.id.col_img);
            ivDelete= (ImageView) view.findViewById(R.id.col_img_del);
	    }
	  
	}  
	
}
