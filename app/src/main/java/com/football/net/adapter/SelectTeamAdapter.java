package com.football.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.football.net.R;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class SelectTeamAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;

    public SelectTeamAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 10;
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
            convertView = inflater.inflate(R.layout.item_select_team,null);
            vHolder = new ViewHolder();
            convertView.setTag(vHolder);
        }else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public static class ViewHolder {

    }
}
