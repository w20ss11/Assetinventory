package com.bishe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager {
	private MySqliteOpenHelper mOpenHelper;
	private static DBManager dbConn = null;
	private Cursor cursor;
	private Context mContext = null;
	private SQLiteDatabase db = null;
	
	private DBManager(Context paramContext)
	  {
	    this.mContext = paramContext;
	  }

	public static DBManager getInstance(Context paramContext)
	{
	  if (dbConn == null)
	    dbConn = new DBManager(paramContext);
	  return dbConn;
	}
	//�򿪹ر�
	public void open()
	 {
	    if (this.mOpenHelper == null)
	      this.mOpenHelper = new MySqliteOpenHelper(this.mContext);
	    this.db = this.mOpenHelper.getWritableDatabase();
	 }
	public void close()
	{
		db.close();
		if (this.mOpenHelper != null)
	    this.mOpenHelper.close();
		if (this.cursor == null)
	    return;
		this.cursor.close();
	}
	
	public void taskInsert(TaskCheck t) throws ParseException {
		if(db.isOpen()){
			ContentValues localContentValues = new ContentValues();
		    localContentValues.put("CheckPlanName", t.CheckPlanName);
		    localContentValues.put("CheckPlanCode", t.CheckPlanCode);
		    localContentValues.put("DocCreateTime", formatTime(t.DocCreateTime));
		    localContentValues.put("DocUserName", t.DocUserName);
		    localContentValues.put("DocSendUserName", t.DocSendUserName);
		    localContentValues.put("CheckDocumentID", t.CheckDocumentID);
		    db.insert("task_table", null, localContentValues);
		}
	}
	public void equipmentInsert(EquipmentCheck e) throws ParseException {
		if(db.isOpen()){
			ContentValues localContentValues = new ContentValues();
		    localContentValues.put("UseState", e.UseState);
		    localContentValues.put("TechState", e.TechState);
		    localContentValues.put("StationName", e.StationName);
		    localContentValues.put("SetupPlace", e.SetupPlace);
		    localContentValues.put("RemoveState", e.RemoveState);
		    localContentValues.put("CheckState", e.CheckState);
		    localContentValues.put("ProfitCenter", e.ProfitCenter);
		    localContentValues.put("SimpleReason", e.SimpleReason);
		    localContentValues.put("Productor", e.Productor);
		    localContentValues.put("LinkUser", e.LinkUser);
		    localContentValues.put("PlateNumber", e.PlateNumber);
		    localContentValues.put("MainArgument", e.MainArgument);
		    localContentValues.put("LinkPhone", e.LinkPhone);
		    localContentValues.put("CostCenterCode", e.CostCenterCode);
		    localContentValues.put("LargeClass", e.LargeClass);
		    localContentValues.put("ModelType", e.ModelType);
		    localContentValues.put("CostCenterName", e.CostCenterName);
		    localContentValues.put("EquipmentName", e.EquipmentName);
		    localContentValues.put("EquipmentBar", e.EquipmentBar);
		    localContentValues.put("DutyUser", e.DutyUser);
		    localContentValues.put("DocListID", e.DocListID);
		    localContentValues.put("CompanyName", e.CompanyName);
		    localContentValues.put("CheckDocumentID", e.CheckDocumentID);//23
		    
		    db.insert("equ_table", null, localContentValues);
		}
	}
	
	private String formatTime(String docCreateTime) throws ParseException {
		String beginDate=docCreateTime;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sd = sdf.format(new Date(Long.parseLong(beginDate)));
		return sd;
	}

	//�����ݿ������ɾ�Ĳ�
	public void queryAll(){
		
	}
	
	
	private class MySqliteOpenHelper extends SQLiteOpenHelper{

		public MySqliteOpenHelper(Context context) {
			super(context,  "check.db", null, 1);
			// TODO �Զ����ɵĹ��캯�����
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			 db.execSQL("CREATE TABLE equ_table(id INTEGER PRIMARY KEY AUTOINCREMENT,EquipmentBar TEXT,CheckState TEXT,TechState TEXT,UseState TEXT,RemoveState TEXT,DocListID TEXT,SimpleReason TEXT,CheckDocumentID TEXT,LargeClass TEXT,EquipmentName TEXT,ModelType TEXT,MainArgument TEXT,PlateNumber TEXT,Productor TEXT,CostCenterName TEXT,SetupPlace TEXT,StationName TEXT,DutyUser TEXT,CompanyName TEXT,ProfitCenter TEXT,LinkUser TEXT,LinkPhone TEXT,CostCenterCode TEXT);");
		     db.execSQL("CREATE TABLE task_table(id INTEGER PRIMARY KEY AUTOINCREMENT,CheckPlanName TEXT,DocSendUserName TEXT,CheckDocumentID TEXT,DocCreateTime TEXT,DocUserName TEXT,CheckPlanCode TEXT,isInList TEXT DEFAULT FALSE);");
		     System.out.println("db sql����table���ִ��");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion==1&&newVersion==2){
				Log.i("TAG", "DBManager:���ݿ������");
			}
		}
		
	}


	public HashMap<String, String> queryByEquipmentBar(String equipmentBar) {
		HashMap<String, String> map=new HashMap<String, String>();
		Cursor cursor = null;
		Log.i("TAG", "DBManager��queryByEquipmentBar������db.isOpen()��ֵ��"+String.valueOf(db.isOpen()));
		if(db.isOpen()){
			String[] columns={"CheckState","CompanyName","CostCenterCode","DocListID","DutyUser","EquipmentBar","EquipmentName","LargeClass","LinkPhone","LinkUser","MainArgument","ModelType","PlateNumber","Productor","ProfitCenter","RemoveState","SetupPlace","SimpleReason","StationName","TechState","UseState","CostCenterName"};
			String selection="EquipmentBar=?";
			String[] selectionArgs={equipmentBar};
			String groupBy=null;
			String having=null;
			String orderBy=null;
			
			cursor=db.query("equ_table", columns, selection, selectionArgs, groupBy, having, orderBy);
		}
		Log.i("TAG", "DBManager��queryByEquipmentBar������cursor!=null��ֵ��"+String.valueOf(cursor!=null));
		if(cursor!=null&&cursor.moveToFirst()){
			map.put("�̵����", cursor.getString(0));
			map.put("��˾����", cursor.getString(1));
			map.put("�ɱ����ı���", cursor.getString(2));
			map.put("�ļ��б�ID", cursor.getString(3));
			map.put("������", cursor.getString(4));
			map.put("�豸��ά��", cursor.getString(5));
			map.put("�ʲ�����", cursor.getString(6));
			map.put("���ʲ����", cursor.getString(7));
			map.put("��ϵ�绰", cursor.getString(8));
			map.put("��ϵ��", cursor.getString(9));
			map.put("������/���/���ܺ�", cursor.getString(10));
			map.put("����ͺ�/�ṹ", cursor.getString(11));
			map.put("��������/���պ�", cursor.getString(12));
			map.put("������/������/������", cursor.getString(13));
			map.put("����������", cursor.getString(14));
			map.put("�������", cursor.getString(15));
			map.put("�ʲ�����λ��", cursor.getString(16));
			map.put("ԭ�����", cursor.getString(17));
			map.put("��վ����", cursor.getString(18));
			map.put("����״��", cursor.getString(19));
			map.put("ʹ��״��", cursor.getString(20));
			map.put("�ɱ�������", cursor.getString(21));//22 ��CheckDocumentID
		}
		return map;
	}

}
