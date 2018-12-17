package com.zzx.blog.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hedan.basedialoglibrary.BaseDialog;
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
    @BindView(R.id.ivRecommend)
    ImageView ivRecommend;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.tvRecommend)
    TextView tvRecommend;

    private Fragment[] fragments;
    private FragmentTransaction ft;
    private FragmentManager manager;
    private int selIndex = 0;
    private BaseDialog dialog;


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
        fragments = new Fragment[5];
        fragments[0] = manager.findFragmentById(R.id.frgHomepage);
        fragments[1] = manager.findFragmentById(R.id.frgRecommend);
        fragments[2] = manager.findFragmentById(R.id.frgBlog);
        fragments[3] = manager.findFragmentById(R.id.frgPersonal);
        ft.hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3]);
        ft.show(fragments[selIndex]).commit();
        tvHomepage.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        ivHomepage.setBackgroundResource(R.mipmap.img_homepage_green);
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.llHomepage, R.id.llBlog, R.id.llPersonal, R.id.llRecommend, R.id.ivAdd})
    public void onViewClicked(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.llHomepage:
                initSelect(0);
                tvHomepage.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivHomepage.setBackgroundResource(R.mipmap.img_homepage_green);
                break;
            case R.id.llRecommend:
                initSelect(1);
                tvRecommend.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivRecommend.setBackgroundResource(R.mipmap.icon_recommend_green);
                break;
            case R.id.llBlog:
                initSelect(2);
                tvBlog.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivBlog.setBackgroundResource(R.mipmap.img_blog_green);
                break;
            case R.id.llPersonal:
                initSelect(3);
                tvPersonal.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                ivPersonal.setBackgroundResource(R.mipmap.img_personal_green);
                break;
            case R.id.ivAdd:
                showDialog(Gravity.BOTTOM, R.style.Bottom_Top_aniamtion);
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
        tvRecommend.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayText));

        ivRecommend.setBackgroundResource(R.mipmap.icon_recommend);
        ivHomepage.setBackgroundResource(R.mipmap.img_homepage_gray);
        ivBlog.setBackgroundResource(R.mipmap.img_blog_gray);
        ivPersonal.setBackgroundResource(R.mipmap.img_personal_gray);
    }

    private void showDialog(int grary, int animationStyle) {
        BaseDialog.Builder builder = new BaseDialog.Builder(this);
        dialog = builder.setViewId(R.layout.dialog_post_message)
                .setPaddingdp(10, 0, 10, 0)//设置dialogpadding
                .setGravity(grary)//设置显示位置
                .setAnimation(animationStyle)//设置动画
                .setWidthHeightpx(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)//设置dialog的宽高
                .isOnTouchCanceled(true)//设置触摸dialog外围是否关闭dialog
                .addViewOnClickListener(R.id.llSendBlog,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startActivity(SendBlogActivity.class);
                        dialog.close();
                    }
                })
                .addViewOnClickListener(R.id.llMyBlog,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startActivity(MyBlogListActivity.class);
                        dialog.close();
                    }
                })
                .addViewOnClickListener(R.id.iv_cancel_message, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.close();
                    }
                })//设置监听事件


                .builder();
        dialog.show();
    }

}
