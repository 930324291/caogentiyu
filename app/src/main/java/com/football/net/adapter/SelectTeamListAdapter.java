package com.football.net.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.ScoreListBean;
import com.football.net.bean.SimpleTeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.widget.Displayer;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SelectTeamListAdapter extends UltimateViewAdapter<SelectTeamListAdapter.MyViewHolder> {

    private ArrayList<SimpleTeamBean> dataList;
    private OnItemClickListener mOnItemClickLitener;

    public SelectTeamListAdapter(ArrayList<SimpleTeamBean> msgList) {
        this.dataList = msgList;
    }


    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MyViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MyViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_team_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v, true);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
//            if(position % 2 == 0) {
//                holder.relCoupon.setVisibility(View.GONE);
//                holder.tvOtherMsg.setVisibility(View.VISIBLE);
//            }else {
//                holder.tvOtherMsg.setVisibility(View.GONE);
//                holder.relCoupon.setVisibility(View.VISIBLE);
//            }
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder.itemView, position);
                    }
                });
            }
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            SimpleTeamBean bean = dataList.get(position);
            holder.bindView(bean);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }



//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//        swapPositions(fromPosition, toPosition);
////        notifyItemMoved(fromPosition, toPosition);
//        super.onItemMove(fromPosition, toPosition);
//    }

//    public void swapPositions(int from, int to) {
//        swapPositions(data, from, to);
//    }


    public class MyViewHolder extends UltimateRecyclerviewViewHolder {
        TextView teamName;

        public MyViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                teamName = (TextView) itemView.findViewById(R.id.teamName);
            }
        }

        public void bindView(SimpleTeamBean bean){
            teamName.setText((String)bean.getTeamTitle());
        }
    }

}
