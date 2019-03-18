package com.football.net.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.football.net.R;
import com.football.net.common.constant.IntentKey;
import com.football.net.widget.DeviceUtil;
import com.football.net.widget.StaticObjectUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.widget.TouchImageView;

/**
 * 查看大图的Activity界面。
 *
 * @author
 */
public class ImageDetailsActivity extends BaseActivity implements OnPageChangeListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;

    private ArrayList<String> imageUrls = new ArrayList<>();

    /**
     * 用于管理图片的滑动
     */
    private ViewPager viewPager;

    /**
     * 显示当前图片的页数
     */
    private TextView pageText;

    private ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initLayout() {
        setContentView(R.layout.image_details);
        setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();

    }

    //为1 用原图片地址
    private int mType;

    @SuppressLint("WrongConstant")
    @Override
    public void initView(Bundle savedInstanceState) {

        imageUrls = StaticObjectUtils.getImageUrls();

        int imagePosition = getIntent().getIntExtra("image_position", 0);
        mType = getIntent().getIntExtra(IntentKey.General.KEY_TYPE, -1);
        pageText = (TextView) findViewById(R.id.page_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);


        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagePosition);
        viewPager.setOnPageChangeListener(this);
        viewPager.setEnabled(false);
        // 设定当前的页数和总页数
        pageText.setText((imagePosition + 1) + "/" + imageUrls.size());


    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setupActivityComponent() {
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }

    }

    /**
     * ViewPager的适配器
     *
     * @author
     */
    class ViewPagerAdapter extends PagerAdapter {


        @SuppressLint("WrongConstant")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ImageDetailsActivity.this).inflate(
                    R.layout.zoom_image_layout, null);
            TouchImageView zoomImageView = (TouchImageView) view.findViewById(R.id.zoom_image_view);
            String path = "";
            if (mType == 1) {
                path = imageUrls.get(position);
            } else {
            }
            Glide.with(ImageDetailsActivity.this)
                    .load(path)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .dontTransform()
                    .placeholder(R.color.pickerview_bg_topbar)
                    .override(DeviceUtil.getWidth(), DeviceUtil.getHeight())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(zoomImageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            ivLoading.setVisibility(View.GONE);
                        }

                        @Override
                        protected void setResource(GlideDrawable resource) {
                            super.setResource(resource);

                        }

                        @Override
                        public void onStart() {
                            super.onStart();

                        }

                        @Override
                        public void onStop() {
                            super.onStop();

                        }
                    });


            zoomImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onPageSelected(int currentPage) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        pageText.setText((currentPage + 1) + "/" + imageUrls.size());


    }


}