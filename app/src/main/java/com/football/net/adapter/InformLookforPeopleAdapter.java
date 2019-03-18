package com.football.net.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.net.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class InformLookforPeopleAdapter extends UltimateViewAdapter<InformLookforPeopleAdapter.MineAdapterViewHolder> {

    private OnItemClickLitener mOnItemClickLitener;
    private List data;

    public InformLookforPeopleAdapter() {
        data = new ArrayList();
        data = new ArrayList();
        data = new ArrayList();
        for(int i= 0; i < 6 ; i++){
            data.add(new Object());
        }
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
                .inflate(R.layout.item_inform_lookforpeople, parent, false);
        MineAdapterViewHolder vh = new MineAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final MineAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {
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

//        TextView relCoupon,commitBtn;

        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
//                commitBtn = (TextView) itemView.findViewById(R.id.commitBtn);
            }
        }
    }

}
