package com.football.net.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
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
public class PaihangbangTeamAdapter extends UltimateViewAdapter<PaihangbangTeamAdapter.MyAdapterViewHolder> {

    //    private final DisplayImageOptions options;
    private ArrayList<TeamBean> data;
    private OnItemClickListener mOnItemClickLitener;
    private int index;

    public PaihangbangTeamAdapter(ArrayList<TeamBean> msgList) {
        this.data = msgList;
        //显示图片的配置
//        options = new DisplayImageOptions.Builder() //直角
//                .cacheInMemory(false)    //设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
//                .showImageOnLoading(R.mipmap.head)// 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.head) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.head) // 设置图片加载或解码过程中发生错误显示的图片
//                .displayer(new Displayer(0))
//                .build();
    }

    public PaihangbangTeamAdapter(ArrayList<TeamBean> msgList, int index) {
        this.data = msgList;
        this.index = index;
        //显示图片的配置
//        options = new DisplayImageOptions.Builder() //直角
//                .cacheInMemory(false)    //设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
//                .showImageOnLoading(R.mipmap.head)// 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.head) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.head) // 设置图片加载或解码过程中发生错误显示的图片
//                .displayer(new Displayer(0))
//                .build();
    }


    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MyAdapterViewHolder newFooterHolder(View view) {
        return new MyAdapterViewHolder(view, false);
    }

    @Override
    public MyAdapterViewHolder newHeaderHolder(View view) {
        return new MyAdapterViewHolder(view, false);
    }

    @Override
    public MyAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paihangbang_team, parent, false);
        MyAdapterViewHolder vh = new MyAdapterViewHolder(v, true);
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
    public void onBindViewHolder(final MyAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {
//            if(position % 2 == 0) {
//                holder.relCoupon.setVisibility(View.GONE);
//                holder.tvOtherMsg.setVisibility(View.VISIBLE);
//            }else {
//                holder.tvOtherMsg.setVisibility(View.GONE);
//                holder.relCoupon.setVisibility(View.VISIBLE);
//            }
//            if (mOnItemClickLitener != null) {
//                holder.commitBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mOnItemClickLitener.onItemClick(holder.itemView, position);
//                    }
//                });
//            }
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


    public class MyAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView num, teamName, teamStyle, allGameNum, region, winPercent, renqi;
        ImageView image, likeimage;

        public MyAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                num = (TextView) itemView.findViewById(R.id.num);
                teamName = (TextView) itemView.findViewById(R.id.teamName);
                teamStyle = (TextView) itemView.findViewById(R.id.teamStyle);
                allGameNum = (TextView) itemView.findViewById(R.id.allGameNum);
                region = (TextView) itemView.findViewById(R.id.region);
                winPercent = (TextView) itemView.findViewById(R.id.winPercent);
                renqi = (TextView) itemView.findViewById(R.id.renqi);
                likeimage = (ImageView) itemView.findViewById(R.id.likeimage);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }

        public void bindView(int position, TeamBean bean) {
            num.setText((position + 1) + "");
            teamName.setText(bean.getTeamTitle());
            teamStyle.setText(bean.getTeamType() + "人制");
            int total = bean.getTotal();
            allGameNum.setText(total + "");
            region.setText(bean.getRegion());
            winPercent.setText(CommonUtils.getGamePercent(bean.getWin(), bean.getTotal()) + "%");
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
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getIconUrl()), image, FootBallApplication.options);
        }
    }

}
