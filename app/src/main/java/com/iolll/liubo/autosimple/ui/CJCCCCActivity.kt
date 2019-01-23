package com.iolll.liubo.autosimple.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iolll.liubo.autosimple.R
import com.iolll.liubo.autosimple.data.cj.SelectBean
import com.iolll.liubo.autosimple.data.cj.SelectViewBinder
import com.iolll.liubo.myapplication.utils.UIUtil
import kotlinx.android.synthetic.main.activity_cjcccc.*
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter

class CJCCCCActivity : AppCompatActivity() {
    val adapter :MultiTypeAdapter = MultiTypeAdapter()
    val items:Items= Items()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cjcccc)
        for (i in  1..10){
            items.add(SelectBean(i.toString(),false))
        }
        adapter.register(SelectBean::class.java,SelectViewBinder())
        UIUtil.initRecyclerview(list,adapter,items)
    }


}
