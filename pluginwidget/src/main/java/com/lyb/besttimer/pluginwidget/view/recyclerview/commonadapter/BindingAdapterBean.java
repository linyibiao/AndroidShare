package com.lyb.besttimer.pluginwidget.view.recyclerview.commonadapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */
public class BindingAdapterBean {
    private int workType;
    private int viewType;
    private Object data;

    public static <T> List<List<T>> getGroupByViewType(List<BindingAdapterBean> bindingAdapterBeans, int viewType) {
        List<List<T>> lists = new ArrayList<>();
        List<T> currList = null;
        for (BindingAdapterBean bindingAdapterBean : bindingAdapterBeans) {
            if (bindingAdapterBean.getViewType() == viewType) {
                if (currList == null) {
                    currList = new ArrayList<>();
                    lists.add(currList);
                }
                currList.add((T) bindingAdapterBean.getData());
            } else if (currList != null) {
                currList = null;
            }
        }
        return lists;
    }

    public static <T> List<List<T>> getGroupByWorkViewType(List<BindingAdapterBean> bindingAdapterBeans, int workType, int viewType) {
        List<List<T>> lists = new ArrayList<>();
        List<T> currList = null;
        for (BindingAdapterBean bindingAdapterBean : bindingAdapterBeans) {
            if (bindingAdapterBean.getWorkType() == workType && bindingAdapterBean.getViewType() == viewType) {
                if (currList == null) {
                    currList = new ArrayList<>();
                    lists.add(currList);
                }
                currList.add((T) bindingAdapterBean.getData());
            } else if (currList != null) {
                currList = null;
            }
        }
        return lists;
    }

    public static int findPosByViewType(List<BindingAdapterBean> bindingAdapterBeans, int targetPos) {
        return findPosByViewType(bindingAdapterBeans, bindingAdapterBeans.get(targetPos));
    }

    public static int findPosByViewType(List<BindingAdapterBean> bindingAdapterBeans, BindingAdapterBean bindingAdapterBean) {
        int posByViewType = 0;
        for (int i = 0; i < bindingAdapterBeans.size(); i++) {
            BindingAdapterBean currBean = bindingAdapterBeans.get(i);
            if (currBean == bindingAdapterBean) {
                break;
            }
            if (currBean.getViewType() == bindingAdapterBean.getViewType()) {
                posByViewType++;
            }
        }
        return posByViewType;
    }

    public static List<BindingAdapterBean> lastViewTypeList(List<BindingAdapterBean> bindingAdapterBeans, int viewType, int currIndex, boolean upTodown) {
        List<BindingAdapterBean> filterBeans = new ArrayList<>();
        if (upTodown) {
            for (int i = currIndex + 1; i < bindingAdapterBeans.size(); i++) {
                BindingAdapterBean bindingAdapterBean = bindingAdapterBeans.get(i);
                if (bindingAdapterBean.getViewType() == viewType) {
                    filterBeans.add(bindingAdapterBean);
                } else {
                    break;
                }
            }
        } else {
            for (int i = currIndex - 1; i >= 0; i--) {
                BindingAdapterBean bindingAdapterBean = bindingAdapterBeans.get(i);
                if (bindingAdapterBean.getViewType() == viewType) {
                    filterBeans.add(0, bindingAdapterBean);
                } else {
                    break;
                }
            }
        }
        return filterBeans;
    }

    public static List<BindingAdapterBean> lastWorkTypeList(List<BindingAdapterBean> bindingAdapterBeans, int workType, int currIndex, boolean upTodown) {
        List<BindingAdapterBean> filterBeans = new ArrayList<>();
        if (upTodown) {
            for (int i = currIndex + 1; i < bindingAdapterBeans.size(); i++) {
                BindingAdapterBean bindingAdapterBean = bindingAdapterBeans.get(i);
                if (bindingAdapterBean.getWorkType() == workType) {
                    filterBeans.add(bindingAdapterBean);
                } else {
                    break;
                }
            }
        } else {
            for (int i = currIndex - 1; i >= 0; i--) {
                BindingAdapterBean bindingAdapterBean = bindingAdapterBeans.get(i);
                if (bindingAdapterBean.getWorkType() == workType) {
                    filterBeans.add(0, bindingAdapterBean);
                } else {
                    break;
                }
            }
        }
        return filterBeans;
    }

    public static int findPosByWorkType(List<BindingAdapterBean> bindingAdapterBeans, int targetPos) {
        return findPosByWorkType(bindingAdapterBeans, bindingAdapterBeans.get(targetPos));
    }

    public static int findPosByWorkType(List<BindingAdapterBean> bindingAdapterBeans, BindingAdapterBean bindingAdapterBean) {
        int posByWorkType = 0;
        for (int i = 0; i < bindingAdapterBeans.size(); i++) {
            BindingAdapterBean currBean = bindingAdapterBeans.get(i);
            if (currBean == bindingAdapterBean) {
                break;
            }
            if (currBean.getWorkType() == bindingAdapterBean.getWorkType()) {
                posByWorkType++;
            }
        }
        return posByWorkType;
    }

    public static int findPosByViewWorkType(List<BindingAdapterBean> bindingAdapterBeans, int targetPos) {
        return findPosByViewWorkType(bindingAdapterBeans, bindingAdapterBeans.get(targetPos));
    }

    public static int findPosByViewWorkType(List<BindingAdapterBean> bindingAdapterBeans, BindingAdapterBean bindingAdapterBean) {
        int posByWorkType = 0;
        for (int i = 0; i < bindingAdapterBeans.size(); i++) {
            BindingAdapterBean currBean = bindingAdapterBeans.get(i);
            if (currBean == bindingAdapterBean) {
                break;
            }
            if (currBean.getViewType() == bindingAdapterBean.getViewType() && currBean.getWorkType() == bindingAdapterBean.getWorkType()) {
                posByWorkType++;
            }
        }
        return posByWorkType;
    }

    public static List<BindingAdapterBean> convertList(List<?> datas) {
        return convertList(0, datas);
    }

    public static List<BindingAdapterBean> convertList(int viewType, List<?> datas) {
        return convertList(0, viewType, datas);
    }

    public static List<BindingAdapterBean> convertList(int workType, int viewType, List<?> datas) {
        List<BindingAdapterBean> bindingAdapterBeans = new ArrayList<>();
        for (Object data : datas) {
            bindingAdapterBeans.add(new BindingAdapterBean(viewType, data).setWorkType(workType));
        }
        return bindingAdapterBeans;
    }

    public static BindingAdapterBean convert(Object data) {
        return convert(0, data);
    }

    public static BindingAdapterBean convert(int viewType, Object data) {
        return new BindingAdapterBean(viewType, data);
    }

    public BindingAdapterBean(Object data) {
        this(0, data);
    }

    public BindingAdapterBean(int viewType, Object data) {
        this.viewType = viewType;
        this.data = data;
    }

    public int getWorkType() {
        return workType;
    }

    public BindingAdapterBean setWorkType(int workType) {
        this.workType = workType;
        return this;
    }

    public int getViewType() {
        return viewType;
    }

    public Object getData() {
        return data;
    }
}
