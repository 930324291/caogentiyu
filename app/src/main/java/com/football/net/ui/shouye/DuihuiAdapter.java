package com.football.net.ui.shouye;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.DuihuiBean;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class DuihuiAdapter extends UltimateViewAdapter<DuihuiAdapter.ScoreMemberAdapterViewHolder> {

    private ArrayList<DuihuiBean> data;
    private OnItemClickListener mOnItemClickLitener;

    public DuihuiAdapter(ArrayList<DuihuiBean> msgList) {
        this.data = msgList;
    }


    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public ScoreMemberAdapterViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ScoreMemberAdapterViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ScoreMemberAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_duihui, parent, false);
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickLitener != null){
//                        holder.iv_zhezhao.setVisibility(View.GONE);
                        mOnItemClickLitener.onItemClick(view,position);
                    }
                }
            });
            if(position == 0) {
                holder.tv_upload_duihui.setVisibility(View.VISIBLE);
                holder.imageview.setVisibility(View.GONE);
//                holder.iv_zhezhao.setVisibility(View.GONE);
            }else {
                holder.tv_upload_duihui.setVisibility(View.GONE);
                holder.imageview.setVisibility(View.VISIBLE);
//                holder.iv_zhezhao.setVisibility(View.VISIBLE);
                holder.bindView(position, data.get(position - 1));
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

        ImageView imageview,iv_zhezhao;
        RelativeLayout relSelectDuihui;
        TextView tv_upload_duihui;
        public ScoreMemberAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                relSelectDuihui = (RelativeLayout) itemView.findViewById(R.id.rel_select_duihui);
                tv_upload_duihui = (TextView) itemView.findViewById(R.id.tv_upload_duihui);
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                iv_zhezhao = (ImageView) itemView.findViewById(R.id.iv_zhezhao);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        UIUtils.getScreenWidth()/3
                );
                relSelectDuihui.setLayoutParams(params);
            }
        }

        public void bindView(int position,DuihuiBean bean) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getUrl()), imageview, FootBallApplication.options);
        }
    }

}
