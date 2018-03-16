package com.ustcinfo.mobile.platform.ability.utils;

/**
 * Created by Administrator on 2018/1/2.
 */

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取包名
 */
public class PackageUtils {
    public static  JSONObject getAppInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            String pkName = context.getPackageName();
            jsonObject.put("data",pkName);
            jsonObject.put("errCode","0");
            return jsonObject;
        } catch (Exception e) {
            jsonObject.put("data","获取包名失败");
            jsonObject.put("errCode","-1");
        }
        return jsonObject;
    }
}
