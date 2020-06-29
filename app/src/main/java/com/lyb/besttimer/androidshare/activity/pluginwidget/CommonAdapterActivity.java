package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.databinding.ItemAdapterType1Binding;
import com.lyb.besttimer.androidshare.databinding.ItemAdapterType2Binding;
import com.lyb.besttimer.pluginwidget.view.recyclerview.commonadapter.BindingAdapterBean;
import com.lyb.besttimer.pluginwidget.view.recyclerview.commonadapter.CommonBindingAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonAdapterActivity extends AppCompatActivity {

    @BindView(R.id.rv_data)
    RecyclerView rv_data;

    private static final int type1 = 1;
    private static final int type2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_adapter);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                CommonBindingAdapter commonBindingAdapter = (CommonBindingAdapter) rv_data.getAdapter();
                if (commonBindingAdapter.getBindingAdapterBeans().get(position).getViewType() == type1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        CommonBindingAdapter commonBindingAdapter = new CommonBindingAdapter();
        rv_data.setLayoutManager(gridLayoutManager);
        rv_data.setAdapter(commonBindingAdapter);

        commonBindingAdapter.setResId(new int[][]{{type1, R.layout.item_adapter_type1}, {type2, R.layout.item_adapter_type2}});
        commonBindingAdapter.setPresenter(type1, new CommonBindingAdapter.PresenterCreator() {
            @Override
            public CommonBindingAdapter.Presenter createInstance() {
                return new CommonBindingAdapter.Presenter() {

                    private ItemAdapterType1Binding binding;

                    @Override
                    public void init(ViewDataBinding viewDataBinding) {
                        super.init(viewDataBinding);
                        binding = (ItemAdapterType1Binding) viewDataBinding;
                    }

                    @Override
                    public void handle(List<BindingAdapterBean> bindingAdapterBeans, BindingAdapterBean bindingAdapterBean, int position) {
                        super.handle(bindingAdapterBeans, bindingAdapterBean, position);
                        binding.setTitle(bindingAdapterBean.getData().toString());
                    }
                };
            }
        });
        commonBindingAdapter.setPresenter(type2, new CommonBindingAdapter.PresenterCreator() {
            @Override
            public CommonBindingAdapter.Presenter createInstance() {
                return new CommonBindingAdapter.Presenter() {

                    private ItemAdapterType2Binding binding;

                    @Override
                    public void init(ViewDataBinding viewDataBinding) {
                        super.init(viewDataBinding);
                        binding = (ItemAdapterType2Binding) viewDataBinding;
                    }

                    @Override
                    public void handle(List<BindingAdapterBean> bindingAdapterBeans, BindingAdapterBean bindingAdapterBean, int position) {
                        super.handle(bindingAdapterBeans, bindingAdapterBean, position);
                        binding.setTitle(bindingAdapterBean.getData().toString());
                    }
                };
            }
        });

        List<BindingAdapterBean> bindingAdapterBeans = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            if (random.nextBoolean()) {
                bindingAdapterBeans.add(BindingAdapterBean.convert(type1, "type1 " + i));
            } else {
                bindingAdapterBeans.add(BindingAdapterBean.convert(type2, "type2 " + i));
            }
        }
        commonBindingAdapter.setBindingAdapterBeans(bindingAdapterBeans);
        commonBindingAdapter.notifyDataSetChanged();

    }
}
