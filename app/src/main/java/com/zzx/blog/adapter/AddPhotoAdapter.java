package com.zzx.blog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zzx.blog.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import loder.GlideImageLoader;
import utils.DensityUtils;

/**
 * Created by zzx on 2018/1/2 0002.
 */

public class AddPhotoAdapter extends RecyclerView.Adapter<AddPhotoAdapter.PhotoViewHolder> {
    private int size = 0;
    private Context mContext;
    private List<String> dataLists;

    public AddPhotoAdapter(Context mContext, List<String> dataLists, int size) {
        this.mContext = mContext;
        this.dataLists = dataLists;
        this.size = size;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_photo, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        //设置布局大小
        RelativeLayout rlPhotoContent = holder.rlPhotoContent;
        ViewGroup.LayoutParams layoutParams = rlPhotoContent.getLayoutParams();
        layoutParams.width = (int) (DensityUtils.getWidthInPx(mContext) - DensityUtils.dip2px(mContext, 50)) / 4;
        layoutParams.height = layoutParams.width;
        rlPhotoContent.setLayoutParams(layoutParams);
        //
        GlideImageLoader imageLoader = new GlideImageLoader(mContext);
        if (position == dataLists.size()) {//添加图片
            holder.rlPhotoDel.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.mipmap.img_add)
                    .thumbnail(0.1f)
                    .into(holder.ivPhoto);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {//添加图片
                        onItemClick.OnItemAdd(position);
                    }
                }
            });
        } else {
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext)
                    .load(dataLists.get(position))
                    .thumbnail(0.1f)
                    .error(R.drawable.android_base_img_default)
                    .into(holder.ivPhoto);
//            imageLoader.load(holder.ivPhoto, dataLists.get(position), R.drawable.android_base_img_default);
            holder.rlPhotoDel.setVisibility(View.VISIBLE);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {//选择图片
                        onItemClick.OnItemSel(position);
                    }
                }
            });
            holder.rlPhotoDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {//删除图片
                        onItemClick.OnItemDel(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataLists.size() < size ? dataLists.size() + 1 : size;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;

    public interface OnItemClick {
        void OnItemSel(int position);

        void OnItemAdd(int position);

        void OnItemDel(int position);
    }


    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;
        @BindView(R.id.rlPhotoContent)
        RelativeLayout rlPhotoContent;
        @BindView(R.id.rlPhotoDel)
        RelativeLayout rlPhotoDel;

        PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
