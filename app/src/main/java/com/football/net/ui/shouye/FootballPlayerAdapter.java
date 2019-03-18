package com.football.net.ui.shouye;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class FootballPlayerAdapter extends UltimateViewAdapter<FootballPlayerAdapter.ScoreMemberAdapterViewHolder> {

    private ArrayList<UserBean> data;
    private OnItemClickLitener mOnItemClickLitener;

    public FootballPlayerAdapter(ArrayList<UserBean> msgList) {
        this.data = msgList;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
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
                .inflate(R.layout.item_football_player, parent, false);
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
                        mOnItemClickLitener.onItemClick(view,position);
                    }
                }
            });
            holder.bindView(position, data.get(position));
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

        TextView playerName;
        ImageView teamImage,iscaptain,imageview;

        public ScoreMemberAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                playerName = (TextView) itemView.findViewById(R.id.playerName);
                teamImage = (ImageView) itemView.findViewById(R.id.teamImage);
                iscaptain = (ImageView) itemView.findViewById(R.id.iscaptain);
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
              /*  numb1 = (TextView) itemView.findViewById(R.id.numb1);
                price = (TextView) itemView.findViewById(R.id.price);
                teamName = (TextView) itemView.findViewById(R.id.teamName);
                levelV = (TextView) itemView.findViewById(R.id.levelV);
                imageview = (ImageView) itemView.findViewById(R.id.imageview);*/
            }
        }

        public void bindView(int position,UserBean bean) {

            playerName.setText(TextUtils.isEmpty(bean.getName())?"暂无":bean.getName());

            if (!StringUtils.isEmpty(bean.getIconUrl())) {
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getIconUrl()), imageview, FootBallApplication.options);
            }
            /*
            else {
                ImageLoader.getInstance().displayImage("http://football001.com/web/img/noplayer.jpg", imageview, FootBallApplication.options);
            }*/

            if(bean.getTeam() != null){
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getTeam().getIconUrl()), teamImage, FootBallApplication.options);
            }

            /* else{
                ImageLoader.getInstance().displayImage("http://football001.com/web/img/icon_unknow.png", teamImage, FootBallApplication.options);
                // teamImage.setImageResource(R.color.bg_FFA19D9D);
            } */

            if(bean.getIsCaptain() == 1){
                iscaptain.setVisibility(View.VISIBLE);
            }else {
                iscaptain.setVisibility(View.INVISIBLE);
            }

        }
    }

}
