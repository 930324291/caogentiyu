package com.football.net.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.football.net.R;
import com.football.net.interFace.OnItemClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class PlayerPictureAdapter extends UltimateViewAdapter<PlayerPictureAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;

    public PlayerPictureAdapter(Context context, ArrayList<String> data) {
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onitemClickListener) {
        this.mOnitemClickListener = onitemClickListener;
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        return 6;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
        } else {
            holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        LinearLayout linRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
        }
    }

}
