package com.football.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserInfoBean2;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerInfoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class PlayerTeamHisAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<UserInfoBean2> list2 = new ArrayList<UserInfoBean2>();

    public PlayerTeamHisAdapter(Context context,ArrayList<UserInfoBean2> list2) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list2=list2;
    }

    @Override
    public int getCount() {
        if(list2 != null){
            return  list2.size();
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
        if (convertView == null) {
            vHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_player_team_his, null);
            vHolder.image1 = (ImageView) convertView.findViewById(R.id.image1);
            vHolder.name = (TextView) convertView.findViewById(R.id.name);
            vHolder.timeV = (TextView) convertView.findViewById(R.id.timeV);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        UserInfoBean2 bean = list2.get(position);
        ImageLoader.getInstance().displayImage(bean.getUrl(),vHolder.image1, FootBallApplication.options);
        vHolder.name.setText(bean.getTeamName());
        vHolder.timeV.setText("暂无");
        return convertView;
    }

    public static class ViewHolder {
        ImageView image1;
        TextView name,timeV;
    }
}
