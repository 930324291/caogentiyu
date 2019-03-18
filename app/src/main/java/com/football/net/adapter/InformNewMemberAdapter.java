package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.ApplyBean;
import com.football.net.bean.ApplyBean2;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class InformNewMemberAdapter extends UltimateViewAdapter<InformNewMemberAdapter.MineAdapterViewHolder> {

    private OnItemClickLitener mOnItemClickLitener;
    private List<ApplyBean2> dataList;

    private Context mContext;

    public InformNewMemberAdapter(Context mContext, List<ApplyBean2> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    public InformNewMemberAdapter(List<ApplyBean2> dataList) {
        this.dataList = dataList;
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
                .inflate(R.layout.item_inform_new_member, parent, false);
        MineAdapterViewHolder vh = new MineAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final MineAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(holder.itemView, position);
                    }
                }
            });

            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.bindView(dataList.get(position));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }


    public class MineAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView name, text2, text3, timeview;
        ImageView image1;

        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                name = (TextView) itemView.findViewById(R.id.name);
                text2 = (TextView) itemView.findViewById(R.id.text2);
                text3 = (TextView) itemView.findViewById(R.id.text3);
                timeview = (TextView) itemView.findViewById(R.id.timeview);
                image1 = (ImageView) itemView.findViewById(R.id.image1);
            }
        }

        void bindView(final ApplyBean2 bean2) {
            final ApplyBean apply = bean2.getApply();
            name.setText(apply.getPlayer().getName());
            text2.setText(apply.getTitle());

            Integer auditStatus = bean2.getAuditStatus();
            Integer confirmStatus = bean2.getConfirmStatus();

            if (auditStatus==null) {
                text3.setText("\u3000\u3000" + "待审核");
            } else {
                if (auditStatus==1) {
                    if (confirmStatus==null) {
                        text3.setText("\u3000\u3000" + "已邀请入队，待球员确认");
                    } else {
                        if (confirmStatus==1) {
                            text3.setText("\u3000\u3000" + "已入队");
                        } else if (confirmStatus==2) {
                            text3.setText("\u3000\u3000" + "已放弃入队");
                        } else {
                            text3.setText("\u3000\u3000" + "ERROR");
                        }
                    }
                } else if (auditStatus==2) {
                    text3.setText("\u3000\u3000" + "已拒绝");
                }
            }

            timeview.setText(CommonUtils.getFullTime(bean2.getCreateTime()));

            if (!StringUtils.isEmpty(apply.getPlayer().getIconUrl())) {
                ImageLoader.getInstance().displayImage( CommonUtils.getRurl(apply.getPlayer().getIconUrl()), image1, FootBallApplication.circOptions);
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
    }

}
