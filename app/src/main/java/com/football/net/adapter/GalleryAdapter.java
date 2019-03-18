package com.football.net.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.FabuSigninMessageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<UserBean> dataList;
    private OnItemClickListener mOnitemClickListener;
    HashSet<Integer> selectedSet = new HashSet<>();

    public GalleryAdapter(Context context, ArrayList<UserBean> datats,HashSet<Integer> selectedSet) {
        mInflater = LayoutInflater.from(context);
        dataList = datats;
        this.selectedSet = selectedSet;
    }

    public void setmOnitemClickListener(OnItemClickListener mOnitemClickListener) {
        this.mOnitemClickListener = mOnitemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg,mcView;
        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.fabu_signin_message_recycler_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view.findViewById(R.id.image);
        viewHolder.mcView = (ImageView) view.findViewById(R.id.mcView);
        viewHolder.mTxt = (TextView) view.findViewById(R.id.name);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ImageLoader.getInstance().displayImage(CommonUtils.getRurl(dataList.get(position).getIconUrl()),viewHolder.mImg,FootBallApplication.circOptions);
        viewHolder.mTxt.setText(dataList.get(position).getName());
        if(selectedSet.contains(position)){
            viewHolder.mcView.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.mcView.setVisibility(View.VISIBLE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnitemClickListener != null){
                    mOnitemClickListener.onItemClick(view,position);
                }
            }
        });
    }

}
