package com.zzx.blog.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lzy.okgo.model.Response;
import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;
import com.zzx.blog.bean.CommonBean;
import com.zzx.blog.filter.MoneyInputFilter;
import com.zzx.blog.http.AppConfig;
import com.zzx.blog.http.HttpUtils;
import com.zzx.blog.http.JsonCallback;

import org.greenrobot.eventbus.EventBus;

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
 * 申请补贴
 */
public class ApplySubsidyActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.edAppName)
    EditText edAppName;
    @BindView(R.id.edLoanMoney)
    EditText edLoanMoney;
    @BindView(R.id.edInterestMoney)
    EditText edInterestMoney;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.rlPhoto)
    RelativeLayout rlPhoto;

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_apply_subsidy);
    }

    @Override
    public void initView() {
        setTitle("申请补贴");

        InputFilter[] loanFilters = {new MoneyInputFilter()};
        InputFilter[] interestFilters = {new MoneyInputFilter()};

        edLoanMoney.setFilters(loanFilters);
        edInterestMoney.setFilters(interestFilters);
    }

    @Override
    public void initLogic() {

    }


    @OnClick({R.id.rlPhoto, R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlPhoto:
                showPhotoDialog();
                break;
            case R.id.tvSubmit:
                if (checkAddSubsidy()) {
                    requestAddSubsidy();
                }
                break;
        }
    }

    private boolean checkAddSubsidy() {
        if (isEmpty(edAppName)) {
            showToast("请输入借款APP名称");
            return false;
        } else if (isEmpty(edAppName)) {
            showToast("请输入借款金额");
            return false;
        } else if (isEmpty(edAppName)) {
            showToast("请输入利息金额");
            return false;
        } else if (imgFile == null) {
            showToast("请上传还款截图");
            return false;
        }
        return true;
    }

    /**
     * 申请补贴
     */
    private void requestAddSubsidy() {
        Map<String, Object> map = new HashMap<>();
        map.put("loan", edLoanMoney.getText().toString());
        map.put("interest", edInterestMoney.getText().toString());
        map.put("appname", edAppName.getText().toString());
        map.put("SubsidyPic", imgFile);
        HttpUtils.requestPosts(mContext, AppConfig.requestAddSubsidy, map, new JsonCallback<String>(mContext, "提交中...") {
            @Override
            public void onSuccess(Response<String> response) {
                EventBus.getDefault().post(new CommonBean<>("addSubsidySuccess"));
                showToast("提交成功,请等待管理员审核!");
                finish();
            }
        });
    }


    /**
     * 图片
     */
    private String path;
    private File imgFile;
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
        imgFile = new File(result.getImage().getOriginalPath());
        ivPhoto.setImageBitmap(BitmapFactory.decodeFile(result.getImage().getOriginalPath()));
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
                    getTakePhoto().onPickFromGallery();
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
}
