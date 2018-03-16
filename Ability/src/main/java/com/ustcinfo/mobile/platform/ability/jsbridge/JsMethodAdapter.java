package com.ustcinfo.mobile.platform.ability.jsbridge;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.database.database.DataBaseManager;
import com.database.database.SqliteDatabase;
import com.database.interfaces.SqliteInitICallBack;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;
import com.sdsmdg.tastytoast.TastyToast;
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.event.WebViewEvent;
import com.ustcinfo.mobile.platform.ability.map.LocationHelper;
import com.ustcinfo.mobile.platform.ability.qrcode.CaptureActivity;
import com.ustcinfo.mobile.platform.ability.qrcode.PlayVoiceActivity;
import com.ustcinfo.mobile.platform.ability.qrcode.RecordVoiceActivity;
import com.ustcinfo.mobile.platform.ability.qrcode.SendSmallVideoActivity;
import com.ustcinfo.mobile.platform.ability.qrcode.VideoPlayerActivity;
import com.ustcinfo.mobile.platform.ability.receiver.LockReceiver;
import com.ustcinfo.mobile.platform.ability.utils.Constants;
import com.ustcinfo.mobile.platform.ability.utils.PackageUtils;
import com.ustcinfo.mobile.platform.ability.utils.Path;
import com.ustcinfo.mobile.platform.ability.utils.WifiUtils;
import com.ustcinfo.mobile.platform.ability.widgets.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import rx.Observable;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lxq on 2017/10/26.
 */

public class JsMethodAdapter {

    private static JsMethodAdapter mInstance;
    private BridgeWebView webView;
    public static final int SUCCESS = 0;
    public static final int FAILD = 1;
    public static final String CODE = "CODE";
    private static final int REQUEST_TAKE_PHOTO = 0x111;
    private static final int REQUEST_PICK_PHOTO = 0x112;
    private static final int REQUEST_CODE = 0x113;
    private static final int ADDRESS_BOOK = 0x114;
    private static final int RECORD_VOICE=0x115;
    private CallBackFunction callBackFunction;
    private String photoPath;
    private JSONObject json;
    private SqliteDatabase database;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private Activity mActivty;

    private JsMethodAdapter(BridgeWebView webView) {
        this.webView = webView;
        this.mActivty = (Activity)webView.getContext();
    }
    public static JsMethodAdapter getmInstance() {
        return mInstance;
    }

    public static void register(BridgeWebView webView) {
        if (mInstance == null) {
            mInstance = new JsMethodAdapter(webView);
        }
        mInstance.init();
    }

    public static void unRegister() {
        mInstance = null;
    }

    private void init() {
        registerGetUserInfos();
        registerTakePhotos();
        registerBarCode();
        registerPickPic();
        registerShowLoading();
        registerCancelLoading();
        registerTelephoneCall();
        registerGetLocationInfo();
        registerShortMessage();
        registerAddressBook();
        registerGetPhoneDeviceName();
        registerCreateTable();
        registerInsertInfo();
        registerSelectInfos();
        registerdeleteInfo();
        registerUpdateInfo();
        registerDropTable();
        registerLockScreen();
        registerScreenShot();
        registerChangeScreenIntensity();
        //录音
        registerRecordVoice();
        //播放
        registerPlayVoice();
        //上传
        registerUploadVoice();
        //录制视频
        registerRecordVideo();
        registerPlayVideo();
        registerGetPackInfor();
        registerGetWifiInfor();
        registerShareTest();
    }

