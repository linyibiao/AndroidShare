package com.example.test_webview_demo;

import com.example.test_webview_demo.utils.X5WebView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FlashPlayerActivity  extends Activity{


	X5WebView webView;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.filechooser_layout);
			webView=(X5WebView) findViewById(R.id.web_filechooser);
			webView.loadUrl("http://wap.ithome.com/html/85517.htm");
			this.initBtn();
	}
	
	private void initBtn(){
		Button btnFlush=(Button) findViewById(R.id.bt_filechooser_flush);
		btnFlush.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webView.reload();
			}
		});
		
		Button btnBackForward=(Button) findViewById(R.id.bt_filechooser_back);
		btnBackForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webView.goBack();
			}
		});
		
		Button btnHome=(Button) findViewById(R.id.bt_filechooser_home);
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webView.loadUrl("http://wap.ithome.com/html/85517.htm");
			}
		});
	}
	
	
	private void appendFlashPlugin(String flashUrl){
		String temp = "<html><body bgcolor=\"" + "black"  
                + "\"> <br/><embed src=\"" + flashUrl + "\" width=\"" + "100%"  
                + "\" height=\"" + "90%" + "\" scale=\"" + "noscale"  
                + "\" type=\"" + "application/x-shockwave-flash"  
                + "\"> </embed></body></html>";  
		String mimeType = "text/html";  
		String encoding = "utf-8";  
		webView.loadDataWithBaseURL("null", temp, mimeType, encoding, ""); 
	}
}
