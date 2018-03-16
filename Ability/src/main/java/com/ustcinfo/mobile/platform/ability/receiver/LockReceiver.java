package com.ustcinfo.mobile.platform.ability.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class LockReceiver extends DeviceAdminReceiver {
    private String TAG = "LockReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("", "接收");
        super.onReceive(context, intent);
    }
    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i(TAG, "激活");
        super.onEnabled(context, intent);
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i(TAG, "取消");
        super.onDisabled(context, intent);
    }

}
