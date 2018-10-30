package com.zzx.blog.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zzx.blog.R;
import com.zzx.blog.http.AppConfig;

import java.util.List;

import adapter.ZRecyclerViewAdapter;
import adapter.ZViewHolder;
import utils.DensityUtils;

/**
 * 图片列表
 */

public class PhotoAdapter extends ZRecyclerViewAdapter<String> {

    public PhotoAdapter(@NonNull RecyclerView mRecyclerView, List<String> dataLists) {
        super(mRecyclerView, dataLists, R.layout.adapter_photo);
    }

    @Override
    protected void bindData(ZViewHolder holder, String data, int position) {
        ImageView rivPhoto = holder.getView(R.id.rivPhoto);
        rivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = rivPhoto.getLayoutParams();
        layoutParams.width = (int) (DensityUtils.getWidthInPx(getContext()) - DensityUtils.dip2px(getContext(), 30f)) / 3;
        layoutParams.height = layoutParams.width;
        rivPhoto.setLayoutParams(layoutParams);
        Glide.with(getContext())
                .load(AppConfig.photoUrl + dataLists.get(position))
                .thumbnail(0.1f)
                .error(R.drawable.android_base_img_default)
                .into(rivPhoto);
    }

}
