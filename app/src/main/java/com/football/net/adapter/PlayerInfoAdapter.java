package com.football.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserInfoBean;
import com.football.net.ui.PlayerInfoActivity;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class PlayerInfoAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<UserInfoBean> datalist = new ArrayList<>();

    public PlayerInfoAdapter(Context context,ArrayList<UserInfoBean> list) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        datalist = list;
    }

    @Override
    public int getCount() {
        if(datalist != null){
            return datalist.size();
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
            convertView = inflater.inflate(R.layout.item_player_info, null);
            vHolder = new ViewHolder();
            vHolder.imageV = (ImageView) convertView.findViewById(R.id.imageV);
            vHolder.text = (TextView) convertView.findViewById(R.id.text);
            vHolder.textV = (TextView) convertView.findViewById(R.id.textV);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        vHolder.imageV.setImageResource(datalist.get(position).getImageRid());
        vHolder.text.setText(datalist.get(position).getcName());
        vHolder.textV.setText(datalist.get(position).getName());
        return convertView;
    }

    public static class ViewHolder {
        ImageView imageV;
        TextView textV,text;
    }
}
