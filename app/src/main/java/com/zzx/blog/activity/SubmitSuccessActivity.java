package com.zzx.blog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 审核状态
 */
public class SubmitSuccessActivity extends BaseActivity {

    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvFinish)
    TextView tvFinish;
    @BindView(R.id.tvPrompt)
    TextView tvPrompt;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    public static int STATE_SUBMIT = 1; //审核状态
    public static int STATE_SUCCESS = 2;//审核成功
    public static int STATE_FAILE = 3;//审核失败

    public static int FLAG_BLOG = 1;//帖子审核
    public static int FLAG_PERSONAL = 2;//个人信息审核

    private int flag = 0, state = 0;

    @Override
    public void initBundle(Bundle bundle) {
        flag = bundle.getInt("flag");
        state = bundle.getInt("state");
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_submit_success);
    }

    @Override
    public void initView() {
        if (flag == FLAG_BLOG) {//
            setTitle("待审核");
            if (state == STATE_SUBMIT) {//状态提交
                ivState.setBackgroundResource(R.mipmap.img_submit);
            }
        } else if (flag == FLAG_PERSONAL) {//实名认证
            setTitle("实名认证");
            tvFinish.setVisibility(View.GONE);
            if (state == STATE_SUBMIT) {//状态提交
                ivState.setBackgroundResource(R.mipmap.img_submit);
            } else if (state == STATE_SUCCESS) {//成功
                tvPrompt.setText("恭喜，您的实名认证已通过!");
                ivState.setBackgroundResource(R.mipmap.img_state_success);
            } else if (state == STATE_FAILE) {//失败
                ivState.setBackgroundResource(R.mipmap.img_state_faile);
                tvPrompt.setText("抱歉，你的实名认证审核未通过!");
                tvMessage.setText("如有疑问请咨询客服");
            }
        }
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.tvFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvFinish:
                finish();
                break;
        }
    }
}
