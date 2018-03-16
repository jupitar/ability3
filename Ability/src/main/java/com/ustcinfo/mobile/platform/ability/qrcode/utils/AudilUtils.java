package com.ustcinfo.mobile.platform.ability.qrcode.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.ustcinfo.mobile.platform.ability.R;

import java.io.File;

/**
 * Created by Administrator on 2018/2/6.
 */

public class AudilUtils {
    Context mcontext;

    private static AudilUtils audilUtils;

    private AudilUtils(){}

    public synchronized static AudilUtils getAudilUtils(){
            if(audilUtils==null)
                audilUtils = new AudilUtils();
       return  audilUtils;
    }


    AudioRecordManager audioRecordManager;

    AudioPlayManager audioPlayManager;

    IRecordVoice  recordVoice;

    public interface  IRecordVoice{
        void recordAudioSuccess(String path);
    }

    public void setRecordVoice(IRecordVoice recordVoice) {
        this.recordVoice = recordVoice;
    }

    //检查是否手指滑动离开录音按钮
    public boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }
    public AudioRecordManager getAudioRecordManager(Context context, View mRoot) {
        if (audioRecordManager == null) {
            audioRecordManager = AudioRecordManager.getInstance(context);
            audioRecordManager.setMaxVoiceDuration(30);
            File mAudioDir = new File(Environment.getExternalStorageDirectory(), "smschat");
            if (!mAudioDir.exists()) {
                mAudioDir.mkdirs();
            }
            audioRecordManager.setAudioSavePath(mAudioDir.getAbsolutePath());
            audioRecordManager.setAudioRecordListener(new IAudioRecordListener() {


                private TextView mTimerTV;
                private TextView mStateTV;
                private ImageView mStateIV;
                private PopupWindow mRecordWindow;

                @Override
                public void initTipView() {
                    View view = View.inflate(context, R.layout.popup_audio_wi_vo, null);
                    mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
                    mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
                    mTimerTV = (TextView) view.findViewById(R.id.rc_audio_timer);
                    mRecordWindow = new PopupWindow(view, -1, -1);
                    mRecordWindow.showAtLocation(mRoot, 17, 0, 0);
                    mRecordWindow.setFocusable(true);
                    mRecordWindow.setOutsideTouchable(false);
                    mRecordWindow.setTouchable(false);
                }

                @Override
                public void setTimeoutTipView(int counter) {
                    if (this.mRecordWindow != null) {
                        this.mStateIV.setVisibility(View.GONE);
                        this.mStateTV.setVisibility(View.VISIBLE);
                        this.mStateTV.setText(R.string.voice_rec);
                        this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                        this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                        this.mTimerTV.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void setRecordingTipView() {
                    if (this.mRecordWindow != null) {
                        this.mStateIV.setVisibility(View.VISIBLE);
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                        this.mStateTV.setVisibility(View.VISIBLE);
                        this.mStateTV.setText(R.string.voice_rec);
                        this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                        this.mTimerTV.setVisibility(View.GONE);
                    }
                }

                @Override
                public void setAudioShortTipView() {
                    if (this.mRecordWindow != null) {
                        mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                        mStateTV.setText(R.string.voice_short);
                    }
                }

                @Override
                public void setCancelTipView() {
                    if (this.mRecordWindow != null) {
                        this.mTimerTV.setVisibility(View.GONE);
                        this.mStateIV.setVisibility(View.VISIBLE);
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                        this.mStateTV.setVisibility(View.VISIBLE);
                        this.mStateTV.setText(R.string.voice_cancel);
                        this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                    }
                }

                @Override
                public void destroyTipView() {
                    if (this.mRecordWindow != null) {
                        this.mRecordWindow.dismiss();
                        this.mRecordWindow = null;
                        this.mStateIV = null;
                        this.mStateTV = null;
                        this.mTimerTV = null;
                    }
                }

                @Override
                public void onStartRecord() {
                    //开始录制
                }

                @Override
                public void onFinish(Uri audioPath, int duration) {
                    //发送文件
                    File file = new File(audioPath.getPath());
                    if (file.exists()) {
                        recordVoice.recordAudioSuccess(file.getAbsolutePath());
                    }
                }

                @Override
                public void onAudioDBChanged(int db) {
                    switch (db / 5) {
                        case 0:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                            break;
                        case 1:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                            break;
                        case 2:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                            break;
                        case 3:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                            break;
                        case 4:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                            break;
                        case 5:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                            break;
                        case 6:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                            break;
                        default:
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                    }
                }
            });

        }
        return audioRecordManager;
    }

    public AudioPlayManager getAudioPlayManager() {
        if (audioPlayManager == null) {
            audioPlayManager = AudioPlayManager.getInstance();
        }
        return audioPlayManager;
    }

    public ObjectAnimator getShowAction(View view) {
        view.setPivotX(0);
        view.setPivotY(0);
        ObjectAnimator mShowAction = ObjectAnimator.ofFloat(view, "ScaleY", 0f, 1f).setDuration(500);
        mShowAction.setInterpolator(new AccelerateInterpolator());
        return mShowAction;
    }

    public ObjectAnimator getHideAction(View view) {
        view.setPivotX(0);
        view.setPivotY(0);
        ObjectAnimator mHiddenAction = ObjectAnimator.ofFloat(view, "ScaleY", 1f, 0f).setDuration(500);
        mHiddenAction.setInterpolator(new DecelerateInterpolator());
        mHiddenAction.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        return mHiddenAction;
    }


    public static void destoryUtils(){
        audilUtils =null;
    }


}
