package loder;

import android.content.Context;
import android.widget.ImageView;



import utils.imageload.ImageLoader;


public class GlideImageLoader implements ImageLoader {

    private Context mContext;


    public GlideImageLoader(Context context) {
        this.mContext = context;
    }

    @Override
    public void load(ImageView imageView, Object imageUrl) {
//        Glide.with(mContext)
//                .load(imageUrl)
//                .thumbnail(0.1f)
//                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, Object imageUrl, int defaultImage) {
//        Glide.with(mContext)
//                .load(imageUrl)
//                .thumbnail(0.1f)
////                .apply(new RequestOptions().placeholder(defaultImage))
//                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, Object imageUrl, Object transformation) {
//        Glide.with(mContext)
//                .load(imageUrl)
//                .thumbnail(0.1f)
////                .apply(new RequestOptions().transform((BitmapTransformation) transformation))
//                .into(imageView);
    }
}
