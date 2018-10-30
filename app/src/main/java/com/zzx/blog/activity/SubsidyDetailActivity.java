package com.zzx.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.bean.SubsidyDetailBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;
import com.zzx.blog.photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 补贴详情
 */
public class SubsidyDetailActivity extends BaseActivity {

    @BindView(R.id.tvAppName)
    TextView tvAppName;
    @BindView(R.id.tvLoanMoney)
    TextView tvLoanMoney;
    @BindView(R.id.tvInterestMoney)
    TextView tvInterestMoney;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.rlPhoto)
    RelativeLayout rlPhoto;

    private int subId;
    private SubsidyDetailBean detailBean;
    private List<String> photos;

    @Override
    public void initBundle(Bundle bundle) {
        subId = bundle.getInt("subId");
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_subsidy_detail);
    }

    @Override
    public void initView() {
        setTitle("补贴详情");
        photos = new ArrayList<>();
        requestSubsidyDetails();
    }

    @Override
    public void initLogic() {

    }

    /**
     * 补贴详情
     */
    private void requestSubsidyDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", subId);
        HttpUtils.requestPosts(mContext, AppConfig.requestSubsidyDetails, map, new JsonCallback<SubsidyDetailBean>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<SubsidyDetailBean> response) {
                if (response.body() != null) {
                    detailBean = response.body();
                    initUI();
                }
            }
        });
    }

    private void initUI() {
        tvAppName.setText(detailBean.getAppname());
        tvLoanMoney.setText(detailBean.getLoan() + "元");
        tvInterestMoney.setText(detailBean.getInterest() + "元");
        ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        loadImg(ivPhoto, detailBean.getImage());
        photos.add(detailBean.getImage());
    }

    @OnClick({R.id.ivPhoto, R.id.rlPhoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivPhoto:
                if (detailBean != null && !isEmpty(detailBean.getImage())) {
                    String path = photos.get(0);
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, photos.toArray(new String[]{}));
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FIRST, path);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                    startActivity(intent);
                }
                break;
            case R.id.rlPhoto:

                break;
        }
    }
}
