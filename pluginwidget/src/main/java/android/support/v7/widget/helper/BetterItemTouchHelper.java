package android.support.v7.widget.helper;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * 具有更大修改空间的ItemTouchHelper
 * Created by linyibiao on 2017/9/18.
 */

public class BetterItemTouchHelper extends ItemTouchHelper {

    private HandleEventWithXY handleEventWithXY;

    public BetterItemTouchHelper(Callback callback, HandleEventWithXY handleEventWithXY) {
        super(callback);
        this.handleEventWithXY = handleEventWithXY;
    }

    @Override
    void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        super.moveIfNecessary(viewHolder);
        if (mActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (handleEventWithXY != null) {
                handleEventWithXY.handleMove(mRecyclerView, mSelected, currRawX, currRawY);
            }
        }
    }

    @Override
    void select(RecyclerView.ViewHolder selected, int actionState) {
        //如果要在这里删除viewholder，只能强制自己置空
        boolean handleUp = false;
        if (mSelected == null && actionState == ItemTouchHelper.ACTION_STATE_DRAG && mActionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            //长按刚要移动holder
            if (handleEventWithXY != null) {
                handleEventWithXY.handleDown(mRecyclerView, selected, currRawX, currRawY);
            }
        } else if (mSelected != null && selected == null && actionState == ItemTouchHelper.ACTION_STATE_IDLE && mActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //拖拽后刚松手
            if (handleEventWithXY != null) {
                handleUp = handleEventWithXY.handleUp(mRecyclerView, mSelected, currRawX, currRawY);
            }
        }
        if (handleUp) {
            mSelected = null;
        }
        super.select(selected, actionState);
    }

    private float currRawX, currRawY;

    @Override
    void updateDxDy(MotionEvent ev, int directionFlags, int pointerIndex) {
        super.updateDxDy(ev, directionFlags, pointerIndex);
        currRawX = ev.getRawX();
        currRawY = ev.getRawY();
    }

    public interface HandleEventWithXY {
        void handleDown(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY);

        void handleMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY);

        boolean handleUp(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY);
    }

}
