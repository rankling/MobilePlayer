package com.yzx.frank.moblieplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yzx.frank.moblieplayer.R;
import com.yzx.frank.moblieplayer.interfaces.UiOperation;
import com.yzx.frank.moblieplayer.util.Utils;

/**
 * Fragment的基类， 其它Fragment应该继承这个类
 * Created by Frank on 2017/12/6.
 */

public abstract class BaseFragment extends Fragment implements UiOperation{


    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResId(), null);
        Utils.setButtonOnClickListener(rootView,this);
        initView();
        initListener();
        initData();
        return rootView;
    }


    /**
     * 查找View， 这个方法可以省去强制转换的操作
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findView(int id) {
        T view = (T) rootView.findViewById(id);
        return view;
    }


    /**
     * 在屏幕中间显示一个Toast
     * @param text  传入想要显示的一段文本
     */
    public void showToast(String text) {
        Utils.showToast(getActivity(), text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().finish();
                break;
            default:
                //如果单击的不是返回按钮，那么就由子类处理
                onClick(view, view.getId());
                break;
        }
    }
}
