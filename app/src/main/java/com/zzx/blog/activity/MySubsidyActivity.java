package com.zzx.blog.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.fragment.SubsidyListFragment;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentPagerAdapter;
import butterknife.BindView;

/**
 * 我的补贴
 */
public class MySubsidyActivity extends BaseActivity {

    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private String[] mTitles = new String[]{"已申请补贴", "已完成补贴", "被拒绝补贴"};
    private List<Fragment> mFragments;

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_subsidy);
    }

    @Override
    public void initView() {
        setTitle("我的补贴");
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            SubsidyListFragment orderFragment = new SubsidyListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("status", i );
            orderFragment.setArguments(bundle);
            mFragments.add(orderFragment);
        }
        for (String title : mTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(((BaseActivity) mContext).getSupportFragmentManager(), mFragments, mTitles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void initLogic() {

    }
}
