package com.ustcinfo.mobile.platform.ability.qrcode;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ustcinfo.mobile.platform.ability.R;
import java.util.ArrayList;
import java.util.List;


/**
 * 录制视频界面
 */
public class RecordVideoActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SurfaceView mSurfaceview;
    private Button mBtnStartStop;
    private Button mBtnPlay;
    private boolean mStartedFlg = false;//是否正在录像
    private boolean mIsPlay = false;//是否正在播放录像
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private ImageView mImageView;
    private Camera camera;
    private MediaPlayer mediaPlayer;
    private String path;
    private TextView textView;
    private int text = 0;
    private List<String> permissionList1=new ArrayList<>();
    private android.os.Handler handler = new android.os.Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            text++;
//            textView.setText(text+"");
//            handler.postDelayed(this,1000);
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_record_video);
        Log.i("infor","hhahah");
//        initSmallVideo();
        //获得权限
        getPermnission();
        // recording
//        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
//                .fullScreen(false)
//                .smallVideoWidth(360)
//                .smallVideoHeight(480)
//                .recordTimeMax(6000)
//                .recordTimeMin(1500)
//                .maxFrameRate(20)
//                .videoBitrate(600000)
//                .captureThumbnailsTime(1)
//                .build();
//        MediaRecorderActivity.goSmallVideoRecorder(this, RecordVideoActivity.class.getName(), config);
//        initView();
//        setListener();
//        SurfaceHolder holder = mSurfaceview.getHolder();
//        holder.addCallback(this);
//        // setType必须设置，要不出错.
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    //    //视频初始化
//    public static void initSmallVideo() {
//        // Set the cache path for video
//        File dcim = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
//            } else {
//                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
//                        "/sdcard-ext/")
//                        + "/mabeijianxi/");
//            }
//        } else {
//            JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
//        }
//        // Initialize the shooting, encounter problems can choose to open this tag to facilitate the generation of logs
//        JianXiCamera.initialize(false,null);
//    }



//    private void initView() {
//        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
//        mImageView = (ImageView) findViewById(R.id.imageview);
//        mBtnStartStop = (Button) findViewById(R.id.btnStartStop);
//        mBtnPlay = (Button) findViewById(R.id.btnPlayVideo);
//        textView = (TextView)findViewById(R.id.text);
//    }
//
    private void getPermnission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //            读sd卡权限
            if(ContextCompat.checkSelfPermission(RecordVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED){
                permissionList1.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            //写sd卡权限
            if(ContextCompat.checkSelfPermission(RecordVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED){
                permissionList1.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            // 录音权限
            if(ContextCompat.checkSelfPermission(RecordVideoActivity.this, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED){
                permissionList1.add(Manifest.permission.RECORD_AUDIO);
            }
            if(ContextCompat.checkSelfPermission(RecordVideoActivity.this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED){
                permissionList1.add(Manifest.permission.CAMERA);
            }
            if (!permissionList1.isEmpty()) {
                String[] permissions = permissionList1.toArray(new String[permissionList1.size()]);
                ActivityCompat.requestPermissions(RecordVideoActivity.this, permissions, 1);
            }

        }
    }
//
//
//    private void setListener() {
//        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mIsPlay) {
//                    if (mediaPlayer != null) {
//                        mIsPlay = false;
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//                }
//                if (!mStartedFlg) {
//                    handler.postDelayed(runnable,1000);
//                    mImageView.setVisibility(View.GONE);
//                    if (mRecorder == null) {
//                        mRecorder = new MediaRecorder();
//                    }
//
//                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//                    if (camera != null) {
//                        camera.setDisplayOrientation(90);
//                        camera.unlock();
//                        mRecorder.setCamera(camera);
//                    }
//
//                    try {
//                        // 这两项需要放在setOutputFormat之前
//                        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//                        // Set output file format
//                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//
//                        // 这两项需要放在setOutputFormat之后
//                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
//
//                        mRecorder.setVideoSize(640, 480);
//                        mRecorder.setVideoFrameRate(30);
//                        mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
//                        mRecorder.setOrientationHint(90);
//                        //设置记录会话的最大持续时间（毫秒）
//                        mRecorder.setMaxDuration(30 * 1000);
//                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//                        path = getSDPath();
//                        if (path != null) {
//                            File dir = new File(path + "/recordtest");
//                            if (!dir.exists()) {
//                                dir.mkdir();
//                            }
//                            path = dir + "/" + getDate() + ".mp4";
//                            mRecorder.setOutputFile(path);
//                            mRecorder.prepare();
//                            mRecorder.start();
//                            mStartedFlg = true;
//                            mBtnStartStop.setText("Stop");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    //stop
//                    if (mStartedFlg) {
//                        try {
//                            handler.removeCallbacks(runnable);
//                            mRecorder.stop();
//                            mRecorder.reset();
//                            mRecorder.release();
//                            mRecorder = null;
//                            mBtnStartStop.setText("Start");
//                            if (camera != null) {
//                                camera.release();
//                                camera = null;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    mStartedFlg = false;
//                }
//            }
//        });
//
//        mBtnPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mIsPlay = true;
//                mImageView.setVisibility(View.GONE);
//                if (mediaPlayer == null) {
//                    mediaPlayer = new MediaPlayer();
//                }
//                mediaPlayer.reset();
//                Uri uri = Uri.parse(path);
//                mediaPlayer = MediaPlayer.create(RecordVideoActivity.this, uri);
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setDisplay(mSurfaceHolder);
//                try{
//                    mediaPlayer.prepare();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                mediaPlayer.start();
//            }
//        });
//
//    }
    /**
     * 判断权限是否被用户同意
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须统一所有权限!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!mStartedFlg) {
//            mImageView.setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * 获取系统时间
     *
     * @return
     */
//    public static String getDate() {
//        Calendar ca = Calendar.getInstance();
//        int year = ca.get(Calendar.YEAR);           // 获取年份
//        int month = ca.get(Calendar.MONTH);         // 获取月份
//        int day = ca.get(Calendar.DATE);            // 获取日
//        int minute = ca.get(Calendar.MINUTE);       // 分
//        int hour = ca.get(Calendar.HOUR);           // 小时
//        int second = ca.get(Calendar.SECOND);       // 秒
//        String date = "" + year + (month + 1) + day + hour + minute + second;
//        Log.d(TAG, "date:" + date);
//        return date;
//    }

//    /**
//     * 获取SD path
//     *
//     * @return
//     */
//    public String getSDPath() {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
//            return sdDir.toString();
//        }
//        return null;
//    }
//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        mSurfaceHolder = surfaceHolder;
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
//        mSurfaceHolder = surfaceHolder;
//    }

//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        mSurfaceview = null;
//        mSurfaceHolder = null;
//        handler.removeCallbacks(runnable);
//        if (mRecorder != null) {
//            mRecorder.release();
//            mRecorder = null;
//            Log.d(TAG, "surfaceDestroyed release mRecorder");
//        }
//        if (camera != null) {
//            camera.release();
//            camera = null;
//        }
//        if (mediaPlayer != null){
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
}
