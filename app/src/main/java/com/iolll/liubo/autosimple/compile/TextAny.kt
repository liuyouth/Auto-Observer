package com.iolll.liubo.autosimple.compile

import com.iolll.liubo.autosimple.data.cj.SelectBean

/**
 * Created by LiuBo on 2018/11/1.
 */


fun main(args: Array<String>) {
    var selectBean:SelectBean = SelectBean("true",true)
    var selectBeanNul:SelectBean ? = null
    println("start    :$selectBean")
    selectBean.withNotNull {
        println("notNull $isSelect")
    }
    selectBeanNul.withNotNull {
        println("2222"+ this!!.isSelect)
    }.withNotNull {
        println("2222"+this!!.isSelect)
    }.withNull {
        println("null")
    }
    selectBean.also {

    }
    selectBean.run {

    }
    selectBean.apply {
        print(name+isSelect)
    }
//    selectBean.apply {  }.also {  }
//    selectBean.withNotNull{
//        println(isSelect)
//        isSelect = false
//    }.withNotNull {
//        println("wweqqq")
////        println(isSelect)
//    }.withNull {
//        println("www")
//    }.run2 {
//        println("run2")
//    }

//    selectBean.withNull {
//        println(" null ")
//    }

}