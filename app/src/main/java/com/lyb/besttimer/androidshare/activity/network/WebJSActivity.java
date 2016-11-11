package com.lyb.besttimer.androidshare.activity.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lyb.besttimer.androidshare.R;

public class WebJSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_js);
        WebView webView = (WebView) findViewById(R.id.wv);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:function setTop(){document.getElementById(\"closeChatBtn\").style.visibility=\"hidden\";}setTop();");
            }
        });
        webView.loadUrl("http://chat56.live800.com/live800/chatClient/chatbox.jsp?companyID=736294&configID=98548&jid=4754800841");

    }
}
