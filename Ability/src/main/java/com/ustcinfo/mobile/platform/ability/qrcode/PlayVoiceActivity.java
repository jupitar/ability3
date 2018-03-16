package com.ustcinfo.mobile.platform.ability.qrcode;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/27.
 */

/**
 * 视频播放界面
 */
public class PlayVoiceActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //测试路径
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordVoice/" + "20171228093135.mp3";

    //真实路径
//    private String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/recordVoice/"+"20171227162222.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_record);
        //真实传递过来的录音文件路径
//        path=getIntent().getStringExtra("playPath");
        Log.i("infor", "123>>>" + getIntent().getStringExtra("playPath"));
        setListener();
        play();
    }

    private void setListener() {
        //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
//        //以便其他应用程序可以使用该资源:
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();//释放音频资源
                mediaPlayer = null;
//                stopButton.setEnabled(false);
//                setTitle("资源已经被释放了");
            }
        });
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    //注销
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}


