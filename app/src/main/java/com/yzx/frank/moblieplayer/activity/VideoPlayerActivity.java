package com.yzx.frank.moblieplayer.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.VideoView;

import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.bean.VideoItem;
import com.yzx.frank.moblieplayer.interfaces.Keys;
import com.yzx.frank.moblieplayer.util.Logger;

import java.util.ArrayList;

/**
 * Created by Frank on 2017/12/7.
 */

public class VideoPlayerActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initView() {

        videoView = findView(R.id.video_view);
    }

    @Override
    public void initListener() {

        videoView.setOnPreparedListener(mOnPreparedListener);
    }

    @Override
    public void initData() {
        ArrayList<VideoItem> videoItems =
                (ArrayList<VideoItem>)getIntent().getSerializableExtra(Keys.ITEMS);
        int currentPosition = getIntent().getIntExtra(Keys.CURRENT_POSITION, -1);
        if(videoItems == null || videoItems.isEmpty() || currentPosition == -1) {
            return;
        }
        VideoItem videoItem = videoItems.get(currentPosition);
        String path = videoItem.getData();
        Logger.i(VideoPlayerActivity.class, path);
        videoView.setVideoPath(path);
       // videoView.setMediaController(new MediaController(this));
    }

    @Override
    public void onClick(View view, int id) {
        switch (id) {
            case R.id.btn_voice:             //静音按钮
                showToast("静音按钮");
            break;
            case R.id.btn_exit:             //退出按钮
                showToast("退出按钮");
                break;
            case R.id.btn_pre:              //上一首按钮
                showToast("上一首按钮");
                break;
            case R.id.btn_play:             //播放按钮
                showToast("播放按钮");
                break;
            case R.id.btn_next:             //下一首按钮
                showToast("下一首按钮");
                break;
            case R.id.btn_full_screen:     //全屏按钮
                showToast("全屏按钮");
                break;
            default:
                break;
        }

    }

    MediaPlayer.OnPreparedListener mOnPreparedListener
            = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
        }

    };

}
