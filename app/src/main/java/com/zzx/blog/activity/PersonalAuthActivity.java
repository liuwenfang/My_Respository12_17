package com.zzx.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.model.Response;
import com.zzx.blog.AppContext;
import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.bean.CommentBean;
import com.zzx.blog.bean.CommonBean;
import com.zzx.blog.bean.UserBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.RegexUtils;

/**
 * 实名认证
 */
public class PersonalAuthActivity extends BaseActivity {

    @BindView(R.id.edRealName)
    EditText edRealName;
    @BindView(R.id.edIDCard)
    EditText edIDCard;
    @BindView(R.id.edBankCard)
    EditText edBankCard;
    @BindView(R.id.edPhone)
    EditText edPhone;

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_auth);
    }

    @Override
    public void initView() {
        setTitle("实名认证");
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                if (checkReanNameAuth()) {
                    requestRealNameAuth();
                }
                break;
        }
    }

    private boolean checkReanNameAuth() {
        if (isEmpty(edRealName)) {
            showToast("请输入您的真实姓名");
            return false;
        } else if (!RegexUtils.checkUserName(edRealName.getText().toString())) {
            showToast("请输入正确的姓名");
            return false;
        } else if (isEmpty(edIDCard)) {
            showToast("请输入您的身份证号");
            return false;
        } else if (!RegexUtils.checkIdCard(edIDCard.getText().toString())) {
            showToast("身份证号格式错误");
            return false;
        } else if (isEmpty(edBankCard)) {
            showToast("请输入您的银行卡号");
            return false;
        } else if (!RegexUtils.checkBankCard(edBankCard.getText().toString())) {
            showToast("银行卡号格式错误");
            return false;
        }
        return true;
    }

    /**
     * 实名认证
     */
    private void requestRealNameAuth() {
        Map<String, Object> map = new HashMap<>();
        map.put("Identity", edIDCard.getText().toString());
        map.put("TrueName", edRealName.getText().toString());
        map.put("CardNo", edBankCard.getText().toString());
        HttpUtils.requestPosts(mContext, AppConfig.requestRealNameAuth, map, new JsonCallback<String>(mContext, "提交中...") {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.body() != null) {
                    JSONObject obj;
                    try {
                        obj = new JSONObject(response.body());
                        int limit = obj.getInt("limit");
                        UserBean userBean = AppContext.getInstance().getUserInfo();
                        userBean.setIssmrz(1);
                        userBean.setLimit(limit + "");
                        userBean.setRealName(edRealName.getText().toString());
                        AppContext.getInstance().saveUserInfo(userBean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        EventBus.getDefault().post(new CommonBean<>("authSuccess"));
                        startActivity(new Intent(mContext, SubmitSuccessActivity.class)
                                .putExtra("flag", SubmitSuccessActivity.FLAG_PERSONAL)
                                .putExtra("state", SubmitSuccessActivity.STATE_SUCCESS));
                        finish();
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                startActivity(new Intent(mContext, SubmitSuccessActivity.class)
                        .putExtra("flag", SubmitSuccessActivity.FLAG_PERSONAL)
                        .putExtra("state", SubmitSuccessActivity.STATE_FAILE));
                finish();
            }
        });
    }
}
