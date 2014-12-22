package com.joe.orangee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.joe.orangee.R;
import com.joe.orangee.model.PictureCollection;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import java.util.List;

public class PicsRecyclerViewAdapter extends Adapter<ViewHolder> {

	private List<PictureCollection> dataList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions picOptions;

	public PicsRecyclerViewAdapter(Context context, List<PictureCollection> dataList) {
		super();
		imageLoader = ImageLoader.getInstance();
		this.dataList = dataList;
		this.context=context;

		picOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.pic_default)
			.showImageOnFail(R.drawable.pic_default)
			.showImageOnLoading(R.drawable.pic_default)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.build();
	}


	@Override
	public int getItemCount() {
        return dataList.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		if (holder instanceof MyViewHolder) {
			PictureCollection collection=dataList.get(position);
            imageLoader.displayImage(collection.getUrl(), ((MyViewHolder) holder).ivCollection, picOptions);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_col, parent, false);
        ViewHolder vh = new MyViewHolder(view);

        return vh;
	}

	public static class MyViewHolder extends ViewHolder{
		  
		public ImageView ivCollection;

		public MyViewHolder(View view) {
	        super(view);
            ivCollection= (ImageView) view.findViewById(R.id.col_img);
	    }
	  
	}  
	
}
