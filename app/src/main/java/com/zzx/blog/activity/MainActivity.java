package com.zzx.blog.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StatusBarUtils;
/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.ivHomepage)
    ImageView ivHomepage;
    @BindView(R.id.tvHomepage)
    TextView tvHomepage;
    @BindView(R.id.ivBlog)
    ImageView ivBlog;
    @BindView(R.id.tvBlog)
    TextView tvBlog;
    @BindView(R.id.ivPersonal)
    ImageView ivPersonal;
    @BindView(R.id.tvPersonal)
    TextView tvPersonal;

    private Fragment[] fragments;
    private FragmentTransaction ft;
    private FragmentManager manager;
    private int selIndex = 0;


    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        StatusBarUtils.translucent(this);

        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        fragments = new Fragment[4];
        fragments[0] = manager.findFragmentById(R.id.frgHomepage);
        fragments[1] = manager.findFragmentById(R.id.frgBlog);
        fragments[2] = manager.findFragmentById(R.id.frgPersonal);
        ft.hide(fragments[0]).hide(fragments[1]).hide(fragments[2]);
        ft.show(fragments[selIndex]).commit();
        tvHomepage.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        ivHomepage.setBackgroundResource(R.mipmap.img_homepage_green);
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.llHomepage, R.id.llBlog, R.id.llPersonal})
    public void onViewClicked(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.llHomepage:
                initSelect(0);
                tvHomepage.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivHomepage.setBackgroundResource(R.mipmap.img_homepage_green);
                break;
            case R.id.llBlog:
                initSelect(1);
                tvBlog.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivBlog.setBackgroundResource(R.mipmap.img_blog_green);
                break;
            case R.id.llPersonal:
                initSelect(2);
                tvPersonal.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivPersonal.setBackgroundResource(R.mipmap.img_personal_green);
                break;
        }
    }

    private void initSelect(int index) {
        if (selIndex == index) {
            return;
        }
        ft.hide(fragments[selIndex]);
        selIndex = index;
        ft.show(fragments[selIndex]);
        ft.commit();
        tvHomepage.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayText));
        tvBlog.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayText));
        tvPersonal.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayText));

        ivHomepage.setBackgroundResource(R.mipmap.img_homepage_gray);
        ivBlog.setBackgroundResource(R.mipmap.img_blog_gray);
        ivPersonal.setBackgroundResource(R.mipmap.img_personal_gray);
    }

}
