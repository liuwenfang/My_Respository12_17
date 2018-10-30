package com.zzx.blog.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.EncryptUtils;
import utils.PreferenceUtils;
import utils.RegexUtils;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.edNum)
    EditText edNum;
    @BindView(R.id.edName)
    EditText edName;
    @BindView(R.id.edPwd)
    EditText edPwd;
    @BindView(R.id.edPwdCon)
    EditText edPwdCon;
    @BindView(R.id.edCode)
    EditText edCode;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.llConPwd)
    LinearLayout llConPwd;
    @BindView(R.id.llName)
    LinearLayout llName;
    @BindView(R.id.viewLineName)
    View viewLineName;

    private String userName;

    public static int FLAG_REGISTER = 1;
    public static int FLAG_PWD = 2;
    private int flag;

    @Override
    public void initBundle(Bundle bundle) {
        flag = bundle.getInt("flag");
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initView() {
        userName = PreferenceUtils.getStringPreferences(mContext, "userName", "");
        if (!isEmpty(userName)) {
            edNum.setText(userName);
        }
        if (flag == FLAG_REGISTER) {
            mToolbar.setTitle("注册");
            tvRegister.setText("立 即 注 册");
        } else if (flag == FLAG_PWD) {
            llConPwd.setVisibility(View.VISIBLE);
            llName.setVisibility(View.GONE);
            viewLineName.setVisibility(View.GONE);
            mToolbar.setTitle("忘记密码");
            tvRegister.setText("确定");
        }
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.tvRegister, R.id.tvCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCode:
                if (isEmpty(edNum)) {
                    showToast("请输入用户名");
                    return;
                } else if (!RegexUtils.checkMobile(edNum.getText().toString())) {
                    showToast("手机号格式不正确");
                    return;
                }
                getCode();
                break;
            case R.id.tvRegister:
                if (isEmpty(edNum)) {
                    showToast("请输入您的手机号");
                    return;
                } else if (!RegexUtils.checkMobile(edNum.getText().toString())) {
                    showToast("手机号格式不正确");
                    return;
                } else if (isEmpty(edCode)) {
                    showToast("请输入您的验证码");
                    return;
                } else if (isEmpty(edPwd)) {
                    showToast("请输入您的密码");
                    return;
                }
                if (flag == FLAG_REGISTER) {
                    if (isEmpty(edName)) {
                        showToast("请输入您的昵称");
                        return;
                    }
                    register();
                } else if (flag == FLAG_PWD) {
                    if (isEmpty(edPwdCon)) {
                        showToast("请输入您的确认密码");
                        return;
                    } else if (!edPwd.getText().toString().equals(edPwdCon.getText().toString())) {
                        showToast("两次密码不一致");
                        return;
                    }
                    forgetPwd();
                }
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("MobilePwd", EncryptUtils.MD5(edNum.getText().toString() + "|_sc901@Pwmd5"));
        map.put("Mobile", edNum.getText().toString());
        String url = "";
        if (flag == FLAG_REGISTER) {
            url = AppConfig.requestSendCode;
        } else if (flag == FLAG_PWD) {
            url = AppConfig.requestSendCodeByRePwd;
        }
        HttpUtils.requestPosts(mContext, url, map, new JsonCallback<String>(mContext, "获取中...") {
            @Override
            public void onSuccess(Response<String> response) {
                showToast("获取成功!");
                tvCode.setClickable(false);
                tvCode.setTextColor(getResources().getColor(R.color.colorTheme));
                tvCode.setBackgroundResource(R.drawable.shape_gray_5);
                countDown();
            }
        });
    }

    private CountDownTimer timer;

    private void countDown() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCode.setText(millisUntilFinished / 1000 + "S");
            }

            @Override
            public void onFinish() {
                tvCode.setBackgroundResource(R.drawable.selector_submit_5);
                tvCode.setClickable(true);
                tvCode.setText("获取验证码");
            }
        };
        // 调用start方法开始倒计时
        timer.start();
    }

    /**
     * 注册
     */
    private void register() {
        Map<String, Object> map = new HashMap<>();
        map.put("Mobile", edNum.getText().toString());
        map.put("Psd", edPwd.getText().toString());
        map.put("Code", edCode.getText().toString());
        map.put("NickName", edName.getText().toString());
        HttpUtils.requestPosts(mContext, AppConfig.requestRegister, map, new JsonCallback<String>(mContext, "注册中...") {
            @Override
            public void onSuccess(Response<String> response) {
                PreferenceUtils.saveStringPreferences(mContext, "userPhone", edNum.getText().toString());
                showToast("注册成功!");
                finish();
            }
        });
    }

    /**
     * 忘记密码
     */
    private void forgetPwd() {
        Map<String, Object> map = new HashMap<>();
        map.put("Mobile", edNum.getText().toString());
        map.put("Psd", edPwd.getText().toString());
        map.put("Code", edCode.getText().toString());
        HttpUtils.requestPosts(mContext, AppConfig.requestResetPassword, map, new JsonCallback<String>(mContext, "修改中...") {
            @Override
            public void onSuccess(Response<String> response) {
                PreferenceUtils.saveStringPreferences(mContext, "userPhone", edNum.getText().toString());
                showToast("修改成功!");
                finish();
            }
        });
    }
}
