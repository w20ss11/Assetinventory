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
	//打开关闭
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

	//对数据库进行增删改查
	public void queryAll(){
		
	}
	
	
	private class MySqliteOpenHelper extends SQLiteOpenHelper{

		public MySqliteOpenHelper(Context context) {
			super(context,  "check.db", null, 1);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			 db.execSQL("CREATE TABLE equ_table(id INTEGER PRIMARY KEY AUTOINCREMENT,EquipmentBar TEXT,CheckState TEXT,TechState TEXT,UseState TEXT,RemoveState TEXT,DocListID TEXT,SimpleReason TEXT,CheckDocumentID TEXT,LargeClass TEXT,EquipmentName TEXT,ModelType TEXT,MainArgument TEXT,PlateNumber TEXT,Productor TEXT,CostCenterName TEXT,SetupPlace TEXT,StationName TEXT,DutyUser TEXT,CompanyName TEXT,ProfitCenter TEXT,LinkUser TEXT,LinkPhone TEXT,CostCenterCode TEXT);");
		     db.execSQL("CREATE TABLE task_table(id INTEGER PRIMARY KEY AUTOINCREMENT,CheckPlanName TEXT,DocSendUserName TEXT,CheckDocumentID TEXT,DocCreateTime TEXT,DocUserName TEXT,CheckPlanCode TEXT,isInList TEXT DEFAULT FALSE);");
		     System.out.println("db sql创建table语句执行");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion==1&&newVersion==2){
				Log.i("TAG", "DBManager:数据库更新了");
			}
		}
		
	}


	public HashMap<String, String> queryByEquipmentBar(String equipmentBar) {
		HashMap<String, String> map=new HashMap<String, String>();
		Cursor cursor = null;
		Log.i("TAG", "DBManager中queryByEquipmentBar方法中db.isOpen()的值："+String.valueOf(db.isOpen()));
		if(db.isOpen()){
			String[] columns={"CheckState","CompanyName","CostCenterCode","DocListID","DutyUser","EquipmentBar","EquipmentName","LargeClass","LinkPhone","LinkUser","MainArgument","ModelType","PlateNumber","Productor","ProfitCenter","RemoveState","SetupPlace","SimpleReason","StationName","TechState","UseState","CostCenterName"};
			String selection="EquipmentBar=?";
			String[] selectionArgs={equipmentBar};
			String groupBy=null;
			String having=null;
			String orderBy=null;
			
			cursor=db.query("equ_table", columns, selection, selectionArgs, groupBy, having, orderBy);
		}
		Log.i("TAG", "DBManager中queryByEquipmentBar方法中cursor!=null的值："+String.valueOf(cursor!=null));
		if(cursor!=null&&cursor.moveToFirst()){
			map.put("盘点情况", cursor.getString(0));
			map.put("公司分类", cursor.getString(1));
			map.put("成本中心编码", cursor.getString(2));
			map.put("文件列表ID", cursor.getString(3));
			map.put("责任人", cursor.getString(4));
			map.put("设备二维码", cursor.getString(5));
			map.put("资产名称", cursor.getString(6));
			map.put("新资产类别", cursor.getString(7));
			map.put("联系电话", cursor.getString(8));
			map.put("联系人", cursor.getString(9));
			map.put("主参数/面积/车架号", cursor.getString(10));
			map.put("规格型号/结构", cursor.getString(11));
			map.put("出厂编码/牌照号", cursor.getString(12));
			map.put("生产商/建造商/代理商", cursor.getString(13));
			map.put("利润中心名", cursor.getString(14));
			map.put("核销情况", cursor.getString(15));
			map.put("资产安放位置", cursor.getString(16));
			map.put("原因简述", cursor.getString(17));
			map.put("库站名称", cursor.getString(18));
			map.put("技术状况", cursor.getString(19));
			map.put("使用状况", cursor.getString(20));
			map.put("成本中心名", cursor.getString(21));//22 无CheckDocumentID
		}
		return map;
	}

}
