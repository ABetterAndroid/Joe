package com.joe.orangee.fragment.weibo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.joe.orangee.R;
import com.joe.orangee.activity.common.ImagePickActivity;
import com.joe.orangee.util.Constants;

import java.util.List;

@SuppressLint("ValidFragment")
public class WeiboEditFragment extends Fragment implements OnClickListener {

	private View view;
	private LinearLayout imgLayout;
	private int width=(int)(40*Constants.DENSITY+0.5f);
	private int height=(int)(40*Constants.DENSITY+0.5f);
	private int margin=(int)(5*Constants.DENSITY+0.5f);
	public EditText etWeibo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null ) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.weibo_edit_fragment, container, false);
		etWeibo = (EditText) view.findViewById(R.id.weibo_edit);
		ImageView ivAddImage=(ImageView) view.findViewById(R.id.edit_add_img);
		ivAddImage.setOnClickListener(this);
		imgLayout = (LinearLayout) view.findViewById(R.id.edit_img_layout);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_add_img:
			getActivity().startActivityForResult(new Intent(getActivity(), ImagePickActivity.class), 100);
			break;

		default:
			break;
		}
		
	}

	public void setImages(List<String> picPaths){
		
		for (String path : picPaths) {
			ImageView imageView=new ImageView(getActivity());
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
			params.setMargins(margin, margin, margin, margin);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				Bitmap takeBitmap = BitmapFactory.decodeFile(path,options);
				imageView.setImageBitmap(takeBitmap);
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
			}
			imgLayout.addView(imageView);
		}
		
	}
	
}
