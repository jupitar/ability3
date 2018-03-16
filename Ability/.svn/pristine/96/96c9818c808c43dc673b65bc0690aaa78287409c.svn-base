package com.ustcinfo.mobile.platform.ability.qrcode.utils;

/**
 * Created by Administrator on 2017/12/27.
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理时间的类
 */
public class TimeFormatUtils {
    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String s2HMS(long  s){
        if(s==0)
            return "";
        String HMStime;
        long  hour=s/3600;
       long mint=(s%3600)/60;
      long  sed=s%60;
        String hourStr=String.valueOf(hour);
        if(hour<10){
            hourStr="0"+hourStr;
        }

        String mintStr=String.valueOf(mint);
        if(mint<10){
            mintStr="0"+mintStr;
        }
        String sedStr=String.valueOf(sed);
        if(sed<10){
            sedStr="0"+sedStr;
        }
        if(hour==0){
            HMStime=mintStr+":"+sedStr;
        }else{
            HMStime=hourStr+":"+mintStr+":"+sedStr;
        }

        return HMStime;
    }


}
