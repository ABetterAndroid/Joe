package com.joe.orangee.adapter;

import java.util.List;
import com.joe.orangee.R;
import com.joe.orangee.activity.weibo.WeiboCommentActivity;
import com.joe.orangee.activity.weibo.WeiboCommentRetweetActivity;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.model.Comment;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCommentAdapter extends BaseAdapter {

	public List<Comment> dataList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;
	
	public MyCommentAdapter(List<Comment> dataList, Context context) {
		super();
		this.dataList = dataList;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		imageOptions = Utils.getRoundedPicDisplayImageOptions();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView!=null && convertView instanceof RelativeLayout) {
			view=convertView;
			holder=(ViewHolder)view.getTag();
		}else {
			view=View.inflate(context, R.layout.my_comment_item, null);
			holder=new ViewHolder();
			holder.avatar=(ImageView) view.findViewById(R.id.my_comment_avatar);
			holder.name=(TextView) view.findViewById(R.id.my_comment_name);
			holder.time=(TextView) view.findViewById(R.id.my_comment_time);
			holder.source=(TextView) view.findViewById(R.id.my_comment_source);
			holder.commentText=(TextView) view.findViewById(R.id.my_comment_text);
			holder.replyedText=(TextView) view.findViewById(R.id.replyed_text);
			holder.replyLayout=(LinearLayout) view.findViewById(R.id.reply_btn_layout);
			holder.sourceLayout=(LinearLayout) view.findViewById(R.id.source_btn_layout);
			view.setTag(holder);
		}
		final Comment comment=dataList.get(position);
		imageLoader.displayImage(comment.getUser().getAvatar(), holder.avatar, imageOptions, new OrangeeImageLoadingListener.LoadingListener());
		/*holder.avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, PersonPageActivity.class);
				intent.putExtra(Constants.PERSON_NAME, comment.getUser().getName());
				context.startActivity(intent);
				
			}
		});*/
		holder.sourceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiboStatus status=comment.getStatus();
				Intent intent=new Intent(context, WeiboCommentActivity.class);
				intent.putExtra("WeiboStatus", status);
				context.startActivity(intent);
				
			}
		});
		
		holder.replyLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String cid=comment.getComment_id();
				String IDstr=comment.getStatus().getWeiboId();
				Intent intent=new Intent(context, WeiboCommentRetweetActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("IDstr", IDstr);
				intent.putExtra("cid", cid);
				context.startActivity(intent);
				
			}
		});
		
		holder.name.setText(comment.getUser().getName());
		holder.time.setText(comment.getTime());
		holder.source.setText(comment.getSource());
		holder.commentText.setMovementMethod(LinkMovementMethod.getInstance());
		holder.commentText.setText(Utils.spanText(context, comment.getText(), false));
		holder.replyedText.setMovementMethod(LinkMovementMethod.getInstance());
		String replyedText;
		try {
			replyedText=comment.getReplyComment().getText();
		} catch (Exception e) {
			replyedText=comment.getStatus().getUser().getName()+":"+comment.getStatus().getPostText();
		}
		holder.replyedText.setText(Utils.spanText(context, replyedText, false));
		return view;
	}

	private class ViewHolder{
		ImageView avatar;
		TextView name;
		TextView time;
		TextView source;
		TextView commentText;
		TextView replyedText;
		LinearLayout replyLayout;
		LinearLayout sourceLayout;
	}
}
