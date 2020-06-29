package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.ColorStateListUtil;
import com.lyb.besttimer.pluginwidget.utils.ViewState;
import com.lyb.besttimer.pluginwidget.view.recyclerview.BaseRecyclerView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.LevelAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.LevelHolder;

import java.util.ArrayList;
import java.util.List;

public class LevelAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_adapter);

        List<LevelAdapter.LevelData> levelDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LevelAdapter.LevelData child1Data = new LevelAdapter.LevelData("" + i, i == 0, false, false);
            levelDatas.add(child1Data);
            for (int j = 0; j < 20; j++) {
                LevelAdapter.LevelData child2Data = new LevelAdapter.LevelData("" + i + j, j == 0, false, false);
                child1Data.getNextLevelDatas().add(child2Data);
                for (int k = 0; k < 30; k++) {
                    LevelAdapter.LevelData child3Data = new LevelAdapter.LevelData("" + i + j + k,
                            (i == 5 && j == 5 && k == 5) ||
                                    (i == 5 && j == 5 && k == 7) ||
                                    (i == 5 && j == 6 && k == 9) ||
                                    (i == 8 && j == 5 && k == 5) ||
                                    (i == 9 && j == 5 && k == 5)
                            , k == 0, true);
                    child2Data.getNextLevelDatas().add(child3Data);
                }
            }
        }

        BaseRecyclerView brv_1 = (BaseRecyclerView) findViewById(R.id.brv_1);
        BaseRecyclerView brv_2 = (BaseRecyclerView) findViewById(R.id.brv_2);
        BaseRecyclerView brv_3 = (BaseRecyclerView) findViewById(R.id.brv_3);

        brv_1.setLayoutManager(new LinearLayoutManager(this));
        brv_2.setLayoutManager(new LinearLayoutManager(this));
        brv_3.setLayoutManager(new LinearLayoutManager(this));

        TestLevelAdapter testLevelAdapter3 = new TestLevelAdapter(null, new ArrayList<LevelAdapter.LevelData>(), false);
        TestLevelAdapter testLevelAdapter2 = new TestLevelAdapter(testLevelAdapter3, new ArrayList<LevelAdapter.LevelData>(), true);
        final TestLevelAdapter testLevelAdapter1 = new TestLevelAdapter(testLevelAdapter2, levelDatas, true);

        testLevelAdapter1.hasSelectAndMark();

        brv_1.setAdapter(testLevelAdapter1);
        brv_2.setAdapter(testLevelAdapter2);
        brv_3.setAdapter(testLevelAdapter3);

        findViewById(R.id.btn_operate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<LevelAdapter.LevelData> levelDataList = testLevelAdapter1.getSelectDataList();
                Log.e("what", levelDataList.size() + "数量");
            }
        });

    }

    private static class TestLevelAdapter extends LevelAdapter<TestLevelAdapter.TestLevelHolder> {

        public TestLevelAdapter(LevelAdapter<? extends LevelHolder> nextLevelAdapter, List<LevelData> levelDatas, boolean singleCheck) {
            super(nextLevelAdapter, levelDatas, singleCheck);
        }

        @Override
        public TestLevelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestLevelHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false));
        }

        class TestLevelHolder extends LevelHolder {

            private TextView tv_level;

            public TestLevelHolder(View itemView) {
                super(itemView);
                tv_level = (TextView) itemView.findViewById(R.id.tv_level);
                tv_level.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(0xffff0000, android.R.attr.state_selected), new ViewState<>(0xff000000)));
            }

            @Override
            public void fillView(LevelData data, int position) {
                super.fillView(data, position);
                tv_level.setText(data.getData().toString());
            }

        }

    }

}
