package com.iolll.liubo.autosimple.compile.loadSirCallback;

import com.iolll.liubo.autosimple.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuBo on 2018/7/25.
 */

public class StatusSetting {
    public static Map<String, EmptyBean> emptyMap = new HashMap<String, EmptyBean>() {
        {
            put("Default", new EmptyBean("没有数据", R.drawable.empty));
//            put("CertificatesListActivity", new EmptyBean("一定要添加证件信息，不然怎么订票", R.drawable.credentials_nothing_normal));
//            put("HotelDetailActivity", new EmptyBean("很抱歉，当前日期已售完", R.mipmap.hotel_soldout_noroom));
        }
    };

    public static class EmptyBean {
        public String msg;
        public String title;
        public int imgId;

        public EmptyBean(String msg, int imgId) {
            this.msg = msg;
            this.imgId = imgId;
        }
    }
    public enum EmptyStatus{
        DEFAULT,
    }
}