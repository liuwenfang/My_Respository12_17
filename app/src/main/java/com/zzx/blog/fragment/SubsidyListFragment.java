package com.zzx.blog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.model.Response;
import com.zzx.blog.R;
import com.zzx.blog.activity.ApplySubsidyActivity;
import com.zzx.blog.activity.SubsidyDetailActivity;
import com.zzx.blog.adapter.SubsidyAdapter;
import com.zzx.blog.base.BaseFragment;
import com.zzx.blog.bean.CommonBean;
import com.zzx.blog.bean.SubsidyBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;
import com.zzx.blog.myinterface.EventInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ZRecyclerViewAdapter;
import adapter.decoration.SpaceDecoration;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import utils.DensityUtils;

/**
 * Created by Administrator on 2018/7/5.
 */

public class SubsidyListFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    private int status;// 0已申请   1已完成   2已拒绝
    private int pageIndex = 1;
    private List<SubsidyBean> beanList;
    private SubsidyAdapter subsidyAdapter;

    @Override
    public void initBundle(Bundle bundle) {
        status = bundle.getInt("status");
    }

    @Override
    public void initView() {
        mRefreshLayout.setDelegate(this);
        beanList = new ArrayList<>();
        subsidyAdapter = new SubsidyAdapter(mRecyclerView, beanList, status);
        subsidyAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startActivity(new Intent(mContext, SubsidyDetailActivity.class).putExtra("subId", beanList.get(position).getID()));

            }
        });
        for (int i = 0; i < 10; i++) {
            beanList.add(new SubsidyBean());
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceDecoration(DensityUtils.dip2px(mContext, 5)));
        mRecyclerView.setAdapter(subsidyAdapter);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void initLogic() {
        setEventInterface(new EventInterface() {
            @Override
            public void setEvent(CommonBean enity) {
                if (enity.getType().equals("addSubsidySuccess")) {
                    pageIndex = 1;
                    beanList.clear();
                    requestSubsidyList();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmet_subsidy_list;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pageIndex = 1;
        beanList.clear();
        requestSubsidyList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pageIndex++;
        requestSubsidyList();
        return true;
    }

    private void requestSubsidyList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        map.put("status", status);
        HttpUtils.requestPosts(mContext, AppConfig.requestSubsidyList, map, new JsonCallback<List<SubsidyBean>>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<List<SubsidyBean>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    beanList.addAll(response.body());
                } else if (pageIndex > 1) {
                    pageIndex--;
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                subsidyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<List<SubsidyBean>> response) {
                super.onError(response);
                if (pageIndex > 1) {
                    pageIndex--;
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
            }
        });
    }


    @OnClick({R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                startActivity(ApplySubsidyActivity.class);
                break;
        }
    }
}
