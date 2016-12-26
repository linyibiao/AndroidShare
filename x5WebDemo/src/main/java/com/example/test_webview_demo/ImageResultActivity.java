package com.example.test_webview_demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.example.test_webview_demo.utils.X5PopMenu;
import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.sdk.WebView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

public class ImageResultActivity extends Activity{
	
	
	/**
	 * 用于展示图片长按的返回的结果,
	 * 注意：如果需要使用长按弹出contextMenu的方式来下载图片是不可行的
	 * X5拦截了Image的弹出contextMenu操作
	 * 唯一的方法是迂回策略，直接拦截webview的onLongClick事件，并且自动以 popupwindow来实现类似的功能
	 */
	
	X5WebView webView;
	private String resourceUrl="";
	private Point hitPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fullscreen_web);
		webView=(X5WebView) findViewById(R.id.full_web_webview);
		webView.loadUrl("file:///android_asset/webpage/Image.html");
	
		webView.getView().setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				WebView.HitTestResult hitTestResult=ImageResultActivity.this.webView.getHitTestResult();
				final String path=resourceUrl=hitTestResult.getExtra();
				switch(hitTestResult.getType()){
				case WebView.HitTestResult.IMAGE_TYPE://获取点击的标签是否为图片
					Toast.makeText(ImageResultActivity.this, "当前选定的图片的URL是"+ path, Toast.LENGTH_LONG).show();
				case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE://获取点击的标签是否为图片
					Toast.makeText(ImageResultActivity.this, "当前选定的图片的URL是"+ path, Toast.LENGTH_LONG).show();
//					
					break;
				}
				return false;
			}
		});
		
		webView.getView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				return;
			}
		});
		
		
		
	}
	
	
	
	//ͼƬ���ضԻ���
	private void showDialog(final String msg,String path){
		AlertDialog.Builder builder=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("保存图片");
		builder.setMessage("url是"+path);
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	
	
	//后端下载保存服务
	private static final String DEFAULT_DOWNLOAD_PATH="tbsDownload"+File.separator+"image"+File.separator;
	public class SaveNetWorkResourse extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result="";
			try{
				String sdcardPath;
				if(Environment.getExternalStorageDirectory()!=null){
					sdcardPath=Environment.getExternalStorageDirectory().toString();
					File file=new File(sdcardPath+File.separator+DEFAULT_DOWNLOAD_PATH);
					if(!file.exists()){
						file.mkdir();
					}
					int pos=resourceUrl.lastIndexOf(".");
					StringBuffer endTag=new StringBuffer().append("TBS download"+new Date().getTime()).append(resourceUrl.substring(pos));
					InputStream inputStream;
					URL url=new URL(resourceUrl);
					HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
					urlConnection.setConnectTimeout(20000);
					urlConnection.setRequestMethod("GET");
					if(urlConnection.getResponseCode()==200){
						inputStream=urlConnection.getInputStream();
						byte[] buffer=new byte[4*1024];
						int len=inputStream.read(buffer);
						FileOutputStream fileOutputStream=new FileOutputStream(file);
						while(len>=0){
							fileOutputStream.write(buffer);
							len=inputStream.read(buffer);
						}
						result="文件选择:"+sdcardPath+File.separator+DEFAULT_DOWNLOAD_PATH+endTag;
						fileOutputStream.close();
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
				result="failed";
			}
			return result;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(this.webView!=null&&hitPoint !=null){
			hitPoint.x= (int) event.getX();
			hitPoint.y= (int) event.getY();
		}
		return super.onTouchEvent(event);
	}
	
	
}
