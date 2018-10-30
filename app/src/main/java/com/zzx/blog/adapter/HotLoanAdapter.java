package com.zzx.blog.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzx.blog.R;
import com.zzx.blog.bean.LoanSuperBean;
import com.zzx.blog.http.AppConfig;

import java.util.List;

import adapter.ZRecyclerViewAdapter;
import adapter.ZViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import view.RoundAngleImageView;

/**
 * Created by zzx on 2018/1/2 0002.
 * 贷超
 */

public class HotLoanAdapter extends ZRecyclerViewAdapter<LoanSuperBean> {

    public HotLoanAdapter(@NonNull RecyclerView mRecyclerView, List<LoanSuperBean> dataLists) {
        super(mRecyclerView, dataLists, R.layout.adapter_hot_loan);
    }

    @Override
    protected void bindData(ZViewHolder holder, LoanSuperBean data, int position) {
        RoundAngleImageView imageView = holder.getView(R.id.rivHead);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(getContext()).load(AppConfig.photoUrl + data.getImage()).centerCrop().into(imageView);
        holder.setText(R.id.tvName, data.getName() + "");
        holder.setText(R.id.tvMoney, data.getLimit() + "");
        holder.setText(R.id.tvTips, data.getTips() + "");
        holder.setText(R.id.tvInterest, "利息：" + data.getInterest());
    }

    static class ViewHolder {
        @BindView(R.id.rivHead)
        RoundAngleImageView rivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvInterest)
        TextView tvInterest;
        @BindView(R.id.tvApply)
        TextView tvApply;
        @BindView(R.id.tvComment)
        TextView tvComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
