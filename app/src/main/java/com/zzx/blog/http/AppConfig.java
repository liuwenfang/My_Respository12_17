package com.zzx.blog.http;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * 全局配置
 */
public class AppConfig {

    // 标记程序是否第一次打开的Key
    public final String FIRST_OPEN_KEY = "first_open";
    public static String mainUrl = "http://ts.910ok.com/";//测试服地址
    public static String photoUrl = "http://img.910ok.com/";
    /**
     * 注册验证码
     */
    public static String requestSendCode = mainUrl + "LunTan/SendCode";
    /**
     * 注册验证码  忘记密码
     */
    public static String requestSendCodeByRePwd = mainUrl + "LunTan/SendCodeByRePwd";
    /**
     * 注册
     */
    public static String requestRegister = mainUrl + "LunTan/Reg";
    /**
     * 忘记密码
     */
    public static String requestResetPassword = mainUrl + "LunTan/ResetPassword";
    /**
     * 登录
     */
    public static String requestLogin = mainUrl + "LunTan/Login";
    /**
     * 获取banner
     */
    public static String requestGetBanner = mainUrl + "LunTan/GetBanner";
    /**
     * 实名认证
     */
    public static String requestRealNameAuth = mainUrl + "LunTan/RealNameAuth";
    /**
     * 论坛帖子列表
     */
    public static String requestNoteList = mainUrl + "LunTan/NoteList";
    /**
     * 我的帖子列表
     */
    public static String requestMyNote = mainUrl + "LunTan/MyNote";
    /**
     * 论坛发帖
     */
    public static String requestAddNote = mainUrl + "LunTan/AddNote";
    /**
     * 帖子详情
     */
    public static String requestNoteDetails = mainUrl + "LunTan/NoteDetails";
    /**
     * 发表评论
     */
    public static String requestAddComment = mainUrl + "LunTan/AddComment";
    /**
     * 申请补贴
     */
    public static String requestAddSubsidy = mainUrl + "LunTan/AddSubsidy";
    /**
     * 补贴列表
     */
    public static String requestSubsidyList = mainUrl + "LunTan/SubsidyList";
    /**
     * 补贴详情
     */
    public static String requestSubsidyDetails = mainUrl + "LunTan/SubsidyDetails";
    /**
     * 消息
     */
    public static String requestGetMsg = mainUrl + "LunTan/GetMsg";
    /**
     * 贷超
     */
    public static String requestLoanSuperList = mainUrl + "LunTan/LoanSuperList";
    /**
     * 使用规则 sygz 、法律责任 flzr
     */
    public static String requestWeb = mainUrl + "LunTan/SinglePage";

    /**
     * 检查版本号 hnLoginName
     */
    public String checkVersion = "http://www.hefeiapp.cn/checkVersion?entity.appCode=LouYu&entity.appType=0";
    //    public  String checkVersion = "http://www.hefeiapp.cn/checkVersion?entity.appCode=YouFuBao&entity.appType=0";
    public Dialog dialog = null;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

}
