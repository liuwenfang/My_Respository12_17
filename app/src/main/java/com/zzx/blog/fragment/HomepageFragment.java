package com.zzx.blog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.model.Response;
import com.xiaosu.DataSetAdapter;
import com.xiaosu.VerticalRollingTextView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zzx.blog.R;
import com.zzx.blog.activity.WebViewActivity;
import com.zzx.blog.adapter.HotLoanAdapter;
import com.zzx.blog.base.BaseFragment;
import com.zzx.blog.bean.BannerBean;
import com.zzx.blog.bean.LoanSuperBean;
import com.zzx.blog.bean.MessageBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;
import com.zzx.blog.image.GlideImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ZRecyclerViewAdapter;
import adapter.decoration.SpaceDecoration;
import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import utils.DensityUtils;


/**
 * 个人中心
 *
 * @author zzx
 */

public class HomepageFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.mBanner)
    Banner mBanner;
    @BindView(R.id.mRollingTextView)
    VerticalRollingTextView mRollingTextView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    private List<LoanSuperBean> beanList;
    private HotLoanAdapter loanAdapter;

    private List<MessageBean> messageBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void initBundle(Bundle savedInstanceState) {

    }


    @Override
    public void initView() {
        bannerBeans = new ArrayList<>();
        messageBeans = new ArrayList<>();
        initRecyclerView();
        requestGetBanner();
        requestLoanGetMsg();
    }

    private void initRecyclerView() {
        mRefreshLayout.setDelegate(this);
        beanList = new ArrayList<>();
        loanAdapter = new HotLoanAdapter(mRecyclerView, beanList);
        loanAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("mUrl", beanList.get(position).getUrl()));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceDecoration(DensityUtils.dip2px(mContext, 5)));
        mRecyclerView.setAdapter(loanAdapter);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void initLogic() {

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pageIndex = 1;
        beanList.clear();
        requestLoanSuperList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pageIndex++;
        requestLoanSuperList();
        return true;
    }

    /**
     * 获取banner
     */
    private void requestGetBanner() {
        Map<String, Object> map = new HashMap<>();
        HttpUtils.requestPosts(mContext, AppConfig.requestGetBanner, map, new JsonCallback<List<BannerBean>>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<List<BannerBean>> response) {
                if (response != null && response.body().size() > 0) {
                    bannerBeans.addAll(response.body());
                    initBanner();
                }
            }
        });
    }

    private List<BannerBean> bannerBeans;
    private List<String> bannerUrls;

    /**
     * banner
     */
    private void initBanner() {
        bannerUrls = new ArrayList<>();
        for (int i = 0; i < bannerBeans.size(); i++) {
            bannerUrls.add(AppConfig.photoUrl + bannerBeans.get(i).getImage());
//            bannerUrls.add("http://img.910ok.com/" + bannerModels.get(i).getUrl());
        }
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("mUrl", bannerBeans.get(position).getUrl()));
            }
        });
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(bannerUrls);
        mBanner.start();
    }

    private int pageIndex = 1;

    /**
     * 贷超
     */
    private void requestLoanSuperList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        HttpUtils.requestPosts(mContext, AppConfig.requestLoanSuperList, map, new JsonCallback<List<LoanSuperBean>>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<List<LoanSuperBean>> response) {
                if (response != null && response.body().size() > 0) {
                    beanList.addAll(response.body());
                } else if (pageIndex > 1) {
                    pageIndex--;
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                loanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<List<LoanSuperBean>> response) {
                super.onError(response);
                if (pageIndex > 1) {
                    pageIndex--;
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
            }
        });
    }

    /**
     * 消息
     */
    private void requestLoanGetMsg() {
        HttpUtils.requestPosts(mContext, AppConfig.requestGetMsg, null, new JsonCallback<List<MessageBean>>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<List<MessageBean>> response) {
                if (response != null && response.body().size() > 0) {
                    messageBeans.addAll(response.body());
                    initRollingTextView();
                }
            }
        });
    }

    private void initRollingTextView() {
        DataSetAdapter dataSetAdapter = new DataSetAdapter(messageBeans) {
            @Override
            protected String text(Object o) {
                MessageBean messageBean = (MessageBean) o;
                return messageBean.getMsgContent() + "";
            }

        };
        mRollingTextView.setDataSetAdapter(dataSetAdapter);
        mRollingTextView.run();
    }
}
