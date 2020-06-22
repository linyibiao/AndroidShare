package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.FragmentUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentUtilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_util);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_replace1, R.id.btn_replace2, R.id.btn_add1, R.id.btn_add2, R.id.btn_show1, R.id.btn_show2, R.id.btn_showAndHideOthers1, R.id.btn_showAndHideOthers2,})
    public void onClickEvent(View view) {
        Bundle args1 = new Bundle();
        args1.putString("text", "text111111111");
        args1.putInt("color", 0xffff0000);
        Bundle args2 = new Bundle();
        args2.putString("text", "222222222text");
        args2.putInt("color", 0xff00ff00);
        switch (view.getId()) {
            case R.id.btn_replace1:
                FragmentUtil.replace(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args1, "111");
                break;
            case R.id.btn_replace2:
                FragmentUtil.replace(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args2, "222");
                break;
            case R.id.btn_add1:
                FragmentUtil.add(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args1, "111");
                break;
            case R.id.btn_add2:
                FragmentUtil.add(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args2, "222");
                break;
            case R.id.btn_show1:
                FragmentUtil.show(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args1, "111");
                break;
            case R.id.btn_show2:
                FragmentUtil.show(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args2, "222");
                break;
            case R.id.btn_showAndHideOthers1:
                FragmentUtil.showAndHideOthers(getSupportFragmentManager(), R.id.fl_fragment, SimpleFragment.class, args1, "111");
                break;
            case R.id.btn_showAndHideOthers2:
                FragmentUtil.showAndHideOthers(getSupportFragmentManager(), R.id.fl_fragment_2, SimpleFragment.class, args2, "222");
                break;
        }
    }

    public static class SimpleFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle args = getArguments();
            String text = args.getString("text");
            int color = args.getInt("color");
            TextView textView = new TextView(container.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setText(text);
            textView.setTextColor(color);
            return textView;
        }

    }

}
