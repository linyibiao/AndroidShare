package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lyb.besttimer.pluginwidget.R;
import com.lyb.besttimer.pluginwidget.view.textview.BaseTextView;

/**
 * load more adapter
 * Created by linyibiao on 2016/11/25.
 */

public class LoadMoreAdapter<T extends BaseAdapter> extends BaseAdapter<BaseHolder> {

    private T mWrapperAdapter;
    private MoreListener moreListener;

    //more type
    public static final int MORETYPE = Integer.MAX_VALUE;

    public LoadMoreAdapter(T mWrapperAdapter, MoreListener moreListener) {
        this.mWrapperAdapter = mWrapperAdapter;
        this.moreListener = moreListener;
    }

    public T getmWrapperAdapter() {
        return mWrapperAdapter;
    }

    public void updateMoreData(MoreData moreData) {
        this.moreData = moreData;
        notifyItemChanged(getItemCount() - 1);
    }

    private MoreData moreData = new MoreData();

    public static class MoreData {

        MORE_STATE more_state = MORE_STATE.NORMAL;
        String moreTip = "bottom to load more";
        String failTip = "load more fail";
        String doneTip = "no more data";

        public MoreData() {
        }

        public MoreData(String moreTip, String failTip, String doneTip) {
            this.moreTip = moreTip;
            this.failTip = failTip;
            this.doneTip = doneTip;
        }

    }

    private enum MORE_STATE {
        NORMAL, LOADING, FAIL, DONE
    }

    protected abstract class BaseMoreHolder extends BaseHolder<MoreData> {

        public BaseMoreHolder(View itemView) {
            super(itemView);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoadMore(true);
            }
        };

        @Override
        public void fillView(MoreData data, int position) {
            super.fillView(data, position);
            switch (data.more_state) {
                case NORMAL:
                    normal();
                    break;
                case LOADING:
                    loading();
                    break;
                case FAIL:
                    fail();
                    break;
                case DONE:
                    done();
                    break;
            }
        }

        public abstract void normalUI(MoreData data);

        //reset
        final void normal() {
            normalUI(data);
            itemView.setOnClickListener(onClickListener);
        }

        public abstract void loadingUI(MoreData data);

        //to load more
        final void loading() {
            loadingUI(data);
            itemView.setOnClickListener(null);
        }

        public abstract void failUI(MoreData data);

        //load fail
        void fail() {
            failUI(data);
            itemView.setOnClickListener(onClickListener);
        }

        public abstract void doneUI(MoreData data);

        //load done
        void done() {
            doneUI(data);
            itemView.setOnClickListener(onClickListener);
        }

    }

    private class MoreHolder extends BaseMoreHolder {

        private ProgressBar pb_more_load;
        private BaseTextView btv_more_tip;

        MoreHolder(View itemView) {
            super(itemView);
            pb_more_load = (ProgressBar) itemView.findViewById(R.id.pb_more_load);
            btv_more_tip = (BaseTextView) itemView.findViewById(R.id.btv_more_tip);
        }

        @Override
        public void normalUI(MoreData data) {
            btv_more_tip.setText(data.moreTip);
            pb_more_load.setVisibility(View.INVISIBLE);
            btv_more_tip.setVisibility(View.VISIBLE);
        }

        @Override
        public void loadingUI(MoreData data) {
            pb_more_load.setVisibility(View.VISIBLE);
            btv_more_tip.setVisibility(View.INVISIBLE);
        }

        @Override
        public void failUI(MoreData data) {
            btv_more_tip.setText(data.failTip);
            pb_more_load.setVisibility(View.INVISIBLE);
            btv_more_tip.setVisibility(View.VISIBLE);
        }

        @Override
        public void doneUI(MoreData data) {
            btv_more_tip.setText(data.doneTip);
            pb_more_load.setVisibility(View.INVISIBLE);
            btv_more_tip.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MORETYPE) {
            return onCreateMoreHolder(parent);
        } else {
            return (BaseHolder) mWrapperAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (getItemViewType(position) == MORETYPE) {
            holder.fillView(moreData, position);
        } else {
            mWrapperAdapter.onBindViewHolder(holder, position);
        }
    }

    protected MoreHolder onCreateMoreHolder(ViewGroup parent) {
        return new MoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_more_loader, parent, false));
    }

    private MoreHolder getMoreHolder() {
        return (MoreHolder) getRecyclerView().findViewHolderForAdapterPosition(getItemCount() - 1);
    }

    protected void toLoadMore(boolean force) {
        if (moreListener != null && (force || moreData.more_state == MORE_STATE.NORMAL)) {
            MoreHolder moreHolder = getMoreHolder();
            if (moreHolder != null) {
                moreHolder.loading();
            }
            moreData.more_state = MORE_STATE.LOADING;
            moreListener.onLoadMore();
        }
    }

    /**
     * load more completed
     */
    public void loadMoreCompleted(boolean successful) {
        loadMoreCompleted(successful, false);
    }

    /**
     * load more completed
     */
    public void loadMoreCompleted(boolean successful, boolean done) {
        MoreHolder moreHolder = getMoreHolder();
        if (done) {
            moreData.more_state = MORE_STATE.DONE;
            if (moreHolder != null) {
                moreHolder.done();
            }
        } else if (successful) {
            moreData.more_state = MORE_STATE.NORMAL;
            if (moreHolder != null) {
                moreHolder.normal();
            }
        } else {
            moreData.more_state = MORE_STATE.FAIL;
            if (moreHolder != null) {
                moreHolder.fail();
            }
        }
        if (moreListener != null) {
            moreListener.onLoadCompleted(successful);
        }
    }

    //interrupt load operation
    public void interruptLoad() {
        if (moreListener != null) {
            moreListener.interruptLoad();
        }
        MoreHolder moreHolder = getMoreHolder();
        moreData.more_state = MORE_STATE.NORMAL;
        if (moreHolder != null) {
            moreHolder.normal();
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int vScrollExtent = recyclerView.computeVerticalScrollExtent();
            int vScrollOffset = recyclerView.computeVerticalScrollOffset();
            int vScrollRange = recyclerView.computeVerticalScrollRange();
            if (vScrollExtent + vScrollOffset >= vScrollRange) {
                toLoadMore(false);
            }
        }
    };

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            interruptLoad();
        }

    };

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(onScrollListener);
        registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
        unregisterAdapterDataObserver(adapterDataObserver);
        interruptLoad();
    }

    @Override
    public int getItemCount() {
        return mWrapperAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return MORETYPE;
        } else {
            return mWrapperAdapter.getItemViewType(position);
        }
    }

    public interface MoreListener {

        void onLoadMore();

        //interrupt load
        void interruptLoad();

        //completed
        void onLoadCompleted(boolean successful);
    }

}
