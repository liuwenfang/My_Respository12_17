package com.zzx.blog.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zzx.blog.AppContext;
import com.zzx.blog.R;
import com.zzx.blog.activity.LoginActivity;
import com.zzx.blog.activity.MyBlogListActivity;
import com.zzx.blog.activity.MySubsidyActivity;
import com.zzx.blog.activity.PersonalAuthActivity;
import com.zzx.blog.activity.WebViewActivity;
import com.zzx.blog.base.BaseFragment;
import com.zzx.blog.bean.CommonBean;
import com.zzx.blog.bean.UserBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;
import com.zzx.blog.myinterface.EventInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 个人中心
 *
 * @author zzx
 */

public class PersonalFragment extends BaseFragment {


    @BindView(R.id.llQuota)
    LinearLayout llQuota;
    @BindView(R.id.llUser)
    LinearLayout llUser;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvUserName)
    TextView tvUserName;

    private UserBean userBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initBundle(Bundle savedInstanceState) {

    }


    @Override
    public void initView() {
        userBean = AppContext.getInstance().getUserInfo();
        if (userBean.getIssmrz() == 0) { //未认证
            llQuota.setVisibility(View.VISIBLE);
            llUser.setVisibility(View.GONE);
        } else { //已认证  显示姓名和额度
            llQuota.setVisibility(View.GONE);
            llUser.setVisibility(View.VISIBLE);
            tvLimit.setText(userBean.getLimit() + "");//额度
            tvUserName.setText(userBean.getRealName() + "");//人名
        }
    }

    @Override
    public void initLogic() {
        setEventInterface(new EventInterface() {
            @Override
            public void setEvent(CommonBean enity) {
                if (enity.getType().equals("authSuccess")) { //认证成功
                    initView();
                }
            }
        });
    }

    @OnClick({R.id.llQuota, R.id.llUser, R.id.llApplySubsidy, R.id.llMyBlog, R.id.llUseRule, R.id.llLaw, R.id.llLogOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llQuota:
                if (userBean.getIssmrz() == 0) { //未认证 无额度
                    startActivity(PersonalAuthActivity.class);
                }
                break;
            case R.id.llUser:
//                llQuota.setVisibility(View.VISIBLE);
//                llUser.setVisibility(View.GONE);
                break;
            case R.id.llApplySubsidy:
                startActivity(MySubsidyActivity.class);
                break;
            case R.id.llMyBlog:
                startActivity(MyBlogListActivity.class);
                break;
            case R.id.llUseRule://使用规则
                requestWeb("sygz");
                break;
            case R.id.llLaw://法律责任
                requestWeb("flzr");
                break;
            case R.id.llLogOut://退出
                showPromatDialog();
                break;
        }
    }


    /**
     * 使用规则 sygz 、法律责任 flzr
     */
    private void requestWeb(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        HttpUtils.requestPosts(mContext, AppConfig.requestWeb, map, new JsonCallback<String>(mContext, "登陆中...") {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.body() != null) {
                    JSONObject obj;
                    try {
                        obj = new JSONObject(response.body());
                        String url = obj.getString("url");
                        startActivity(new Intent(mContext, WebViewActivity.class).putExtra("mUrl", url));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }
        });
    }

    private AlertDialog promptDialog;

    /**
     * 发货提醒
     */
    private void showPromatDialog() {
        if (promptDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle("退出提醒")
                    .setMessage("确定退出吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AppContext.getInstance().cleanUserInfo();
                            startActivity(LoginActivity.class);
                            ((Activity) mContext).finish();
                        }
                    });
            promptDialog = builder.create();
        }
        promptDialog.show();
    }
}
