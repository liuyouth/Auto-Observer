package com.iolll.liubo.autosimple.ui;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.iolll.liubo.autoobserver.AutoListObserver;
import com.iolll.liubo.autosimple.R;
import com.iolll.liubo.autosimple.compile.autoObserver.AutoLoadSirPageListener;
import com.iolll.liubo.autosimple.compile.autoObserver.AutoSmartRefreshLayoutListener;
import com.iolll.liubo.autosimple.data.ApiResult;
import com.iolll.liubo.autosimple.data.DataManager;
import com.iolll.liubo.autosimple.data.echo.EchoSearchViewBinder;
import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;
import com.iolll.liubo.myapplication.utils.UIUtil;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class JavaActivity extends AppCompatActivity {

    @BindView(R.id.searchEdit)
    EditText searchEdit;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.backLayout)
    LinearLayout backLayout;
    private Context context;
    private MultiTypeAdapter adapter = new MultiTypeAdapter();
    private Items items = new Items();
    private String keyword = "";
    private AutoListObserver<ApiResult<ArrayList<EchoSearch>>> echoSearchByNameObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.adaptScreen4VerticalSlide(this, 360);
        setContentView(R.layout.activity_java);
        context = this;
        ButterKnife.bind(this);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath())), "file/*.txt");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        //intent.setType(“image/*”);//选择图片
//        //intent.setType(“audio/*”); //选择音频
//        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
//        //intent.setType(“video/*;image/*”);//同时选择视频和图片
//        intent.setType("*/*");//无类型限制
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 222);
        initView();
    }
    public void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void initView() {
        adapter.register(EchoSearch.class, new EchoSearchViewBinder());
        UIUtil.INSTANCE.initRecyclerview(list, adapter, items, new LinearLayoutManager(context));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> getData(true))
                .setOnLoadMoreListener(refreshLayout -> getData());
        AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this));
        echoSearchByNameObserver = new AutoListObserver.Builder<ApiResult<ArrayList<EchoSearch>>>()
                .setAutoSwitchStatusPageObserver(new AutoLoadSirPageListener(LoadSir.getDefault().register(smartRefreshLayout, v -> getData())))
                .setAutoRefreshObserver(new AutoSmartRefreshLayoutListener(smartRefreshLayout))
                .setItems(items)
                .onRefresh(arrayListApiResult -> items.clear())
                .onComplete(arrayListApiResult -> {
                    items.addAll(arrayListApiResult.getData());
                    adapter.notifyDataSetChanged();


                })
                .build();

        searchEdit.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_SEARCH) {
                keyword = searchEdit.getText().toString();
                getData(true);
            }
            return false;
        });
    }

    private void getData() { // 默认为 加载模式 因为加载比较常用  刷新只是 在 上啦数据 或者 重新搜索时
        getData(false);
        requestPermission(0X01, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    private void getData(boolean isRefresh) {
        echoSearchByNameObserver.setRefresh(isRefresh);
        DataManager.echoSearchByName(keyword, echoSearchByNameObserver.getPage())
//                .compose(provider.bindToLifecycle())
                .subscribe(echoSearchByNameObserver);
    }
}