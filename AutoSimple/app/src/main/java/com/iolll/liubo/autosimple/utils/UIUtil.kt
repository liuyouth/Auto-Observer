package com.iolll.liubo.myapplication.utils


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.iolll.liubo.autosimple.utils.MyUtils
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter

/**
 * Created by LiuBo on 2018/4/26.
 * 依赖于Utils.getContext()
 */
object UIUtil {
    fun initRecyclerview(list: RecyclerView, adapter: MultiTypeAdapter, items: Items? = Items(),
                         layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(MyUtils.getContext())) {
        adapter.items = items!!
        list.layoutManager = layoutManager
        list.adapter = adapter
    }
}
