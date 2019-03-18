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

import com.football.net.R;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.interFace.OnButtonClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PictureActivity1;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SquarePhotoAdapter extends UltimateViewAdapter<SquarePhotoAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SquarePhotoBean> dataList;
    private boolean ifSshowDelete;
    OnButtonClickListener onDeleteButtonClick;

    public SquarePhotoAdapter(Context context, ArrayList<SquarePhotoBean> data) {
        mContext = context;
        dataList = data;
    }

    public boolean isIfSshowDelete() {
        return ifSshowDelete;
    }

    public void setIfSshowDelete(boolean ifSshowDelete) {
        this.ifSshowDelete = ifSshowDelete;
    }

    public OnButtonClickListener getOnDeleteButtonClick() {
        return onDeleteButtonClick;
    }

    public void setOnDeleteButtonClick(OnButtonClickListener onDeleteButtonClick) {
        this.onDeleteButtonClick = onDeleteButtonClick;
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
                .inflate(R.layout.item_square_photo, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, PictureActivity.class);
                    Intent intent = new Intent(mContext, PictureActivity1.class);
//                    intent.putExtra("Url",dataList.get(position).getPics().get(0).getUrl());
                    intent.putExtra("SquarePhotoBean", dataList.get(position));
//                    mContext.startActivity(intent);
                    ((Activity) mContext).startActivityForResult(intent, 10);
                }
            });
            if (dataList.get(position) != null)
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

        ImageView imageview, deleteBtn;
        TextView contentV, timeview, numb1, numb2;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                deleteBtn = (ImageView) itemView.findViewById(R.id.deleteBtn);
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                contentV = (TextView) itemView.findViewById(R.id.contentV);
                timeview = (TextView) itemView.findViewById(R.id.timeview);
                numb1 = (TextView) itemView.findViewById(R.id.numb1);
                numb2 = (TextView) itemView.findViewById(R.id.numb2);
            }
        }


        private void bindview(final int position, SquarePhotoBean bean) {
            if (bean.getPicsList() != null && bean.getPicsList().size() > 0) {
                String pic = bean.getPicsList().get(0).getUrl();
                if (!StringUtils.isEmpty(pic)) {
                    ImageLoader.getInstance().displayImage(CommonUtils.getRurl(pic), imageview, FootBallApplication.options);
                } else {
                    imageview.setImageResource(R.color.pickerview_bg_topbar);
                }
            }

            contentV.setText(bean.getComment());
            String timeTxt = CommonUtils.getDateStr(bean.getCreateTime(), "yyyy-MM-dd HH:mm");
            long diff = System.currentTimeMillis() - bean.getCreateTime();
            if (diff > 0) {
                long minute = diff / 1000 / 60;
                if (minute < 5) {
                    timeTxt = "刚刚";
                }
            }
            timeview.setText(timeTxt);

            bean.getLikes();
            numb1.setText(bean.getLikseNum() + "");
            numb2.setText(bean.getViewTimes() + "");

            if (ifSshowDelete) {
                deleteBtn.setVisibility(View.VISIBLE);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onDeleteButtonClick != null) {
                            onDeleteButtonClick.onItemClick(deleteBtn, position);
                        }
                    }
                });
            }
        }
    }

}
