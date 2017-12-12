package com.yzx.frank.moblieplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.yzx.frank.moblieplayer.view.VideoView;
import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.bean.VideoItem;
import com.yzx.frank.moblieplayer.interfaces.Keys;
import com.yzx.frank.moblieplayer.util.Logger;
import com.yzx.frank.moblieplayer.util.Utils;



import java.util.ArrayList;

/**
 * Created by Frank on 2017/12/7.
 */

public class VideoPlayerActivity extends BaseActivity {

    private VideoView videoView;
    private VideoItem videoItem;
    private TextView  tv_title;
    private ImageView iv_system_battery;
    private Button btn_full_screen;
    private Button btn_play;
    private Button btn_next;
    private Button btn_pre;
    private SeekBar sb_video;
    private SeekBar sb_voice;
    private TextView tv_total_duration;
    private TextView tv_current_position;
    private TextView tv_system_time;
    /** 显示系统时间*/
    private static final int SHOW_SYSTEM_TIME = 0;
    /** 更新插入进度*/
    private static final int UPDATE_PLAY_PROGRESS = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_SYSTEM_TIME:
                    showSystemTime();
                    break;
                case UPDATE_PLAY_PROGRESS:
                    updatePlayProgress();
                    break;
                default:
                    break;

            }
        }
    };
    private BroadcastReceiver batteryChangeReceiver;
    private int maxVolume;
    private AudioManager audioManger;

    SeekBar.OnSeekBarChangeListener OnVoiceSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {


        /**
         * 进度发生改变的时候
         * @param seekBar
         * @param progress
         * @param fromUser         是否是用户触发的
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                setStreamVolume(progress);
            }

        }

        /**
         * 开始拖动SeekBar
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * 停止拖动SeekBar
         * param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private int currentVolume;
    private boolean isDownLeftOfScreen;
    private View view_brightness;
    private float currentAlpha;
    private ArrayList<VideoItem> videoItems;
    private int currentPosition;

    /**
     * 设置SeekBar的音量值
     * @param value
     */
    private void setStreamVolume(int value) {
        int flags = 0;  // 1- 显示系统的音量浮动面板; 0-不显示系统的音量浮动面板
        audioManger.setStreamVolume(AudioManager.STREAM_MUSIC
                ,value
                ,flags);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initView() {
        videoView           =   findView(R.id.video_view);
        tv_title            =   findView(R.id.tv_title);
        iv_system_battery   =   findView(R.id.iv_system_battery);
        tv_system_time      =   findView(R.id.tv_system_time);
        tv_current_position =   findView(R.id.tv_current_position);
        tv_total_duration   =   findView(R.id.tv_total_duration);
        sb_voice            =   findView(R.id.sb_voice);
        sb_video            =   findView(R.id.sb_video);
        btn_pre             =   findView(R.id.btn_pre);
        btn_next            =   findView(R.id.btn_next);
        btn_play            =   findView(R.id.btn_play);
        btn_full_screen     =   findView(R.id.btn_full_screen);
        view_brightness     =   findView(R.id.view_brightness);
        view_brightness.setVisibility(View.VISIBLE);
        btn_full_screen = findView(R.id.btn_full_screen);
        float alpha = 0.0f;
        setBrightness(alpha);
        showSystemTime();
    }

    /**
     * 设置屏幕亮度（本示例中使用透明度来模拟，实际过程需要调用系统函数）
     * @param alpha
     */
    private void setBrightness(float alpha) {
        view_brightness.setAlpha(alpha);
    }

    /**
     * 显示系统时间
     */
    private void showSystemTime() {
        tv_system_time.setText(DateFormat.format("kk:mm:ss", System.currentTimeMillis()));

        /**
         * 每一秒钟会更新一下时间
         */
        handler.sendEmptyMessageDelayed(SHOW_SYSTEM_TIME, 1000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        unregisterReceiver(batteryChangeReceiver);
        super.onDestroy();


    }

    @Override
    public void initListener() {

        videoView.setOnPreparedListener(mOnPreparedListener);
        videoView.setOnCompletionListener(mOnCompletionListener);

        sb_voice.setOnSeekBarChangeListener(OnVoiceSeekBarChangeListener);
        sb_video.setOnSeekBarChangeListener(mOnVideoSeekBarChangeListener);
        gestureDetector = new GestureDetector(this, mSimpleOnGestureListener);



    }

    /**
     * 视频插入完会回调这个监听器
     */
    MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            videoView.seekTo(0);
            tv_current_position.setText(Utils.formatMilliSeconds(0));
            sb_video.setProgress(0);
        }
    };

    SeekBar.OnSeekBarChangeListener mOnVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        /**
         * 进度发生改变的时候
         * @param seekBar
         * @param progress
         * @param fromUser         是否是用户触发的
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                videoView.seekTo(progress);
            }
        }
        /**
         * 开始拖动SeekBar
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        /**
         * 停止拖动SeekBar
         * param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void initData() {
        videoItems = (ArrayList<VideoItem>)getIntent().getSerializableExtra(Keys.ITEMS);
        currentPosition = getIntent().getIntExtra(Keys.CURRENT_POSITION, -1);
        openVideo();
        registerBatteryChangeReceiver();
        initVoice();
    }

    private void openVideo() {
        if(videoItems == null || videoItems.isEmpty() || currentPosition == -1) {
            return ;
        }

        btn_pre.setEnabled(currentPosition !=0);
        btn_next.setEnabled(currentPosition != videoItems.size()-1);

        Logger.d(this, "currentPosition="+currentPosition);

        videoItem = videoItems.get(currentPosition);
        String path = videoItem.getData();
        Logger.i(VideoPlayerActivity.class, path);
        videoView.setVideoPath(path);
        // videoView.setMediaController(new MediaController(this));

    }

    /**
     * 初始化音量
     */
    private void initVoice() {
        audioManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //int currentVolume = getStreamVolume();
        sb_voice.setMax(maxVolume);
        sb_voice.setProgress(getStreamVolume());
    }

    /**
     * 获取当前音量
     * @return
     */
    private int getStreamVolume() {
        return audioManger.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 注册电量改变接收器
     */
    private void registerBatteryChangeReceiver() {
        //获取电量的电平值
        batteryChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //获取电量的电平值
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                updateBatteryBg(level);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryChangeReceiver, filter);

    }

    /**
     * 更新电量背景图片
     * @param level
     */
    private void updateBatteryBg(int level) {
        Logger.i(this, "level=" + level);
        int resId = R.drawable.ic_battery_0;
        if(level != 0){
            switch (level/ 10)
            {
                case 0:
                    resId = R.drawable.ic_battery_0;
                    break;
                case 1:
                    resId = R.drawable.ic_battery_10;
                    break;
                case 2:
                    resId = R.drawable.ic_battery_20;
                    break;
                case 3:
                    resId = R.drawable.ic_battery_20;
                    break;
                case 4:
                    resId = R.drawable.ic_battery_40;
                    break;
                case 5:
                    resId = R.drawable.ic_battery_40;
                    break;
                case 6:
                    resId = R.drawable.ic_battery_60;
                    break;
                case 7:
                    resId = R.drawable.ic_battery_60;
                    break;
                case 8:
                    resId = R.drawable.ic_battery_80;
                    break;
                case 9:
                    resId = R.drawable.ic_battery_80;
                    break;
                case 10:
                    resId = R.drawable.ic_battery_100;
                    break;
                default:
                    break;
            }
        }

        iv_system_battery.setBackgroundResource(resId);

    }


    @Override
    public void onClick(View view, int id) {
        switch (id) {
            case R.id.btn_voice:             //静音按钮
                mute();
                break;
            case R.id.btn_exit:             //退出按钮
                finish();
                break;
            case R.id.btn_pre:              //上一首按钮
                preVideoItem();
                break;
            case R.id.btn_play:             //播放按钮
                showToast("播放");
                break;
            case R.id.btn_next:             //下一首按钮
                nextVideoItem();
                break;
            case R.id.btn_full_screen:     //全屏按钮
                makeFullScreen();
                break;
            default:
                break;
        }

    }

    /**
     * 在全屏和默认大小之间切换
     */
    private void makeFullScreen() {
        videoView.switchFullScreen();
        updateFullScreenBtnBg();
    }

    /**
     * 更新全屏按钮的背景
     */
    private void updateFullScreenBtnBg() {
        if(videoView.isFullScreen()) {
            //显示一个恢复默认按钮大小的背景
            btn_full_screen.setBackgroundResource(R.drawable.selector_btn_defaultscreen);
        } else {
            // 显示一个全屏按钮的背景
            btn_full_screen.setBackgroundResource(R.drawable.selector_btn_fullscreen);
        }

    }

    /**
     * 播放上一个视频节目
     */
    private void preVideoItem() {
        if(currentPosition != 0) {
            currentPosition --;
            openVideo();
        }

    }

    /**
     * 播放下一个视频节目
     */
    private void nextVideoItem() {
        if(videoItems == null) {
            return;

        }
        if( currentPosition != videoItems.size()-1) {
            currentPosition ++;
            openVideo();
        }

    }
    /**
     * 静音或者恢复原来的音量
     */
    private void mute() {
        if(getStreamVolume() > 0 ) {
            //如果当前音量大于0， 则保存一下这个音量，然后设置为0
            currentVolume = getStreamVolume();
            setStreamVolume(0);
            sb_voice.setProgress(0);
        } else {
            //如果当前音量为0， 则恢复原来的音量
            setStreamVolume(currentVolume);
            sb_voice.setProgress(currentVolume);
        }

    }

    MediaPlayer.OnPreparedListener mOnPreparedListener
            = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
            tv_title.setText(videoItem.getTitle());
            //显示视频总时长
            tv_total_duration.setText(Utils.formatMilliSeconds(videoView.getDuration()));
            sb_video.setMax(videoView.getDuration());//设置SeekBar总进度
            updatePlayProgress();
        }

    };

    /**
     * 更新播放进度
     */
    private void updatePlayProgress() {
        tv_current_position.setText(
                Utils.formatMilliSeconds(videoView.getCurrentPosition()));
        sb_video.setProgress(videoView.getCurrentPosition());
        handler.sendEmptyMessageDelayed(UPDATE_PLAY_PROGRESS, 300);
    }


    GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            currentVolume = getStreamVolume();
            isDownLeftOfScreen = e.getX() < Utils.getScreenWidth(VideoPlayerActivity.this)/2;
            currentAlpha = view_brightness.getAlpha();

            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 1. onTouchEvent (处理触摸事件)
            // 2. GuestureDetector （手势识别）
            // 3. 计算在屏幕Y方向滑动的距离（e1-e2）
            float distanceYY = e1.getY()-e2.getY();

            if(isDownLeftOfScreen) {
                changeBrightness(distanceYY);
            }else {
                changeVolume(distanceYY);
            }


            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    };

    /**
     * 根据移动屏幕的距离改变屏幕亮度值
     * @param distanceY
     */
    private void changeBrightness(float distanceY) {
       /**
        * 本实现方法是在布局文档中加入一个View， 然后使用View的透明度Alpha
        * 来模拟亮度的改变， 在实际的编程中， 我们需要使用系统的改变亮度的方法
        * 直接调用就可以
        */

        Logger.i(VideoPlayerActivity.this, "distanceYY = " +distanceY);
        // 1. onTouchEvent (处理触摸事件)
        // 2. GestureDetector （手势识别）
        // 3. 计算在屏幕Y方向滑动的距离（e1-e2）
        // 4. 计算滑动的距离等于多少对应的亮度值
        // a> 计算亮度最大值与屏幕高的比例
        float scale = 1.0f / Utils.getScreenHeight(VideoPlayerActivity.this);

        // b> 计算滑动的距离等于多少对应的亮度值：移动距离X比例
        float moveAlpha = distanceY * scale;

        // 5.  在原来亮度的基础上加上计算出来的对应亮度值
        float resultAlpha= currentAlpha - moveAlpha;
        //预防走出范围
        if(resultAlpha < 0) {
            resultAlpha = 0;
        } else if(resultAlpha > 0.8f) {
            resultAlpha = 0.8f;
        }

        Logger.i(VideoPlayerActivity.this, "resultAlpha = " +resultAlpha);
        // 6. 使用这个音量值
        setBrightness(resultAlpha);

    }

    /**
     * 根据移动屏幕的距离改变音量值
     * @param distanceY
     */
    private void changeVolume(float distanceY) {
        // 1. onTouchEvent (处理触摸事件)
        // 2. GestureDetector （手势识别）
        // 3. 计算在屏幕Y方向滑动的距离（e1-e2）
        // 4. 计算滑动的距离等于多少对应的音量值
        // a> 计算音量最大值与屏幕高的比例
        float scale = (float) maxVolume / Utils.getScreenHeight(VideoPlayerActivity.this);

        // b> 计算滑动的距离等于多少对应的音量值：移动距离X比例
        int moveVolume = (int)(distanceY * scale);

        // 5.  在原来音量的基础上加上计算出来的对应音量值
        int resultVolume = currentVolume + moveVolume;
        //预防走出范围
        if(resultVolume < 0) {
            resultVolume = 0;
        } else if(resultVolume > maxVolume) {
            resultVolume = maxVolume;
        }

        //Logger.i(VideoPlayerActivity.this, "resultVolume = " +resultVolume);
        // 6. 使用这个音量值
        setStreamVolume(resultVolume);
        sb_voice.setProgress(resultVolume);
    }

    private GestureDetector gestureDetector;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把触摸事件传给手势监听器
        boolean result = gestureDetector.onTouchEvent(event);

        return result;
    }
}
