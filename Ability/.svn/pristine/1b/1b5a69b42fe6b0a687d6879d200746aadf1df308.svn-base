package com.ustcinfo.mobile.platform.ability.utils;

/**
 * Created by Administrator on 2018/1/2.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import static android.content.Context.WIFI_SERVICE;

/**
 * 获取wifi信息
 */
public class WifiUtils {
    public static JSONObject getWifiInfor(Context context) {
        //获取当前连接的wifi信息
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        JSONObject jsonObject = new JSONObject();
        boolean isAviable=isWifiConnected(context);
        if (isAviable == false) {
            jsonObject.put("name", "亲，请连接wifi");
            jsonObject.put("mac", "亲，请连接wifi");
            jsonObject.put("errCode", "-1");
        } else if (wifiInfo!=null){
            //获取MAC地址。
            jsonObject.put("name", wifiInfo.getSSID());
            jsonObject.put("mac", wifiInfo.getMacAddress());
            jsonObject.put("errCode", "0");
        }
        Log.i("infor","wifi名称:"+jsonObject.getString("name"));
        return jsonObject;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected())
            return true;
        return false;
    }
}
