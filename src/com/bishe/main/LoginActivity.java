package com.bishe.main;

import com.bishe.again.R;
import com.bishe.util.NetUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private SharedPreferences sp;
	private EditText userName;
	private EditText passWord;
	private CheckBox rem_pw;
	private CheckBox auto_login;
	private Handler handler;
	private String account;
	private String psw;
	private static String URL_LOGIN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			handler = new Handler() {
				// 开始新的意图
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					Log.i("TAG", "LoginActivity:message.what=" + msg.what);
					if (msg.what == 0) {//登录成功后
						// intent
						if(rem_pw.isChecked())  
	                    {  
	                     //记住用户名、密码、  
	                      Editor editor = sp.edit();  
	                      editor.putString("USER_NAME", account);  
	                      editor.putString("PASSWORD",psw);  
	                      editor.commit();  
	                    } 
						Intent intent=new Intent(LoginActivity.this,TasksActivity.class);
						startActivity(intent);
					}
				}
			};
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		setContentView(R.layout.activity_login);
		this.sp = getSharedPreferences("userInfo", 0);
	    this.userName = ((EditText)findViewById(R.id.account));
	    this.passWord = ((EditText)findViewById(R.id.psw));
	    this.rem_pw = ((CheckBox)findViewById(R.id.rem_psw));
	    this.auto_login = ((CheckBox)findViewById(R.id.auto_login));
	    Button but = (Button)findViewById(R.id.login);
	    URL_LOGIN = "http://192.168.191.1:8080/server/Login?";
	    but.setOnClickListener(this);
	    
	  //判断记住密码是否被选中
	  	if (sp.getBoolean("ISCHECK", false)) {
	  		// 设置默认是记录密码状态
	  		rem_pw.setChecked(true);
	  		userName.setText(sp.getString("name", ""));
	 		passWord.setText(sp.getString("password", ""));
	  		// 判断自动登陆多选框状态
	  		if (sp.getBoolean("AUTO_ISCHECK", true)) {
	  			// 设置默认是自动登录状态
	  			//new MyHttpThread().start();
	  		}
	 	}
	  		
	    rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(rem_pw.isChecked())
					sp.edit().putBoolean("ISCHECK", true).commit();
				else 
					sp.edit().putBoolean("ISCHECK", false).commit();
			}  
			
		});
	    auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(auto_login.isChecked())
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				else {
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});

	   
	}
	@Override
	public void onClick(View arg0) {
		account = userName.getText().toString();
		psw = passWord.getText().toString();
		if(TextUtils.isEmpty(account)||TextUtils.isEmpty(psw)){
			Toast.makeText(this, "账号或密码不能为空", 0).show();
			return;
		}
		new MyHttpThread().start();
	}
	
	private class MyHttpThread extends Thread{
		public void run(){
			//3链接到服务器端验证
				//验证结果为String res
			Log.i("TAG", "LoginActivity:MyHttpThread执行  登录模块子线程"+URL_LOGIN+"account="+account+"&psw="+psw);
			String res = NetUtils.login(URL_LOGIN+"account="+account+"&psw="+psw);
			Log.i("TAG","LoginActivity:线程中的result:" + res);
			Message msg=new Message();
			msg.what=0;
			handler.sendMessage(msg);
		}
	} 
}
