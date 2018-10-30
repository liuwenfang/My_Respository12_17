package base;

import android.support.v4.app.Fragment;

/**
 * Created by zzx on 2017/12/28 0028.
 */

public abstract class ZBaseFragment extends Fragment implements IFrgCallback {
    //返回布局文件id
    protected abstract int getLayoutId();
}
