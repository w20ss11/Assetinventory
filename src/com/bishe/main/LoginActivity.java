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
				// ��ʼ�µ���ͼ
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					Log.i("TAG", "LoginActivity:message.what=" + msg.what);
					if (msg.what == 0) {//��¼�ɹ���
						// intent
						if(rem_pw.isChecked())  
	                    {  
	                     //��ס�û��������롢  
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
			// TODO �Զ����ɵ� catch ��
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
	    
	  //�жϼ�ס�����Ƿ�ѡ��
	  	if (sp.getBoolean("ISCHECK", false)) {
	  		// ����Ĭ���Ǽ�¼����״̬
	  		rem_pw.setChecked(true);
	  		userName.setText(sp.getString("name", ""));
	 		passWord.setText(sp.getString("password", ""));
	  		// �ж��Զ���½��ѡ��״̬
	  		if (sp.getBoolean("AUTO_ISCHECK", true)) {
	  			// ����Ĭ�����Զ���¼״̬
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
			Toast.makeText(this, "�˺Ż����벻��Ϊ��", 0).show();
			return;
		}
		new MyHttpThread().start();
	}
	
	private class MyHttpThread extends Thread{
		public void run(){
			//3���ӵ�����������֤
				//��֤���ΪString res
			Log.i("TAG", "LoginActivity:MyHttpThreadִ��  ��¼ģ�����߳�"+URL_LOGIN+"account="+account+"&psw="+psw);
			String res = NetUtils.login(URL_LOGIN+"account="+account+"&psw="+psw);
			Log.i("TAG","LoginActivity:�߳��е�result:" + res);
			Message msg=new Message();
			msg.what=0;
			handler.sendMessage(msg);
		}
	} 
}
