package com.zzx.blog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.model.Response;
import com.zzx.blog.R;
import com.zzx.blog.activity.BlogDetailActivity;
import com.zzx.blog.activity.MyBlogListActivity;
import com.zzx.blog.activity.SendBlogActivity;
import com.zzx.blog.adapter.BlogAdapter;
import com.zzx.blog.base.BaseFragment;
import com.zzx.blog.bean.BlogBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;

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
 * 论坛
 *
 * @author zzx
 */

public class BlogFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    private List<BlogBean> beanList;
    private BlogAdapter blogAdapter;
    private int pageIndex = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blog;
    }

    @Override
    public void initBundle(Bundle savedInstanceState) {

    }


    @Override
    public void initView() {
        mToolbar.setNavigationIcon(null);
        setTitle("论坛");
        mRefreshLayout.setDelegate(this);
        beanList = new ArrayList<>();
        blogAdapter = new BlogAdapter(mRecyclerView, beanList, 1);
        blogAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startActivity(new Intent(mContext, BlogDetailActivity.class).putExtra("blogBean", beanList.get(position)));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceDecoration(DensityUtils.dip2px(mContext, 5)));
        mRecyclerView.setAdapter(blogAdapter);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    private void requestNoteList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        HttpUtils.requestPosts(mContext, AppConfig.requestNoteList, map, new JsonCallback<List<BlogBean>>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<List<BlogBean>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    beanList.addAll(response.body());
                } else if (pageIndex > 1) {
                    pageIndex--;
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                blogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<List<BlogBean>> response) {
                super.onError(response);
                if (pageIndex > 1) {
                    pageIndex--;
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
            }
        });
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.llSendBlog, R.id.llMyBlog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llSendBlog:
                startActivity(SendBlogActivity.class);
                break;
            case R.id.llMyBlog:
                startActivity(MyBlogListActivity.class);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pageIndex = 1;
        beanList.clear();
        requestNoteList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pageIndex++;
        requestNoteList();
        return true;
    }
}
