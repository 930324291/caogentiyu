package com.football.net.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.InformNewMemberAdapter;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.ApplyBean;
import com.football.net.bean.ApplyBean2;
import com.football.net.bean.MessageBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.ApplyBean2Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.InformNewMemberApproveActivity;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class InformNewMemberFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    ListView mListV;
    private CommonAdapter<ApplyBean2> mOrderAdp;

    private static final int APPROVE_CODE = 1001;
    //    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView ultimateRecyclerView;
//    InformNewMemberAdapter adapter;
    List<ApplyBean2> dataList = new ArrayList<ApplyBean2>();
    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.frg_comm_refresh_listv;
    }

    @Override
    protected void initView() {
        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mOrderAdp = new CommonAdapter<ApplyBean2>(mContext, dataList, R.layout.item_inform_new_member) {
            @Override
            public void convert(final ViewHolder helper, final ApplyBean2 bean2) {
                if (helper.getPosition() % 2 == 0) {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg1);
                } else {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg2);
                }
                TextView name = (TextView) helper.getViewById(R.id.name);
                TextView text2 = (TextView) helper.getViewById(R.id.text2);
                TextView text3 = (TextView) helper.getViewById(R.id.text3);
                TextView timeview = (TextView) helper.getViewById(R.id.timeview);
                ImageView image1 = (ImageView) helper.getViewById(R.id.image1);
                final ApplyBean apply = bean2.getApply();
                name.setText(apply.getPlayer().getName());
                text2.setText(apply.getTitle());

                Integer auditStatus = bean2.getAuditStatus();
                Integer confirmStatus = bean2.getConfirmStatus();

                if (auditStatus == null) {
                    text3.setText("\u3000\u3000" + "待审核");
                } else {
                    if (auditStatus == 1) {
                        if (confirmStatus == null) {
                            text3.setText("\u3000\u3000" + "已邀请入队，待球员确认");
                        } else {
                            if (confirmStatus == 1) {
                                text3.setText("\u3000\u3000" + "已入队");
                            } else if (confirmStatus == 2) {
                                text3.setText("\u3000\u3000" + "已放弃入队");
                            } else {
                                text3.setText("\u3000\u3000" + "ERROR");
                            }
                        }
                    } else if (auditStatus == 2) {
                        text3.setText("\u3000\u3000" + "已拒绝");
                    }
                }

                timeview.setText(CommonUtils.getFullTime(bean2.getCreateTime()));

                if (!StringUtils.isEmpty(apply.getPlayer().getIconUrl())) {
                    ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(apply.getPlayer().getIconUrl()), image1, FootBallApplication.circOptions);
                } else {
                    // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic.png", image1, FootBallApplication.circOptions);
                    Glide.with(mContext).load(R.mipmap.nopic).crossFade().into(image1);
                }

                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid", apply.getPlayer().getId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid", apply.getPlayer().getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        };
        mListV.setAdapter(mOrderAdp);
        mListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dataList.get(position).getAuditStatus() == null) { // 待队长审核
                    Intent intent = new Intent(getActivity(), InformNewMemberApproveActivity.class);
                    intent.putExtra("itemBean", dataList.get(position));
                    startActivityForResult(intent, APPROVE_CODE);
                }
            }
        });

//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadData(page);
//            }
//        });
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                loadData(page);
//            }
//        });

        loadData(1);
    }


    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("currentPage", page);
        params.put("pageSize", 10);
        params.put("isEnabled", 1);
        params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listApplyTank", params.toString(), new SmartCallback<ApplyBean2Result>() {

            @Override
            public void onSuccess(int statusCode, ApplyBean2Result result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                mOrderAdp.notifyDataSetChanged();
                if (page > 1) {
                    if (result.getList() == null || result.getList().isEmpty())
                        ToastUtil.show(mContext, "没有更多数据");
                }
//                if (dataList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, ApplyBean2Result.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == APPROVE_CODE) {
                loadData(1);
            }
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            loadData(1);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loadData(page);
        }
    }
}
