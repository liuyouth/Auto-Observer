package com.iolll.liubo.autoobserver;


import android.util.Log;

import com.iolll.liubo.autoobserver.exception.ApiException;
import com.iolll.liubo.autoobserver.exception.HTTPCODE;
import com.iolll.liubo.autoobserver.functions.Action;
import com.iolll.liubo.autoobserver.functions.Consumer;
import com.iolll.liubo.autoobserver.functions.OnSubscribe;
import com.iolll.liubo.autoobserver.listener.AutoDialogListener;
import com.iolll.liubo.autoobserver.listener.AutoPageData;
import com.iolll.liubo.autoobserver.listener.AutoRefreshListener;
import com.iolll.liubo.autoobserver.listener.AutoSwitchStatusPageListener;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;

import static android.content.ContentValues.TAG;
import static com.iolll.liubo.autoobserver.utils.MyUtils.handlefilter;
import static com.iolll.liubo.autoobserver.utils.MyUtils.isNotNull;
import static com.iolll.liubo.autoobserver.utils.MyUtils.isNull;
import static com.iolll.liubo.autoobserver.utils.MyUtils.isNullDefault;


/**
 * 列表自动观察者
 * Created by LiuBo on 2018/6/15.
 */
public class AutoListObserver<T> implements Observer<T> {
    boolean isDebug = false;  // 是否打印log
    private String tag = "Default"; // 状态页的图片和文字分发 标识
    private int page; // 页码
    private boolean isRefresh = false; // 是否是刷新模式
    private Items items; // 列表数据总数
    private AutoPageData autoPageData;
    private Consumer<? super T> onSuccess; // OnNext 开始时的回调
    private Consumer<? super T> onRefresh; // OnNext 中 刷新回调
    private Consumer<? super T> onLoadMore; // OnNext 中 加载回调
    private Consumer<? super T> onComplete; // OnNext 完成前的回调
    private Consumer<? super ApiException> onFailed; // 失败回调 一般为业务失败 即链接正常 http状态码为 200 的情况下 返回前后台交互的 Result 结构 一般包含 status/code 状态值 msg/message 信息说明 data 数据
    private Consumer<? super ApiException> onErrors; // 错误回调 一般为 网络连接错误 超时 服务器异常 400 405 500 等 此处指的是 http 状态码 不是业务状态码
    private Action onEnd; // 结束回调 无论什么情况在最后结束时候 都会走的 onError  onComplete 都会在结束前触发该回调
    private OnSubscribe onSubscribe; // 订阅前回调 根据这里的返回值来判断 是否要开始订阅
    private AutoRefreshListener autoRefreshObserver; // 列表自动刷新加载观察者
    private AutoSwitchStatusPageListener autoSwitchStatusPageObserver; // 状态页自动切换观察者
    private AutoDialogListener autoDialogObserver; // 弹框观察者  正在施工中...


    //    setOnErrors(isNullDefault(builder.onErrors, e -> defaultFailed()));
//    setOnErrors(isNullDefault(builder.onFailed, e -> defaultFailed()));
//
    private AutoListObserver(Builder<T> builder) {
        setPage(builder.page);
        setItems(builder.items);
        setOnEnd(builder.onEnd);
        setRefresh(builder.isRefresh);
        setOnRefresh(builder.onRefresh);
        setOnSuccess(builder.onSuccess);
        setOnComplete(builder.onComplete);
        setOnLoadMore(builder.onLoadMore);
        setOnSubscribe(builder.onSubscribe);
        setAutoPageData(builder.autoPageData);
        setAutoDialogObserver(builder.autoDialogObserver);
        setAutoRefreshObserver(builder.autoRefreshObserver);
        setOnErrors((Consumer<? super ApiException>) isNullDefault(builder.onErrors, new Consumer<ApiException>() {
            @Override
            public void accept(ApiException o) {
                defaultErrors();
            }
        }));
        setOnFailed((Consumer<? super ApiException>) isNullDefault(builder.onFailed, new Consumer<ApiException>() {
            @Override
            public void accept(ApiException o) {
                defaultFailed();
            }
        }));
        setAutoSwitchStatusPageObserver(builder.autoSwitchStatusPageObserver);
    }


