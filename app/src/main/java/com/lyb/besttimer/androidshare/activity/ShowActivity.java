package com.lyb.besttimer.androidshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.monitorCheck.MemoryCheckActivity;
import com.lyb.besttimer.androidshare.activity.network.GlideActivity;
import com.lyb.besttimer.androidshare.activity.network.SimpleRetrofitActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.CardViewActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.CountUpTimerActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.DecorationActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.DrawLayoutActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.EditTextActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.FragmentUtilActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.HeadFootAdapterActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.HeaderRecyclerViewActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ImageScaleActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.InfinityNestedScrollViewActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ItemTouchActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ItemTouchHelperActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ItemTreeActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.LevelAdapterActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.LinearLayoutActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ListScrollInfinityActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.NestedScrollViewControlActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.NumberEditActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.PorterDuffActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.PullRefreshActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.RecyclerViewActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.RecyclerViewInfinityActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.RecyclerViewShowMoreActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.RefreshLayoutActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.SimpleFragmentActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.SwipeLayoutActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.TableComplexActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.TableLayoutActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.TestTabLayoutActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.TextViewActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ToolBarActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.VideoViewActivity;
import com.lyb.besttimer.androidshare.activity.pluginwidget.ViewTestActivity;
import com.lyb.besttimer.androidshare.activity.rxandroid.RxBusActivity;
import com.lyb.besttimer.androidshare.activity.rxandroid.RxIntervalActivity;
import com.lyb.besttimer.androidshare.activity.rxandroid.RxLifeActivity;
import com.lyb.besttimer.androidshare.activity.rxandroid.SimpleRxActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.lv);
        List<LVData> lvDatas = (List<LVData>) getIntent().getSerializableExtra("datas");
        if (lvDatas == null) {

            lvDatas = new ArrayList<>();

            LVData lvData = new LVData(ShowActivity.class, "network");
            lvData.getLvDatas().add(new LVData(SimpleRetrofitActivity.class, "simple retrofit"));
            lvData.getLvDatas().add(new LVData(GlideActivity.class, "simple glide"));
            lvDatas.add(lvData);

            lvData = new LVData(ShowActivity.class, "pluginwidget");
            lvData.getLvDatas().add(new LVData(NestedScrollViewControlActivity.class, "ScrollView触摸代理切换"));
            lvData.getLvDatas().add(new LVData(RecyclerViewActivity.class, "RecyclerView下拉刷新"));
            lvData.getLvDatas().add(new LVData(HeaderRecyclerViewActivity.class, "RecyclerView悬浮header"));
            lvData.getLvDatas().add(new LVData(ItemTreeActivity.class, "多层次RecyclerView"));
            lvData.getLvDatas().add(new LVData(ItemTouchActivity.class, "方格移动RecyclerView"));
            lvData.getLvDatas().add(new LVData(PullRefreshActivity.class, "复古的下拉刷新"));
            lvData.getLvDatas().add(new LVData(RecyclerViewShowMoreActivity.class, "列表元素显示更多"));
            lvData.getLvDatas().add(new LVData(TextViewActivity.class, "textview基类"));
            lvData.getLvDatas().add(new LVData(DecorationActivity.class, "decoration模板"));
            lvData.getLvDatas().add(new LVData(SwipeLayoutActivity.class, "swipe layout"));
            lvData.getLvDatas().add(new LVData(TestTabLayoutActivity.class, "test tablayout"));
            lvData.getLvDatas().add(new LVData(PorterDuffActivity.class, "simple porterDuff"));
            lvData.getLvDatas().add(new LVData(EditTextActivity.class, "simple edittext"));
            lvData.getLvDatas().add(new LVData(TableLayoutActivity.class, "simple tablelayout"));
            lvData.getLvDatas().add(new LVData(LinearLayoutActivity.class, "simple linearlayout"));
            lvData.getLvDatas().add(new LVData(TableComplexActivity.class, "complex tablelayout"));
            lvData.getLvDatas().add(new LVData(SimpleFragmentActivity.class, "simple fragment"));
            lvData.getLvDatas().add(new LVData(NumberEditActivity.class, "number edit"));
            lvData.getLvDatas().add(new LVData(CardViewActivity.class, "card view"));
            lvData.getLvDatas().add(new LVData(LevelAdapterActivity.class, "level adapter"));
            lvData.getLvDatas().add(new LVData(FragmentUtilActivity.class, "fragment util"));
            lvData.getLvDatas().add(new LVData(DrawLayoutActivity.class, "侧拉菜单"));
            lvData.getLvDatas().add(new LVData(RefreshLayoutActivity.class, "统一下拉刷新"));
            lvData.getLvDatas().add(new LVData(ItemTouchHelperActivity.class, "列表拖拽尝试"));
            lvData.getLvDatas().add(new LVData(VideoViewActivity.class, "视频播放"));
            lvData.getLvDatas().add(new LVData(ToolBarActivity.class, "ToolBar使用"));
            lvData.getLvDatas().add(new LVData(ImageScaleActivity.class, "imageview scale"));
            lvData.getLvDatas().add(new LVData(CountUpTimerActivity.class, "计时器"));
            lvData.getLvDatas().add(new LVData(ViewTestActivity.class, "view show"));
            lvData.getLvDatas().add(new LVData(HeadFootAdapterActivity.class, "带有头部尾部的adapter"));
            lvData.getLvDatas().add(new LVData(ListScrollInfinityActivity.class, "循环滚动列表"));
            lvData.getLvDatas().add(new LVData(RecyclerViewInfinityActivity.class, "循环滚动列表(recyclerview)"));
            lvData.getLvDatas().add(new LVData(InfinityNestedScrollViewActivity.class, "循环滚动列表(nestedscrollview)"));
            lvDatas.add(lvData);

            lvData = new LVData(ShowActivity.class, "rxandroid");
            lvData.getLvDatas().add(new LVData(SimpleRxActivity.class, "simple rxjava"));
            lvData.getLvDatas().add(new LVData(RxBusActivity.class, "simple rxbus"));
            lvData.getLvDatas().add(new LVData(RxIntervalActivity.class, "rxjava interval"));
            lvData.getLvDatas().add(new LVData(RxLifeActivity.class, "rxandroid life"));
            lvDatas.add(lvData);

            lvData = new LVData(ShowActivity.class, "monitorCheck");
            lvData.getLvDatas().add(new LVData(MemoryCheckActivity.class, "memoryCheck"));
            lvDatas.add(lvData);

        }
        listView.setAdapter(new LVAdapter(lvDatas));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * listview data
     * Created by linyibiao on 2016/10/28.
     */
    private static class LVData implements Serializable {

        private Class<?> activityC;
        private String itemTitle;
        private List<LVData> lvDatas = new ArrayList<>();

        private LVData(Class<?> activityC, String itemTitle) {
            this.activityC = activityC;
            this.itemTitle = itemTitle;
        }

        public LVData(Class<?> activityC, String itemTitle, List<LVData> lvDatas) {
            this.activityC = activityC;
            this.itemTitle = itemTitle;
            this.lvDatas = lvDatas;
        }

        public Class<?> getActivityC() {
            return activityC;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public List<LVData> getLvDatas() {
            return lvDatas;
        }
    }

    private class LVAdapter extends BaseAdapter {

        private List<LVData> lvDatas = new ArrayList<>();

        public LVAdapter(List<LVData> lvDatas) {
            this.lvDatas = lvDatas;
        }

        @Override
        public int getCount() {
            return lvDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return lvDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_main, parent, false);
            }

            final LVData lvData = (LVData) getItem(position);

            TextView textView = (TextView) view.findViewById(R.id.tv);
            textView.setText(lvData.itemTitle);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), lvData.getActivityC());
                    intent.putExtra("title", lvData.getItemTitle());
                    if (lvData.getLvDatas().size() > 0) {
                        intent.putExtra("datas", (Serializable) lvData.getLvDatas());
                    }
                    startActivity(intent);
                }
            });

            return view;
        }

    }
}
