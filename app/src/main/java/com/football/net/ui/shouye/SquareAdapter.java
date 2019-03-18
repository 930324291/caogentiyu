package com.football.net.ui.shouye;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.football.net.R;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.ui.FindTeaamDetailActivity;
import com.football.net.ui.PictureActivity;
import com.football.net.ui.RecruitPeopleDetailActivity;
import com.football.net.ui.SquareVideoActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SquareAdapter extends UltimateViewAdapter<SquareAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    int mFirstType,mSecondType;
    public SquareAdapter(Context context,ArrayList<String> data,int firstType,int secondType) {
        mContext = context;
        mFirstType = firstType;
        mSecondType = secondType;
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
                .inflate(R.layout.item_square, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        return 10;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mSecondType == 0) {
            holder.linPic.setVisibility(View.VISIBLE);
            holder.linTeam.setVisibility(View.GONE);
            holder.linGame.setVisibility(View.GONE);
        }else if(mSecondType == 1) {
            holder.linPic.setVisibility(View.VISIBLE);
            holder.linTeam.setVisibility(View.GONE);
            holder.linGame.setVisibility(View.GONE);
        }else if(mSecondType == 2) {
            holder.linPic.setVisibility(View.GONE);
            holder.linTeam.setVisibility(View.VISIBLE);
            holder.linGame.setVisibility(View.GONE);
        }else if(mSecondType == 3) {
            holder.linPic.setVisibility(View.GONE);
            holder.linTeam.setVisibility(View.VISIBLE);
            holder.linGame.setVisibility(View.GONE);
        }else if(mSecondType == 4) {
            holder.linPic.setVisibility(View.GONE);
            holder.linTeam.setVisibility(View.GONE);
            holder.linGame.setVisibility(View.VISIBLE);
            switch (position % 3) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }
        if (position % 2 == 0) {
            holder.linRoot.setBackgroundResource(R.mipmap.item_bg1);
        } else {
            holder.linRoot.setBackgroundResource(R.mipmap.item_bg2);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSecondType == 0) {
                    mContext.startActivity(new Intent(mContext, PictureActivity.class));
                } else if(mSecondType == 1) {
                    mContext.startActivity(new Intent(mContext, SquareVideoActivity.class));
                } else if(mSecondType == 2) {
                    mContext.startActivity(new Intent(mContext, RecruitPeopleDetailActivity.class));
                } else if(mSecondType == 3) {
                    mContext.startActivity(new Intent(mContext, FindTeaamDetailActivity.class));
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        LinearLayout linPic,linTeam,linGame;
        RelativeLayout linRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (RelativeLayout) itemView.findViewById(R.id.rel_root);
            linPic = (LinearLayout) itemView.findViewById(R.id.lin_pic);
            linTeam = (LinearLayout) itemView.findViewById(R.id.lin_team);
            linGame = (LinearLayout) itemView.findViewById(R.id.lin_game);
        }
    }

}
