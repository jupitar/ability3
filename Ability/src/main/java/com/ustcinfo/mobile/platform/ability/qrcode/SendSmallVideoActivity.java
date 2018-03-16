package com.ustcinfo.mobile.platform.ability.qrcode;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;

import static com.ustcinfo.mobile.platform.ability.jsbridge.JsMethodAdapter.CODE;


/**
 * Created by jian on 2016/7/21 15:52
 * mabeijianxi@gmail.com
 */
public class SendSmallVideoActivity extends AppCompatActivity {

    public static String videoUri="";
//    //视频是否保存
//    public static boolean isSaved=true;
//    private TextView tv_send;
//    private TextView tv_cancel;
//    private String videoScreenshot;
//    private ImageView iv_video_screenshot;
//    private EditText et_send_content;
//    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        initView();
//        initData();
//        initEvent();
    }

    private void initView() {
        Intent intent = getIntent();
       videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SendSmallVideoActivity.this);
        normalDialog.setTitle("视频录制");
        normalDialog.setMessage("视频保存地址:"+videoUri);
        AlertDialog dialog = normalDialog.show();
        new Handler().postDelayed(() -> {
            {
                dialog.dismiss();
                finish();
            }
        }, 2000);

    }

//    private void initEvent() {
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hesitate();
//                isSaved=false;
//            }
//        });
//        tv_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        et_send_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        iv_video_screenshot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SendSmallVideoActivity.this, VideoPlayerActivity.class).putExtra(
//                        "path", videoUri));
//            }
//        });
//    }
//
//
//    private void initData() {
//        Intent intent = getIntent();
//        videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
//        videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
//        Bitmap bitmap = BitmapFactory.decodeFile( videoScreenshot);
//        iv_video_screenshot.setImageBitmap(bitmap);
//        et_send_content.setHint("您视频地址为:"+videoUri);
//    }
//
//    private void initView() {
//        setContentView(R.layout.smallvideo_text_edit_activity);
//        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
//        tv_send = (TextView) findViewById(R.id.tv_send);
//        et_send_content = (EditText) findViewById(R.id.et_send_content);
//        iv_video_screenshot = (ImageView) findViewById(R.id.iv_video_screenshot);
//
//
//    }
//
//
//
//
//    @Override
//    public void onBackPressed() {
//       finish();
//    }
//
//    private void hesitate() {
//        if (dialog == null) {
//            dialog = new AlertDialog.Builder(this)
//                    .setTitle(R.string.hint)
//                    .setMessage(R.string.record_camera_exit_dialog_message)
//                    .setNegativeButton(
//                            R.string.record_camera_cancel_dialog_yes,
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    finish();
//
////                                    FileUtils.deleteDir(getIntent().getStringExtra(MediaRecorderActivity.OUTPUT_DIRECTORY));
//
//                                }
//
//                            })
//                    .setPositiveButton(R.string.record_camera_cancel_dialog_no,
//                            null).setCancelable(false).show();
//        } else {
//            dialog.show();
//        }
//    }

}
