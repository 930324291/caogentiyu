package com.football.net.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.MessageBean;
import com.football.net.common.util.CommonUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SigninMessageAdapter extends UltimateViewAdapter<SigninMessageAdapter.ViewHolder> {

    private Context mContext;
    private  ArrayList<MessageBean> dataList;
    private OnItemClickLitener mOnItemClickLitener;
    public SigninMessageAdapter(Context context, ArrayList<MessageBean> data) {
        mContext = context;
        dataList = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_signin_message, parent, false);
        return new ViewHolder(view,true);
    }

    @Override
    public int getAdapterItemCount() {
        if(dataList != null){
            return dataList.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickLitener != null){
                        mOnItemClickLitener.onItemClick(v,position);
                    }
                }
            });
            holder.bindview(position,dataList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView titletv,contentV,timeview;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                titletv = (TextView) itemView.findViewById(R.id.titletv);
                contentV = (TextView) itemView.findViewById(R.id.contentV);
                timeview = (TextView) itemView.findViewById(R.id.timeview);
            }
        }


        private void bindview(int position,MessageBean bean){

            titletv.setText( bean.getTitle());
            contentV.setText(bean.getContent());
//            String timeTxt = CommonUtils.getDateStr(bean.get,"yy-MM-dd HH:mm");
//            long diff = System.currentTimeMillis() - bean.getCreateTime();
//            if(diff > 0 ){
//               long minute = diff/1000/60;
//                if(minute < 5){
//                    timeTxt = "刚刚";
//                }
//            }
            timeview.setText(CommonUtils.getDateStr(bean.getOpTime()));
        }
    }

}
