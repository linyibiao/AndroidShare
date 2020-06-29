package com.lyb.besttimer.androidshare.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.pluginwidget.utils.FragmentUtil;

/**
 * tab fragment
 * Created by linyibiao on 2016/10/28.
 */

public class TabFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private String mContent;

    public static TabFragment newInstance(String mContent) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, mContent);
        return FragmentUtil.newInstance(TabFragment.class, args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getArguments().getString(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        TextView textView = new TextView(container.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.append("Fragment #");
        textView.append(mContent);
        return textView;
    }
}
