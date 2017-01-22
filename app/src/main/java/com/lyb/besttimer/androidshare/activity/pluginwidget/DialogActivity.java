package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.lyb.besttimer.androidshare.R;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Dialog dialog= new Dialog(DialogActivity.this);
//                dialog.setContentView(LayoutInflater.from(DialogActivity.this).inflate(R.layout.dialog_common, null),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                dialog.show();

//                AlertDialog alertDialog=new AlertDialog.Builder(DialogActivity.this).setView(LayoutInflater.from(DialogActivity.this).inflate(R.layout.dialog_common, null), 0, 0, 0, 0).create();
                AlertDialog alertDialog=new AlertDialog.Builder(DialogActivity.this).setView(R.layout.dialog_common).setPositiveButton("what",null).create();
//                alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                WindowManager.LayoutParams layoutParams=alertDialog.getWindow().getAttributes();
//                layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
//                layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
//                alertDialog.getWindow().setAttributes(layoutParams);
//                alertDialog.setView(LayoutInflater.from(DialogActivity.this).inflate(R.layout.dialog_common, null), 0, 0, 0, 0);
                alertDialog.show();

            }
        });
    }
}
