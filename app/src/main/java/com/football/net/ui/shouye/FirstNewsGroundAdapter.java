package com.football.net.ui.shouye;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.FieldBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class FirstNewsGroundAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<FieldBean> dataList = new ArrayList<FieldBean>();

    public FirstNewsGroundAdapter(Context context,ArrayList<FieldBean> fieldList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        dataList = fieldList;
    }

    @Override
    public int getCount() {
        if(dataList != null){
            return  dataList.size()>6?6:dataList.size();
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
            convertView = inflater.inflate(R.layout.item_first_news_ground,null);
            vHolder = new ViewHolder();
            vHolder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
            vHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(vHolder);
        }else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        FieldBean bean = dataList.get(position);
        vHolder.name.setText(bean.getName());
        ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getUrl()), vHolder.imageview, FootBallApplication.options);
        return convertView;
    }

    public static class ViewHolder {
            ImageView imageview;
        TextView name;

    }
}
