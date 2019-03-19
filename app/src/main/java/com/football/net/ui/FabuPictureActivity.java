package com.football.net.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.football.net.R;
import com.football.net.adapter.AddPlacePhotoAdapter;
import com.football.net.adapter.CommentAdapter;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.IconBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.fileIo.FileIoUtil;
import com.football.net.common.fileIo.StorageUtil;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.VolleyImageUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.UpLoadPictureResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.NoScrollGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

@EActivity(R.layout.activity_fabu_picture)
public class FabuPictureActivity extends BaseActivity {

    public final static int REQUEST_CODE = 1;
    public final static int DELETE_CODE = 2;

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.tvaddvideo)
    TextView tvaddvideo;
    @ViewById(R.id.select1)
    ImageView select1;
    @ViewById(R.id.select2)
    ImageView select2;

    @ViewById(R.id.videoDescrip)
    EditText videoDescrip;
    @ViewById(R.id.wordCount)
    TextView wordCount;

    @ViewById(R.id.grid_add_pic)
    NoScrollGridView gridView;
    //    CommonAdapter<String> photoAdapter;
    AddPlacePhotoAdapter photoAdapter;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    int count = 3;
    int successUploadfiles = 0;
    int viewType = 1;
    List<String> pictus = new ArrayList();

    @AfterViews
    public void initView() {
        title.setText("发布图片");
        videoDescrip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int num = editable.length();
                wordCount.setText(num + "/1200");
            }
        });
//        photoAdapter = new CommonAdapter<String>(this, selectedPhotos, R.layout.item_look_pic) {
//            @Override
//            public void convert(ViewHolder helper, String item) {
//                if (selectedPhotos.size() < 12) {
//                    if (helper.getPosition() < selectedPhotos.size()) {
//                        StringUtils.showRoundImg(FabuPictureActivity.this, item, R.color.bg_999999, R.color.bg_999999, (ImageView) helper.getViewById(R.id.id_show_pic_img));
//                    } else {
//                        helper.setImageResource(R.id.id_show_pic_img, R.mipmap.dianjihou);
//                    }
//                } else {
//                    StringUtils.showRoundImg(FabuPictureActivity.this, item, R.color.bg_999999, R.color.bg_999999, (ImageView) helper.getViewById(R.id.id_show_pic_img));
//                }
//            }
//        };
        photoAdapter = new AddPlacePhotoAdapter(this, selectedPhotos, 3);
        gridView.setAdapter(photoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (selectedPhotos.size() < count) {
                    if (position == selectedPhotos.size()) {
                        //权限
                        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            selectPicture();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constant.REQUEST_PERMISSIONS);
                        }

                    } else {
                        Intent intent = new Intent(FabuPictureActivity.this, PhotoPagerActivity.class);
                        intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
                        intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, selectedPhotos);
                        startActivityForResult(intent, DELETE_CODE);
                    }
                } else {
                    Intent intent = new Intent(FabuPictureActivity.this, PhotoPagerActivity.class);
                    intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
                    intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, selectedPhotos);
                    startActivityForResult(intent, DELETE_CODE);
                }
            }
        });
        select1.setSelected(true);
    }

    private void selectPicture() {
        PhotoPickerIntent intent = new PhotoPickerIntent(FabuPictureActivity.this);
        intent.setPhotoCount(count - selectedPhotos.size());
        intent.setShowCamera(true);
        intent.setShowGif(true);
        startActivityForResult(intent, REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            Log.d("huang", data + "=========");
            if (requestCode == DELETE_CODE) {
                selectedPhotos.clear();
                tvaddvideo.setVisibility(View.VISIBLE);
            }
            if (photos != null) {
                selectedPhotos.addAll(photos);
                tvaddvideo.setVisibility(View.GONE);
                Log.d("huang", photos.toString() + "[][][][][]");
            }
//            photoAdapter.setCount(selectedPhotos,"p",3);
            photoAdapter.setDatas(selectedPhotos);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Click({R.id.commit, R.id.selectView1, R.id.selectView2, R.id.tvaddvideo})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commit:
                process();
                break;
            case R.id.selectView1:
                select1.setSelected(true);
                select2.setSelected(false);
                viewType = 1;
                break;
            case R.id.selectView2:
                select1.setSelected(false);
                select2.setSelected(true);
                viewType = 2;
                break;
            case R.id.tvaddvideo:
                //权限
                int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    selectPicture();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constant.REQUEST_PERMISSIONS);
                }
                break;
        }
    }


    /**
     * 上传头像
     */
    private void upLoadFiles(final String path) {
        String dbDir = StorageUtil.getAvaRoot() + FootBallApplication.CACHE_PATH;
        FileIoUtil.makeFolder(dbDir);
        final String tmpPath = dbDir + path.substring(path.lastIndexOf("/"));
        VolleyImageUtils.compress(path, tmpPath);

        new SmartClient(this, true).uploadFile(HttpUrlConstant.SERVER_URL + "/common/uploadImg", tmpPath, new SmartCallback<UpLoadPictureResult>() {

            @Override
            public void onSuccess(int statusCode, UpLoadPictureResult result) {
                if (result.isSuccess()) {
                    new File(tmpPath).delete();
                    successUploadfiles++;
                    pictus.add(result.getPicPath());
                    if (successUploadfiles == selectedPhotos.size()) {
                        dealHttp(videoDescrip.getText().toString());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
//                dissProgress();
//                showMsg("发布失败请重试");
//                for (String str : tmpList) {
//                    new File(str).delete();
//                }
            }
        }, UpLoadPictureResult.class);
    }


    private void process() {

        String content = videoDescrip.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showMsg("内容不能为空！");
            return;
        }

        if (selectedPhotos.size() > 0) {
            successUploadfiles = 0;
//            showProgress("提交中....");
            Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT);
            finish();
//            for (String path : selectedPhotos) {
//                upLoadFiles(path);
//
//            }
        } else {
            showMsg("请选择图片");
        }
    }


    public void dealHttp(final String param1) {

        {
            List<IconBean> urls = new ArrayList<IconBean>();
            for (int i = 0; i < pictus.size(); i++) {
                IconBean bean = new IconBean();
                bean.setUrl(pictus.get(i));
                bean.setSeq(i);
                bean.setStatus(1);
                urls.add(bean);
            }
            RequestParam params = new RequestParam();
            params.put("comment", param1);
            params.put("isEnabled", 1);
            params.put("createTime", System.currentTimeMillis());
            params.put("auditStatus", 1);
            params.put("pics", urls);
            params.put("player", FootBallApplication.userbean);
            params.put("viewType", viewType);   //1发布到广场，2发布到队内
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "photo/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

                @Override
                public void onSuccess(int statusCode, Result result) {
                    dismissProgress();
                    if (!result.isSuccess()) {
                        showMsg(Constant.interfaceInnorErr);
                        return;
                    }
                    showMsg("提交成功！");
                    finish();


                }

                @Override
                public void onFailure(int statusCode, String message) {
                    dismissProgress();
                    showMsg(message);
                }

            }, Result.class);
        }
    }
}
