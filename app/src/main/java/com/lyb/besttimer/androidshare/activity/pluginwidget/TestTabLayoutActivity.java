package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;

public class TestTabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tab_layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_tab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_indicator);

        viewPager.setAdapter(new TestPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setIcon(R.drawable.selector_tab);
        }

    }

    private class TestPagerAdapter extends FragmentPagerAdapter {

        private int pageCount = 3;

        public TestPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("title", "title" + position);
            TestFragment testFragment = new TestFragment();
            testFragment.setArguments(args);
            return testFragment;
        }

        @Override
        public int getCount() {
            return pageCount;
        }

    }

    public static class TestFragment extends Fragment {

        private TextView tv_text;

        private String title;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            title = getArguments().getString("title");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_text, container, false);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            tv_text.setText(title);
        }

    }

}
