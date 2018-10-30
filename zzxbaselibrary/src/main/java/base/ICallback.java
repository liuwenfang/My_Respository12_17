package base;

import android.os.Bundle;


public interface ICallback {
//    //返回布局文件id
//    int getLayoutId();

    //Bundle  传递数据
    void initBundle(Bundle bundle);

    //替代onCreate  setContentView(R.layout.activity_order_list);
    void initContentView(Bundle savedInstanceState);

    //初始化布局文件
    void initView();

    //初始化逻辑
    void initLogic();
}
