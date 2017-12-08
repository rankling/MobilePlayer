package com.yzx.frank.moblieplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.interfaces.UiOperation;
import com.yzx.frank.moblieplayer.util.Utils;

/**
 * Activity的基类， 其他的Activity应该继承这个类
 * Created by Frank on 2017/12/5.
 */

public abstract class BaseActivity extends FragmentActivity implements UiOperation {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());    //多态
        View rootView = findViewById(android.R.id.content); //android.R.id.content 这个id可以获取activity的根view
        Utils.setButtonOnClickListener(rootView,this);
        initView();
        initListener();
        initData();
    }

    /**
     * 查找View， 这个方法可以省去强制转换的操作
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findView(int id) {
        T view = (T) findViewById(id);
        return view;
    }



    /**
     * 在屏幕中间显示一个Toast
     * @param text  传入想要显示的一段文本
     */
    public void showToast(String text) {
        Utils.showToast(this, text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                //如果单击的不是返回按钮，那么就由子类处理
                onClick(view, view.getId());
                break;
        }
    }



}
