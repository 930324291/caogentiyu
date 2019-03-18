package com.football.net.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片加载工具类
 *
 * @author HQ
 *         created at 2017/3/22 0022 15:35
 */
public class ImageUtils {

    /**
     * 压缩图片
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        // 此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480fcompress
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    public static File compressBmpToFile(Context context, Bitmap bmp, int minSize) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "cache");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > minSize && options > 50) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 根据ImageView的大小自动缩放图片
     *
     * @param picturePath 图片路径
     * @param mImageView  图片控件
     * @return
     * @throws IOException
     */
    public static Bitmap autoResizeFromLocalFile(String picturePath, ImageView mImageView) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, mImageView.getWidth(), mImageView.getHeight());
        Log.d("ImageHeight", String.valueOf(options.outHeight));
        Log.d("ImageWidth", String.valueOf(options.outWidth));
        Log.d("Height", String.valueOf(mImageView.getWidth()));
        Log.d("Width", String.valueOf(mImageView.getWidth()));
        //options.inSampleSize = 10;

        Log.d("SampleSize", String.valueOf(options.inSampleSize));
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    /**
     * 计算图片缩放比例
     *
     * @param options
     * @param reqWidth  图片长度
     * @param reqHeight 图片宽度
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    /**
     * 获取相册路径
     *
     * @param context
     * @param data
     * @return
     */
    public static String getAlbum(Activity context, Intent data) {
//        Bitmap bm = null;
        try {
            // 获得图片的uri
            Uri originalUri = data.getData();
//            ContentResolver resolver = getContentResolver();
//            bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//使用系统的一个工具类，参数列表为 Bitmap Width,Height  这里使用压缩后显示，否则在华为手机上ImageView 没有显示
//            imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bm, 100, 100));
            // 显得到bitmap图片
            // imageView.setImageBitmap(bm);

            String[] proj = {MediaStore.Images.Media.DATA};

            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = context.managedQuery(originalUri, proj, null, null, null);

            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径
            String path = cursor.getString(column_index);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static DisplayImageOptions getImageOptions(int defaltImage) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaltImage)
                .showImageForEmptyUri(defaltImage).showImageOnFail(defaltImage)
                .cacheInMemory(true).cacheOnDisk(true).build();
        return options;
    }
}
