package com.football.net.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.MineBean;
import com.football.net.interFace.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/27.
 */
public class MineNewAdapter extends UltimateViewAdapter<MineNewAdapter.MineAdapterViewHolder> {

    private ArrayList<HashMap> data;
    private OnItemClickListener mOnItemClickLitener;

    public MineNewAdapter(ArrayList<HashMap> msgList) {
        this.data = msgList;
    }


    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
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
                .inflate(R.layout.item_mine_new, parent, false);
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
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.imageView.setImageResource((Integer) data.get(position).get("imageSrc"));
            holder.titleName.setText((String) data.get(position).get("name"));
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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


    public class MineAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView titleName;
        ImageView imageView;

        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                titleName = (TextView) itemView.findViewById(R.id.titleName);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }
        }
    }

}
