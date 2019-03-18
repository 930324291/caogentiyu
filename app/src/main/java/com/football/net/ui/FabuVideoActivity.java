package com.football.net.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.IconBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youku.uploader.IUploadResponseHandler;
import com.youku.uploader.YoukuUploader;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

@EActivity(R.layout.activity_fabu_video)
public class FabuVideoActivity extends BaseActivity {
    private static final Logger logger = LoggerFactory.getLogger(FabuVideoActivity_.class);

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.layout_progress)
    View layout_progress;
    @ViewById(R.id.pictureTitle)
    EditText pictureTitle;
    @ViewById(R.id.addvideo)
    ImageView addvideo;
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
    @ViewById(R.id.progressbar)
    ProgressBar progressBar;
    @ViewById(R.id.percent)
    TextView percent;

    private YoukuUploader uploader;
    private String access_token = "e80ae1db0442cfcb38ec17ab2bd081ac";

    int viewType = 1;

    @AfterViews
    public void initView() {
        title.setText("发布视频");
        if(!TextUtils.isEmpty(FootBallApplication.access_token)){
            access_token = FootBallApplication.access_token;
        }
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
                wordCount.setText(num+"/1200");
            }
        });
        select1.setSelected(true);
        uploader = YoukuUploader.getInstance("937f9355701f8383", "bc17f29237940a00027e2813f07ce754", getApplicationContext());
    }
    private final int REQUEST_MEDIA = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Click({R.id.commit,R.id.selectView1,R.id.selectView2, R.id.tvaddvideo})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commit:
                if(TextUtils.isEmpty(pictureTitle.getText())){
                    showMsg("请输入视频标题！");
                    return;
                }
                if(TextUtils.isEmpty(videoDescrip.getText())){
                    showMsg("请输入视频描述！");
                    return;
                }
                layout_progress.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                percent.setText("等待中");
                upload();
                break;
            case R.id.selectView1:
                select1.setSelected(true);
                select2.setSelected(false);
                viewType =1;
                break;
            case R.id.selectView2:
                select1.setSelected(false);
                select2.setSelected(true);
                viewType =2;
                break;
            case R.id.tvaddvideo:
                //权限
                int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    pickerVideo();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constant.REQUEST_PERMISSIONS);
                }
                break;
        }
    }

    private void pickerVideo() {
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options = builder.selectVideo().canSelectMultiVideo(false).build();
        if (options != null) {
            MediaPickerActivity.open(this, REQUEST_MEDIA, options);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickerVideo();
            } else {
                showMsg("未提供授权");
            }
        }
    }

    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                List<MediaItem> mMediaSelectedList = MediaPickerActivity
                        .getMediaItemSelected(data);
                if (mMediaSelectedList != null) {
                    for (MediaItem mediaItem : mMediaSelectedList) {
                        String thumbnailpath = "";
                        if (mediaItem.getUriCropped() == null) {
                            path = mediaItem.getPathOrigin(this);
                            thumbnailpath = mediaItem.getUriOrigin().toString();
                        } else {
                            path = mediaItem.getPathCropped(this);
                            thumbnailpath = mediaItem.getUriCropped().toString();
                        }
                        tvaddvideo.setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(thumbnailpath, addvideo, FootBallApplication.options);
                    }
                } else {

                }
            }
        }
    }
    String video_id ;
    private void upload(){
        HashMap<String, String> params = new HashMap<String, String>();
//access_token可以在http://cloud.youku.com/tools的手动获取选项卡获取
        params.put("access_token", access_token);
        HashMap<String, String> uploadInfo = new HashMap<String, String>();
        uploadInfo.put("title", pictureTitle.getText().toString());
        uploadInfo.put("tags", "video");
        uploadInfo.put("file_name", path);

        uploader.upload(params, uploadInfo, new IUploadResponseHandler() {

            @Override
            public void onStart() {
                progressBar.setProgress(0);
                percent.setText("等待中");
            }

            @Override
            public void onSuccess(JSONObject response) {
                logger.debug("wenxue33333Main upload onSuccess:"+response.toString());
                try {
                    video_id = response.getString("video_id");
                    if(!TextUtils.isEmpty(video_id)){
                        dealHttp(pictureTitle.getText().toString(),videoDescrip.getText().toString(),"http://player.youku.com/player.php/sid/"+video_id + "/v.swf");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgressUpdate(int counter) {
                progressBar.setProgress(counter);
                percent.setText(counter + "%");
            }

            @Override
            public void onFailure(JSONObject errorResponse) {
                logger.debug("Main upload onFailure JsonObject "+ errorResponse.toString());
                showMsg("上传失败请重试！");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void dealHttp(String title, final String comment,String videoDiv) {

        {
            showProgress("提交中....");
            RequestParam params = new RequestParam();
            params.put("title", title);
            params.put("comment", comment);
            params.put("videoDiv", videoDiv);
            params.put("isEnabled", 1);
            params.put("createTime", System.currentTimeMillis());
            params.put("auditStatus", 1);
            params.put("player", FootBallApplication.userbean);
            params.put("viewType", viewType);   //1发布到广场，2发布到队内
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "video/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

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
