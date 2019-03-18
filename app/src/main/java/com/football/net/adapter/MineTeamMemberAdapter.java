package com.football.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class MineTeamMemberAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<UserBean> dataList = new ArrayList<UserBean>();

    public MineTeamMemberAdapter(Context context,ArrayList<UserBean> playerList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        dataList = playerList;
    }

    @Override
    public int getCount() {
        if(dataList != null){
            return  dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_mine_team_member,null);
            vHolder = new ViewHolder();
            vHolder.imageV = (ImageView) convertView.findViewById(R.id.imageV);
            vHolder.captainView = (ImageView) convertView.findViewById(R.id.captainView);
            vHolder.playerName = (TextView) convertView.findViewById(R.id.playerName);
            vHolder.liksNum = (TextView) convertView.findViewById(R.id.liksNum);
            vHolder.valueView = (TextView) convertView.findViewById(R.id.valueView);
            convertView.setTag(vHolder);
        }else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        if(position ==0 || position ==3 || position ==4){
            convertView.setBackgroundResource(R.color.bg_6088b2);
        }else{
            convertView.setBackgroundResource(R.color.transparent);
        }
        UserBean bean = dataList.get(position);
        ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getIconUrl()),vHolder.imageV, FootBallApplication.circOptions);
        vHolder.playerName.setText(bean.getName());
        vHolder.liksNum.setText("人气："+bean.getLikeNum());
        vHolder.valueView.setText("身价：$"+bean.getValue());
        if("1".equals(bean.getIsCaptain())){
            vHolder.captainView.setVisibility(View.VISIBLE);
        }else{
            vHolder.captainView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView imageV;
        ImageView captainView;
        TextView playerName;
        TextView liksNum;
        TextView valueView;
    }
}
