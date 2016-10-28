package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.androidshare.fragment.TabFragment;

/**
 * tablayout activity
 * Created by linyibiao on 2016/10/28.
 */

public class TabLayoutActivity extends BaseActivity {

    private String[] titles = {"1", "11", "111", "1111", "11111"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new SimpleAdapter(getSupportFragmentManager(), titles));
        TabLayout btl = (TabLayout) findViewById(R.id.btl);
        btl.setupWithViewPager(vp);
        int tabCount = btl.getTabCount();
        for (int index = 0; index < tabCount; index++) {
            TabLayout.Tab tab = btl.getTabAt(index);
            assert tab != null;
            View view = tab.setCustomView(R.layout.item_tablayout).getCustomView();
            TextView item_title = (TextView) view.findViewById(R.id.item_title);
            TextView item_number = (TextView) view.findViewById(R.id.item_number);
            item_title.setText(titles[index]);
            item_number.append(index + "");
        }
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
