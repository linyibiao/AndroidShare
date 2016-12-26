package com.example.test_webview_demo;

import com.example.test_webview_demo.utils.JavaScriptInterface;
import com.example.test_webview_demo.utils.WebViewJavaScriptFunction;
import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.sdk.QbSdk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JavaToJsActivity extends Activity {

	

	/**
	 * 本activity将演示 webview 与 web 之间的函数相互调用
	 * 也就是android方法 与 js 函数的相互调用 和传值
	 */
	
	
	X5WebView webView;
	TextView textView;
	EditText editText;
	Button btn_add;
	Button btn_dec;
	Button btn_submit;
	
	private int num=0;
	private String msg="Hello world!";
	
	Handler handler;
	private static final int MSG=0;
	private static final int NUM=1;
	private static final int MSG_SUBMIT=2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.java_to_js_layout);
		
		this.handler=new Handler(Looper.myLooper()){
			
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case NUM:
					JavaToJsActivity.this.textView.setText(String.valueOf(num));
					break;
				case MSG:
					JavaToJsActivity.this.editText.setText(JavaToJsActivity.this.msg);
					break;
				case MSG_SUBMIT:
					JavaToJsActivity.this.msg=JavaToJsActivity.this.editText.getEditableText().toString();
					break;
				}
			 super.handleMessage(msg);
				
			}
			
			
		};
		
		this.initUI();
		
		webView=(X5WebView) findViewById(R.id.web_jsjava);
		webView.loadUrl("file:///android_asset/webpage/jsToJava.html");
		
		
		webView.addJavascriptInterface(new WebViewJavaScriptFunction() {
			
			@Override
			public void onJsFunctionCalled(String tag) {
				// TODO Auto-generated method stub
				
			}
			///////////////////////////////////////////////
			//javascript to java methods
			@JavascriptInterface
			public void onSubmit(String s){
				Log.i("jsToAndroid","onSubmit happend!");
				JavaToJsActivity.this.msg=s;
				Message.obtain(handler, MSG).sendToTarget();
			}
			
			@JavascriptInterface
			public void onSubmitNum(String s){
				Log.i("jsToAndroid","onSubmitNum happend!");
				JavaToJsActivity.this.num=Integer.parseInt(s);
				Message.obtain(handler, NUM).sendToTarget();
			}
			
			

			/**
			 * java 调用 js方法 并且 传值 
			 * 步骤：1、调用 js函数  2、js回调一个android方法得到参数  3、js处理函数
			 * @return
			 */
			@JavascriptInterface
			public String getAndroidMsg(){
				Log.i("jsToAndroid","onSubmitNum happend!");
				return JavaToJsActivity.this.msg;
			}
			
			@JavascriptInterface
			public String getAndroidNum(){
				Log.i("jsToAndroid","onSubmitNum happend!");
				return String.valueOf(JavaToJsActivity.this.num);
			}
			
			
			
			/**
			 * 各种类型的传递
			 */
			@JavascriptInterface
			public void getManyValue(String key,String value){
				
				Log.i("jsToAndroid", "get key is:"+key+"  value is:"+value);
			}
			
			/**
			 * 关闭当前的窗口
			 */
			@JavascriptInterface
			public void closeCurrentWindow(){
				JavaToJsActivity.this.finish();
			}
		}, "Android");
		
		
		
		
		
		
	}
	
	private void initUI(){
		this.textView=(TextView) findViewById(R.id.text_jsjava_num);
		this.textView.setText(String.valueOf(this.num));
		this.editText=(EditText) findViewById(R.id.edit_jsjava_edit);
		this.editText.setText(this.msg);
		
		
		
		
		this.btn_add=(Button) findViewById(R.id.bt_jsjava_num_add);
		this.btn_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JavaToJsActivity.this.num++;
				int num=Integer.parseInt(JavaToJsActivity.this.textView.getText().toString());
				JavaToJsActivity.this.num=(++num);
				Message.obtain(handler, NUM).sendToTarget();
				JavaToJsActivity.this.webView.loadUrl("javascript:returnNum()");
			}
		});
		
		this.btn_dec=(Button) findViewById(R.id.bt_jsjava_num_minuse);
		this.btn_dec.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int num=Integer.parseInt(JavaToJsActivity.this.textView.getText().toString());
				JavaToJsActivity.this.num=(--num);
				Message.obtain(handler, NUM).sendToTarget();
				JavaToJsActivity.this.webView.loadUrl("javascript:returnNum()");
			}
		});
		
		this.btn_submit=(Button) findViewById(R.id.bt_jsjava_edit);
		this.btn_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message.obtain(handler, MSG_SUBMIT).sendToTarget();
				JavaToJsActivity.this.webView.loadUrl("javascript:returnMsg()");
			}
		});
		
		this.btn_submit=(Button) findViewById(R.id.web_jsjava_close_btn);
		this.btn_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JavaToJsActivity.this.webView.loadUrl("javascript:closeWnd()");
			}
		});
	
	}

	
	
	//////////////////////////////////////////////////
	//java to javascript methods

	
	
	
	
	
	
	
	
	
	



	
	
}
