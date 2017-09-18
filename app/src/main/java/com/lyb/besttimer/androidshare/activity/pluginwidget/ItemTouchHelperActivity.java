package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.BetterItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;
import com.lyb.besttimer.pluginwidget.view.recyclerview.decoration.BaseItemDecoration;
import com.lyb.besttimer.pluginwidget.view.recyclerview.decoration.ColorDecorateDetail;

import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_helper);
        final Button btn_delete = (Button) findViewById(R.id.btn_delete);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 4));
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            strings.add("item" + i);
        }
        rv.addItemDecoration(new BaseItemDecoration(4, 10, false, BaseItemDecoration.DRAWORIENTATION.BOTH, new ColorDecorateDetail(0)));
        rv.setAdapter(new TouchAdapter(strings));
        ItemTouchHelper itemTouchHelper = new BetterItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                return super.canDropOver(recyclerView, current, target);
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
                return super.chooseDropTarget(selected, dropTargets, curX, curY);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setSelected(false);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (viewHolder != null) {
                    viewHolder.itemView.setSelected(actionState == ItemTouchHelper.ACTION_STATE_DRAG);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                List<String> strings = ((TouchAdapter) recyclerView.getAdapter()).getStrings();
                int sourcePos = viewHolder.getAdapterPosition();
                int targetPos = target.getAdapterPosition();
                strings.add(targetPos, strings.remove(sourcePos));
                recyclerView.getAdapter().notifyItemMoved(sourcePos, targetPos);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }, new BetterItemTouchHelper.HandleEventWithXY() {

            private boolean selectedToDelete = false;

            @Override
            public void handleDown(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY) {
                deleteStatus(false);
            }

            @Override
            public void handleMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY) {
                int[] holderLocation = new int[2];
                viewHolder.itemView.getLocationInWindow(holderLocation);
                int[] deleteLocation = new int[2];
                btn_delete.getLocationInWindow(deleteLocation);
                boolean deleteStatus = holderLocation[1] + viewHolder.itemView.getHeight() > deleteLocation[1];
                if (selectedToDelete != deleteStatus) {
                    selectedToDelete = deleteStatus;
                    deleteStatus(deleteStatus);
                }
            }

            private void deleteStatus(boolean deleteStatus) {
                btn_delete.setText(deleteStatus ? "删除特么滴" : "先放他一马");
            }

            @Override
            public boolean handleUp(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY) {
                btn_delete.setText("删除");
                if (selectedToDelete) {
                    List<String> strings = ((TouchAdapter) recyclerView.getAdapter()).getStrings();
                    int sourcePos = viewHolder.getAdapterPosition();
                    strings.remove(sourcePos);
                    recyclerView.getAdapter().notifyItemRemoved(sourcePos);
                }
                boolean toDelete = selectedToDelete;
                selectedToDelete = false;
                return toDelete;
            }
        });
        itemTouchHelper.attachToRecyclerView(rv);
    }

    private static class TouchAdapter extends BaseAdapter<TouchAdapter.TouchHolder> {

        private List<String> strings = new ArrayList<>();

        public TouchAdapter(List<String> strings) {
            this.strings = strings;
        }

        public List<String> getStrings() {
            return strings;
        }

        @Override
        public TouchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TouchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_touch, parent, false));
        }

        @Override
        public void onBindViewHolder(TouchHolder holder, int position) {
            holder.fillView(strings.get(position), position);
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        static class TouchHolder extends BaseHolder<String> {

            private TextView tv_touch;

            public TouchHolder(View itemView) {
                super(itemView);
                tv_touch = (TextView) itemView.findViewById(R.id.tv_touch);
            }

            @Override
            public void fillView(String data, int position) {
                super.fillView(data, position);
                tv_touch.setText(data);
            }
        }

    }

}
