package com.example.test_webview_demo;

import java.util.Properties;

import com.example.test_webview_demo.utils.SystemWebView;
import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class SystemWebActivity extends Activity{
	
	

	
	private SystemWebView webView;
	private static final String mHomeUrl = "file:///android_asset/webpage/creat_new_window.html";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreen_sys_web);
		webView=(SystemWebView) findViewById(R.id.full_sys_web_webview);
		webView.loadUrl(mHomeUrl);
		webView.addJavascriptInterface(new Object(){
			
			@JavascriptInterface
			public void alertMsg(String msg){
				Toast.makeText(SystemWebActivity.this,msg, Toast.LENGTH_SHORT).show();
				
			}
		
		},"Android");
		
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				android.webkit.WebView.HitTestResult hitTestResult=SystemWebActivity.this.webView.getHitTestResult();
				final String path=hitTestResult.getExtra();
				switch(hitTestResult.getType()){
				case WebView.HitTestResult.IMAGE_TYPE://获取点击的标签是否为图片
					Toast.makeText(SystemWebActivity.this, "当前选定的图片的URL是"+ path, Toast.LENGTH_LONG).show();
				case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE://获取点击的标签是否为图片
					Toast.makeText(SystemWebActivity.this, "当前选定的图片的URL是"+ path, Toast.LENGTH_LONG).show();
//					
					break;
				}
				return false;
			}
		});
		
	}
	
	

}
