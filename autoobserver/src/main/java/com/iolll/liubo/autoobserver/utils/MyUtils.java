package com.iolll.liubo.autoobserver.utils;


import android.util.Log;

import static android.text.TextUtils.isEmpty;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 */
public class MyUtils {

    /**
     * 仅仅用于单层结构  ，嵌套结构不可使用 切记
     *
     * @param objects
     * @return
     */
    public static boolean isNotNull(Object... objects) {
        return !isNull(objects);
    }

    public static boolean isNull(Object... objects) {
        for (Object o : objects) {
            if (null == o)
                return true;
        }
        return false;
    }


    public static <D> D isNullDefault(D appoint, D defaultObject) {
        return isNotNull(appoint) ? appoint : defaultObject;
    }

    /**
     * 过滤数据，并且在界面提示信息
     *
     * @param isShow 是否过滤该数据  true  丢弃该数据并弹出提示
     * @param msg    为"" 时不予以展示
     * @return !isShow
     */
    public static boolean handlefilter(boolean isShow, String msg) {
        if (!isEmpty(msg))
            if (isShow)
                Log.e(TAG, "handlefilter: "+ msg,null );
        return !isShow;
    }

    private static final String TAG = "AutoObserver";

}