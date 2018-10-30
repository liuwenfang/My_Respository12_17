package com.zzx.blog.photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.zzx.blog.R;
import com.zzx.blog.http.AppConfig;


public class ImagePagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_FIRST = "image_first";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IMAGE_HTTP = "image_http";
    public static final String EXTRA_IMAGE_TYPE = "image_type";
    private SelfHackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private String http;
    private int type = 1;//1网页  2本地

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        type = getIntent().getIntExtra(EXTRA_IMAGE_TYPE, 1);
        String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        http = AppConfig.photoUrl;
//        http = getIntent().getStringExtra(EXTRA_IMAGE_HTTP);
        if (pagerPosition == 0) {
            String firstPath = getIntent().getStringExtra(EXTRA_IMAGE_FIRST);
            if (firstPath != null) {
                for (int i = 0; i < urls.length; i++) {
                    if (urls[i] == firstPath) {
                        pagerPosition = i;
                    }
                }
            }
        }

        mPager = findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            if (type == 1) {//网页
                if (http != null && !url.startsWith("http") && !url.startsWith("file://")) {
                    url = http + url;
                }
            }
            return ImageDetailFragment.newInstance(url);
        }

    }
}