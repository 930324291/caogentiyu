package com.football.net.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.ScoreListBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.Displayer;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class ScoreTeamListAdapter extends UltimateViewAdapter<ScoreTeamListAdapter.ScoreTeamAdapterViewHolder> {

    private final DisplayImageOptions options;
    private ArrayList<ScoreListBean> data;
    private OnItemClickLitener mOnItemClickLitener;

    public ScoreTeamListAdapter(ArrayList<ScoreListBean> msgList) {
        this.data = msgList;
        options = new DisplayImageOptions.Builder() //直角
                .cacheInMemory(false)    //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
                .showImageOnLoading(R.mipmap.head)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.head) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.head) // 设置图片加载或解码过程中发生错误显示的图片
                .displayer(new Displayer(0))
                .build();
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public ScoreTeamAdapterViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ScoreTeamAdapterViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ScoreTeamAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fabu_score_team_list, parent, false);
        ScoreTeamAdapterViewHolder vh = new ScoreTeamAdapterViewHolder(v, true);
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
    public void onBindViewHolder(final ScoreTeamAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {
//            if(position % 2 == 0) {
//                holder.relCoupon.setVisibility(View.GONE);
//                holder.tvOtherMsg.setVisibility(View.VISIBLE);
//            }else {
//                holder.tvOtherMsg.setVisibility(View.GONE);
//                holder.relCoupon.setVisibility(View.VISIBLE);
//            }
            if (mOnItemClickLitener != null) {
                holder.commitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.get(position).getTeamB() != null) {
                            mOnItemClickLitener.onItemClick(holder.itemView, position);
                        } else {
                            ToastUtil.show(FootBallApplication.getInstance(), "暂无球队");
                        }
                    }
                });
            }
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.bindView(data.get(position));
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


    public class ScoreTeamAdapterViewHolder extends UltimateRecyclerviewViewHolder {
        TextView teamName1, teamName2, time, address, commitBtn;
        ImageView aimge, bImage;

        public ScoreTeamAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                commitBtn = (TextView) itemView.findViewById(R.id.commitBtn);
                teamName1 = (TextView) itemView.findViewById(R.id.teamName1);
                teamName2 = (TextView) itemView.findViewById(R.id.teamName2);
                time = (TextView) itemView.findViewById(R.id.time);
                address = (TextView) itemView.findViewById(R.id.address);
                aimge = (ImageView) itemView.findViewById(R.id.aimge);
                bImage = (ImageView) itemView.findViewById(R.id.bImage);
            }
        }

        public void bindView(ScoreListBean bean) {
            teamName1.setText(bean.getTeamA() == null ? "暂无" : bean.getTeamA().getTeamTitle());
            teamName2.setText(bean.getTeamB() == null ? "暂无" : bean.getTeamB().getTeamTitle());
            time.setText(CommonUtils.getDateStr(bean.getBeginTime(), "yyyy-MM-dd HH:mm"));
            address.setText(bean.getAddress());
//            aimge.setImageResource(R.mipmap.home_icon);
            ImageLoader.getInstance().displayImage((bean.getTeamA() == null ? "errorPicture" : CommonUtils.getRurl(bean.getTeamA().getIconUrl())), aimge, FootBallApplication.options);
//            bImage.setImageResource(R.mipmap.home_icon);
            ImageLoader.getInstance().displayImage((bean.getTeamB() == null ? "errorPicture" : CommonUtils.getRurl(bean.getTeamB().getIconUrl())), bImage, FootBallApplication.options);
        }
    }

}
