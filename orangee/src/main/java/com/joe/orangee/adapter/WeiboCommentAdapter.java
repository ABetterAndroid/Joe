package com.joe.orangee.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.joe.orangee.R;
import com.joe.orangee.model.Comment;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("UseSparseArrays")
public class WeiboCommentAdapter extends BaseAdapter {

	private List<Comment> dataList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public WeiboCommentAdapter(List<Comment> dataList, Context context) {
		super();
		imageLoader = ImageLoader.getInstance();
		this.dataList = dataList;
		this.context=context;
		
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.pic_default)
			.showImageOnFail(R.drawable.pic_default)
			.showImageOnLoading(R.drawable.pic_default)
			.displayer(new RoundedBitmapDisplayer(1000))
			.cacheInMemory(true)
			.cacheOnDisk(true)
				.build();
	}

	public void clearDataList(){
		dataList.clear();
	}
	
	public void addData(List<Comment> dataList){
		this.dataList.addAll(dataList);
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view;
		ViewHolder holder;
		if (convertView!=null && convertView instanceof RelativeLayout) {
			view=convertView;
			holder=(ViewHolder)view.getTag();
		}else {
			view=View.inflate(context, R.layout.weibo_comment_item, null);
			holder=new ViewHolder();
			holder.avatar=(ImageView) view.findViewById(R.id.comment_avatar);
			holder.name=(TextView) view.findViewById(R.id.comment_name);
			holder.time=(TextView) view.findViewById(R.id.comment_time);
			holder.source=(TextView) view.findViewById(R.id.comment_source);
			holder.content=(TextView) view.findViewById(R.id.comment_content);
			view.setTag(holder);
		}
		final Comment comment=dataList.get(position);
		imageLoader.displayImage(comment.getUser().getAvatar(), holder.avatar, options);
		/*holder.avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, PersonPageActivity.class);
				intent.putExtra(Constants.PERSON_NAME, comment.getUser().getName());
				context.startActivity(intent);
				
			}
		});*/
		holder.name.setText(comment.getUser().getName());
		holder.time.setText(comment.getTime());
		holder.source.setText(comment.getSource());
		holder.content.setMovementMethod(LinkMovementMethod.getInstance());
		holder.content.setText(Utils.spanText(context, comment.getText(), false));
		return view;
	}

	private class ViewHolder{
		ImageView avatar;
		TextView name;
		TextView time;
		TextView source;
		TextView content;
	}
}
