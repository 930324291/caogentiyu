package com.football.net.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.DuihuiBean;
import com.football.net.bean.RecruitBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.fileIo.FileIoUtil;
import com.football.net.common.fileIo.StorageUtil;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.FileUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.common.util.VolleyImageUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.DuihuiBeanResult;
import com.football.net.http.reponse.impl.RecruitBeanResult;
import com.football.net.http.reponse.impl.UpLoadPictureResult;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.DuihuiAdapter;
import com.football.net.ui.shouye.FootballTeamAdapter;
import com.football.net.ui.shouye.SquareRecuirAdapter;
import com.football.net.widget.DividerGridItemDecoration;
import com.football.net.widget.DividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.GridPaddingDecorator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class DuihuiActivity extends BasicActivity {
    private static final int REQUEST_CODE = 1001;
    private static final int PHOTO_REQUEST_CUT = 1002;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;


    @BindView(R.id.title)
    TextView title;

    DuihuiAdapter adapter;
    ArrayList<DuihuiBean>  dataList = new ArrayList<DuihuiBean>();

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_duihui;
    }

    public void initView() {
        recyView.setHasFixedSize(false);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        recyView.setLayoutManager(manager);
        dataList.add(new DuihuiBean());
        adapter = new DuihuiAdapter(dataList);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
        recyView.addItemDecoration(new DividerGridItemDecoration(this));
        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(View view, int position) {
                if(position == 0) {
                    int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        selectPicture();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constant.REQUEST_PERMISSIONS);
                    }
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("url",dataList.get(position - 1).getUrl());
                    setResult(100,intent);
                    finish();
                }
            }
        });
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecruit(1);
            }
        });

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });
        loadRecruit(1);
    }

    public void loadRecruit(final int page) {
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "getLogos", null, new SmartCallback<DuihuiBeanResult>() {

            @Override
            public void onSuccess(int statusCode, DuihuiBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                    dataList.add(new DuihuiBean());
                }
                if(result.getData() != null){
                    dataList.addAll(result.getData());
                }
                if(dataList.size() > 0){
                    adapter.notifyDataSetChanged();
                }
//                if (videoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, DuihuiBeanResult.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPicture();
            } else {
                showMsg("未提供授权");
            }
        }
    }

    private void selectPicture() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        intent.setShowGif(false);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE) {
                if (data != null) {
                    List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    if (photos != null && photos.size() > 0) {
                        File file = new File(photos.get(0));
                        if (file == null || !file.exists()) {
                            return;
                        }
                        Uri uri = Uri.fromFile(file);
                        startPhotoZoom(uri);
                    }
                }
            }else if (requestCode == PHOTO_REQUEST_CUT) {
                showProgress("正在上传....");
                upLoadUserHeader(uritempFile.getPath());
            }

        }
    }


    private void upLoadUserHeader(String path) {
        String dbDir = StorageUtil.getAvaRoot() + FootBallApplication.CACHE_PATH;
        FileIoUtil.makeFolder(dbDir);
        final String tmpPath = dbDir + path.substring(path.lastIndexOf("/"));
        VolleyImageUtils.compress(path, tmpPath);

        new SmartClient(this,true).uploadFile(HttpUrlConstant.SERVER_URL + "/common/uploadImg", tmpPath, new SmartCallback<UpLoadPictureResult>() {

            @Override
            public void onSuccess(int statusCode, UpLoadPictureResult result) {
                dismissProgress();
                if (result.isSuccess()) {
                    new File(tmpPath).delete();
                    cropImage( CommonUtils.getRurl(result.getPicPath()));
                } else {
                    showMsg("上传失败，请重试");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
            }
        }, UpLoadPictureResult.class);
    }

    private void cropImage(String path) {
        RequestParam params = new RequestParam();
        params.put("h", 256);
        params.put("w", 256);
        params.put("x", 0);
        params.put("y", 0);
        params.put("x2", 256);
        params.put("y2", 256);
        params.put("path", path);
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.SERVER_URL + "/common/cropImage", params.toString(), new SmartCallback<UpLoadPictureResult>() {

            @Override
            public void onSuccess(int statusCode, UpLoadPictureResult result) {
                if (result.isSuccess()) {
                    String url = CommonUtils.getRurl(result.getPicPath());
                    Intent intent = new Intent();
                    intent.putExtra("url", url);
                    setResult(100, intent);
                    finish();
                } else {
                    showMsg("上传失败，请重试！");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                showMsg("上传失败，请重试！");
            }

        }, UpLoadPictureResult.class);
    }

    Uri uritempFile;
    /**
     * 裁剪头像
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256); /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
//        intent.putExtra("return-data", true);

        //uritempFile为Uri类变量，实例化uritempFile
//        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        uritempFile = Uri.parse("file://" + FileUtils.getIconDir() + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}
