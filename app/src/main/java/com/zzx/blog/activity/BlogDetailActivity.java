package com.zzx.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zzx.blog.R;
import com.zzx.blog.adapter.CommentAdapter;
import com.zzx.blog.adapter.PhotoAdapter;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.bean.BlogBean;
import com.zzx.blog.bean.BlogDetailBean;
import com.zzx.blog.bean.CommentBean;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;
import com.zzx.blog.photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ZRecyclerViewAdapter;
import adapter.decoration.DividerGridItemDecoration;
import butterknife.BindView;
import butterknife.OnClick;
import utils.DensityUtils;
import view.RoundAngleImageView;

/**
 * 帖子详情
 */
public class BlogDetailActivity extends BaseActivity {
    @BindView(R.id.tvBlogTitle)
    TextView tvBlogTitle;
    @BindView(R.id.mRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.mNestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.rivHead)
    RoundAngleImageView rivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvAddTime)
    TextView tvAddTime;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.mRecyclerViewPhoto)
    RecyclerView mRecyclerViewPhoto;
    @BindView(R.id.mRecyclerViewComment)
    RecyclerView mRecyclerViewComment;
    @BindView(R.id.edInput)
    EditText edInput;
    private BlogDetailBean detailBean;
    private BlogBean blogBean;

    private List<CommentBean> commentListBeans;
    private CommentAdapter commentAdapter;
    private int pageIndex = 1;

    private List<String> photos;
    private PhotoAdapter photoAdapter;


    @Override
    public void initBundle(Bundle bundle) {
        blogBean = (BlogBean) bundle.get("blogBean");
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_blog_detail);
    }

    @Override
    public void initView() {
        setTitle("帖子详情");
        initRecycler();
        requestNoteDetails();
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentListBeans.clear();
                pageIndex = 1;
                requestNoteDetails();
            }
        });
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                    pageIndex++;
                    requestNoteDetails();
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 上拉刷新实现
                }
            }
        });
    }

    private void initRecycler() {
        photos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(mRecyclerViewPhoto, photos);
        photoAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, photos.toArray(new String[]{}));
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FIRST, photos.get(position));
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }
        });
        mRecyclerViewPhoto.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerViewPhoto.setNestedScrollingEnabled(false);
        mRecyclerViewPhoto.addItemDecoration(new DividerGridItemDecoration(mContext, DensityUtils.dip2px(mContext, 5), R.color.transparent));
        mRecyclerViewPhoto.setAdapter(photoAdapter);
        //评论
        commentListBeans = new ArrayList<>();
        commentAdapter = new CommentAdapter(mRecyclerViewComment, commentListBeans);
        mRecyclerViewComment.setNestedScrollingEnabled(false);
        mRecyclerViewComment.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewComment.setAdapter(commentAdapter);
    }

    private void initUI() {
        tvBlogTitle.setText(detailBean.getTitle() + "");
        tvName.setText(detailBean.getUserName() + "");
        tvContent.setText(detailBean.getContent() + "");
        tvAddTime.setText(detailBean.getAddTime() + "");
        commentAdapter.notifyDataSetChanged();

        if (!isEmpty(detailBean.getImage1())) {
            photos.add(detailBean.getImage1());
        } else {
            mRecyclerViewPhoto.setVisibility(View.GONE);
            return;
        }
        if (!isEmpty(detailBean.getImage2())) {
            photos.add(detailBean.getImage2());
        } else {
            photoAdapter.notifyDataSetChanged();
            return;
        }
        if (!isEmpty(detailBean.getImage3())) {
            photos.add(detailBean.getImage3());
        } else {
            photoAdapter.notifyDataSetChanged();
            return;
        }
        if (!isEmpty(detailBean.getImage4())) {
            photos.add(detailBean.getImage4());
        } else {
            photoAdapter.notifyDataSetChanged();
            return;
        }
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.rlRight, R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlRight:
                break;
            case R.id.tvSubmit:
                if (isEmpty(edInput)) {
                    showToast("请输入您要评论的内容!");
                } else {
                    requestAddComment();
                }
                break;
        }
    }

    /**
     * 帖子详情
     */
    private void requestNoteDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", blogBean.getID());
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        HttpUtils.requestPosts(mContext, AppConfig.requestNoteDetails, map, new JsonCallback<BlogDetailBean>(mContext, "加载中...") {
            @Override
            public void onSuccess(Response<BlogDetailBean> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response != null && response.body() != null) {
                    if (detailBean == null) {
                        detailBean = response.body();
                        if (response.body().getCommentList() != null && response.body().getCommentList().size() > 0) {
                            commentListBeans.addAll(response.body().getCommentList());
                        }
                        initUI();
                    } else {
                        if (response.body().getCommentList() != null && response.body().getCommentList().size() > 0) {
                            commentListBeans.addAll(response.body().getCommentList());
                        } else if (pageIndex > 1) {
                            pageIndex--;
                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 发表评论
     */
    private void requestAddComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("noteID", blogBean.getID());
        map.put("comment", edInput.getText().toString());
        HttpUtils.requestPosts(mContext, AppConfig.requestAddComment, map, new JsonCallback<CommentBean>(mContext, "评论中...") {
            @Override
            public void onSuccess(Response<CommentBean> response) {
                if (response.body() != null) {
                    commentListBeans.add(0, response.body());
                    commentAdapter.notifyDataSetChanged();
                    edInput.setText("");
                }
            }
        });
    }


}
