package com.iolll.liubo.autosimple.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;

import com.iolll.liubo.autosimple.R;
import com.iolll.liubo.autosimple.compile.autoObserver.AutoListListener;
import com.iolll.liubo.autosimple.data.ApiResult;
import com.iolll.liubo.autosimple.data.DataManager;
import com.iolll.liubo.autosimple.data.echo.EchoSearchViewBinder;
import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;
import com.iolll.liubo.myapplication.utils.UIUtil;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
    private Context context;
    private MultiTypeAdapter adapter = new MultiTypeAdapter();
    private Items items = new Items();
    private String keyword = "";
    private AutoListListener<ApiResult<ArrayList<EchoSearch>>> echoSearchByNameObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        context = this;
        ButterKnife.bind(this);
        initView();
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
                .setOnLoadMoreListener(refreshLayout -> getData(false));
        echoSearchByNameObserver = new AutoListListener.Builder<ApiResult<ArrayList<EchoSearch>>>()
                .setAutoSwitchStatusPageObserver(LoadSir.getDefault().register(smartRefreshLayout, v -> getData()))
                .setAutoRefreshObserver(smartRefreshLayout)
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

    private void getData() {
        getData(null);
    }

    private void getData(Boolean isRefresh) {
        DataManager.echoSearchByName(keyword, echoSearchByNameObserver.getPage())
//                .compose(provider.bindToLifecycle())
                .subscribe(echoSearchByNameObserver.setRefresh(isRefresh));
    }
}