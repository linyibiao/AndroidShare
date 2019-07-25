package android.support.v7.widget.helper;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import java.lang.reflect.Field;

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

    private int getPreActionState() {
        try {
            Field preActionState = getClass().getSuperclass().getDeclaredField("mActionState");
            preActionState.setAccessible(true);
            return preActionState.getInt(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ItemTouchHelper.ACTION_STATE_IDLE;
    }

    @Override
    void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        super.moveIfNecessary(viewHolder);
        if (getPreActionState() == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (handleEventWithXY != null) {
                handleEventWithXY.handleMove(mRecyclerView, mSelected, currRawX, currRawY);
            }
        }
    }

    @Override
    void select(RecyclerView.ViewHolder selected, int actionState) {
        if (mSelected == null && actionState == ItemTouchHelper.ACTION_STATE_DRAG && getPreActionState() == ItemTouchHelper.ACTION_STATE_IDLE) {
            //长按刚要移动holder
            if (handleEventWithXY != null) {
                handleEventWithXY.handleDown(mRecyclerView, selected, currRawX, currRawY);
            }
        } else if (mSelected != null && selected == null && actionState == ItemTouchHelper.ACTION_STATE_IDLE && getPreActionState() == ItemTouchHelper.ACTION_STATE_DRAG) {
            //拖拽后刚松手
            if (handleEventWithXY != null) {
                handleEventWithXY.handleUp(mRecyclerView, mSelected, currRawX, currRawY);
            }
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

        void handleUp(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float currRawX, float currRawY);
    }

}
