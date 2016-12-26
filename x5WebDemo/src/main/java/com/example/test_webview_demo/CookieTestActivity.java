package com.example.test_webview_demo;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CookieTestActivity  extends Activity{
	
	/**
	 * 用于演示X5cookie的使用方法
	 */
	
	
	private X5WebView webView;
	private CookieManager cookieManager;
	
	private static final String DEFAULT_URL="http:\\\\www.mxzlm.com\\index.html?host=abc&pswd=123456&skey=123456";
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.filechooser_layout);
			webView=(X5WebView) findViewById(R.id.web_filechooser);
			webView.loadUrl("file:///android_asset/webpage/fileChooser.html");
			CookieSyncManager.createInstance(this);
			cookieManager=CookieManager.getInstance();
			test_getCookie();
			
		}	
		
		private void test_getCookie(){
			
			cookieManager.setCookie(".aa.com", "key = 123456;");
			
			String cookieResult = cookieManager.getCookie("www.aa.com");
			String[] cookies = null;
			if(cookieResult == null){
				Toast.makeText(this, "cookie is not exist!", Toast.LENGTH_SHORT).show();
				Log.i("Cookies", "cookie is not exist!");
				
			}else{
				cookies = cookieResult.split(";");
				for(String cookie: cookies){
					Log.i("Cookies", "get cookie is "+cookie);
				}
			}
			

			removeCookie("aa.com", "key");
			
			cookieResult = cookieManager.getCookie(".aa.com");
			if(cookieResult == null){
				Toast.makeText(this, "cookie is not exist!", Toast.LENGTH_SHORT).show();
				Log.i("Cookies", "cookie is not exist!");
			}else{
				cookies = cookieResult.split(";");
				for(String cookie: cookies){
					Log.i("Cookies", "get cookie is "+cookie);
				}
			}
			cookieManager.setCookie(".aa.com", "key = abcdefg;");
			
			cookieResult = cookieManager.getCookie("www.aa.com");
			if(cookieResult == null){
				Toast.makeText(this, "cookie is not exist!", Toast.LENGTH_SHORT).show();
				Log.i("Cookies", "cookie is not exist!");
			}else{
				cookies = cookieResult.split(";");
				for(String cookie: cookies){
					Log.i("Cookies", "get cookie is "+cookie);
				}
			}

			
		}
		
		/**
		 * 删除cookie
		 * 目前的方法是让已有的cookie过期
		 * @param domain
		 * @param name
		 */
		private void removeCookie(String domain , String name){
			Log.i("Cookies", "remove cookie ：name="+name);
			cookieManager.setCookie(domain, name+"="+";expires=Mon, 03 Jun 0000 07:01:29 GMT;");
		}
		
		
		private void setCookie(String value){
			cookieManager.setCookie(DEFAULT_URL, value);
		}
		
		private void setLoginCookie(){
			this.setCookie("username=abc");
			this.setCookie("password=123456");
			this.setCookie("skey=123456");
		}
}
