package com.zzx.blog.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzx.blog.R;
import com.zzx.blog.base.BaseActivity;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    private String mUrl;

    @Override
    public void initBundle(Bundle bundle) {
        mUrl = bundle.getString("mUrl");
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web_view);
    }

    @Override
    public void initView() {
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //加载需要显示的网页
//        webView.loadUrl(UrlContents.AWARD, map);
        //设置Web视图
        webView.setWebViewClient(new webViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 判断方法1 ,注释掉方法2再测试
                if (url.toLowerCase().contains("platformapi/startapp")) {
                    startAlipayActivity(url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = view.getTitle();
                if (!isEmpty(title) && !isEnglish(title)) {
                    setTitle(view.getTitle() + "");
                }
                super.onPageFinished(view, url);
            }
        });

        if (mUrl != null) {
            webView.loadUrl(mUrl);
        }
    }

    @Override
    public void initLogic() {

    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean isEnglish(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
