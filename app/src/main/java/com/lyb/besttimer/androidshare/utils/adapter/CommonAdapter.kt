package com.lyb.besttimer.androidshare.utils.adapter

import android.util.Pair
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * simple binding adapter
 *
 * @author linyibiao
 * @since 2018/7/23 17:18
 */
class CommonAdapter : RecyclerView.Adapter<CommonAdapter.BindingHolder>() {

    private val typeArray = SparseIntArray()
    var commonAdapterBeans = mutableListOf<CommonAdapterBean>()
    private val presenters = SparseArray<PresenterCreator>()

    fun setResId(resId: Int) {
        setResId(arrayOf(intArrayOf(0, resId)))
    }

    fun addResId(key: Int, resId: Int) {
        typeArray.put(key, resId)
    }

    fun setResId(resIds: Array<IntArray>) {
        typeArray.clear()
        for (resId in resIds) {
            typeArray.put(resId[0], resId[1])
        }
    }

    fun setPresenter(presenter: PresenterCreator) {
        setPresenter(0, presenter)
    }

    fun setPresenter(viewType: Int, presenter: PresenterCreator) {
        val presentersWithType = ArrayList<Pair<Int, PresenterCreator>>()
        presentersWithType.add(Pair(viewType, presenter))
        setPresentersWithType(presentersWithType)
    }

    fun setPresentersWithType(presentersWithType: List<Pair<Int, PresenterCreator>>) {
        for (pair in presentersWithType) {
            presenters.put(pair.first, pair.second)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(
                LayoutInflater.from(parent.context).inflate(typeArray.get(viewType), parent, false),
                viewType
        )
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.fillView(this, commonAdapterBeans, position)
    }

    override fun getItemCount(): Int {
        return if (typeArray.size() > 0) commonAdapterBeans.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return commonAdapterBeans[position].viewType
    }

    abstract class PresenterCreator {
        abstract fun createInstance(): Presenter
    }

    abstract class Presenter {

        lateinit var view: View
        var adapter: CommonAdapter? = null
        var commonAdapterBeans: List<CommonAdapterBean>? = null
        var commonAdapterBean: CommonAdapterBean? = null
        var position: Int = 0

        open fun init(view: View) {
            this.view = view
        }

        fun handle(
                adapter: CommonAdapter,
                commonAdapterBeans: MutableList<CommonAdapterBean>,
                commonAdapterBean: CommonAdapterBean,
                position: Int
        ) {
            this.adapter = adapter
            handle(commonAdapterBeans, commonAdapterBean, position)
        }

        open fun handle(
                commonAdapterBeans: MutableList<CommonAdapterBean>,
                commonAdapterBean: CommonAdapterBean,
                position: Int
        ) {
            this.commonAdapterBeans = commonAdapterBeans
            this.commonAdapterBean = commonAdapterBean
            this.position = position
        }

    }

    inner class BindingHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
        private lateinit var presenter: Presenter

        init {
            val presenterCreator = presenters.get(viewType)
            if (presenterCreator != null) {
                presenter = presenterCreator.createInstance()
            }
            presenter.init(view)
        }

        fun fillView(adapter: CommonAdapter, commonAdapterBeans: MutableList<CommonAdapterBean>, position: Int) {
            presenter.handle(adapter, commonAdapterBeans, commonAdapterBeans[position], position)
        }

    }

}
