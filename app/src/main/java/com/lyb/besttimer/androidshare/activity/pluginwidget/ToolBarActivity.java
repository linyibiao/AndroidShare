package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;

public class ToolBarActivity extends BaseActivity {

    private Toolbar toolB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);
        toolB = (Toolbar) findViewById(R.id.toolB);
        toolB.setTitle("");
        setSupportActionBar(toolB);
        toolB.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_tb1:
                        item.setChecked(!item.isChecked());
                        Toast.makeText(ToolBarActivity.this, "menu_tb1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_tb2:
                        Toast.makeText(ToolBarActivity.this, "menu_tb2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_tb3:
                        Toast.makeText(ToolBarActivity.this, "menu_tb3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_tb4:
                        Toast.makeText(ToolBarActivity.this, "menu_tb4", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
