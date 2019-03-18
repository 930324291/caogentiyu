package com.football.net.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.BeanUtils;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class PaihangbangMemberAdapter extends UltimateViewAdapter<PaihangbangMemberAdapter.ScoreMemberAdapterViewHolder> {

    private ArrayList<UserBean> data;
    private OnItemClickListener mOnItemClickLitener;

    private int index;

    public PaihangbangMemberAdapter(ArrayList<UserBean> msgList, int index) {
        this.data = msgList;
        this.index = index;
    }

    public PaihangbangMemberAdapter(ArrayList<UserBean> msgList) {
        this.data = msgList;
    }


    public OnItemClickListener getmOnItemClickLitener() {
        return mOnItemClickLitener;
    }

    public void setmOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ScoreMemberAdapterViewHolder newFooterHolder(View view) {
        return new ScoreMemberAdapterViewHolder(view, false);
    }

    @Override
    public ScoreMemberAdapterViewHolder newHeaderHolder(View view) {
        return new ScoreMemberAdapterViewHolder(view, false);
    }

    @Override
    public ScoreMemberAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paihangbang_member, parent, false);
        ScoreMemberAdapterViewHolder vh = new ScoreMemberAdapterViewHolder(v, true);
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
    public void onBindViewHolder(final ScoreMemberAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.likeimage.setBackgroundResource(R.mipmap.like_icon);
            holder.renqi.setTextColor(Color.parseColor("#ffffff"));
            holder.bindView(position, data.get(position));
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onItemClick(holder.itemView, position);
                    }
                });
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


    public class ScoreMemberAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView num, teamMemberName, teamName, positionInTeam, age, bodyHeight, idNo, renqi;
        ImageView image, duiHuiImage, likeimage;

        public ScoreMemberAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                num = (TextView) itemView.findViewById(R.id.num);
                teamName = (TextView) itemView.findViewById(R.id.teamName);
                teamMemberName = (TextView) itemView.findViewById(R.id.teamMemberName);
                positionInTeam = (TextView) itemView.findViewById(R.id.positionInTeam);
                age = (TextView) itemView.findViewById(R.id.age);
                bodyHeight = (TextView) itemView.findViewById(R.id.bodyHeight);
                idNo = (TextView) itemView.findViewById(R.id.idNo);
                renqi = (TextView) itemView.findViewById(R.id.renqi);
                likeimage = (ImageView) itemView.findViewById(R.id.likeimage);
                image = (ImageView) itemView.findViewById(R.id.image);
                duiHuiImage = (ImageView) itemView.findViewById(R.id.duiHuiImage);
            }
        }

        public void bindView(int position, UserBean bean) {
            num.setText((position + 1) + "");

            teamMemberName.setText(bean.getName());
            String weizhi = CommonUtils.getPositionStr(bean.getPosition());
            positionInTeam.setText(weizhi);
            int ageN = BeanUtils.getAge(bean.getBirth());
            age.setText(ageN == -1 ? "暂无" : ageN + "");
            bodyHeight.setText(bean.getHeight() + "");
            idNo.setText(bean.getUniformNumber() == null ? "暂无" : bean.getUniformNumber());
            if (index == 0) {
                likeimage.setVisibility(View.VISIBLE);
                renqi.setText(bean.getLikeNum() + "");
            } else if (index == 1) {
                likeimage.setVisibility(View.GONE);
                renqi.setText(bean.getValue() + "");
            } else if (index == 2) {
                likeimage.setVisibility(View.GONE);
                renqi.setText(bean.getPoint() + "");
            }
            image.setImageResource(R.mipmap.head);
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getIconUrl()), image, FootBallApplication.circOptions);
            duiHuiImage.setImageResource(R.mipmap.home_icon);
            TeamBean team = bean.getTeam();
            if (team != null) {
                teamName.setText(team.getTeamTitle());
                ImageLoader.getInstance().displayImage(CommonUtils.getRurl(team.getIconUrl()), duiHuiImage, FootBallApplication.options);
            }

        }
    }

}
