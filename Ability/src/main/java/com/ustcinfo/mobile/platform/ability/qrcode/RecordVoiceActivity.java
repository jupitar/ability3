package com.ustcinfo.mobile.platform.ability.qrcode;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.lqr.audio.IAudioPlayListener;
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.qrcode.utils.AudilUtils;
import com.ustcinfo.mobile.platform.ability.widgets.Indicator;
import static com.ustcinfo.mobile.platform.ability.jsbridge.JsMethodAdapter.CODE;


/**
 * 录音界面
 */
public class RecordVoiceActivity extends AppCompatActivity implements  AudilUtils.IRecordVoice{
   RelativeLayout  capture_container,relAudio ;
   ImageView ivPlayAduio;
    Indicator indicator;
    Button record_button ;
    String audioPath;
    AudilUtils audilUtils;
    AudioManager audio ;
    boolean isPlaying;
    int  flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        //播放音频
        if(flag==1){
            audioPath=getIntent().getStringExtra("playPath");
            Log.i("infor","audioPath:"+audioPath);
            audilUtils=AudilUtils.getAudilUtils();
            playVoice();
        }
        //录制音频
        else{
            recordVideo();
        }
    }

    private void initView() {
        capture_container= (RelativeLayout) findViewById(R.id.capture_container);
        relAudio= (RelativeLayout) findViewById(R.id.rel_audio);
        indicator= (Indicator) findViewById(R.id.indicator);
        record_button= (Button) findViewById(R.id.record_voice);
        ivPlayAduio= (ImageView) findViewById(R.id.iv_play_aduio);
        flag=getIntent().getIntExtra("flag",0);
    }

    private void recordVideo() {
        record_button.setVisibility(View.VISIBLE);
        audilUtils=AudilUtils.getAudilUtils();
        audilUtils.setRecordVoice(this);
        record_button.setOnTouchListener((view, event) -> {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audilUtils.getAudioRecordManager(getApplicationContext(), capture_container).startRecord();
                       // scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (audilUtils.isCancelled(view, event)) {
                            audilUtils.getAudioRecordManager(getApplicationContext(), capture_container).willCancelRecord();
                        } else {
                            audilUtils.getAudioRecordManager(getApplicationContext(), capture_container).continueRecord();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        audilUtils.getAudioRecordManager(getApplicationContext(), capture_container).stopRecord();
                        audilUtils.getAudioRecordManager(getApplicationContext(), capture_container).destroyRecord();
                        break;
                }
            return true;
        });
    }

    private void playVoice() {
        record_button.setVisibility(View.INVISIBLE);
        if(isPlaying){
            audilUtils.getAudioPlayManager().stopPlay();
            isPlaying=false;
        }else{
            isPlaying=true;
           // audioPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/smschat/1517967163825temp.voice";
            relAudio.setVisibility(View.VISIBLE);
            audilUtils.getAudioPlayManager().startPlay(getApplicationContext(), Uri.parse(audioPath), new IAudioPlayListener() {
                @Override
                public void onStart(Uri var1) {

                }

                @Override
                public void onStop(Uri var1) {
                    indicator.setDuration(0);
                    indicator.setStepNum(1);
                    indicator.setBarNum(1);
                    indicator.requestLayout();
                    ivPlayAduio.setImageResource(R.mipmap.icon_play);
                }

                @Override
                public void onComplete(Uri var1) {
                    indicator.setDuration(0);
                    indicator.setStepNum(1);
                    indicator.setBarNum(1);
                    indicator.requestLayout();
                    ivPlayAduio.setImageResource(R.mipmap.icon_play);
                }
            });
            ivPlayAduio.setImageResource(R.mipmap.icon_stop);
            indicator.setDuration(20000);
            indicator.setStepNum(70);
            indicator.setBarNum(50);
            indicator.requestLayout();
        }

        ivPlayAduio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVoice();
            }
        });

    }

    @Override
    public void recordAudioSuccess(String path) {
        audioPath = path;
        Intent intent =new Intent();
        if(audioPath!=null){
            audioPath=audioPath.replace("/","$");
        }
        intent.putExtra(CODE,audioPath);
       setResult(RESULT_OK,intent);
       //将mPopup置空
      Toast.makeText(getApplicationContext(),"录音成功!",Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AudilUtils.destoryUtils();
        super.onDestroy();
    }



    /**
     * 重写返回键功能
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         switch (keyCode) {
               case KeyEvent.KEYCODE_VOLUME_UP:
                   audio.adjustStreamVolume(
                               AudioManager.STREAM_MUSIC,
                               AudioManager.ADJUST_RAISE, // 增加音量
                              AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                   return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                 audio.adjustStreamVolume(
                               AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,//减少音量
                           AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
//                case
//               default:
//                   break;
//                   case
            }
            //是否是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            audilUtils.getAudioPlayManager().stopPlay();
            isPlaying=false;
            finish();

            //TODO something
            return true;
        }
         return super.onKeyDown(keyCode, event);
      }


}
