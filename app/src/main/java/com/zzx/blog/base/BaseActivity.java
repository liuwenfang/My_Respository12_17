package com.zzx.blog.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzx.blog.AppContext;
import com.zzx.blog.R;
import com.zzx.blog.bean.CommonBean;
import com.zzx.blog.bean.UserBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.myinterface.EventInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import base.ZBaseActivity;
import butterknife.ButterKnife;
import dialog.LoadingDialog;
import slide.SlideLayout;
import utils.EmptyUtils;
import utils.ToastUtils;


/**
 * Created by zzx on 2017/12/28 0028.
 */

public abstract class BaseActivity extends ZBaseActivity {
    protected Context mContext;
    protected Toolbar mToolbar;
    protected TextView tvTitle, tvRight;
    protected RelativeLayout rlRight;
    protected ImageView ivRight;
    protected boolean canSlide = false;
    protected UserBean userBean;
    protected String TOKEN = "token";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundle(getIntent().getExtras());
        initContentView(savedInstanceState);
        if (canSlide) {
            SlideLayout rootView = new SlideLayout(this);
            rootView.bindActivity(this);
        }
        mContext = this;
        mToolbar = findViewById(R.id.mToolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            initTitle();
        }
        if (AppContext.getInstance().checkUser()) {
            userBean = AppContext.getInstance().getUserInfo();
        }
        initView();
        initLogic();
        EventBus.getDefault().register(this);
    }

    public void loadImg(ImageView imageView, String imageUrl) {
        if (!imageUrl.startsWith("http")) {
            imageUrl = AppConfig.photoUrl + imageUrl;
        }
        Glide.with(mContext)
                .load(imageUrl)
                .thumbnail(0.1f)
                .into(imageView);
    }

    public void loadImg(ImageView imageView, String imageUrl, int res) {
        if (!imageUrl.startsWith("http")) {
            imageUrl = AppConfig.photoUrl + imageUrl;
        }
        Glide.with(mContext)
                .load(imageUrl)
                .thumbnail(0.1f)
                .error(res)
                .into(imageView);
    }


    private EventInterface eventInterface;

    /**
     * EventBus接收消息
     *
     * @param commonBean 消息接收
     */
    @Subscribe
    public void onEventMainThread(CommonBean commonBean) {
        if (commonBean.getType().equals("logOverdue")) {//登陆过期
            finish();
        }
        if (eventInterface != null) {
            eventInterface.setEvent(commonBean);
        }
    }

    public void setEventInterface(EventInterface eventInterface) {
        if (eventInterface != null) {
            this.eventInterface = eventInterface;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    protected void initTitle() {
        tvTitle = findViewById(R.id.tvTitle);
        rlRight = findViewById(R.id.rlRight);
        ivRight = findViewById(R.id.ivRight);
        tvRight = findViewById(R.id.tvRight);
        mToolbar.setNavigationIcon(R.mipmap.img_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setTitle(String title) {
        if (null != tvTitle)
            tvTitle.setText(title);
    }

    /**
     * 页面跳转
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected boolean isEmpty(Object obj) {
        if (EmptyUtils.isEmpty(obj)) {
            return true;
        }
        return false;
    }

    protected boolean isEmpty(View view) {
        if (view instanceof EditText) {
            if (isEmpty(((EditText) view).getText().toString())) {
                return true;
            }
        } else if (view instanceof TextView) {
            if (isEmpty(((TextView) view).getText().toString())) {
                return true;
            }
        }
        return false;
    }

    public void showToast(String msg) {
        ToastUtils.showTextToast(mContext, msg);
    }

    public void showToast(int msg) {
        ToastUtils.showTextToast(mContext, msg);
    }

    public LoadingDialog loadingDialog;
    private LoadingDialog.Builder builder;

    public void showProDialog(String msg) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }
        if (loadingDialog == null) {
            builder = new LoadingDialog.Builder(mContext)
                    .setShowMessage(true);
        }
        if (EmptyUtils.isEmpty(msg)) {
            builder.setShowMessage(false);
            builder.setMessage("");
        } else {
            builder.setShowMessage(true);
            builder.setMessage(msg);
        }
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
