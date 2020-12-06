package com.bishe.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bishe.again.R;
import com.bishe.util.DBManager;
import com.bishe.util.EquipmentCheck;
import com.bishe.util.NetUtils;
import com.two.ui.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EquipmentActivity extends Activity implements OnClickListener {
	private static String URL_EQUIPMENTCHECKTASK;
	private MyEquipmentAdapter myEquipmentAdapter;
	public ArrayList<EquipmentCheck> list = new ArrayList<EquipmentCheck>();
	private DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment);
		
		ListView lv_equipment=(ListView) findViewById(R.id.lv_equipment);
		Button zxing=(Button) findViewById(R.id.zxing);
		this.dbManager=DBManager.getInstance(this);
		
		zxing.setOnClickListener(this);
		Intent localIntent = getIntent();
		String CheckDocumentID = localIntent.getStringExtra("com.bishe.checkcheck.CheckDocumentID");
		URL_EQUIPMENTCHECKTASK="http://192.168.191.1:8080/server/"+CheckDocumentID;
		
		MyHttpThread myHttpThread=new MyHttpThread();
		myHttpThread.start();
		try {
			myHttpThread.join();
		} catch (InterruptedException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
	    dbManager.open();
	    try {
			save2db(list);
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Log.i("TAG", "EquipmentActivity:dbManager open 存数据岛数据库");
		dbManager.close();
		
		this.myEquipmentAdapter = new MyEquipmentAdapter(this.list, this);
	    lv_equipment.setAdapter(this.myEquipmentAdapter);
	    
		lv_equipment.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
		        String str = list.get(arg2).EquipmentBar;
		        Intent localIntent = new Intent(EquipmentActivity.this, EquipmentStateActivity.class);
		        localIntent.putExtra("com.bishe.EquipmentBar", str);
		        EquipmentActivity.this.startActivity(localIntent);
			}});
	}

	private void save2db(ArrayList<EquipmentCheck> list2) throws ParseException {
		Iterator<EquipmentCheck> t=list2.iterator();
		while(t.hasNext()){
			EquipmentCheck e=t.next();
			this.dbManager.equipmentInsert(e);
		}
	}

	private class MyEquipmentAdapter extends BaseAdapter{

		public MyEquipmentAdapter(ArrayList<EquipmentCheck> list,
				EquipmentActivity equipmentActivity) {
			// TODO 自动生成的构造函数存根
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			if(convertView==null){
				LayoutInflater inflater=EquipmentActivity.this.getLayoutInflater();
				view=inflater.inflate(R.layout.equipment_item, null);
			}
			else {
				view=convertView;
			}
			TextView EquipmentBar=(TextView) view.findViewById(R.id.equipment_EquipmentBar);
			TextView EquipmentName=(TextView) view.findViewById(R.id.equipment_EquipmentName);
			TextView CheckState=(TextView) view.findViewById(R.id.equipment_CheckState);
			
			EquipmentCheck item=list.get(position);
			Log.i("TAG", "EquipmentActivity-----------"+item.getEquipmentBar());
			EquipmentBar.setText(item.getEquipmentBar());
			EquipmentName.setText(item.getEquipmentName());
			CheckState.setText(item.getCheckState());
			
			return view;
		}
		
	}
	private class MyHttpThread extends Thread{
		public void run(){
			//3链接到服务器端验证
				//验证结果为String res
			Log.i("TAG", "EquipmentActivity:MyHttpThread执行  ");
			String res = NetUtils.login(URL_EQUIPMENTCHECKTASK);
			Log.i("TAG","EquipmentActivity:线程中的result:" + res+"---------------res!=null::::"+String.valueOf(res!=null));
			if(res!=null)
				try {
					Analysis(res);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
		}
		private void Analysis(String res) throws JSONException {
			/******************* 解析 ***********************/
	        JSONArray jsonArray = null;
	        // 初始化list数组对象
	        jsonArray = new JSONArray(res);
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            // 初始化map数组对象
	            EquipmentCheck equipmentCheck=new EquipmentCheck();
	            equipmentCheck.setCheckDocumentID(jsonObject.getString("CheckDocumentID"));
	            equipmentCheck.setCheckState(jsonObject.getString("CheckState"));
	            equipmentCheck.setCompanyName(jsonObject.getString("CompanyName"));
	            equipmentCheck.setCostCenterCode(jsonObject.getString("CostCenterCode"));
	            equipmentCheck.setCostCenterName(jsonObject.getString("CostCenterName"));
	            equipmentCheck.setDocListID( jsonObject.getString("DocListID"));
	            equipmentCheck.setDutyUser( jsonObject.getString("DutyUser"));
	            equipmentCheck.setEquipmentBar( jsonObject.getString("EquipmentBar"));
	            equipmentCheck.setEquipmentName( jsonObject.getString("EquipmentName"));
	            equipmentCheck.setLargeClass( jsonObject.getString("LargeClass"));
	            equipmentCheck.setLinkPhone( jsonObject.getString("LinkPhone"));
	            equipmentCheck.setLinkUser( jsonObject.getString("LinkUser"));
	            equipmentCheck.setMainArgument( jsonObject.getString("MainArgument"));
	            equipmentCheck.setModelType( jsonObject.getString("ModelType"));
	            equipmentCheck.setPlateNumber( jsonObject.getString("PlateNumber"));
	            equipmentCheck.setProductor( jsonObject.getString("Productor"));
	            equipmentCheck.setProfitCenter( jsonObject.getString("ProfitCenter"));
	            equipmentCheck.setRemoveState( jsonObject.getString("RemoveState"));
	            equipmentCheck.setSetupPlace( jsonObject.getString("SetupPlace"));
	            equipmentCheck.setSimpleReason( jsonObject.getString("SimpleReason"));
	            equipmentCheck.setStationName( jsonObject.getString("StationName"));
	            equipmentCheck.setTechState(jsonObject.getString("TechState"));
	            equipmentCheck.setUseState(jsonObject.getString("UseState"));//23
	            System.out.println("EqupmentActivity-----------------Analysis(String res)方法");
	            list.add(equipmentCheck);
	            Log.i("TAG", "EqupmentActivity--------------------------------:list.size::"+list.size());
	        }
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent=new Intent(EquipmentActivity.this,MainActivity.class);
		startActivity(intent);
	}
}
