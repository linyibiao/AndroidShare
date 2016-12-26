package com.example.test_webview_demo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;

public class MainActivity extends Activity {

	public static boolean firstOpening = true;
	// TODO: See init() for initialization!
	// TODO: Title -- Add other items here in strings.xml
	private static String[] titles = null;

	/**
	 * {@value TIPS_SHOW the handler msg to show tips}
	 */
	public static final int MSG_WEBVIEW_CONSTRUCTOR = 1;
	public static final int MSG_WEBVIEW_POLLING = 2;

	///////////////////////////////////////////////////////////////////////////////////////////////////
	// add constant here
	private static final int TBS_WEB = 0;
	private static final int FULL_SCREEN_VIDEO = 1;
	// private static final int TBS_COOKIE=2;
	private static final int JAVA_TO_JS = 2;
	private static final int FILE_CHOOSER = 3;
	private static final int TBS_VIDEO = 4;
	private static final int TBS_IMAGE = 5;
	// private static final int TBS_DB=7;
	private static final int TBS_NEW_WINDOW = 6;
	private static final int SYS_WEB = 7;
	private static final int TBS_FLASH = 8;
	// private static final int TBS_WEB_NOTICE=12;
	// private static final int TBS_BUILDING=13;
	private static final int TBS_LONG_PRESS = 9;
	private static final int TBS_OVER_SCROLL = 10;
	private static final int TBSS_SMALL_QB = 11;

	///////////////////////////////////////////////////////////////////////////////////////////////
	// for view init
	private Context mContext = null;
	private SimpleAdapter gridAdapter;
	private GridView gridView;
	private ArrayList<HashMap<String, Object>> items;

	private static boolean main_initialized = false;

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity OnCreate
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_advanced);
		mContext = this;
		if (!main_initialized) {
			this.new_init();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity OnResume
	@Override
	protected void onResume() {
		this.new_init();

		// this.gridView.setAdapter(gridAdapter);
		super.onResume();
	}

	//////////////////////////////////////////////////////////////////////////////////
	// initiate new UI content
	private void new_init() {
		items = new ArrayList<HashMap<String, Object>>();
		this.gridView = (GridView) this.findViewById(R.id.item_grid);

		if (gridView == null)
			throw new IllegalArgumentException("the gridView is null");

		titles = getResources().getStringArray(R.array.index_titles);
		int[] iconResourse = { R.drawable.tbsweb, R.drawable.fullscreen, R.drawable.jsjava, R.drawable.filechooser,
				R.drawable.tbsvideo, R.drawable.imageselect, R.drawable.webviewtransport,
				R.drawable.sysweb, R.drawable.flash, R.drawable.longclick, R.drawable.refresh, R.drawable.refresh, };

		HashMap<String, Object> item = null;
		// HashMap<String, ImageView> block = null;
		for (int i = 0; i < titles.length; i++) {
			item = new HashMap<String, Object>();

			item.put("title", titles[i]);
			item.put("icon", iconResourse[i]);

			items.add(item);
		}
		this.gridAdapter = new SimpleAdapter(this, items, R.layout.function_block, new String[] { "title", "icon" },
				new int[] { R.id.Item_text, R.id.Item_bt });
		if (null != this.gridView) {
			this.gridView.setAdapter(gridAdapter);
			this.gridAdapter.notifyDataSetChanged();
			this.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> gridView, View view, int position, long id) {
					HashMap<String, String> item = (HashMap<String, String>) gridView.getItemAtPosition(position);

					Intent intent = null;
					switch (position) {
					case FILE_CHOOSER: {
						intent = new Intent(MainActivity.this, FilechooserActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					case FULL_SCREEN_VIDEO: {
						intent = new Intent(MainActivity.this, FullScreenActivity.class);
						MainActivity.this.startActivity(intent);
					}
						break;
					case JAVA_TO_JS: {
						intent = new Intent(MainActivity.this, JavaToJsActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					/*
					 * case TBS_COOKIE: { // intent=new
					 * Intent(MainActivity.this,CookieTestActivity.class); //
					 * MainActivity.this.startActivity(intent);
					 * Toast.makeText(mContext, "未开放功能",
					 * Toast.LENGTH_LONG).show(); } break;
					 */
					case TBS_VIDEO: {

						MainActivity.this.invokeTbsVideoPlayer(
								"http://125.64.133.74/data9/userfiles/video02/2014/12/11/2796948-280-068-1452.mp4");

					}
						break;
					case TBS_WEB: {
						intent = new Intent(MainActivity.this, BrowserActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					case TBS_IMAGE: {
						intent = new Intent(MainActivity.this, ImageResultActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					case SYS_WEB: {
						intent = new Intent(MainActivity.this, SystemWebActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					case TBS_FLASH: {
						intent = new Intent(MainActivity.this, FlashPlayerActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;

					case TBS_NEW_WINDOW: {
						intent = new Intent(MainActivity.this, WebViewTransportActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;

					case TBS_LONG_PRESS: {
						intent = new Intent(MainActivity.this, MyLongPressActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					case TBS_OVER_SCROLL: {
						intent = new Intent(MainActivity.this, RefreshActivity.class);
						MainActivity.this.startActivity(intent);

					}
						break;
					case TBSS_SMALL_QB:
					{
						
					}
						break;
					}

				}
			});

		}
		main_initialized = true;

	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	// Activity menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.tbsSuiteExit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void tbsSuiteExit() {
		// exit TbsSuite?
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("X5功能演示");
		dialog.setPositiveButton("OK", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				android.os.Process.killProcess(Process.myPid());
			}
		});
		dialog.setMessage("quit now?");
		dialog.create().show();
	}

	/**
	 * 用于TBS 视频裸播
	 * 
	 * @param videoUrl
	 *            视频源 url
	 */
	private void invokeTbsVideoPlayer(String videoUrl) {
		
	}
}
