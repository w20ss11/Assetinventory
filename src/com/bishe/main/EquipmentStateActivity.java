package com.bishe.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;






import org.json.JSONException;
import org.json.JSONObject;

import com.bishe.again.R;
import com.bishe.util.DBManager;
import com.bishe.util.NetUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class EquipmentStateActivity extends Activity implements OnCheckedChangeListener, OnClickListener {
	
	private DBManager dbManager;
	private HashMap<String, String> map;
	private RadioGroup checkState;
	private RadioGroup teckState;
	private RadioGroup useState;
	private RadioGroup removeState;
	private static String URL_SUNMIT;
	private JSONObject jsonObject = new JSONObject(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment_state);
		
		LinearLayout ll=(LinearLayout) findViewById(R.id.equipment_stateItem);
		checkState=(RadioGroup) findViewById(R.id.CheckState);
		teckState=(RadioGroup) findViewById(R.id.TeckState);
		useState=(RadioGroup) findViewById(R.id.UseState);
		removeState=(RadioGroup) findViewById(R.id.RemoveState);
		Button submit=(Button) findViewById(R.id.submit);
		TextView problem=(TextView) findViewById(R.id.problem);
		
		this.dbManager=DBManager.getInstance(this);
		
		Intent localIntent = getIntent();
		String equipmentBar = localIntent.getStringExtra("com.bishe.EquipmentBar");
		dbManager.open();
		map=dbManager.queryByEquipmentBar(equipmentBar);
		dbManager.close();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		Log.i("TAG", "EquipmentStateActivity:中equipmentBar是："+equipmentBar+"/////////map.size是："+map.size());
		for (String key : map.keySet()) {
			   Log.i("TAG","EquipmentStateActivity:=============key= "+ key + " and value= " + map.get(key)+"======");
		}
		
		
		
		TextView tv1;
		TextView tv2;
		URL_SUNMIT="http://192.168.191.1:8080/server/Submit";
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
			tv1=new TextView(this);
			tv2=new TextView(this);
			tv1.setGravity(Gravity.LEFT);
			tv2.setGravity(Gravity.RIGHT);
			tv1.setText(entry.getKey());
			tv2.setText(entry.getValue());
			tv1.setTextColor(Color.rgb(0,0,0));
			tv2.setTextColor(Color.rgb(0,0,0));
			ll.addView(tv1);
			ll.addView(tv2);
		}
		checkState.setOnCheckedChangeListener(this);
		teckState.setOnCheckedChangeListener(this);
		useState.setOnCheckedChangeListener(this);
		removeState.setOnCheckedChangeListener(this);
		submit.setOnClickListener(this);
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton rb=(RadioButton) EquipmentStateActivity.this.findViewById(checkedId);
		String info1=(String) rb.getText();
		switch (group.getId()) {
		case R.id.CheckState:try {
				jsonObject.put("CheckState", info1);
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}break;
		case R.id.TeckState:try {
				jsonObject.put("TeckState", info1);
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}break;
		case R.id.UseState:try {
				jsonObject.put("UseState", info1);
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}break;
		default:try {
				jsonObject.put("RemoveState", info1);
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		new MyHttpThread().start();
		Toast.makeText(this, "已将修改信息提交到服务器！", 0).show();
		
	}
	private class MyHttpThread extends Thread{
		public void run(){
			//3链接到服务器端验证
				//验证结果为String res
			Log.i("TAG", "EquipmentStateActivity:MyHttpThread执行  ");
			String res = NetUtils.submit(jsonObject,URL_SUNMIT);
			Log.i("TAG","EquipmentStateActivity:线程中的result:" + res);
		}
	} 

}
