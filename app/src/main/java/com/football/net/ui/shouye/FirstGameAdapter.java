package com.football.net.ui.shouye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.GameDetailAty;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class FirstGameAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();
    private int mType;
    public SparseBooleanArray checkMap;

    public FirstGameAdapter(Context context, int type, ArrayList<GameBean> newGameList) {
        mContext = context;
        mType = type;
        inflater = LayoutInflater.from(mContext);
        this.dataList = newGameList;
        checkMap = new SparseBooleanArray();
    }

    /**
     * 改变某个item的选中状态 <功能详细描述> 单选
     */
    public void setCheckAtPosFalse(int pos, boolean flag) {
        for (int i = 0; i < getCount(); i++) {
            checkMap.put(i, false);
        }
        if (flag) {
            checkMap.put(pos, !checkMap.get(pos, false));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
  /*      if(dataList != null){
          return   dataList.size() > 2?2: dataList.size();
        }*/
        return 2;
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
            convertView = inflater.inflate(R.layout.item_first_news_game, null);
            vHolder = new ViewHolder();
            vHolder.rel_game = convertView.findViewById(R.id.rel_game);
            vHolder.iv_vs = convertView.findViewById(R.id.iv_vs);
            vHolder.tv_result = (TextView) convertView.findViewById(R.id.tv_result);

            vHolder.imageA = (ImageView) convertView.findViewById(R.id.imageA);
            vHolder.imageB = (ImageView) convertView.findViewById(R.id.imageB);
            vHolder.teamType = (ImageView) convertView.findViewById(R.id.teamType);

            vHolder.nameA = (TextView) convertView.findViewById(R.id.nameA);
            vHolder.nameB = (TextView) convertView.findViewById(R.id.nameB);
            vHolder.timeV = (TextView) convertView.findViewById(R.id.timeV);
            vHolder.fildname = (TextView) convertView.findViewById(R.id.fildname);

            vHolder.layout1 = convertView.findViewById(R.id.layout1);
            vHolder.layout2 = convertView.findViewById(R.id.layout2);
            vHolder.layout3 = convertView.findViewById(R.id.layout3);
            vHolder.mDetailLinear = (LinearLayout) convertView.findViewById(R.id.id_detail_foot_rela);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        if (checkMap.get(position, false)) {
            vHolder.layout1.setBackgroundResource(R.color.bg_FFDEAD);
        } else {
            vHolder.layout1.setBackgroundResource(R.color.transparent);
        }
        if (mType == 0) {
            vHolder.iv_vs.setVisibility(View.VISIBLE);
            vHolder.tv_result.setVisibility(View.GONE);
            if (position % 2 == 0) {
                vHolder.rel_game.setBackgroundColor(Color.parseColor("#665881b3"));
            } else {
                vHolder.rel_game.setBackgroundColor(Color.parseColor("#663b5c86"));
            }
        } else {
            vHolder.tv_result.setVisibility(View.VISIBLE);
            vHolder.iv_vs.setVisibility(View.GONE);
            if (position % 2 == 0) {
                vHolder.rel_game.setBackgroundColor(Color.parseColor("#663b5c86"));
            } else {
                vHolder.rel_game.setBackgroundColor(Color.parseColor("#665881b3"));
            }
        }
        if (dataList == null || dataList.size() == 0) {
            if (position == 0) {
                vHolder.layout1.setVisibility(View.GONE);
                vHolder.layout2.setVisibility(View.VISIBLE);
                vHolder.layout3.setVisibility(View.GONE);
            } else {
                vHolder.layout1.setVisibility(View.GONE);
                vHolder.layout2.setVisibility(View.GONE);
                vHolder.layout3.setVisibility(View.VISIBLE);
            }
        } else if (dataList != null && dataList.size() == 1) {
            if (position == 0) {
                bindView(vHolder, position);
            } else {
                vHolder.layout1.setVisibility(View.GONE);
                vHolder.layout2.setVisibility(View.GONE);
                vHolder.layout3.setVisibility(View.VISIBLE);
            }
        } else {
            vHolder.layout1.setVisibility(View.VISIBLE);
            vHolder.layout2.setVisibility(View.GONE);
            vHolder.layout3.setVisibility(View.GONE);
            bindView(vHolder, position);
        }
        return convertView;
    }

    public static class ViewHolder {
        View rel_game;
        View iv_vs;
        TextView tv_result;
        ImageView imageA, imageB, teamType;
        TextView nameA, nameB, timeV, fildname;
        View layout1, layout2, layout3;
        LinearLayout mDetailLinear;
    }

    void bindView(ViewHolder vHolder, int position) {
        final GameBean gameBean = dataList.get(position);

        vHolder.nameA.setText(gameBean.getTeamA().getTeamTitle());
        vHolder.nameB.setText(gameBean.getTeamB() == null ? "暂无" : gameBean.getTeamB().getTeamTitle());
        vHolder.timeV.setText(CommonUtils.getDateStr(gameBean.getBeginTime(), "yyyy-MM-dd HH:mm"));
        vHolder.fildname.setText(gameBean.getAddress());
        if (mType == 0) {
        } else {
            vHolder.tv_result.setText(gameBean.getScoreA1() + " : " + gameBean.getScoreB1());
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()), vHolder.imageA, FootBallApplication.circOptions);
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamB() == null ? "" : gameBean.getTeamB().getIconUrl()), vHolder.imageB, FootBallApplication.circOptions);
        vHolder.teamType.setImageResource(CommonUtils.getTeamTypeImage(gameBean.getTeamType()));

        vHolder.imageA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == gameBean.getTeamA()) {
                    ToastUtil.show(mContext,"没有球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", gameBean.getTeamA());
                mContext.startActivity(intent);
            }
        });
        vHolder.imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == gameBean.getTeamB()) {
                    ToastUtil.show(mContext,"没有球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", gameBean.getTeamB());
                mContext.startActivity(intent);
            }
        });
        vHolder.mDetailLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameDetailAty.class);
                intent.putExtra(IntentKey.General.KEY_MODEL, gameBean);
                intent.putExtra(IntentKey.General.KEY_TYPE, mType);
                mContext.startActivity(intent);
            }
        });
    }
}
