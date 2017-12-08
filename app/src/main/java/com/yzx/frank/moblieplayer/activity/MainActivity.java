package com.yzx.frank.moblieplayer.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.adapter.MainAdapter;
import com.yzx.frank.moblieplayer.fragment.AudioFragment;
import com.yzx.frank.moblieplayer.fragment.VideoFragment;
import com.yzx.frank.moblieplayer.util.Utils;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {
    TextView tv_video ;
    TextView tv_audio ;
    View view_indicator;
    ViewPager view_pager ;
    private int indicatorWidth;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        tv_video = findView(R.id.tv_video);
        tv_audio = findView(R.id.tv_audio);
        view_indicator = findView(R.id.view_indicator);
        view_pager = findView(R.id.view_pager);

        initIndicator();

    }

    private void initIndicator() {
        int screenWidth = Utils.getScreenWidth(this);
        indicatorWidth = screenWidth >>1;//屏幕宽度的一半
        view_indicator.getLayoutParams().width =indicatorWidth ;
        view_indicator.requestLayout();//通知这个View云更新它的参数
    }
    @Override
    public void initData() {
        changeTitleTextStatus(true);
        initViewPager();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new VideoFragment());
        fragments.add(new AudioFragment());
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        tv_video.setOnClickListener(this);
        tv_audio.setOnClickListener(this);


        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 当页面滚动的时候
             *
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                scrollIndicator(position, positionOffset);
            }

            /**
             * 当某一页被选择的时候
             *
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                changeTitleTextStatus(position==0);
            }

            /**
             * 当页面滚动状态发生改变的时候
             *
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 滚动指示线
     * @param position
     * @param positionOffset
     */
    private void scrollIndicator(int position, float positionOffset) {
        float translationX;
        translationX = position*indicatorWidth+indicatorWidth*positionOffset;
        ViewHelper.setTranslationX(view_indicator, translationX);
    }

    @Override
    public void onClick(View view, int id) {
        switch (id) {
            case R.id.tv_video:
                view_pager.setCurrentItem(0);
                break;
            case R.id.tv_audio:
                view_pager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    /**
     * 改变标题状态
     * @param isSelectVideo 是否选择了视频
     */
    private void changeTitleTextStatus(boolean isSelectVideo) {
        //改变文本颜色
        tv_video.setSelected(isSelectVideo);
        tv_audio.setSelected(!isSelectVideo);

        //改变字体大小
        scaleTitle(isSelectVideo ? 1.2f : 1.0f, tv_video);
        scaleTitle(!isSelectVideo ? 1.2f : 1.0f, tv_audio);
    }

    /**
     * 缩放标题
     * @param scale 缩放比例
     * @param textView
     */
    private void scaleTitle(float scale, TextView textView) {

        ViewPropertyAnimator.animate(textView).scaleX(scale).scaleY(scale);
    }

}
