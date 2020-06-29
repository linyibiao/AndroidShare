package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.viewpager.FreeViewPager;

public class DrawLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_layout);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.freeDL);

        FreeViewPager viewPager = (FreeViewPager) findViewById(R.id.vp_drawLayout);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new VPFragment();
            }

            @Override
            public int getCount() {
                return 5;
            }
        });

        viewPager.setCustomSimpleOnGestureListener(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX < 0) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                return true;
            }
        });

    }

    public static class VPFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_text, container, false);
        }

    }

}