    @Override
    public void onSubscribe(Disposable d) {
        if (isDebug)
            System.out.println("onSubscribe");

        if (isNotNull(autoDialogObserver))
            autoDialogObserver.show();
        if (handlefilter(null == getItems(), "items is not set"))
            if (isNotNull(autoSwitchStatusPageObserver) && 0 == getItems().size())
                autoSwitchStatusPageObserver.onLoading();
        if (isNotNull(onSubscribe))
            if (onSubscribe.accept(d)) { // return true 则 断掉链接 绑定
                d.dispose();
                if (isNotNull(autoDialogObserver))
                    autoDialogObserver.dismiss();
            }
    }

    @Override
    public void onNext(T t) {
        if (isDebug)
            Log.i(TAG, "onNext: ");
        // 成功回调 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        if (isNotNull(onSuccess))
            onSuccess.accept(t);
        // 成功回调 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        // 自动列表部分 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        if (isNotNull(autoRefreshObserver)) {
            page++;
            if (isRefresh) {
                if (handlefilter(isNull(onRefresh), "")) {
                    onRefresh.accept(t);
                }
                // Todo 如果是只有一页的情况 需要从刷新处禁掉 下拉情况 如果 有多页那么需要从这里开启多页  需要从外部带新数据过来 根据数据来判断是否可以
                if (isNotNull(autoPageData)) {
                    autoRefreshObserver.finishRefresh(true, autoPageData.isHasNext());
                } else {
                    autoRefreshObserver.finishRefresh();
                }
            } else {
                if (handlefilter(isNull(onLoadMore), ""))
                    onLoadMore.accept(t);
                if (isNotNull(autoPageData)) {
                    autoRefreshObserver.finishLoadMore(true, autoPageData.isHasNext());
                } else {
                    autoRefreshObserver.finishLoadMore();
                }
            }
        } else {// 不用刷新加载部分
            if (isDebug)
                System.out.println(" smartRefreshLayout  is not set");
        }
        // 自动列表部分 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        //完成回调  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        if (handlefilter(isNull(onComplete), ""))
            onComplete.accept(t);
        //完成回调  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        // 自动切换状态页部分 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        if (isNotNull(autoSwitchStatusPageObserver))
            autoSwitchStatusPageObserver.onSuccess();
        if (isNotNull(items, autoSwitchStatusPageObserver))
            if (items.size() == 0)
                autoSwitchStatusPageObserver.onEmpty();
        // 自动切换状态页部分  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    }

