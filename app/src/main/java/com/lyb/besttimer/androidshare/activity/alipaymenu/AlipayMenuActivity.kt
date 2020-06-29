package com.lyb.besttimer.androidshare.activity.alipaymenu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.BetterItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.lyb.besttimer.androidshare.R
import com.lyb.besttimer.androidshare.utils.adapter.CommonAdapter
import com.lyb.besttimer.androidshare.utils.adapter.CommonAdapterBean
import kotlinx.android.synthetic.main.activity_alipay_menu.*
import kotlinx.android.synthetic.main.item_alipaymenu_1.view.*

class AlipayMenuActivity : AppCompatActivity() {

    class Item(val mark: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alipay_menu)

        val adapter = CommonAdapter()
        adapter.addResId(1, R.layout.item_alipaymenu_1)
        adapter.addResId(2, R.layout.item_alipaymenu_2)
        adapter.setPresenter(1, object : CommonAdapter.PresenterCreator() {
            override fun createInstance(): CommonAdapter.Presenter {
                return object : CommonAdapter.Presenter() {

                    override fun init(view: View) {
                        super.init(view)
                        view.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val strings = adapter.commonAdapterBeans
                                val sourcePos = 6
                                val targetPos = 20
                                strings.add(targetPos, strings.removeAt(sourcePos))
                                adapter.notifyItemMoved(sourcePos, targetPos)
                            }
                        })
                    }

                    override fun handle(commonAdapterBeans: MutableList<CommonAdapterBean>, commonAdapterBean: CommonAdapterBean, position: Int) {
                        super.handle(commonAdapterBeans, commonAdapterBean, position)
                        view.tv_mark.text = (commonAdapterBean.data as Item).mark
                    }
                }
            }

        })
        adapter.setPresenter(2, object : CommonAdapter.PresenterCreator() {
            override fun createInstance(): CommonAdapter.Presenter {
                return object : CommonAdapter.Presenter() {

                    override fun init(view: View) {
                        super.init(view)
                        view.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val strings = adapter.commonAdapterBeans
                                val sourcePos = 6
                                val targetPos = 20
                                strings.add(targetPos, strings.removeAt(sourcePos))
                                adapter.notifyItemMoved(sourcePos, targetPos)
                            }
                        })
                    }

                    override fun handle(commonAdapterBeans: MutableList<CommonAdapterBean>, commonAdapterBean: CommonAdapterBean, position: Int) {
                        super.handle(commonAdapterBeans, commonAdapterBean, position)
                        view.tv_mark.text = (commonAdapterBean.data as Item).mark
                    }
                }
            }

        })
        val layoutManager = GridLayoutManager(this, 5)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.commonAdapterBeans[position].viewType
                return if (viewType == 1) {
                    5
                } else {
                    1
                }
            }

        }
        rv_data.layoutManager = layoutManager
        rv_data.adapter = adapter
        val datas = adapter.commonAdapterBeans
        datas.add(CommonAdapterBean.convert(1, 1, Item("我的应用")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("支付")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤1")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤2")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤3")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤4")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤5")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤6")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤7")))
        datas.add(CommonAdapterBean.convert(1, 2, Item("考勤8")))
        datas.add(CommonAdapterBean.convert(2, 1, Item("其他应用")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("食谱")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程1")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程2")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程3")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程4")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程5")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程6")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程7")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程8")))
        datas.add(CommonAdapterBean.convert(2, 2, Item("课程9")))

        val itemTouchHelper = BetterItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {

            override fun getDragDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                if (viewHolder != null) {
                    val bean = (recyclerView!!.adapter as CommonAdapter).commonAdapterBeans[viewHolder.adapterPosition]
                    if (bean.workType == 1 || bean.viewType == 1) {
                        return 0
                    }
                }
                return super.getDragDirs(recyclerView, viewHolder)
            }

            override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val beans = (recyclerView!!.adapter as CommonAdapter).commonAdapterBeans
                return beans[current!!.adapterPosition].workType == beans[target!!.adapterPosition].workType
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val strings = (recyclerView.adapter as CommonAdapter).commonAdapterBeans
                val sourcePos = viewHolder.adapterPosition
                val targetPos = target.adapterPosition
                strings.add(targetPos, strings.removeAt(sourcePos))
                recyclerView.adapter?.notifyItemMoved(sourcePos, targetPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }, object : BetterItemTouchHelper.HandleEventWithXY {

            override fun handleDown(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, currRawX: Float, currRawY: Float) {
            }

            override fun handleMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, currRawX: Float, currRawY: Float) {
            }

            override fun handleUp(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, currRawX: Float, currRawY: Float) {
            }
        })
//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
//            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        }).attachToRecyclerView(rv_data)
        itemTouchHelper.attachToRecyclerView(rv_data)

    }
}
