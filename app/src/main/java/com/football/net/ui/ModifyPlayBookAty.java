package com.football.net.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.football.net.R;
import com.football.net.manager.BaseActivity;
import com.football.net.widget.DragGrid;
import com.football.net.widget.OtherGrid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
public class ModifyPlayBookAty extends BaseActivity {

    //编辑
    @BindView(R.id.id_modify_btn)
    Button mBtnModify;
    //保存
    @BindView(R.id.id_save_btn)
    Button mBtnSave;

    @BindView(R.id.id_modify_gridv)
    DragGrid mTopGView;

    @BindView(R.id.id_modify_pos_gridv)
    OtherGrid mBGridView;
    /** 其它栏目对应的适配器 */
    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;

    /**
     * 是否点击编辑按钮
     */
    boolean isClick = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_modify_book);
        ButterKnife.bind(this);
        initV();
    }

    private void initV() {

    }

    @OnClick({R.id.id_modify_btn, R.id.id_save_btn})
    public void onClick(View mView) {
        switch (mView.getId()) {
            case R.id.id_modify_btn:
                break;
            case R.id.id_save_btn:
                //保存
                break;
        }
    }

    /**
     * 加载球队成员数据
     *
     * @param teamId
     */
    public void loadData(final int teamId) {
//        RequestParam params = new RequestParam();
//        params.put("teamId", teamId);
//        params.put("orderby", "isCaptain asc,u.createTime desc");
//        //3.请求数据
//        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {
//
//            @Override
//            public void onSuccess(int statusCode, UserBeanResult result) {
//                dataList = new ArrayList<UserBean>();
//                dataList.addAll(result.getList());
//                MyAdapter adapter = new MyAdapter(TeamDetialActivity.this, dataList);
//                grideview.setAdapter(adapter);
//
//                peopleNum.setText("成员（" + dataList.size() + ")"); // 设置成员数量
//
//                //设置适配器
//                mAdapter = new GalleryAdapter2(TeamDetialActivity.this, dataList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//            }
//        }, UserBeanResult.class);
    }
}
