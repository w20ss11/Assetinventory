package com.two.ui;

import com.bishe.again.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

public class ResultActivity extends Activity {

	private TextView tv;
	private ImageView img;
	private Button btn;
	private Button btnintent;
	private Bundle bundle;
	private WebView web;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		initView();
		
		
		initdata();
		
	}

	private void initdata() {
		Intent intentvalue = getIntent();
		bundle = intentvalue.getExtras();
		tv.setText(bundle.getString("result"));
		img.setImageBitmap((Bitmap)intentvalue.getParcelableExtra("bitmap"));
		btnintent.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				String str = bundle.getString("result");
				String substr = str.substring(0, 4);
				if(substr.equals("http")){
					web = new WebView(ResultActivity.this);
					web.loadUrl(str);
					setContentView(web);
					Toast.makeText(ResultActivity.this, "success!", 0).show();
				}else{
					Toast.makeText(ResultActivity.this, "你妈，不是网址你点粑粑！", 0).show();
				}
			} 
		});
		
		/*
		 * 点击关闭当前页面
		 * */
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ResultActivity.this.finish();
			}
		});		
	}

	private void initView() {
		tv = (TextView) findViewById(R.id.result_name);
		img = (ImageView) findViewById(R.id.result_bitmap);
		btn = (Button) findViewById(R.id.button_back);
		btnintent = (Button) findViewById(R.id.intent2view);
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
//			web.goBack();
//			return true;
//		}
//		return false;
//		
//	}
	

}
