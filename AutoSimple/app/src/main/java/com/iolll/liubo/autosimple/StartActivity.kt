package com.iolll.liubo.autosimple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.iolll.liubo.autosimple.ui.JavaActivity
import com.iolll.liubo.autosimple.utils.MyUtils

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }
    fun startJava(view:View){
        MyUtils.startAct(JavaActivity::class.java)
    }
}
