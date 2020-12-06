package com.bishe.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bishe.again.R;
import com.bishe.util.DBManager;
import com.bishe.util.NetUtils;
import com.bishe.util.TaskCheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TasksActivity<MySqliteOpenHelper> extends Activity implements OnClickListener {

	private static String URL_LISTCHECKTASK;
	private MyTaskAdapter myTaskAdapter;
	public static ArrayList<TaskCheck> list = new ArrayList<TaskCheck>();
	private DBManager dbManager;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		
		handler = new Handler() {
			// ��ʼ�µ���ͼ
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Log.i("TAG", "TasksActivity:message=");
			}
		};
		Button but1=(Button) findViewById(R.id.but1);
		but1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					v.setBackgroundResource(R.drawable.button_press);
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.button_nomal);
				}
				return false;
			}
		});
		
		ListView lv_task=(ListView) findViewById(R.id.lv_task);
		
		this.dbManager=DBManager.getInstance(this);
		
		URL_LISTCHECKTASK = "http://192.168.191.1:8080/server/Task";
		but1.setOnClickListener(this);
		
		//�������� ����listview������
		Log.i("TAG", "TasksActivity:list.size:"+list.size());
		new MyHttpThread().start();
		Log.i("TAG", "TasksActivity:list.size:"+list.size());
	    
	    dbManager.open();
	    try {
			save2db(list);
		} catch (ParseException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		Log.i("TAG", "TasksActivity:dbManager open �����ݵ����ݿ�");
		dbManager.close();
	   
	    this.myTaskAdapter = new MyTaskAdapter(this.list, this);
	    lv_task.setAdapter(this.myTaskAdapter);
	    
	    lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {//parent�Ǹ���ͼ��viewΪ��ǰ��ͼ��position�ǵ�ǰ��ͼ��adpter��λ�ã�id�ǵ�ǰ��ͼView��ID.
		        String str1 = list.get(arg2).CheckDocumentID;
		        Intent localIntent = new Intent(TasksActivity.this, EquipmentActivity.class);
		        localIntent.putExtra("fromActivity", "FindtasksActivity");
		        localIntent.putExtra("com.bishe.checkcheck.CheckDocumentID", str1);
		        TasksActivity.this.startActivity(localIntent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		new MyHttpThread().start();
        dbManager.open();
		Log.i("TAG", "TasksActivity button:dbManager open �����ݵ����ݿ�");
		dbManager.close();
		Toast.makeText(this, "���ƻ��б�ˢ�³ɹ���", 0).show();  
	}
	private class MyHttpThread extends Thread{
		public void run(){
			//3���ӵ�����������֤
				//��֤���ΪString res
			Log.i("TAG", "TasksActivity:MyHttpThreadִ��  ");
			String res = NetUtils.login(URL_LISTCHECKTASK);
			Log.i("TAG","TasksActivity:�߳��е�result:" + res);
			if(res!=null)
				try {
					Analysis(res);
				} catch (JSONException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			Message msg=new Message();
			handler.sendMessage(msg);
		}
	} 
	
	class MyTaskAdapter extends BaseAdapter {
		private ArrayList<TaskCheck> list;
		private LayoutInflater mInflater;
		public MyTaskAdapter(ArrayList<TaskCheck> paramArrayList, Context paramContext)
		{
			list=paramArrayList;
		    mInflater = LayoutInflater.from(paramContext);
		}
		@Override
		public int getCount() {
			return this.list.size();
		}
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@SuppressWarnings("rawtypes")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			if(convertView==null){
				LayoutInflater inflater=TasksActivity.this.getLayoutInflater();
				view=inflater.inflate(R.layout.task_item, null);
			}
			else {
				view=convertView;
			}
			TextView checkplanname=(TextView) view.findViewById(R.id.task_checkplanname);
			TextView checkplancode=(TextView) view.findViewById(R.id.task_checkplancode);
			TextView DocSendUserName=(TextView) view.findViewById(R.id.task_DocSendUserName);
			TextView DocUserName=(TextView) view.findViewById(R.id.task_DocUserName);
			TextView DocCreateTime=(TextView) view.findViewById(R.id.task_DocCreateTime);
			
			TaskCheck item=list.get(position);
			
			checkplanname.setText(item.getCheckPlanName());
			checkplancode.setText(item.getCheckPlanCode());
			DocSendUserName.setText(item.getDocSendUserName());
			DocUserName.setText(item.getDocUserName());
			DocCreateTime.setText(item.getDocCreateTime());
			
			return view;
		}
	}
	
	private static void Analysis(String jsonStr)
            throws JSONException {
        /******************* ���� ***********************/
        JSONArray jsonArray = null;
        // ��ʼ��list�������
        jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // ��ʼ��map�������
            TaskCheck taskCheck=new TaskCheck();
            taskCheck.setCheckPlanName(jsonObject.getString("CheckPlanName"));
            taskCheck.setCheckPlanCode(jsonObject.getString("CheckPlanCode"));
            taskCheck.setDocSendUserName(jsonObject.getString("DocSendUserName"));
            taskCheck.setDocSendUserName(jsonObject.getString("DocUserName"));
            taskCheck.setDocCreateTime(jsonObject.getString("DocCreateTime"));
            taskCheck.setCheckDocumentID( jsonObject.getString("CheckDocumentID"));
            list.add(taskCheck);
        }
    }
	
	private void save2db(ArrayList<TaskCheck> paramArrayList) throws ParseException{
		Iterator<TaskCheck> iterator=paramArrayList.iterator();
		while(iterator.hasNext()){
			TaskCheck t=iterator.next();
		    this.dbManager.taskInsert(t);
		}
	}


}
