package com.ustcinfo.mobile.platform.ability.qrcode;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ustcinfo.mobile.platform.ability.R;

public class VideoPlayerActivity extends AppCompatActivity {

    TextView tvTitle;
    VideoView videoView;
    RelativeLayout background;
    private String videoPath;
    private MediaController mediaController;
    private ProgressBar progressBar;
    private ImageView img_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initView();
    }

    private void initView() {
//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        img_back= (ImageView) findViewById(R.id.iv_left);
        videoView = (VideoView) findViewById(R.id.videoview);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        background = (RelativeLayout) findViewById(R.id.background);
//        tvTitle.setText("视频播放");
        videoPath = getIntent().getStringExtra("path");
        play(videoPath);
    }

    private void play(String path) {
       //path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        videoView.setVideoPath(path);
        mediaController=new MediaController(this);
//        // 设置MediaController与VideView建立关联
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("infor","test  onclick");
//                finish();
//            }
//        });
        // 让VideoView获取焦点
//        videoView.requestFocus();
        // 开始播放
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
//                background.setBackgroundColor(Color.BLACK);
                background.setVisibility(View.GONE);
//                mp.start();
//                mp.setLooping(true);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                videoView.setVideoPath(path);
//                videoView.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {


                return false;
            }
        });


    }

}
