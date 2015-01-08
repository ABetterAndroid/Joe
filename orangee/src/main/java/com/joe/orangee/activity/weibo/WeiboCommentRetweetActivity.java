package com.joe.orangee.activity.weibo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joe.orangee.R;
import com.joe.orangee.activity.base.BaseActivity;
import com.joe.orangee.model.Comment;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.Downloader.CommentDownloader;
import com.joe.orangee.net.Result;
import com.joe.orangee.util.Utils;

public class WeiboCommentRetweetActivity extends BaseActivity {

	private Context context;
	private String ID;
	private EditText et;
	private int type;
	private CheckBox checkBox;
	private String commentID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.comment_retweet_activity);
//		View contentView=findViewById(R.id.content_layout);
//		Utils.setTopPadding(this, contentView);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  
		setSupportActionBar(toolbar);

		ID = getIntent().getStringExtra("IDstr");
		type=getIntent().getIntExtra("type", 0);
		et = (EditText) findViewById(R.id.comment_retweet_edit);
		et.requestFocus();
		TextView tvTip=(TextView) findViewById(R.id.edit_chk_tip);
		checkBox = (CheckBox) findViewById(R.id.edit_check);
		if (type==0) {
//			tvTip.setText(R.string.edit_retweet);
			checkBox.setVisibility(View.INVISIBLE);
		}else if (type==1) {
            Utils.setActionBarStyle(getSupportActionBar(), R.string.retweet);
			tvTip.setText(R.string.edit_comment);
		} else if (type==2) {
            Utils.setActionBarStyle(getSupportActionBar(), R.string.comment);
			commentID = getIntent().getStringExtra("cid");
			checkBox.setVisibility(View.INVISIBLE);
		}
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (checkBox.isChecked()) {
					checkBox.setChecked(false);
				}else {
					checkBox.setChecked(true);
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id==android.R.id.home) {
			finish();
		}else if (id == R.id.action_send) {
            if (isNetworkOK()) {
                String edittedText = et.getText().toString().trim();
                if (type == 0) {
                    if (!edittedText.equals("")) {
                        sendComment(edittedText, ID);
                    } else {
                        Toast.makeText(context, R.string.edit_blank, Toast.LENGTH_SHORT).show();
                    }
                } else if (type == 1) {
                    sendRepost(edittedText, ID, checkBox.isChecked() ? 1 : 0);
                } else if (type == 2) {
                    sendReply(edittedText, ID);
                }
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
	}
	
	private void sendReply(final String text, final String weiboID) {

		new AsyncTask<Void, Void, Void>(){
			
			private int replyResult;

			@Override
			protected Void doInBackground(Void... params) {
				replyResult = new CommentDownloader(context).postReply(commentID, weiboID, text);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (replyResult==0) {
					Toast.makeText(context, "回复失败", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(context, "回复成功", Toast.LENGTH_SHORT).show();
				}
				finish();
				super.onPostExecute(result);
			}}.execute();
		
	}

	protected void sendRepost(final String text, final String weiboID, final int is_comment) {
		new AsyncTask<Void, Void, Void>(){

			private WeiboStatus status;

			@Override
			protected Void doInBackground(Void... params) {
				status = new CommentDownloader(context).postRepost(text, weiboID, is_comment);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (status!=null) {
					Toast.makeText(context, "转发成功", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(context, "转发失败", Toast.LENGTH_SHORT).show();
				}
				finish();
				super.onPostExecute(result);
			}}.execute();
		
	}

	protected void sendComment(final String text, final String weiboID ) {
		new AsyncTask<Void, Void, Void>() {

			private Result<Comment> commentResult;

			@Override
			protected Void doInBackground(Void... params) {
                commentResult = new CommentDownloader(context).postComment(text, weiboID );
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
                if (commentResult.getResult()==null){
                    if (commentResult.getErrorCode()==Result.NETWORK_INVALID){
                        Toast.makeText(context, "无网络-_-|||", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Comment comment=commentResult.getResult();
                    if (comment!=null) {
                        Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT).show();
                    }

                }
				finish();
			}
		}.execute();
		
	}
	
}