    /**
     * 由于是针对于列表定制的观察者 所以 异常情况没那么多了
     * 1. 本地无网络
     * 2. 网络超时
     * 3. 后台无响应
     * 4. 返回数据为空 null
     * 5. token 过期
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (isDebug)
            Log.e(TAG, "onError: ", null);
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            //分发业务失败和链接错误
            switch (apiException.getCode()) {
                case HTTPCODE.NULL_ERROR:
                    if (isNotNull(onFailed))
                        onFailed.accept(apiException);
                    break;
                default:
                    if (isNotNull(onErrors))
                        onErrors.accept(apiException);
                    break;
            }
        } else if (isNotNull(onErrors))
            onErrors.accept(new ApiException(e, HTTPCODE.UNINTERCEPT));

        if (isNotNull(onEnd))
            onEnd.run();
        if (isNotNull(autoRefreshObserver))
            if (1 != page)
                page--;
        if (isNotNull(autoDialogObserver))
            autoDialogObserver.dismiss();
    }

    @Override
    public void onComplete() {
        if (isDebug)
            System.out.println("onComplete");
        if (null != onEnd)
            onEnd.run();
        if (isNotNull(autoDialogObserver))
            autoDialogObserver.dismiss();
    }


    public Consumer<? super ApiException> getOnFailed() {
        return onFailed;
    }

    public AutoListObserver<T> setOnFailed(Consumer<? super ApiException> onFailed) {
        this.onFailed = onFailed;
        return this;
    }

    public Consumer<? super ApiException> getOnErrors() {
        return onErrors;
    }

    public AutoListObserver<T> setOnErrors(Consumer<? super ApiException> onErrors) {
        this.onErrors = onErrors;
        return this;
    }

    public AutoPageData getAutoPageData() {
        return autoPageData;
    }

    public AutoDialogListener getAutoDialogObserver() {
        return autoDialogObserver;
    }

    public void setAutoDialogObserver(AutoDialogListener autoDialogObserver) {
        this.autoDialogObserver = autoDialogObserver;
    }

    public void setAutoPageData(AutoPageData autoPageData) {
        this.autoPageData = autoPageData;
    }

    public Action getOnEnd() {
        return onEnd;
    }

    public AutoListObserver<T> setOnEnd(Action onEnd) {
        this.onEnd = onEnd;
        return this;
    }

    public int getPage() {
        return page;
    }

    public AutoListObserver<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(Boolean refresh) {
        if (isNotNull(refresh)) {
            isRefresh = refresh;
            if (isRefresh)
                page = 1;
        } else
            isRefresh = false;
    }

    public Items getItems() {
        return items;
    }

    public AutoListObserver<T> setItems(Items items) {
        this.items = items;
        return this;
    }

    public Consumer<? super T> getOnRefresh() {
        return onRefresh;
    }

    public AutoListObserver<T> setOnRefresh(Consumer<? super T> onRefresh) {
        this.onRefresh = onRefresh;
        return this;
    }

    public Consumer<? super T> getOnLoadMore() {
        return onLoadMore;
    }

    public AutoListObserver<T> setOnLoadMore(Consumer<? super T> onLoadMore) {
        this.onLoadMore = onLoadMore;
        return this;
    }

    public Consumer<? super T> getOnComplete() {
        return onComplete;
    }

    public AutoListObserver<T> setOnComplete(Consumer<? super T> onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public Consumer<? super T> getOnSuccess() {
        return onSuccess;
    }

    public AutoListObserver<T> setOnSuccess(Consumer<? super T> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public AutoListObserver<T> setAutoRefreshObserver(AutoRefreshListener autoRefreshObserver) {
        this.autoRefreshObserver = autoRefreshObserver;
        return this;
    }

    public OnSubscribe getOnSubscribe() {
        return onSubscribe;
    }

    public void setOnSubscribe(OnSubscribe onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public AutoRefreshListener getAutoRefreshObserver() {
        return autoRefreshObserver;
    }

    public AutoSwitchStatusPageListener getAutoSwitchStatusPageObserver() {
        return autoSwitchStatusPageObserver;
    }

    public AutoListObserver<T> setAutoSwitchStatusPageObserver(AutoSwitchStatusPageListener autoSwitchStatusPageObserver) {
        this.autoSwitchStatusPageObserver = autoSwitchStatusPageObserver;
        return this;
    }

    public String getTAG() {
        return tag;
    }

    public void setTAG(String tag) {
        this.tag = tag;
    }

    public static final class Builder<T> {
        private String tag = "Default";
        private boolean isDebug;
        private int page = 1;
        private boolean isRefresh = true;
        private Items items = new Items();
        private AutoPageData autoPageData;
        private Consumer<? super T> onRefresh;
        private Consumer<? super T> onLoadMore;
        private Consumer<? super T> onComplete;
        private OnSubscribe onSubscribe;
        private AutoRefreshListener autoRefreshObserver;
        private AutoSwitchStatusPageListener autoSwitchStatusPageObserver;
        private AutoDialogListener autoDialogObserver;
        private Consumer<? super ApiException> onFailed;
        private Consumer<? super ApiException> onErrors;
        private Action onEnd;
        private Consumer<? super T> onSuccess;


        public Builder() {
        }

        public Builder(String tag) {
            this.tag = tag;
        }

        public Builder<T> setPage(int val) {
            page = val;
            return this;
        }

        public Builder setIsRefresh(boolean val) {
            isRefresh = val;
            return this;
        }


        public Builder<T> setRefresh(boolean val) {
            isRefresh = val;
            return this;
        }

        public Builder<T> setItems(Items val) {
            items = val;
            return this;
        }

        public Builder setAutoPageData(AutoPageData val) {
            autoPageData = val;
            return this;
        }

        public Builder setOnSubscribe(OnSubscribe val) {
            onSubscribe = val;
            return this;
        }

        public Builder setOnFailed(Consumer<? super ApiException> val) {
            onFailed = val;
            return this;
        }

        public Builder setOnErrors(Consumer<? super ApiException> val) {
            onErrors = val;
            return this;
        }

        public Builder setOnEnd(Action val) {
            onEnd = val;
            return this;
        }

        public Builder<T> onRefresh(Consumer<? super T> val) {
            onRefresh = val;
            return this;
        }

        public Builder<T> onLoadMore(Consumer<? super T> val) {
            onLoadMore = val;
            return this;
        }

        public Builder<T> onComplete(Consumer<? super T> val) {
            onComplete = val;
            return this;
        }

        public Builder<T> setAutoRefreshObserver(AutoRefreshListener val) {
            autoRefreshObserver = val;
            return this;
        }

        public Builder<T> setAutoSwitchStatusPageObserver(AutoSwitchStatusPageListener val) {
            autoSwitchStatusPageObserver = val;
            return this;
        }

        public Builder<T> setAutoDialogObserver(AutoDialogListener val) {
            val.setCanceledOnTouchOutside(false);
            autoDialogObserver = val;
            return this;
        }


        public AutoListObserver<T> build() {
            return new AutoListObserver<T>(this);
        }

        public Builder<T> onFailed(Consumer<? super ApiException> val) {
            onFailed = val;
            return this;
        }

        public Builder<T> onErrors(Consumer<? super ApiException> val) {
            onErrors = val;
            return this;
        }

        public Builder<T> onEnd(Action val) {
            onEnd = val;
            return this;
        }

        public Builder<T> onSuccess(Consumer<? super T> val) {
            onSuccess = val;
            return this;
        }

        public Builder setTag(String val) {
            tag = val;
            return this;
        }

        public Builder setIsDebug(boolean val) {
            isDebug = val;
            return this;
        }
    }

    /**
     * 默认失败处理
     */
    private void defaultFailed() {
        if (handlefilter(null == getItems(), "items is not set"))
            if (0 == getItems().size()) {
                if (isNotNull(autoSwitchStatusPageObserver))
                    autoSwitchStatusPageObserver.onEmpty();
            } else {
                if (isNotNull(autoRefreshObserver)) {
                    if (isRefresh) {
                        autoRefreshObserver.finishRefresh(false);
                    } else {
                        autoRefreshObserver.finishLoadMore(false);
                    }
                }
            }
    }

    /**
     * 默认错误处理
     * 调用 @see onErrors 方法会导致此方法失效
     */
    private void defaultErrors() {
        if (handlefilter(null == getItems(), "items is not set")) {
            // 数据量 不为0时 可能是 刷新 也可能是 加载 故此处判断如果数据量为0时才去显示 异常 反之不显示
            if (0 == getItems().size()) {
                if (isNotNull(autoSwitchStatusPageObserver))
                    autoSwitchStatusPageObserver.onError();
            }
            if (isNotNull(autoRefreshObserver)) {
                if (isRefresh) {
                    autoRefreshObserver.finishRefresh(false);
                } else {
                    autoRefreshObserver.finishLoadMore(false);
                }
            }

        }
    }


}
