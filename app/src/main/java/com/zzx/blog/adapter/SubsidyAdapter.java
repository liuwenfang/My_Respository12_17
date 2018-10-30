package com.zzx.blog.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzx.blog.R;
import com.zzx.blog.bean.SubsidyBean;

import java.util.List;

import adapter.ZRecyclerViewAdapter;
import adapter.ZViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 补贴
 */

public class SubsidyAdapter extends ZRecyclerViewAdapter<SubsidyBean> {
    private int status;

    public SubsidyAdapter(@NonNull RecyclerView mRecyclerView, List<SubsidyBean> dataLists, int status) {
        super(mRecyclerView, dataLists, R.layout.adapter_subsidy);
        this.status = status;
    }

    @Override
    protected void bindData(ZViewHolder holder, SubsidyBean data, int position) {
        holder.setText(R.id.tvAppName, data.getAppName());
        holder.setText(R.id.tvDate, data.getAddTime());
        if (status == 0) {//已申请
            holder.setBackgroundRes(R.id.ivState, R.mipmap.img_subsidy_applying);
            holder.setText(R.id.tvState, "已申请");
        } else if (status == 1) {//已完成  已成功
            holder.setBackgroundRes(R.id.ivState, R.mipmap.img_subsidy_success);
            holder.setText(R.id.tvState, "已完成");
        } else if (status == 2) {//被拒绝
            holder.setBackgroundRes(R.id.ivState, R.mipmap.img_subsidy_refuse);
            holder.setText(R.id.tvState, "已拒绝");
        }
    }


    static class ViewHolder {
        @BindView(R.id.ivState)
        ImageView ivState;
        @BindView(R.id.tvAppName)
        TextView tvAppName;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvState)
        TextView tvState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
