package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.SearchBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SearchResultAdapter extends UltimateViewAdapter<SearchResultAdapter.MineAdapterViewHolder> {

    private ArrayList<SearchBean> data;
    private OnItemClickLitener mOnItemClickLitener;
    Context mContext;
    boolean isTeamFirst = true, isUserFirst = true;

    public SearchResultAdapter(Context mContext, ArrayList<SearchBean> msgList) {
        this.data = msgList;
        this.mContext = mContext;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MineAdapterViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MineAdapterViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public MineAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        MineAdapterViewHolder vh = new MineAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final MineAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {

            holder.bindView(position, data.get(position));
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    public void swapPositions(int from, int to) {
        swapPositions(data, from, to);
    }


    public class MineAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView tabName;

        LinearLayout linRoot;
        ImageView imageview;
        TextView userName, bodyHeight, bodyWeight, valueView, positionInTeam, idNum, winPercent;

        LinearLayout linTeam;
        ImageView ivDuihui;
        ImageView teamType;
        TextView teamName;
        TextView tvShenglv;
        @BindView(R.id.levelV)
        TextView levelV;


        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {

                tabName = (TextView) itemView.findViewById(R.id.tabName);
                linRoot = (LinearLayout) itemView.findViewById(R.id.lin_user);

                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                userName = (TextView) itemView.findViewById(R.id.name);
                bodyHeight = (TextView) itemView.findViewById(R.id.bodyHeight);
                bodyWeight = (TextView) itemView.findViewById(R.id.bodyWeight);
                valueView = (TextView) itemView.findViewById(R.id.valueView);
                positionInTeam = (TextView) itemView.findViewById(R.id.positionInTeam);
                idNum = (TextView) itemView.findViewById(R.id.idNum);
                winPercent = (TextView) itemView.findViewById(R.id.winPercent);

                linTeam = (LinearLayout) itemView.findViewById(R.id.lin_team);

                ivDuihui = (ImageView) itemView.findViewById(R.id.iv_duihui);
                teamType = (ImageView) itemView.findViewById(R.id.teamType);
                teamName = (TextView) itemView.findViewById(R.id.teamName);
                tvShenglv = (TextView) itemView.findViewById(R.id.tv_shenglv);
                levelV = (TextView) itemView.findViewById(R.id.levelV);

            }
        }

        void bindView(final int position, final SearchBean bean) {

            if (bean instanceof UserBean) {
                tabName.setText("球员搜索结果");
                tabName.setVisibility(View.GONE);
//                if(isUserFirst){
//                    isUserFirst = false;
//                    tabName.setVisibility(View.VISIBLE);
//                }else {
//                    tabName.setVisibility(View.GONE);
//                }
                setLayout(1);
                bindUserView(position, (UserBean) bean);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid", ((UserBean) bean).getId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid", ((UserBean) bean).getId());
                            mContext.startActivity(intent);

                        }
                    }
                });
            } else if (bean instanceof TeamBean) {
                tabName.setText("球队搜索结果");
                tabName.setVisibility(View.GONE);
//                if(isTeamFirst){
//                    isTeamFirst = false;
//                    tabName.setVisibility(View.VISIBLE);
//                }else {
//                    tabName.setVisibility(View.GONE);
//                }
                setLayout(2);
                bindTeamView(position, (TeamBean) bean);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
//                        intent.putExtra("beandata",(TeamBean) bean);
//                        mContext.startActivity(intent);
                        Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                        intent.putExtra("teamBean", (TeamBean) bean);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                setLayout(3);
            }
        }

        void setLayout(int index) {
            linRoot.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
            linTeam.setVisibility(index == 2 ? View.VISIBLE : View.GONE);
        }

        private void bindUserView(int position, UserBean bean) {
            userName.setText(TextUtils.isEmpty(bean.getName()) ? "暂无" : bean.getName());
            bodyHeight.setText(StringUtils.isEmpty(bean.getHeight()) ? "" : bean.getHeight() + "CM");
            bodyWeight.setText(StringUtils.isEmpty(bean.getWeight()) ? "" : bean.getWeight() + "KG");
            valueView.setText("$" + bean.getValue());
            positionInTeam.setText(CommonUtils.getPositionStr(bean.getPosition()));
            idNum.setText(bean.getPoint() + "");
            winPercent.setText("胜率：" + CommonUtils.getGamePercent(bean.getWin(), bean.getTotal()) + "%");

            if (!StringUtils.isEmpty(bean.getIconUrl())) {
                ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getIconUrl()), imageview, FootBallApplication.options);
            } else {
                Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(imageview);
            }
        }

        private void bindTeamView(int position, TeamBean teamBean) {
            if (!StringUtils.isEmpty(teamBean.getIconUrl())) {
                ImageLoader.getInstance().displayImage(CommonUtils.getRurl(teamBean.getIconUrl()), ivDuihui, FootBallApplication.options);
            } else {
                Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(ivDuihui);
            }

            teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
            teamName.setText(teamBean.getTeamTitle());
            tvShenglv.setText("胜率：" + CommonUtils.getGamePercent(teamBean.getWin(), teamBean.getTotal()) + "%");
            levelV.setText("等级：" + teamBean.getKind() + "");
        }

    }

}
