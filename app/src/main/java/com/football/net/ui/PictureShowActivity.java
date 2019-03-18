package com.football.net.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.football.net.R;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class PictureShowActivity extends BasicActivity {

    @BindView(R.id.imageview)
    ImageView iv;

    PhotoViewAttacher attacher;
    @Override
    public int getLayoutId() {
        return R.layout.activity_picture_show;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_pic;
    }

    @Override
    protected void initView() {
//        displayImage();
        //创建一个photoview的一个attacher
        attacher = new PhotoViewAttacher(iv);

        String imgeurl = getIntent().getStringExtra("imgeurl");

        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+imgeurl, iv, FootBallApplication.options,//ImageLoader.getInstance().displayImage使用方法
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        displayImage(loadedImage);
                        attacher.update();
                        //在加载完图片的时候更新一下attacher
                    }
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
    }

    private void displayImage(Bitmap bitmap) {
        //想让图片宽是屏幕的宽度
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.yangyang);
        //测量
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;//只测量
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        //再拿到屏幕的宽
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        //计算如果让照片是屏幕的宽，选要乘以多少？
        double scale = screenWidth*1.0/width;
        //这个时候。只需让图片的宽是屏幕的宽，高乘以比例
        int  displayHeight= (int) (height*scale);//要显示的高，这样避免失真
        //最终让图片按照宽是屏幕 高是等比例缩放的大小
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, displayHeight);
        iv.setLayoutParams(layoutParams);
    }
}
