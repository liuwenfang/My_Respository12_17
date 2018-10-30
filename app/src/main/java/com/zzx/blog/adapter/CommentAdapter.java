package com.zzx.blog.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zzx.blog.R;
import com.zzx.blog.bean.CommentBean;

import java.util.List;

import adapter.ZRecyclerViewAdapter;
import adapter.ZViewHolder;

/**
 * 评论列表
 */

public class CommentAdapter extends ZRecyclerViewAdapter<CommentBean> {

    public CommentAdapter(@NonNull RecyclerView mRecyclerView, List<CommentBean> dataLists) {
        super(mRecyclerView, dataLists, R.layout.adapter_comment);
    }

    @Override
    protected void bindData(ZViewHolder holder, CommentBean data, int position) {
        holder.setText(R.id.tvName, data.getCommentName() + "");
        holder.setText(R.id.tvComment, data.getCommentContent() + "");
        holder.setText(R.id.tvTime, data.getCommentDate() + "");
//        holder.setText(R.id.tvPlace, data.getCommentID() + "");
    }

}
