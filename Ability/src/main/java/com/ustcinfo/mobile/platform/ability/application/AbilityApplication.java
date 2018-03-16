package com.ustcinfo.mobile.platform.ability.application;

import android.app.Application;
import android.os.Environment;

import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.ustcinfo.mobile.platform.ability.utils.ActivityLifecycleHelper;

import java.io.File;


/**
 * Created by 学祺 on 2017/10/18.
 */

public class AbilityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
        initSmallVideo();
    }

    //    //视频初始化
    public static void initSmallVideo() {
        // Set the cache path for video
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        // Initialize the shooting, encounter problems can choose to open this tag to facilitate the generation of logs
        JianXiCamera.initialize(false,null);
    }




}