    /**
     * 获取手机型号（设备名称）
     */
    private void registerGetPhoneDeviceName(/*int i*/) {
        webView.registerHandler("getPhoneDeviceName", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(android.os.Build.MODEL);

            }
        });
    }

    /**
     * 通讯录
     */
    private void registerAddressBook() {
        webView.registerHandler("addressBook", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction = function;
                //进入系统通訊錄頁面
                Uri uri = ContactsContract.Contacts.CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                ((Activity) webView.getContext()).startActivityForResult(intent, ADDRESS_BOOK);

            }
        });
    }

    /**
     * 短信
     */
    private void registerShortMessage() {
        webView.registerHandler("shortMessage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String targetNum = json.getString("telNum");
                String smsBody = json.getString("smsBody");
                //进入系统短信列表界面
                Uri smsToUri = Uri.parse("smsto:" + targetNum);
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("sms_body", smsBody);
                webView.getContext().startActivity(intent);
            }
        });
    }

    /**
     * getUserInfos 获取用户信息
     */
    private void registerGetUserInfos() {
        webView.registerHandler("getUserInfos", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                WebViewEvent event = new WebViewEvent();
                event.webView = webView;
                event.key = "getUserInfos";
                event.callBackFunction = function;
                EventBus.getDefault().post(event);
            }
        });
    }

    /**
     * 定位
     */
    private void registerGetLocationInfo() {
        webView.registerHandler("getLocationInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LocationHelper.getInstance(webView.getContext(), function).startLocation();
            }
        });
    }

    /**
     * 拍照
     */
    private void registerTakePhotos() {
        webView.registerHandler("takePhoto", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                callBackFunction = function;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG);
                if (!file.exists())
                    file.mkdirs();// 创建文件夹
                photoPath = file.getAbsolutePath() + "/" + json.getString("keyword") + "_" + System.currentTimeMillis() + ".jpg";
                Uri uri = Uri.fromFile(new File(photoPath));
                //为拍摄的图片指定一个存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                ((Activity) webView.getContext()).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });
    }

    /**
     * 从相册选照片
     */
    private void registerPickPic() {
        webView.registerHandler("pickPic", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                callBackFunction = function;
                File file = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG);
                if (!file.exists())
                    file.mkdirs();// 创建文件夹
                MultiImageSelector.create()
                        .showCamera(false)
                        .count(json.getIntValue("picNum"))
                        .start((Activity) webView.getContext(), REQUEST_PICK_PHOTO);
            }
        });
    }

    /**
     * 扫码界面
     */
    private void registerBarCode() {
        webView.registerHandler("scanGetCode", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction = function;
                Intent intent = new Intent(webView.getContext(), CaptureActivity.class);
                ((Activity) webView.getContext()).startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    /**
     * 加载框
     */
    private void registerShowLoading() {
        webView.registerHandler("showLoading", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                LoadingDialog.show(webView.getContext(), json.getString("showInfo"), json.getBoolean("isCancel"));
            }
        });
    }

    /**
     * 关闭加载框
     */
    private void registerCancelLoading() {
        webView.registerHandler("cancelLoading", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LoadingDialog.dismiss();
            }
        });
    }

    /**
     * 拨打手机号码
     * callFlag 1:直接跳至拨号页面
     * 2:直接跳至拨打页面
     */
    private void registerTelephoneCall() {
        webView.registerHandler("telephoneCall", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(webView.getContext(), "大吉大利11", Toast.LENGTH_LONG).show();
                json = JSONObject.parseObject(data).getJSONObject("params");
                if (json.getIntValue("callFlag") == 1) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + json.getString("telNum")));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    (webView.getContext()).startActivity(intent);
                } else if (json.getIntValue("callFlag") == 2) {
                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + json.getString("telNum")));
                    (webView.getContext()).startActivity(intentPhone);
                }
            }
        });
    }

    /**
     * 生成数据库表
     */
    private void registerCreateTable() {
        webView.registerHandler("createTableInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                JSONArray arrays = json.getJSONArray("column");
                database = DataBaseManager.getInstance().setDBName(Constants.DB_NAME)
                        .setDBVersion(Constants.DB_VERSION)
                        .setOnInitListener(new SqliteInitICallBack() {
                            @Override
                            public void onCreate(SQLiteDatabase db) {

                            }

                            @Override
                            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                                Log.e("tag","s");
                            }

                            @Override
                            public void onInitSuccess(SqliteDatabase db) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("create table if not exists ").append(tableName).append(" (");
                                for (int i = 0; i < arrays.size(); i++) {
                                    String type = "TEXT";
                                    JSONObject jsonObject = arrays.getJSONObject(i);
                                    switch (jsonObject.getString("type").toLowerCase()) {
                                        case "int":
                                            type = "INTEGER";
                                            break;
                                        case "string":
                                            type = "TEXT";
                                            break;
                                    }

                                    sb.append(" ").append(jsonObject.getString("name")).append(" ").append(type).append(" ").append(jsonObject.getBoolean("isId") ? "primary key " : ",").append(jsonObject.getBoolean("isAutoIncrement")?"AUTOINCREMENT ,":"");
                                    if (i == arrays.size() - 1) {
                                        sb.setLength(sb.length() - 1);
                                        sb.append(")");
                                    }

                                }
                                JSONObject jsonObject = new JSONObject();


                                try {

                                    db.execSQL(sb.toString());
                                    jsonObject.put("code",SUCCESS);
                                    jsonObject.put("msg","生成成功");
                                }catch (SQLException e){
                                    jsonObject.put("code",FAILD);
                                    jsonObject.put("msg","生成失败");
                                }finally {
                                    function.onCallBack(jsonObject.toJSONString());

                                }
                            }

                            @Override
                            public void onInitFailed(String s) {
                                Log.e("tag","s");
                            }
                        }).initDataBase(webView.getContext()).getDatabase(webView.getContext());

            }
        });

    }

    /**
     * 往数据库插入数据
     */
    private void registerInsertInfo() {

        webView.registerHandler("insertInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                JSONArray arrays = json.getJSONArray("column");
                StringBuilder sb = new StringBuilder();
                sb.append("insert into ").append(tableName).append(" ( ");
                for (int i = 0; i < arrays.size(); i++) {
                    JSONObject jsonObject = arrays.getJSONObject(i);
                    sb.append(jsonObject.getString("name")).append(",");
                    if (i == arrays.size() - 1) {
                        sb.setLength(sb.length() - 1);
                        sb.append(") values(");
                    }
                }
                String type = "TEXT";
                for (int i = 0; i < arrays.size(); i++) {
                    JSONObject jsonObject = arrays.getJSONObject(i);
                    switch (jsonObject.getString("type").toLowerCase()) {
                        case "int":
                            sb.append("'").append(jsonObject.getIntValue("value")).append("' ,");
                            break;
                        case "string":
                            sb.append("'").append(jsonObject.getString("value")).append("' ,");
                            break;
                    }

                    if (i == arrays.size() - 1) {
                        sb.setLength(sb.length() - 1);
                        sb.append(")");
                    }
                }


                JSONObject jsonObject = new JSONObject();


                try {

                    getDatabase().execSQL(sb.toString());
                    jsonObject.put("code",SUCCESS);
                    jsonObject.put("msg","插入成功");
                }catch (SQLException e){
                    jsonObject.put("code",FAILD);
                    jsonObject.put("msg","插入失败");
                }finally {
                    function.onCallBack(jsonObject.toJSONString());

                }




            }
        });
    }

    /**
     * 查询数据库
     */
    private void registerSelectInfos() {

        webView.registerHandler("selectInfos", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                sb.append("select * from ").append(tableName);


                JSONObject json = new JSONObject();


                try {

                    List<Map<String,String>> list =   getDatabase().find(sb.toString());
                    JSONArray result =  JSONArray.parseArray(JSON.toJSONString(list));
                    json.put("code",SUCCESS);
                    json.put("msg","查询成功");
                    json.put("data",result);
                }catch (SQLException e){
                    json.put("code",FAILD);
                    json.put("msg","查询失败");
                }finally {
                    function.onCallBack(json.toJSONString());

                }





            }
        });
    }

    /**
     * 修改某条数据
     */
    private void registerUpdateInfo() {

        webView.registerHandler("updateInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                JSONArray arrays = json.getJSONArray("column");
                sb.append("update ").append(tableName).append(" set ");
                for (int i = 0; i < arrays.size(); i++) {
                    JSONObject jsonObject = arrays.getJSONObject(i);
                    sb.append(jsonObject.getString("name")).append("=");
                    switch (jsonObject.getString("type").toLowerCase()) {
                        case "int":
                            sb.append(jsonObject.getIntValue("value")).append(",");
                            break;
                        case "string":
                            sb.append("'").append(jsonObject.getString("value")).append("',");
                            break;
                    }
                    if (i == arrays.size() - 1) {
                        sb.setLength(sb.length() - 1);
                        sb.append(" where ");
                    }
                }
                JSONObject jsonObject = json.getJSONObject("condition");
                switch (jsonObject.getString("type").toLowerCase()) {
                    case "int":
                        sb.append(jsonObject.getString("name")).append("=").append(jsonObject.getIntValue("value"));
                        break;
                    case "string":
                        sb.append(jsonObject.getString("name")).append("='").append(jsonObject.getString("value")).append("'");
                        break;
                }
                JSONObject result = new JSONObject();


                try {

                    getDatabase().execSQL(sb.toString());
                    result.put("code",SUCCESS);
                    result.put("msg","修改成功");
                }catch (SQLException e){
                    result.put("code",FAILD);
                    result.put("msg","修改失败");
                }finally {
                    function.onCallBack(result.toJSONString());

                }


            }
        });
    }

    /**
     * 删除某条数据
     */
    private void registerdeleteInfo() {

        webView.registerHandler("deleteInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                sb.append("delete from ").append(tableName).append(" where ");
                JSONObject jsonObject = json.getJSONObject("condition");
                switch (jsonObject.getString("type").toLowerCase()) {
                    case "int":
                        sb.append(jsonObject.getString("name")).append(" = ").append(jsonObject.getIntValue("value"));
                        break;
                    case "string":
                        sb.append(jsonObject.getString("name")).append(" = ").append(jsonObject.getString("value"));
                        break;
                }

                JSONObject result = new JSONObject();


                try {

                    getDatabase().execSQL(sb.toString());
                    result.put("code",SUCCESS);
                    result.put("msg","删除成功");
                }catch (SQLException e){
                    result.put("code",FAILD);
                    result.put("msg","删除失败");
                }finally {
                    function.onCallBack(result.toJSONString());

                }


            }
        });
    }

    /**
     * 删除表
     */
    private void registerDropTable() {

        webView.registerHandler("dropTable", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                sb.append("drop table if exists ").append(tableName);
                JSONObject result = new JSONObject();
                try {

                    getDatabase().execSQL(sb.toString());
                    result.put("code",SUCCESS);
                    result.put("msg","删除成功");
                }catch (SQLException e){
                    result.put("code",FAILD);
                    result.put("msg","删除失败");
                }finally {
                    function.onCallBack(result.toJSONString());

                }

            }
        });
    }

    /**
     * 锁屏
     */
    private void registerLockScreen() {
        webView.registerHandler("lockScreen", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                Toast.makeText(webView.getContext(),"lockScreenMethod run!",Toast.LENGTH_SHORT).show();
                //获取设备管理器
                mDevicePolicyManager = (DevicePolicyManager)webView.getContext()
                        .getSystemService(Context.DEVICE_POLICY_SERVICE);
                mComponentName = new ComponentName(webView.getContext(), LockReceiver.class);
                // 判断该组件是否有系统管理员的权限
                if (mDevicePolicyManager.isAdminActive(mComponentName)) {
                    mDevicePolicyManager.lockNow(); //锁屏
                } else {
                    activeManager();    //激活权限
                }
                //结束进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    /**
     *截屏
     */
    private void registerScreenShot() {
        webView.registerHandler("screenShot", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                /*获取windows中最顶层的view*/
                View view = mActivty.getWindow().getDecorView();
                //允许当前窗口保存缓存信息
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                //获取状态栏高度
                Rect rect = new Rect();
                view.getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                WindowManager windowManager = mActivty.getWindowManager();
                //获取屏幕宽和高
                DisplayMetrics outMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(outMetrics);
                int width = outMetrics.widthPixels;
                int height = outMetrics.heightPixels;
                //去掉状态栏
                Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
                        height-statusBarHeight);
                //销毁缓存信息
                view.destroyDrawingCache();
                view.setDrawingCacheEnabled(false);
                //保存bitmap到本地
                String path = saveBitmap(bitmap);
//                Toast.makeText(webView.getContext(),"已保存截屏到相册",Toast.LENGTH_SHORT).show();
                JSONObject result = new JSONObject();
                try {
                    if(!(path == null))
                    result.put("code",SUCCESS);
                    result.put("msg",path);
                }catch (SQLException e){
                    result.put("code",FAILD);
                    result.put("msg",null);
                }finally {
                    function.onCallBack(result.toJSONString());
                }
            }
        });
    }
    /**
     * 改变屏幕亮度
     */
    private void registerChangeScreenIntensity(){
        webView.registerHandler("changeScreenIntensity", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(webView.getContext(),"registerChangeScreenIntensity"
                        ,Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 录音
     */

    private void registerRecordVoice() {

        webView.registerHandler("recordVoice", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction=function;
                json = JSONObject.parseObject(data).getJSONObject("params");
                Log.i("infor:","录音传来的参数是："+json);
                String recordTime = json.getString("recordTime");
                Log.i("infor:","录音时长是："+recordTime);
                Intent intent = new Intent(webView.getContext(), RecordVoiceActivity.class);
                intent.putExtra("recordTime",Integer.parseInt(recordTime));
                intent.putExtra("flag",0);
                ((Activity) webView.getContext()).startActivityForResult(intent, RECORD_VOICE);
            }
        });
    }

    /**
     * 播放
     */
    private void registerPlayVoice() {

        webView.registerHandler("playVoice", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                Log.i("infor:","播放传来的参数是："+json);
                String path=json.getString("playPath");
                path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/smschat/1517969403603temp.voice";
                path=path.replace("$","/");
                //可能播放网络音频
                if(path.contains("http")||path.contains("https")){

                }else{
                    File file =new File(path);
                    if(!file.exists()) {
                        Toast.makeText(mActivty.getApplicationContext(), "亲，没有可播放的音频!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent intent = new Intent(webView.getContext(), RecordVoiceActivity.class);
                intent.putExtra("playPath",path);
                intent.putExtra("flag",1);
                ((Activity) webView.getContext()).startActivity(intent);
            }
        });
    }

    /**
     * 上传
     */
    private void registerUploadVoice() {

        webView.registerHandler("uploadVoice", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                Log.i("infor:","上传传来的参数是："+json);
//                String tableName = json.getString("tableName");
//                StringBuilder sb = new StringBuilder();
//                sb.append("drop table if exists ").append(tableName);
//
//                getDatabase().execSQL(sb.toString());


            }
        });
    }

    /**
     * 视频录制
     */
    private void registerRecordVideo() {

        webView.registerHandler("recordVideo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction=function;
                json = JSONObject.parseObject(data).getJSONObject("params");
                Log.i("infor:","录制视频传来的参数是："+json);
                MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                        .fullScreen(false)
                        .smallVideoWidth(360)
                        .smallVideoHeight(480)
                        .recordTimeMax(10000)
                        .recordTimeMin(1500)
                        .maxFrameRate(20)
                        .videoBitrate(600000)
                        .captureThumbnailsTime(1)
                        .build();
                Intent intent = new Intent(mActivty.getApplicationContext(), MediaRecorderActivity.class);
                intent.putExtra(MediaRecorderActivity.OVER_ACTIVITY_NAME, WebViewActivity.class.getName());
                intent.putExtra(MediaRecorderActivity.MEDIA_RECORDER_CONFIG_KEY, config);
                mActivty.startActivity(intent);
            }
        });
    }
    /**
     * 视频播放   registerPlayVideo
     */
    private void registerPlayVideo() {

        webView.registerHandler("playVideo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction=function;
                json = JSONObject.parseObject(data).getJSONObject("params");
//                Toast.makeText(webView.getContext(),"亲，功能开发中!",Toast.LENGTH_SHORT)LENGTH_SHORT.show();
                Intent intent = new Intent(webView.getContext(), VideoPlayerActivity.class);
             String   path=json.getString("playPath").replace("$","/");
               intent.putExtra("path",path);
                ((Activity) webView.getContext()).startActivity(intent);
            }
        });
    }

    /**
     * 获取包的信息
     */
    private void registerGetPackInfor() {

        webView.registerHandler("getPackInfor", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction=function;
                JSONObject packageInfor = PackageUtils.getAppInfo(webView.getContext());
                callBackFunction.onCallBack(packageInfor.toString());
            }
        });
    }
    /**
     * 获取wifi信息
     */
    private void registerGetWifiInfor() {

        webView.registerHandler("getWifiInfor", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction=function;
                JSONObject jsonObject;
                jsonObject= WifiUtils.getWifiInfor(webView.getContext());
                callBackFunction.onCallBack(jsonObject.toString());
            }
        });
    }


    /**
     * 分享
     */
    private void registerShareTest() {

        webView.registerHandler("shareTest", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction=function;
//                JSONObject jsonObject;
//                jsonObject= WifiUtils.getWifiInfor(webView.getContext());
//                callBackFunction.onCallBack(jsonObject.toString());
            }
        });
    }
    /**
     * 回调方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case ADDRESS_BOOK:
                    if (data == null)
                        return;
                    Uri uri = data.getData();
                    String[] contacts = getPhoneContacts(uri);
                    Toast.makeText(webView.getContext(), "姓名:" + contacts[0] + ", " + "手机号:" + contacts[1], Toast.LENGTH_LONG).show();
                    JSONObject contactsJson = new JSONObject();
                    contactsJson.put("phoneNum", contacts[1]);
                    contactsJson.put("name", contacts[0]);
                    mInstance.callBackFunction.onCallBack(contactsJson.toJSONString());
                    break;
                case REQUEST_TAKE_PHOTO:
                    Luban.with(webView.getContext())
                            .load(new File(photoPath))   // 传人要压缩的图片列表
                            .ignoreBy(100)               // 忽略不压缩图片的大小
                            .setTargetDir(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG)                        // 设置压缩后文件存储位置
                            .setCompressListener(new OnCompressListener() { //设置回调
                                @Override
                                public void onStart() {
                                }
                                @Override
                                public void onSuccess(File file) {
                                    File f = new File(photoPath);
                                    if (f.delete()) {
                                        if (file.renameTo(new File(photoPath))) {
                                            JSONObject jsonObject = new JSONObject();
                                            JSONObject photoInfo = new JSONObject();
                                            photoInfo.put("photoName", new File(photoPath).getName());
                                            photoInfo.put("photoPath", new File(photoPath).getAbsoluteFile());
                                            jsonObject.put("data", photoInfo);
                                            callBackFunction.onCallBack(jsonObject.toJSONString());
                                        }
                                    }
                                }
                                @Override
                                public void onError(Throwable e) {
                                    TastyToast.makeText(webView.getContext(), webView.getContext().getString(R.string.photo_exception), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            })
                            .launch();    //启动压缩
                    break;
                case REQUEST_PICK_PHOTO:
                    ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    List<File> files = new ArrayList<>();
                    Luban.with(webView.getContext())
                            .load(list)        // 传人要压缩的图片列表
                            .ignoreBy(100)    // 忽略不压缩图片的大小
                            .setTargetDir(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG)                        // 设置压缩后文件存储位置
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    files.add(file);

                                    if (files.size() == list.size()) {
                                        JSONObject jsonObject = new JSONObject();
                                        JSONArray photoInfos = new JSONArray();
                                        Observable.from(files).subscribe(f -> {
                                            File dest = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG, json.getString("keyword") + "_" + System.currentTimeMillis() + ".jpg");
                                            f.renameTo(dest);
                                            JSONObject photoInfo = new JSONObject();
                                            photoInfo.put("photoName", dest.getName());
                                            photoInfo.put("photoPath", dest.getAbsoluteFile());
                                            photoInfos.add(photoInfo);

                                        });
                                        jsonObject.put("data", photoInfos);
                                        callBackFunction.onCallBack(jsonObject.toJSONString());
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    TastyToast.makeText(webView.getContext(), webView.getContext().getString(R.string.photo_exception), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            })
                            .launch();    //启动压缩

                    break;

                case REQUEST_CODE:
                    JSONObject json = new JSONObject();
                    json.put("data", data.getStringExtra(CODE));
                    mInstance.callBackFunction.onCallBack(json.toJSONString());
                    break;
                //录音
                case RECORD_VOICE:
                    Log.i("infor","RECORD_VOICE:"+RECORD_VOICE);
                    JSONObject recordPath = new JSONObject();
                    Log.i("infor","返回的录音路径是:"+data.getStringExtra(CODE));
                    String path=data.getStringExtra(CODE).replace("$","/");
                    int  errCode= data.getIntExtra("errCode",0);
                    recordPath.put("data", path);
                    recordPath.put("errCode", errCode);
                    mInstance.callBackFunction.onCallBack(recordPath.toJSONString());
                    break;


            }
    }


    public void onNewIntent(Intent intent){
        Log.i("infor","录制视频返回路径"+intent.getStringExtra(MediaRecorderActivity.VIDEO_URI));
        callBackFunction.onCallBack(intent.getStringExtra(MediaRecorderActivity.VIDEO_URI));
    }


    public SqliteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            initDatabase();
        }
        return database;
    }


    private boolean initDatabase() {
        database = DataBaseManager.getInstance().setDBName(Constants.DB_NAME)
                .setDBVersion(Constants.DB_VERSION)
                .setOnInitListener(new SqliteInitICallBack() {

                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
                    }

                    @Override
                    public void onInitSuccess(SqliteDatabase db) {
                    }

                    @Override
                    public void onInitFailed(String msg) {
                    }

                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        // 创建 应用市场列表

                    }
                }).initDataBase(webView.getContext()).getDatabase(webView.getContext());

        if (database == null) {
            return false;
        }
        return true;
    }
    /**
     * 激活设备管理器获取权限
     */
    private void activeManager() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        webView.getContext().startActivity(intent);
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = webView.getContext().getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    /**
     *保存已有的bitmap到手机相册
     */
    private String saveBitmap( Bitmap bmp) {
        String rootPath = getRootPath();
        // 首先保存图片
        File appDir = new File(rootPath, "智慧营维");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "智慧营维" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(webView.getContext().getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        webView.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.parse(file.getAbsolutePath())));
        return  file.getAbsolutePath();
    }
    //获取保存图片的路径
    private String getRootPath() {
        File rootDir;
        if (existSDCard()) {
            rootDir = Environment.getExternalStorageDirectory();//获取SD卡根目录
        } else {
            rootDir = Environment.getRootDirectory();
        }
        return rootDir.toString();
    }
    //判断SD卡是否存在
    private boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}
