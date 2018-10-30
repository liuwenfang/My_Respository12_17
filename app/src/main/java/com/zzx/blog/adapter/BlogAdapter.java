package com.zzx.blog.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zzx.blog.R;
import com.zzx.blog.bean.BlogBean;

import java.util.List;

import adapter.ZRecyclerViewAdapter;
import adapter.ZViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import view.RoundAngleImageView;

/**
 * 论坛列表
 */

public class BlogAdapter extends ZRecyclerViewAdapter<BlogBean> {
    private int flag = 0; // 1 显示列表   2 我的列表

    public BlogAdapter(@NonNull RecyclerView mRecyclerView, List<BlogBean> dataLists, int flag) {
        super(mRecyclerView, dataLists, R.layout.adapter_blog);
        this.flag = flag;
    }

    @Override
    protected void bindData(ZViewHolder holder, BlogBean data, int position) {
        holder.setText(R.id.tvTitle, data.getTitle() + "");
        holder.setText(R.id.tvName, data.getUserName() + "");
        holder.setText(R.id.tvAddTime, data.getAddTime() + "");
        holder.setText(R.id.tvCommentNum, data.getCommentCount() + "");
        if (flag == 1) {
            holder.setText(R.id.tvSendTime, data.getTimeSpan() + "");
        } else if (flag == 2) {
            holder.setText(R.id.tvSendTime, data.getStatus() + "");
        }
    }

    static class ViewHolder {
        @BindView(R.id.rivHead)
        RoundAngleImageView rivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAddTime)
        TextView tvAddTime;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvCommentNum)
        TextView tvCommentNum;
        @BindView(R.id.tvSendTime)
        TextView tvSendTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
