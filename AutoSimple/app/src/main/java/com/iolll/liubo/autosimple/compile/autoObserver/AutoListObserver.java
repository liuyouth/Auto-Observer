package com.iolll.liubo.autosimple.compile.autoObserver;


import com.iolll.liubo.autosimple.compile.autoObserver.exception.ApiException;
import com.iolll.liubo.autosimple.compile.autoObserver.exception.HTTPCODE;
import com.iolll.liubo.autosimple.compile.autoObserver.functions.Action;
import com.iolll.liubo.autosimple.compile.autoObserver.functions.Consumer;
import com.kingja.loadsir.core.LoadService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;

import static com.iolll.liubo.autosimple.utils.MyUtils.handlefilter;
import static com.iolll.liubo.autosimple.utils.MyUtils.isNotNull;
import static com.iolll.liubo.autosimple.utils.MyUtils.isNull;
import static com.iolll.liubo.autosimple.utils.MyUtils.isNullDefault;


/**
 * 列表自动观察者
 * Created by LiuBo on 2018/6/15.
 */
public class AutoListObserver<T> implements Observer<T> {
    boolean isDebug = true;
    private int page;
    private boolean isRefresh = false;
    private Items items;
    private Consumer<? super T> onSuccess;
    private Consumer<? super T> onRefresh;
    private Consumer<? super T> onLoadMore;
    private Consumer<? super T> onComplete;
    private Consumer<? super ApiException> onFailed;
    private Consumer<? super ApiException> onErrors;
    private Action onEnd;
    private AutoRefreshListener autoRefreshObserver;
    private AutoSwitchStatusPageListener autoSwitchStatusPageObserver;

    //    setPage(builder.page);
//    setItems(builder.items);
//    setOnEnd(builder.onEnd);
//    setRefresh(builder.isRefresh);
//    setOnRefresh(builder.onRefresh);
//    setOnComplete(builder.onComplete);
//    setOnLoadMore(builder.onLoadMore);
//    setAutoRefreshObserver(builder.autoRefreshObserver);
//    setOnErrors(isNullDefault(builder.onErrors, e -> defaultFailed()));
//    setOnErrors(isNullDefault(builder.onFailed, e -> defaultFailed()));
//    setAutoSwitchStatusPageObserver(builder.autoSwitchStatusPageObserver);
    private AutoListObserver(Builder<T> builder) {
        setPage(builder.page);
        setItems(builder.items);
        setOnEnd(builder.onEnd);
        setRefresh(builder.isRefresh);
        setOnRefresh(builder.onRefresh);
        setOnSuccess(builder.onSuccess);
        setOnComplete(builder.onComplete);
        setOnLoadMore(builder.onLoadMore);
        setAutoRefreshObserver(builder.autoRefreshObserver);
        setOnErrors(isNullDefault(builder.onErrors, e -> defaultErrors()));
        setOnFailed(isNullDefault(builder.onFailed, e -> defaultFailed()));
        setAutoSwitchStatusPageObserver(builder.autoSwitchStatusPageObserver);
    }


    @Override
    public void onSubscribe(Disposable d) {
        if (isDebug)
            System.out.println("onSubscribe");
        if (handlefilter(null == getItems(), "items is not set"))
            if (isNotNull(autoSwitchStatusPageObserver) && 0 == getItems().size())
                autoSwitchStatusPageObserver.onLoading();
    }

    @Override
    public void onNext(T t) {
        if (isDebug)
            System.out.println("onNext");
        if (isNotNull(onSuccess))
            onSuccess.accept(t);
        if (isNotNull(autoRefreshObserver))
            page++;
        if (isRefresh) {
            if (handlefilter(isNull(onRefresh), "")) {
                onRefresh.accept(t);
            }
            if (handlefilter(isNull(autoRefreshObserver), "smartRefreshLayout  is not set"))
                autoRefreshObserver.finishRefresh();
        } else {
            if (handlefilter(isNull(onLoadMore), "")) {
                onLoadMore.accept(t);

            }
            if (handlefilter(isNull(autoRefreshObserver), "smartRefreshLayout  is not set"))
                autoRefreshObserver.finishLoadMore();
        }
        if (handlefilter(isNull(onComplete), ""))
            onComplete.accept(t);
        if (isNotNull(autoSwitchStatusPageObserver))
            autoSwitchStatusPageObserver.onSuccess();
    }

    @Override
    public void onError(Throwable e) {
        if (isDebug)
            System.out.println("onError");
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            //分发业务错误和链接错误
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
    }

    @Override
    public void onComplete() {
        if (isDebug)
            System.out.println("onComplete");
        if (null != onEnd)
            onEnd.run();
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

    public AutoListObserver<T> setRefresh(Boolean refresh) {
        if (isNotNull(refresh))
            isRefresh = refresh;
        return this;
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

    public static final class Builder<T> {
        private int page = 1;
        private boolean isRefresh = true;
        private Items items = new Items();
        private Consumer<? super T> onRefresh;
        private Consumer<? super T> onLoadMore;
        private Consumer<? super T> onComplete;
        private AutoRefreshListener autoRefreshObserver;
        private AutoSwitchStatusPageListener autoSwitchStatusPageObserver;
        private Consumer<? super ApiException> onFailed;
        private Consumer<? super ApiException> onErrors;
        private Action onEnd;
        private Consumer<? super T> onSuccess;

        public Builder() {
        }

        public Builder<T> setPage(int val) {
            page = val;
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


        public Builder<T> setAutoRefreshObserver(RefreshLayout val) {
            autoRefreshObserver = new AutoSmartRefreshLayoutListener(val);
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

        public Builder<T> setAutoSwitchStatusPageObserver(LoadService val) {
            autoSwitchStatusPageObserver = new AutoLoadSirPageListener(val);
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
     * 调用 @seeonErrors 方法会导致此方法失效
     */
    private void defaultErrors() {
        if (handlefilter(null == getItems(), "items is not set"))
            if (0 == getItems().size()) {
                if (isNotNull(autoSwitchStatusPageObserver))
                    autoSwitchStatusPageObserver.onError();
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

}
