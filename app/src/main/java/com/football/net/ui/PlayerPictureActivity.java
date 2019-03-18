package com.football.net.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.PlayerPictureAdapter;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.SquarePhotoAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class PlayerPictureActivity extends BasicActivity {

    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.teamType)
    ImageView teamType;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.teamName)
    TextView teamName;
    @BindView(R.id.positionInTeam)
    TextView positionInTeam;
    @BindView(R.id.winPercent)
    TextView winPercent;
    @BindView(R.id.likeNumV)
    TextView likeNumV;
    @BindView(R.id.valueView)
    TextView valueView;
    @BindView(R.id.winNum)
    TextView winNum;
    @BindView(R.id.drawNum)
    TextView drawNum;
    @BindView(R.id.loseNum)
    TextView loseNum;
    @BindView(R.id.progressbar1)
    ProgressBar progressbar1;
    @BindView(R.id.progressbar2)
    ProgressBar progressbar2;
    @BindView(R.id.progressbar3)
    ProgressBar progressbar3;

    @BindView(R.id.recyclerview)
    UltimateRecyclerView recyclerView;
    ArrayList<SquarePhotoBean> photoList = new ArrayList<SquarePhotoBean>();
    int page = 1;
    SquarePhotoAdapter photoAdapter;

    UserBean userBean;
    @Override
    public int getLayoutId() {
        return R.layout.activity_player_picture;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_player_people;
    }

    @Override
    protected void initView() {

        userBean = (UserBean) getIntent().getSerializableExtra("userBean");

        TeamBean teamBean = userBean.getTeam();
        if(teamBean != null){
            teamName.setText(teamBean.getTeamTitle());
            teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));

            winNum.setText(teamBean.getWin()+"");
            drawNum.setText(teamBean.getEven()+"");
            loseNum.setText(teamBean.getLost()+"");

            if(teamBean.getTotal() > 0){
                progressbar1.setProgress(teamBean.getWin()*100/teamBean.getTotal());
                progressbar2.setProgress(teamBean.getEven()*100/teamBean.getTotal());
                progressbar3.setProgress(teamBean.getLost()*100/teamBean.getTotal());
            }
        }
        userName.setText(userBean.getName());
        valueView.setText("身价：$"+userBean.getValue()+"");
        positionInTeam.setText(CommonUtils.getPositionStr(userBean.getPosition()));
        winPercent.setText("胜率：暂无");
        likeNumV.setText(userBean.getLikeNum()+"");
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+ CommonUtils.getRurl(userBean.getIconUrl()),imageView1, FootBallApplication.options);


        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        PlayerPictureAdapter adapter = new PlayerPictureAdapter(this, null);
        photoAdapter = new SquarePhotoAdapter(this, photoList);
        recyclerView.setAdapter(photoAdapter);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(photoAdapter);
            }
        });
        loadPhoto(1);
    }

    public void loadPhoto(final int page) {

        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "createTime desc");
        params.put("playId",userBean.getId());
        params.put("viewType", 1);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPhoto", params.toString(), new SmartCallback<SquarePhotoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBeanResult result) {
                recyclerView.setRefreshing(false);
                if (page == 1) {
                    photoList.clear();
                }
                photoList.addAll(result.getList());
                if(photoList.size() > 0){
                    photoAdapter.notifyDataSetChanged();
                }
//                if (photoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyclerView.setRefreshing(false);
            }

        }, SquarePhotoBeanResult.class);
    }
}
