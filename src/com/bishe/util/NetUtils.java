package com.bishe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetUtils extends Thread
{
	
	public static String login(String url){
		HttpURLConnection conn=null;
		try {
			 //����ͻ��� ���� �����������ʵ�壩��
			 Log.i("TAG","NetUtils:login����ִ��"+url);
			 URL url2=new URL(url);
			 conn=(HttpURLConnection) url2.openConnection();
			 conn.setRequestMethod("GET");
			 Log.i("TAG", "NetUtils:conn.getResponseCode():"+conn.getResponseCode());
			 if(conn.getResponseCode()==200){
				 InputStream is=conn.getInputStream();
				 String state=getStringFromInputStream(is);
				 Log.i("TAG", "NetUtils:������ɺ� ���ص�state"+state);
				 return state;
			 }
			 else{
				 Log.i("TAG", "����ʧ�ܣ�");
			 }
			 
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn.disconnect();
		}
		return null;
	}
	public static String link(String url1) throws IOException{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[1024];  
        int len = 0;  
        URL url = new URL(url1);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        InputStream inStream = conn.getInputStream();  
        while ((len = inStream.read(data)) != -1) {  
           outStream.write(data, 0, len);  
        }  
        inStream.close();  
        return new String(outStream.toByteArray());//ͨ��out.Stream.toByteArray��ȡ��д������  
	}
	
	
	
	private static String getStringFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		while((len=is.read(buffer))!=-1){
			baos.write(buffer,0,len);
		}
		is.close();
		String html=new String(baos.toByteArray());
		baos.close();
		return html;
	}

	public static String submit(JSONObject jsonObject,String uRL_SUNMIT) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(uRL_SUNMIT);
			try {
				StringEntity entity = new StringEntity(jsonObject.toString());
				post.setEntity(entity);
				HttpResponse responString = client.execute(post);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return uRL_SUNMIT;
	}
}
