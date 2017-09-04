package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.adapter.ComplexTableAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;
import com.lyb.besttimer.pluginwidget.view.tablelayout.BaseTableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableComplexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_complex);
        RecyclerView brv = (RecyclerView) findViewById(R.id.brv);
        brv.setLayoutManager(new LinearLayoutManager(this));
        List<List<String>> listList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> list = new ArrayList<>();
            listList.add(list);
            for (int j = 0; j < 31; j++) {
                list.add(i + " " + j);
            }
        }
        TableComplexAdapter tableComplexAdapter = new TableComplexAdapter(listList);
        brv.setAdapter(tableComplexAdapter);
    }

    private class TableComplexAdapter extends BaseAdapter<TableComplexAdapter.ComplexHolder> {

        private List<List<String>> listList = new ArrayList<>();

        public TableComplexAdapter(List<List<String>> listList) {
            this.listList = listList;
        }

        class ComplexHolder extends BaseHolder<List<String>> {

            private ComplexTableAdapter complexTableAdapter = new ComplexTableAdapter();

            public ComplexHolder(View itemView) {
                super(itemView);
                BaseTableLayout btl = (BaseTableLayout) itemView.findViewById(R.id.btl);
                btl.setTableAdapter(complexTableAdapter);
            }

            @Override
            public void fillView(List<String> data, int position) {
                super.fillView(data, position);
                complexTableAdapter.setSpScoreValuesChoose(new HashMap<Integer, Boolean>());
                complexTableAdapter.setSpScoreValues(data);
                complexTableAdapter.notifyTableDataSetChanged();
            }
        }

        @Override
        public ComplexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ComplexHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_complex_content, parent, false));
        }

        @Override
        public void onBindViewHolder(ComplexHolder holder, int position) {
            holder.fillView(listList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return listList.size();
        }
    }

}
