package com.zzx.blog;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zzx.blog.bean.UserBean;
import com.zzx.blog.storage.Storage;

import utils.StringUtils;
import utils.ToastUtils;

/**
 * Created by Administrator on 2018/4/21.
 */

public class AppContext extends Application {
    public static AppContext appContext;
    private static String IFLY_ID = "5b32f08f";

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
//        Tiny.getInstance().init(this);
    }

    public static AppContext getInstance() {
        if (appContext == null)
            return new AppContext();
        return appContext;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取缓存用户信息
     *
     * @return
     */
    public UserBean getUserInfo() {
        return Storage.getUserInfo() == null ? new UserBean() : Storage
                .getUserInfo();
    }

    /**
     * 保存缓存用户信息
     *
     * @param user
     */
    public void saveUserInfo(final UserBean user) {
        if (user != null) {
            Storage.ClearUserInfo();
            Storage.saveUsersInfo(user);
        }
    }

    /**
     * 用户存在是ture 否则是false
     *
     * @return
     */
    public boolean checkUser() {
        if (StringUtils.isEmpty(getUserInfo().getToken())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 清除缓存用户信息
     *
     * @param
     */
    public void cleanUserInfo() {
        Storage.ClearUserInfo();
    }
}
