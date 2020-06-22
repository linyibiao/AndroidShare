package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.pluginwidget.view.recyclerview.BaseRecyclerView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.ItemTouchFeature;
import com.lyb.besttimer.pluginwidget.view.swipelayout.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class SwipeLayoutActivity extends BaseActivity {

    private BaseRecyclerView baseRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_layout);
        baseRecyclerView = (BaseRecyclerView) findViewById(R.id.brv);
        baseRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        baseRecyclerView.setAdapter(new SimpleAdapter());

        new ItemTouchFeature(baseRecyclerView, ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

        }.applyFeature();
        baseRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    for (int index = 0; index < baseRecyclerView.getChildCount(); index++) {
                        View childView = baseRecyclerView.getChildAt(index);
                        if (childView instanceof SwipeLayout) {
                            ((SwipeLayout) childView).reset();
                        }
                    }
                }
            }
        });

    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MenuHolder> {

        protected class MenuHolder extends RecyclerView.ViewHolder {

            public MenuHolder(View itemView) {
                super(itemView);
            }

            public void fillView() {
                SwipeLayout swipeLayout = (SwipeLayout) itemView;
                swipeLayout.setLeftPos(false);
                List<String> strings = new ArrayList<>();
                strings.add("what");
                strings.add("the");
                strings.add("hell");
                swipeLayout.setAdapter(new MenuAdapter(strings));
//                swipeLayout.setTouchHolderCall(new SwipeLayout.TouchHolderCall() {
//                    @Override
//                    public void touchHold() {
//                        baseRecyclerView.dispatchOnItemTouch(MotionEvent.ACTION_CANCEL);
//                    }
//                });
            }

        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.fillView();
        }

        @Override
        public int getItemCount() {
            return 20;
        }

    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

        private List<String> strings = new ArrayList<>();

        public MenuAdapter(List<String> strings) {
            this.strings = strings;
        }

        protected class MenuHolder extends RecyclerView.ViewHolder {

            private Button btn_str;

            public MenuHolder(View itemView) {
                super(itemView);
                btn_str = (Button) itemView.findViewById(R.id.btn_str);
            }

            public void fillView(String s) {
                btn_str.setText(s);
            }

        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipelayout_menu_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.fillView(strings.get(position));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

    }

}
