package com.iolll.liubo.autosimple.data;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.iolll.liubo.autosimple.utils.MyUtils.DEBUG;


/**
 * Date：2018/3/19
 * Time：16:39
 * author：CH
 */

public class RxRetrofitManager {
    private static RxRetrofitManager manager;
    private Retrofit retrofit;
    private NetService server;
    /*private Context context;
    private Observable observable;
    private boolean isShowProgressDialog;
    private boolean canCancleProgressDialog;*/

    private RxRetrofitManager() {
        Interceptor appInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
//                        .addHeader("version", AppUtils.getVersionName(MyUtils.getContext()))
//                        .addHeader("token", MyApplication.tokenCode)
//                        .addHeader("os", AppDeviceUtils.getSDKVersionName())
//                        .addHeader("from", "android")
//                        .addHeader("screen", AppDeviceUtils.getScreenWidth()+"x"+AppDeviceUtils.getAllScreenHeight())
//                        .addHeader("model", AppDeviceUtils.getModel())
//                        .addHeader("net", AppDeviceUtils.getNetworkType().name())
                        .build();
                return chain.proceed(request);
            }

        };

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (DEBUG)
                    Log.i("OkHttpInterceptor----->", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        /** OKHttp默认三个超时时间是10s，有些请求时间比较长，需要重新设置下 **/
        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(Constant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(Constant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(Constant.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(appInterceptor)
                .addNetworkInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(NetService.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(NetService.class);

    }

    /**
     * 单例模式
     */
    public static RxRetrofitManager getInstance() {
        if (manager == null) {
            synchronized (RxRetrofitManager.class) {
                if (manager == null) {
                    manager = new RxRetrofitManager();
                }
            }
        }
        return manager;
    }


    public NetService getApiService() {
        return manager.server;
    }



    /*private static class SingletonHolder {
        private static RxRetrofitManager manager = new RxRetrofitManager();
    }

    public static RetrofitServer getApiService(){
        return SingletonHolder.manager.server;
    }*/


//    //设置tag取消请求标签
//    public RxRetrofitManager setTag(String tag) {
//        ApiCancleManager.getInstance().setTagValue(tag);
//        return manager;
//    }


    /*//设置context，使用弱引用防止内存泄漏
    public RxRetrofitManager with(Context mContext){
        WeakReference<Context> weakReference = new WeakReference<Context>(mContext);
        context = weakReference.get();
        return manager;
    }

    //设置是否显示加载进度框
    public RxRetrofitManager setShowProgressDialog(boolean isShowProgressDialog){
        this.isShowProgressDialog = isShowProgressDialog;
        return manager;
    }

    //设置在请求时是否可以关闭加载进度框(比如按了系统返回键等)
    public RxRetrofitManager setCancleProgressDialog(boolean canCancleProgressDialog){
        this.canCancleProgressDialog = canCancleProgressDialog;
        return manager;
    }
    //该方案打断了observable链，如果是多个请求的话就不适用了
    public RxRetrofitManager setObservableArgs(Observable observable){
        this.observable = observable;
        return manager;
    }

    public RxRetrofitManager subscribe(RxObserver<BaseResponseBean> observer){
        observer.setContext(context);
        observer.setShowProgressDialog(isShowProgressDialog);
        observer.setCanCancleProgressDialog(canCancleProgressDialog);
        observable.subscribe(observer);
        return manager;
    }*/
}
