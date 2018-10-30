package com.zzx.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.model.Response;
import com.zzx.blog.AppContext;
import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.bean.UserBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferenceUtils;
import utils.RegexUtils;

/**
 * 登陆
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edNum)
    EditText edNum;
    @BindView(R.id.edPwd)
    EditText edPwd;

    private String uesrPhone;

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        uesrPhone = PreferenceUtils.getStringPreferences(mContext, "userPhone", "");
        if (!isEmpty(uesrPhone)) {
            edNum.setText(uesrPhone);
        }
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.tvLogin, R.id.tvRegister, R.id.tvPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
//                PreferenceUtils.saveStringPreferences(mContext, TOKEN, "EC2280F232DF104BD3F7799F5935A04D");
//                startActivity(MainActivity.class);
                if (isEmpty(edNum)) {
                    showToast("请输入手机号");
                    return;
                } else if (!RegexUtils.checkPhone(edNum.getText().toString())) {
                    showToast("请输入正确的手机号");
                    return;
                } else if (isEmpty(edPwd)) {
                    showToast("请输入密码");
                    return;
                }
                login();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(mContext, RegisterActivity.class).putExtra("flag", RegisterActivity.FLAG_REGISTER));
                break;
            case R.id.tvPwd:
                startActivity(new Intent(mContext, RegisterActivity.class).putExtra("flag", RegisterActivity.FLAG_PWD));
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        Map<String, Object> map = new HashMap<>();
        map.put("Mobile", edNum.getText().toString());
        map.put("Psd", edPwd.getText().toString());
        HttpUtils.requestPosts(mContext, AppConfig.requestLogin, map, new JsonCallback<UserBean>(mContext, "登陆中...") {
            @Override
            public void onSuccess(Response<UserBean> response) {
                if (response.body() != null) {
                    PreferenceUtils.saveStringPreferences(mContext, "userPhone", edNum.getText().toString());
                    AppContext.getInstance().saveUserInfo(response.body());
                    showToast("登陆成功!");
                    startActivity(MainActivity.class);
                    finish();
                }
            }
        });
    }

}
