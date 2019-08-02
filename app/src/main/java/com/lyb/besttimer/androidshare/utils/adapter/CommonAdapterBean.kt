package com.lyb.besttimer.androidshare.utils.adapter

import java.util.*

/**
 * Created by Administrator on 2018/7/25.
 */
@Suppress("UNCHECKED_CAST")
class CommonAdapterBean() {
    var workType: Int = 0
    var viewType: Int = 0
    var data: Any? = null

    constructor(workType: Int, viewType: Int, data: Any?) : this() {
        this.workType = workType
        this.viewType = viewType
        this.data = data
    }

    constructor(data: Any?) : this(0, 0, data)
    constructor(viewType: Int, data: Any?) : this(0, viewType, data)

    fun setWorkType(workType: Int): CommonAdapterBean {
        this.workType = workType
        return this
    }

    companion object {

        fun <T> getGroupByViewType(commonAdapterBeans: List<CommonAdapterBean>, viewType: Int): List<List<T>> {
            val lists = ArrayList<List<T>>()
            var currList: MutableList<T>? = null
            for (bindingAdapterBean in commonAdapterBeans) {
                if (bindingAdapterBean.viewType == viewType) {
                    if (currList == null) {
                        currList = ArrayList()
                        lists.add(currList)
                    }
                    currList.add(bindingAdapterBean.data as T)
                } else if (currList != null) {
                    currList = null
                }
            }
            return lists
        }

        fun <T> getGroupByWorkViewType(
                commonAdapterBeans: List<CommonAdapterBean>,
                workType: Int,
                viewType: Int
        ): List<List<T>> {
            val lists = ArrayList<List<T>>()
            var currList: MutableList<T>? = null
            for (bindingAdapterBean in commonAdapterBeans) {
                if (bindingAdapterBean.workType == workType && bindingAdapterBean.viewType == viewType) {
                    if (currList == null) {
                        currList = ArrayList()
                        lists.add(currList)
                    }
                    currList.add(bindingAdapterBean.data as T)
                } else if (currList != null) {
                    currList = null
                }
            }
            return lists
        }

        fun findPosByViewType(commonAdapterBeans: List<CommonAdapterBean>, targetPos: Int): Int {
            return findPosByViewType(commonAdapterBeans, commonAdapterBeans[targetPos])
        }

        fun findPosByViewType(
                commonAdapterBeans: List<CommonAdapterBean>,
                commonAdapterBean: CommonAdapterBean
        ): Int {
            var posByViewType = -1
            for (i in commonAdapterBeans.indices) {
                val currBean = commonAdapterBeans[i]
                if (currBean === commonAdapterBean) {
                    posByViewType++
                    break
                }
                if (currBean.viewType == commonAdapterBean.viewType) {
                    posByViewType++
                }
            }
            return posByViewType
        }

        fun lastViewTypeList(
                commonAdapterBeans: List<CommonAdapterBean>,
                viewType: Int,
                currIndex: Int,
                upTodown: Boolean
        ): List<CommonAdapterBean> {
            val filterBeans = ArrayList<CommonAdapterBean>()
            if (upTodown) {
                for (i in currIndex + 1 until commonAdapterBeans.size) {
                    val bindingAdapterBean = commonAdapterBeans[i]
                    if (bindingAdapterBean.viewType == viewType) {
                        filterBeans.add(bindingAdapterBean)
                    } else {
                        break
                    }
                }
            } else {
                for (i in currIndex - 1 downTo 0) {
                    val bindingAdapterBean = commonAdapterBeans[i]
                    if (bindingAdapterBean.viewType == viewType) {
                        filterBeans.add(0, bindingAdapterBean)
                    } else {
                        break
                    }
                }
            }
            return filterBeans
        }

        fun lastViewTypeList_getall_exclude(
                commonAdapterBeans: List<CommonAdapterBean>,
                viewType_exclude: Int,
                currIndex: Int,
                upTodown: Boolean
        ): List<CommonAdapterBean> {
            val filterBeans = ArrayList<CommonAdapterBean>()
            if (upTodown) {
                for (i in currIndex + 1 until commonAdapterBeans.size) {
                    val bindingAdapterBean = commonAdapterBeans[i]
                    if (bindingAdapterBean.viewType != viewType_exclude) {
                        filterBeans.add(bindingAdapterBean)
                    } else {
                        break
                    }
                }
            } else {
                for (i in currIndex - 1 downTo 0) {
                    val bindingAdapterBean = commonAdapterBeans[i]
                    if (bindingAdapterBean.viewType != viewType_exclude) {
                        filterBeans.add(0, bindingAdapterBean)
                    } else {
                        break
                    }
                }
            }
            return filterBeans
        }

        fun findPosByWorkType(commonAdapterBeans: List<CommonAdapterBean>, targetPos: Int): Int {
            return findPosByWorkType(commonAdapterBeans, commonAdapterBeans[targetPos])
        }

        fun findPosByWorkType(
                commonAdapterBeans: List<CommonAdapterBean>,
                commonAdapterBean: CommonAdapterBean
        ): Int {
            var posByWorkType = 0
            for (i in commonAdapterBeans.indices) {
                val currBean = commonAdapterBeans[i]
                if (currBean === commonAdapterBean) {
                    break
                }
                if (currBean.workType == commonAdapterBean.workType) {
                    posByWorkType++
                }
            }
            return posByWorkType
        }

        fun findPosByViewWorkType(commonAdapterBeans: List<CommonAdapterBean>, targetPos: Int): Int {
            return findPosByViewWorkType(commonAdapterBeans, commonAdapterBeans[targetPos])
        }

        fun findPosByViewWorkType(
                commonAdapterBeans: List<CommonAdapterBean>,
                commonAdapterBean: CommonAdapterBean
        ): Int {
            var posByWorkType = 0
            for (i in commonAdapterBeans.indices) {
                val currBean = commonAdapterBeans[i]
                if (currBean === commonAdapterBean) {
                    break
                }
                if (currBean.viewType == commonAdapterBean.viewType && currBean.workType == commonAdapterBean.workType) {
                    posByWorkType++
                }
            }
            return posByWorkType
        }

        fun convertList(datas: List<*>): List<CommonAdapterBean> {
            return convertList(0, datas)
        }

        fun convertList(viewType: Int, datas: List<*>): List<CommonAdapterBean> {
            return convertList(0, viewType, datas)
        }

        fun convertList(workType: Int, viewType: Int, datas: List<*>?): List<CommonAdapterBean> {
            val bindingAdapterBeans = ArrayList<CommonAdapterBean>()
            if (datas != null) {
                for (data in datas) {
                    bindingAdapterBeans.add(CommonAdapterBean(viewType, data).setWorkType(workType))
                }
            }
            return bindingAdapterBeans
        }

        fun convert(data: Any): CommonAdapterBean {
            return convert(0, data)
        }

        fun convert(viewType: Int, data: Any): CommonAdapterBean {
            return CommonAdapterBean(viewType, data)
        }

        fun convert(workType: Int, viewType: Int, data: Any): CommonAdapterBean {
            return CommonAdapterBean(workType, viewType, data)
        }
    }
}
