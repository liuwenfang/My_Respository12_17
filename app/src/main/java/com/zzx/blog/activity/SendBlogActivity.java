package com.zzx.blog.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lzy.okgo.model.Response;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import com.zzx.blog.R;
import com.zzx.blog.adapter.AddPhotoAdapter;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;
import com.zzx.blog.photo.ImagePagerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import dialog.SelectDialog;
import utils.PermissionUtils;

/**
 * 我要发帖
 */
public class SendBlogActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.edTitle)
    EditText edTitle;
    @BindView(R.id.edContent)
    EditText edContent;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.mRecyclerViewPhoto)
    RecyclerView mRecyclerViewPhoto;

    @Override
    public void initBundle(Bundle bundle) {

    }


    private List<String> selectedPhotos, selectedCompressPhotos;
    private AddPhotoAdapter addPhotoAdapter;
    private int MAX_SIZE = 4;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_blog);
    }

    @Override
    public void initView() {
        setTitle("我要发帖");
        initAddPhotoAdapter();
    }

    @Override
    public void initLogic() {

    }

    @OnClick({R.id.ivPhoto, R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivPhoto:
                showPhotoDialog();
                break;
            case R.id.tvSubmit:
                if (isEmpty(edTitle)) {
                    showToast("请填写帖子标题");
                } else if (isEmpty(edContent)) {
                    showToast("请填写帖子内容");
                } else {
                    addNote();
                }
                break;
        }
    }

    private void initAddPhotoAdapter() {
        //  图片
        selectedPhotos = new ArrayList<>();
        selectedCompressPhotos = new ArrayList<>();
        addPhotoAdapter = new AddPhotoAdapter(mContext, selectedPhotos, MAX_SIZE);
        addPhotoAdapter.setOnItemClick(new AddPhotoAdapter.OnItemClick() {
            @Override
            public void OnItemSel(int position) {
                String path = selectedPhotos.get(position).toString();
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, selectedPhotos.toArray(new String[]{}));
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FIRST, path);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPE, 2);
                startActivity(intent);
            }

            @Override
            public void OnItemAdd(int position) {
                showPhotoDialog();
            }

            @Override
            public void OnItemDel(int position) {
                addPhotoAdapter.notifyItemRemoved(position);
                selectedPhotos.remove(position);
                File mFile = new File(selectedCompressPhotos.get(position));
                if (mFile.exists()) mFile.delete();
                selectedCompressPhotos.remove(position);
            }
        });
        mRecyclerViewPhoto.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
//        mRecyclerViewPhoto.setHasFixedSize(true);
//        mRecyclerViewPhoto.setNestedScrollingEnabled(false);
        mRecyclerViewPhoto.setAdapter(addPhotoAdapter);
        addPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 论坛发帖
     */
    private void addNote() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", edTitle.getText().toString());
        map.put("content", edContent.getText().toString());
        for (int i = 0; i < selectedCompressPhotos.size(); i++) {
            map.put("NotePic" + (i + 1), new File(selectedCompressPhotos.get(i)));
        }
        HttpUtils.requestPosts(mContext, AppConfig.requestAddNote, map, new JsonCallback<String>(mContext, "提交中...") {
            @Override
            public void onSuccess(Response<String> response) {
                for (int i = 0; i < selectedCompressPhotos.size(); i++) {
                    File file = new File(selectedCompressPhotos.get(i));
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
                startActivity(new Intent(mContext, SubmitSuccessActivity.class)
                        .putExtra("flag", SubmitSuccessActivity.FLAG_BLOG)
                        .putExtra("state", SubmitSuccessActivity.STATE_SUBMIT));
                finish();
//                if (response.body() != null) {
//                    Log.d("TAG", response.body());
//                }
            }
        });
    }


    /**
     * 图片
     */
    private String path;
    private SelectDialog selectDialog;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private void showPhotoDialog() {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + +System.currentTimeMillis()
                + ".jpg";
        if (selectDialog == null) {
            List<String> datas = new ArrayList<>();
            datas.add("拍照");
            datas.add("相册");
            selectDialog = new SelectDialog(mContext, datas);
            selectDialog.setOnSelItem(new SelectDialog.OnSelItem() {
                @Override
                public void OnSelItem(int position) {
                    selectDialog.dismiss();
                    checkPermission(position);
                }
            });
        }
        selectDialog.show();
    }

    @Override
    public void takeSuccess(TResult result) {
//        imgFile = new File(result.getImage().getOriginalPath());
//        ivPhoto.setImageBitmap(BitmapFactory.decodeFile(result.getImage().getOriginalPath()));

        if (result.getImages() != null) {
            for (TImage tImage : result.getImages()) {
                selectedPhotos.add(tImage.getOriginalPath());
            }
            compressFile();
        }
        addPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        showToast(msg);
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /**
     * 判断权限
     */
    private void checkPermission(final int position) {
        final Uri imageUri = Uri.fromFile(new File(path));
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionUtils.requestPermissions(mContext, 310, permissions, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (position == 0) {//拍照
                    getTakePhoto().onPickFromCapture(imageUri);
                } else {//相册
                    getTakePhoto().onPickMultiple(MAX_SIZE - selectedCompressPhotos.size());
                }
            }

            @Override
            public void onPermissionDenied() {
                PermissionUtils.showTipsDialog(mContext);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePhoto.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Tiny 压缩文件
     */
    private void compressFile() {
        String res[] = new String[selectedPhotos.size()];
        for (int i = 0; i < selectedPhotos.size(); i++) {
            res[i] = selectedPhotos.get(i);
        }
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(res).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile) {
                //return the batch compressed file path
                if (isSuccess) {
                    for (String outFile : outfile) {
                        selectedCompressPhotos.add(outFile);
                    }
                }
            }
        });
    }
}
