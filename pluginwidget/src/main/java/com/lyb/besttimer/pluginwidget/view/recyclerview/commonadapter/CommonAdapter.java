package com.lyb.besttimer.pluginwidget.view.recyclerview.commonadapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * simple binding adapter
 *
 * @author linyibiao
 * @since 2018/7/23 17:18
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.BindingHolder> {

    private SparseIntArray typeArray = new SparseIntArray();
    private List<BindingAdapterBean> bindingAdapterBeans = new ArrayList<>();
    private SparseArray<PresenterCreator> presenters = new SparseArray<>();

    public void setResId(int resId) {
        setResId(new int[][]{{0, resId}});
    }

    public void addResId(int key, int resId) {
        typeArray.put(key, resId);
    }

    public void setResId(int[][] resIds) {
        typeArray.clear();
        for (int[] resId : resIds) {
            typeArray.put(resId[0], resId[1]);
        }
    }

    public List<BindingAdapterBean> getBindingAdapterBeans() {
        return bindingAdapterBeans;
    }

    public void setBindingAdapterBeans(List<BindingAdapterBean> bindingAdapterBeans) {
        this.bindingAdapterBeans = bindingAdapterBeans;
    }

    public void setPresenter(PresenterCreator presenter) {
        setPresenter(0, presenter);
    }

    public void setPresenter(int viewType, PresenterCreator presenter) {
        List<Pair<Integer, PresenterCreator>> presentersWithType = new ArrayList<>();
        presentersWithType.add(new Pair<>(viewType, presenter));
        setPresentersWithType(presentersWithType);
    }

    public void setPresentersWithType(List<Pair<Integer, PresenterCreator>> presentersWithType) {
        for (Pair<Integer, PresenterCreator> pair : presentersWithType) {
            presenters.put(pair.first, pair.second);
        }
    }

    @NonNull
    @Override
    public BindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(typeArray.get(viewType), parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder holder, int position) {
        holder.fillView(bindingAdapterBeans, position);
    }

    @Override
    public int getItemCount() {
        return typeArray.size() > 0 ? bindingAdapterBeans.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return bindingAdapterBeans.get(position).getViewType();
    }

    public abstract static class PresenterCreator {
        public abstract Presenter createInstance();
    }

    public static abstract class Presenter {

        public View view;
        public List<BindingAdapterBean> bindingAdapterBeans;
        public BindingAdapterBean bindingAdapterBean;
        public int position;

        public void init(View view) {
            this.view = view;
        }

        public void handle(List<BindingAdapterBean> bindingAdapterBeans, BindingAdapterBean bindingAdapterBean, int position) {
            this.bindingAdapterBeans = bindingAdapterBeans;
            this.bindingAdapterBean = bindingAdapterBean;
            this.position = position;
        }
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private Presenter presenter;

        public BindingHolder(View view, int viewType) {
            super(view);
            PresenterCreator presenterCreator = presenters.get(viewType);
            if (presenterCreator != null) {
                presenter = presenterCreator.createInstance();
            }
            if (presenter != null) {
                presenter.init(view);
            }
        }

        public void fillView(List<BindingAdapterBean> bindingAdapterBeans, int position) {
            if (presenter != null) {
                presenter.handle(bindingAdapterBeans, bindingAdapterBeans.get(position), position);
            }
        }

    }

}
