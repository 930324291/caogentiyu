package com.football.net.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class InformEventASignAdapter extends UltimateViewAdapter<InformEventASignAdapter.MineAdapterViewHolder> {

    private OnItemClickLitener mOnItemClickLitener;
    private   ArrayList<NewsBean> dataList = new ArrayList<NewsBean>();

    public InformEventASignAdapter(ArrayList<NewsBean> newsList) {
        dataList = newsList;
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
                .inflate(R.layout.item_inform_news, parent, false);
        MineAdapterViewHolder vh = new MineAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if(dataList != null){
            return dataList.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final MineAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if(position%2==0){
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            }else{
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickLitener != null){
                        mOnItemClickLitener.onItemClick(view,position);
                    }
                }
            });
            holder.bindView(dataList.get(position));
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
        ImageView image1;
        TextView content,titleV,timeV;

        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                image1 = (ImageView) itemView.findViewById(R.id.image1);
                content = (TextView) itemView.findViewById(R.id.content);
                titleV = (TextView) itemView.findViewById(R.id.titleV);
                timeV = (TextView) itemView.findViewById(R.id.timeV);
            }
        }

        private void bindView(NewsBean bean){
            if (!StringUtils.isEmpty(bean.getThumbnail())) {
                // ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getThumbnail()), image1, FootBallApplication.options);
                Glide.with(getContext()).load(bean.getThumbnail()).crossFade().into(image1);
            }
            titleV.setText(bean.getTitle());
            content.setText(Html.fromHtml(bean.getContent()));
            timeV.setText(CommonUtils.getDateStr(bean.getStartTime(),"yyyy-MM-dd HH:mm"));
        }
    }

}
