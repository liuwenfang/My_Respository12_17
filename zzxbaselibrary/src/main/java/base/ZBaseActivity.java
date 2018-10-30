package base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import utils.PermissionUtils;

/**
 * Created by zzx on 2017/12/27 0027.
 */

public abstract class ZBaseActivity extends AppCompatActivity implements ICallback {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZActivityStack.getInstance().addActivity(this);
//        initBundle(getIntent().getExtras());
//        initContentView(savedInstanceState);
//        initView();
//        initLogic();
    }

    /**
     * Android M 全局权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZActivityStack.getInstance().finishActivity();
    }
}
