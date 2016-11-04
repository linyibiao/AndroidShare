package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.androidshare.adapter.SimpleTabAdapter;
import com.lyb.besttimer.androidshare.fragment.TabFragment;
import com.lyb.besttimer.pluginwidget.view.tablayout.BaseTabLayout;

/**
 * tablayout activity
 * Created by linyibiao on 2016/10/28.
 */

public class TabLayoutActivity extends BaseActivity {

    private String[] titles = {"1", "11", "111", "1111", "11111", "111111", "1111111"};

    private BaseTabLayout btl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new SimpleAdapter(getSupportFragmentManager(), titles));
        btl = (BaseTabLayout) findViewById(R.id.btl);
        btl.setIndicatorShape(BaseTabLayout.SHAPE_INDICATOR.Circle);
//        btl.setIndicatorShape(BaseTabLayout.SHAPE_INDICATOR.Triangle);
        btl.setIndicatorColor(0xFFFF0000);
        btl.setIndicatorPaddingVertical(10);
        btl.setBackgroundShape(BaseTabLayout.SHAPE_BACKGROUND.Circle);
//        btl.setBackgroundShape(BaseTabLayout.SHAPE_BACKGROUND.Normal);
        btl.setBGColor(0xFFad0000);
//        btl.setBGColor(0);
        btl.setTabMode(TabLayout.MODE_SCROLLABLE);
        btl.setmTabPadding(0);
        setAdapter(vp);
    }

    private void setAdapter(ViewPager vp) {
        btl.setupWithViewPager(vp);
        new SimpleTabAdapter(btl, titles).notifyDataSetChanged();
    }

    private static class SimpleAdapter extends FragmentPagerAdapter {

        private String[] tabTitles;

        SimpleAdapter(FragmentManager fm, String[] tabTitles) {
            super(fm);
            this.tabTitles = tabTitles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return TabFragment.newInstance(tabTitles[position]);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
