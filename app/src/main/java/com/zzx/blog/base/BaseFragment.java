package com.zzx.blog.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zzx.blog.R;
import com.zzx.blog.bean.CommonBean;
import com.zzx.blog.myinterface.EventInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import base.ZBaseFragment;
import butterknife.ButterKnife;

/**
 * Created by zzx on 2017/12/28 0028.
 */

public abstract class BaseFragment extends ZBaseFragment {
    protected Context mContext;
    protected View mRoot;
    protected Toolbar mToolbar;
    protected TextView tvTitle, tvRight;
    protected RelativeLayout rlRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundle(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null) {
                parent.removeView(mRoot);
            }
        } else {
            mRoot = inflater.inflate(getLayoutId(), null);
            mToolbar = mRoot.findViewById(R.id.mToolbar);
            ButterKnife.bind(this, mRoot);
            EventBus.getDefault().register(this);
        }
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != mToolbar) {
            ((BaseActivity) mContext).setSupportActionBar(mToolbar);
            ((BaseActivity) mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);
            initTitle();
        }
        initView();
        initLogic();
    }

    protected void initTitle() {
        tvTitle = mRoot.findViewById(R.id.tvTitle);
        rlRight = mRoot.findViewById(R.id.rlRight);
        tvRight = mRoot.findViewById(R.id.tvRight);
    }

    protected void setTitle(String title) {
        if (null != tvTitle)
            tvTitle.setText(title);
    }

    private EventInterface eventInterface;

    /**
     * EventBus接收消息
     *
     * @param commonBean 消息接收
     */
    @Subscribe
    public void onEventMainThread(CommonBean commonBean) {
        if (eventInterface != null) {
            eventInterface.setEvent(commonBean);
        }
    }

    public void setEventInterface(EventInterface eventInterface) {
        if (eventInterface != null) {
            this.eventInterface = eventInterface;
        }
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
        intent.setClass(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 判空
     */
    public boolean isEmpty(Object object) {
        if (((BaseActivity) mContext).isEmpty(object)) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(View view) {
        if (((BaseActivity) mContext).isEmpty(view)) {
            return true;
        }
        return false;
    }

    public void showToast(String msg) {
        ((BaseActivity) mContext).showToast(msg);
    }

    public void showToast(int msg) {
        ((BaseActivity) mContext).showToast(msg);
    }

    public void loadImg(ImageView imageView, String imageUrl) {
        ((BaseActivity) mContext).loadImg(imageView, imageUrl);
    }

    public void loadImg(ImageView imageView, String imageUrl, int res) {
        ((BaseActivity) mContext).loadImg(imageView, imageUrl, res);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
