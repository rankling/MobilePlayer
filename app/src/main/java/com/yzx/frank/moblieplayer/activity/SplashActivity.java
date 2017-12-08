package com.yzx.frank.moblieplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.yzx.frank.moblieplayer.R;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 * Created by Frank on 2017/12/5.
 */

public class SplashActivity extends BaseActivity {
    private Handler handler;
    public static final int MY_REQUEST_CODE		= 100;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        requestPermissions();
    }

    private void requestPermissions() {

        PermissionGen.with(this)
                .addRequestCode(MY_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();

    }

    private void delayEnterHome() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enterHome();
            }
        }, 3000);
    }

    /**
     * 延迟3秒进入首页
     */
    protected void enterHome () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    /**
     * 进入首页
     * @param view 单击的控件
     * @param id    单击控件的id
     */
    @Override
    public void onClick(View view, int id) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacksAndMessages(null);
                enterHome();

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = MY_REQUEST_CODE)
    public void doSomething(){
       // showToast("获得访问外部存储的权限");
        delayEnterHome();//权限获取成功才进入主页，否则退出
    }


    @PermissionFail(requestCode = MY_REQUEST_CODE)
    public void doFailSomething(){
        showToast("您已拒绝访问本机的外部存储，请重新授权！");
        finish();
    }

}
