package com.football.net.ui.shouye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.SquareVideoBean;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.SquareVideoActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SquareVideoNewAdapter extends UltimateViewAdapter<SquareVideoNewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SquareVideoBean> dataList;

    private int mSavePostion;
    private boolean isrFresh;
    private String mLookNum;

    public String getmLookNum() {
        return mLookNum;
    }

    public void setmLookNum(String mLookNum) {
        this.mLookNum = mLookNum;
    }

    public void setIsrFresh(boolean isrFresh) {
        this.isrFresh = isrFresh;
    }

    public int getPostion() {
        return mSavePostion;
    }

    public SquareVideoNewAdapter(Context context, ArrayList<SquareVideoBean> data) {
        mContext = context;
        dataList = data;
    }


    @Override
    public ViewHolder newFooterHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_square_photo_new, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public int getAdapterItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
      /*      if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.semi_transparent));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
            }*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSavePostion = position;
                    String num = holder.numb2.getText().toString().trim();
                    int n = 0;
                    if (StringUtils.isEmpty(num)) {
                        n = 1;
                    } else {
                        n = Integer.parseInt(num) + 1;
                    }
                    holder.numb2.setText(n + "");
                    dataList.get(position).setViewTimes(n);

                    Intent intent = new Intent(mContext, SquareVideoActivity.class);
//                    intent.putExtra("Url",dataList.get(position).getScreenshot());
//                    intent.putExtra("id",dataList.get(position).getId());
//                    intent.putExtra("likes",dataList.get(position).getLikes());
                    intent.putExtra("SquareVideoBean", dataList.get(position));
//                    mContext.startActivity(intent);
                    ((Activity) mContext).startActivityForResult(intent, 11);
                }
            });
            if (position % 3 == 0) {
                holder.rel_root.setPadding(UIUtils.dip2px(10), UIUtils.dip2px(10), UIUtils.dip2px(3), 0);
            } else if (position % 3 == 1) {
                holder.rel_root.setPadding(UIUtils.dip2px(7), UIUtils.dip2px(10), UIUtils.dip2px(6), 0);
            } else if (position % 3 == 2) {
                holder.rel_root.setPadding(UIUtils.dip2px(4), UIUtils.dip2px(10), UIUtils.dip2px(9), 0);
            }
            holder.bindview(position, dataList.get(position));
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

        ImageView imageview;
        TextView contentV, numb1, numb2;
        View rel_root;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                contentV = (TextView) itemView.findViewById(R.id.contentV);
                numb1 = (TextView) itemView.findViewById(R.id.numb1);
                numb2 = (TextView) itemView.findViewById(R.id.numb2);
                rel_root = itemView.findViewById(R.id.rel_root);
            }
        }


        private void bindview(int position, SquareVideoBean bean) {

            if (!StringUtils.isEmpty(bean.getScreenshot())) {
                ImageLoader.getInstance().displayImage(bean.getScreenshot(), imageview, FootBallApplication.options);
            } else {
                Glide.with(mContext).load(R.mipmap.video_auditing).crossFade().into(imageview);
            }

//            String url = bean.getScreenshot();
//            if (StringUtils.isEmpty(url)) {
//                url = "http://football001.com/web/img/video_auditing.png";
//            }
//            ImageLoader.getInstance().displayImage(url, imageview, FootBallApplication.options);

            contentV.setText(bean.getComment());

            numb1.setText(bean.getLikseNum() + "");
            numb2.setText(bean.getViewTimes() + "");

        }
    }
}
