package com.yzx.frank.moblieplayer.interfaces;

import android.view.View;

/**
 * UI 操作接口
 * Created by Frank on 2017/12/6.
 */

public interface UiOperation extends View.OnClickListener {


    /**
     * 返回一个用于显示界面的布局id
     */
    int getLayoutResId();

    /**
     * 初始化View findViewById的方法写到这个方法里面
     */
    void initView();

    /**
     * 初始化监听器
     */
    void initListener();
    /**
     * 初始化数据并且显示到界面上
     */
    void initData();
    /**
     * 单击事件在这个方法内处理
     * @param view 单击的控件
     * @param id    单击控件的id
     */

    void onClick(View view, int id);



}
