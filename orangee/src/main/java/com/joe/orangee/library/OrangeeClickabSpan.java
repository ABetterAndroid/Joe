package com.joe.orangee.library;

import com.joe.orangee.R;
import com.joe.orangee.activity.web.OrangeeWebActivity;
import com.joe.orangee.util.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class OrangeeClickabSpan extends ClickableSpan {

	private Context context;
	private String type;
	private String text;

	public OrangeeClickabSpan(Context context, String type, String text) {
	    super();
	    this.context=context;
	    this.type=type;
	    this.text = text;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
	    ds.setColor(ds.linkColor);
	    ds.setUnderlineText(false); //去掉下划线
	}

	@Override
	public void onClick(View widget) {
		switch (type) {
		case "@":
			/*Intent intent=new Intent(context, PersonPageActivity.class);
			intent.putExtra(Constants.PERSON_NAME, text);
			context.startActivity(intent);*/
			break;
		case "#":
			break;
		case "http":
			Intent webIntent=new Intent(context, OrangeeWebActivity.class);
			webIntent.putExtra(Constants.URL, text);
			context.startActivity(webIntent);
			((Activity)context).overridePendingTransition(R.anim.enter_up_down, R.anim.exit);
			break;
		default:
			break;
		}
		
	}


}
