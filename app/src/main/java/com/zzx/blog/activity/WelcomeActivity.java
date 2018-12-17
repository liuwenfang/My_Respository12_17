package com.zzx.blog.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.zzx.blog.AppContext;
import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;

import utils.PermissionUtils;
import utils.ZZXAnimationUtils;

/**
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {


    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
    }


    @Override
    public void initView() {
        ImageView ivWelcome = findViewById(R.id.ivWelcome);
//                ViewAnimationUtils.invisibleViewByAlpha(ivWelcome, 1500);
        AlphaAnimation alphaAnimation = ZZXAnimationUtils.getAlphaAnimation(0, 1, 1500);
        ivWelcome.setAnimation(alphaAnimation);
        alphaAnimation.start();
        handlerLogin.sendEmptyMessageDelayed(1, 1500);
//        checkPermission();
    }

    @SuppressLint("HandlerLeak")
    Handler handlerLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (AppContext.getInstance().checkUser()) {
                startActivity(MainActivity.class);
            } else {
                startActivity(LoginActivity.class);
            }
            WelcomeActivity.this.finish();
        }
    };

    /**
     * 判断权限
     */
    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO};
        PermissionUtils.requestPermissions(mContext, 311, permissions, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                ImageView ivWelcome = findViewById(R.id.ivWelcome);
                AlphaAnimation alphaAnimation = ZZXAnimationUtils.getAlphaAnimation(0, 1, 1500);
                ivWelcome.setAnimation(alphaAnimation);
                alphaAnimation.start();
                handlerLogin.sendEmptyMessageDelayed(1, 1500);
            }

            @Override
            public void onPermissionDenied() {
                PermissionUtils.showTipsDialog(mContext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void initLogic() {

    }
}
