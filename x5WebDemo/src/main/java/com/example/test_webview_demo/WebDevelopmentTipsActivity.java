package com.example.test_webview_demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.tencent.smtt.sdk.WebStorage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WebDevelopmentTipsActivity extends Activity{
	
	/**
	 * 关于前端开发的一些问题需要通过本例子展示
	 * 注意：其中的一些问题必须规避，否则会出现严重的错误
	 */
	
	private SimpleAdapter simpleAdapter = null;
	private ListView listItems;
	private Button btnSetting;
	private Button btnHelp;
	private List items;
	private static  String[] tips;
	
	
	
	/**
	 * web开发时出现的一些问题，
	 * 应当注意的web开发事项；
	 * 标识符
	 */
	public static final int TIPS_CACHE_CONTROL_MAX_AGE = 1;
	public static final int TIPS_CORDOVA = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_tips_layout);
		
		tips=getResources().getStringArray(R.array.tips);
		this.initView();
	}
	
	/**
	 * initiate the current view 
	 */
	private void initView(){
		this.listItems=(ListView) findViewById(R.id.web_tips_item_list);
		if(listItems!=null){
			items=new ArrayList<Map<String,Object>>();
			
			for(String tip : tips){
				Map item=new HashMap<String, Object>();
			}
			
			int[] toRes={R.id.bt_filechooser_back};
			String[] fromRes={"",""};
			
			this.simpleAdapter=new SimpleAdapter(this, items, R.layout.function_block, fromRes , toRes);
			listItems.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					
				}
			});
		}
	}

}
