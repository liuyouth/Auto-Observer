package com.iolll.liubo.autosimple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.blankj.utilcode.util.FileUtils
import com.iolll.liubo.autosimple.data.ApiResult
import com.iolll.liubo.autosimple.data.DataManager
import com.iolll.liubo.autosimple.ui.JavaActivity
import com.iolll.liubo.autosimple.utils.MyUtils
import com.iolll.liubo.autosimple.utils.UriToPathUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    lateinit var context:Context;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        context = this
        cjCard.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            //选择图片
//        //intent.setType(“audio/*”); //选择音频
//        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
//        //intent.setType(“video/*;image/*”);//同时选择视频和图片
//        intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CHOOSEFILE)
        }

    }
    fun startJava(view:View){
        MyUtils.startAct(JavaActivity::class.java)
    }
    fun startCJ(view:View){
//        MyUtils.startAct(CJCCCCActivity::class.java)
        //调用系统文件管理器打开指定路径目录
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        //选择图片
//        //intent.setType(“audio/*”); //选择音频
//        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
//        //intent.setType(“video/*;image/*”);//同时选择视频和图片
//        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_CHOOSEFILE)
//        DataManager.upload()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK) {
        when(requestCode){
            REQUEST_CHOOSEFILE -> {
                val url = data?.data
                val path = UriToPathUtil.getImageAbsolutePath(context,url)
                println("wwwww$path")
                var f = FileUtils.getFileByPath(path)
                DataManager.upload(f.absolutePath).subscribe(object :Observer<ApiResult<String>>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(t: ApiResult<String>) {
                        println(t.data)
                    }

                    override fun onError(e: Throwable) {
                        println(e
                                .message)
                    }
                })

            }
//                Uri uri = data . getData ();
//                chooseFilePath = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
//                Log.d(TAG, "选择文件返回：" + chooseFilePath);
//                sendFileMessage(chooseFilePath);

        }
        }
    }
    companion object {
        private val REQUEST_CHOOSEFILE = 123
    }
}
