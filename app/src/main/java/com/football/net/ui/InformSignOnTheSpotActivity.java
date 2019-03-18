package com.football.net.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.MessageBean;
import com.football.net.bean.TankInMessageBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/11.
 */
public class InformSignOnTheSpotActivity extends BasicActivity {

    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.address)
    TextView address;

//    @BindView(R.id.recyclerview1)
//    NoScrollGridView mRecyclerView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
//    MyAdapter mFullAdapter;

    List<TankInMessageBean> tanksAllPlayer = new ArrayList<>();
    private SpotAdater spotAdater;


    @Override
    public int getLayoutId() {
        return R.layout.activity_inform_sign_on_the_spot;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_inform_sign_on_the_spot;
    }

    @Override
    protected void initView() {
        //设置适配器
//        mFullAdapter = new MyAdapter(this,tanksAllPlayer);
//        mRecyclerView.setAdapter(mFullAdapter);

        recyclerview.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,3);
        recyclerview.setLayoutManager(mLayoutManager);
        // Disabled nested scrolling since Parent scrollview will scroll the content.
        recyclerview.setNestedScrollingEnabled(false);
        spotAdater = new SpotAdater(tanksAllPlayer);
        recyclerview.setAdapter(spotAdater);
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

        MessageBean bean = (MessageBean) getIntent().getSerializableExtra("itemBean");

        // 获取该签到信对应的已签到列表
        List<TankInMessageBean> tanks = bean.getSignTanks();
        if(tanks != null){
        /*    for (TankInMessageBean tankbean: tanks){
                if(tankbean.getConfirmStatus() == 1){
                    tanksAllPlayer.add(tankbean);
                }
            }*/
            tanksAllPlayer.addAll(tanks);
            spotAdater.notifyDataSetChanged();
        }
        timeTv.setText(CommonUtils.getDateStr(bean.getBeginTime()));
        address.setText(bean.getAddress());
    }

    public void submit( final TankInMessageBean tankBean) {
        showProgress("提交中....");
        SmartParams params = new SmartParams();
        params.put("auditStatus",1);
        params.put("tankId",tankBean.getId());

        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "signInAudit", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if(result.isSuccess()){
                    showMsg("审核通过");
                    tankBean.setAuditStatus(1);
                    spotAdater.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("审核失败，请重试");
            }

        }, Result.class);
    }


//    public class MyAdapter extends BaseAdapter {
//
//        private Context mContext;
//        private final LayoutInflater inflater;
//        List<TankInMessageBean> dataList;
//
//
//        public MyAdapter(Context context,List<TankInMessageBean> dataList) {
//            mContext = context;
//            inflater = LayoutInflater.from(mContext);
//            this.dataList = dataList;
//        }
//
//        @Override
//        public int getCount() {
////            if(dataList != null){
////                return dataList.size();
////            }
//            return 12;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder vHolder = null;
//            if(convertView == null) {
//                convertView = inflater.inflate(R.layout.inform_sign_on_the_spot_item,null);
//                vHolder = new ViewHolder();
//                vHolder.mcView = (ImageView) convertView.findViewById(R.id.mcView);
//                vHolder.mImg = (ImageView) convertView.findViewById(R.id.image);
//                vHolder.mTxt = (TextView) convertView.findViewById(R.id.name);
//                convertView.setTag(vHolder);
//            }else {
//                vHolder = (ViewHolder) convertView.getTag();
//            }
//            final TankInMessageBean tankInMessageBean = dataList.get(position);
//            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(tankInMessageBean.getPlayer().getIconUrl()),vHolder.mImg, FootBallApplication.circOptions);
//            vHolder.mTxt.setText(tankInMessageBean.getPlayer().getName());
//            if(tankInMessageBean.getAuditStatus() == 1){
//                vHolder.mcView.setVisibility(View.GONE);
//                convertView.setOnClickListener(null);
//            }else {
//                vHolder.mcView.setVisibility(View.VISIBLE);
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        submit(tankInMessageBean);
//                    }
//                });
//            }
//
//            return convertView;
//        }
//
//
//    }
//
//    public static class ViewHolder {
//        ImageView mImg,mcView;
//        TextView mTxt;
//        RelativeLayout relHeader;
//    }


    public class SpotAdater extends RecyclerView.Adapter<SpotViewHolder> {

        List<TankInMessageBean> dataList;

        public SpotAdater(List<TankInMessageBean> tanksAllPlayer) {
            dataList = tanksAllPlayer;
        }

        @Override
        public SpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inform_sign_on_the_spot_item, null);
            return new SpotViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SpotViewHolder holder, int position) {
            if(position % 3 == 0) {
                holder.relHeader.setPadding(UIUtils.dip2px(5),0,0,0);
            }else {
                holder.relHeader.setPadding(UIUtils.dip2px(15),0,0,0);
            }
            if(position < 3) {
                holder.itemView.setPadding(UIUtils.dip2px(5),0,0,0);
            }
            final TankInMessageBean tankInMessageBean = dataList.get(position);
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(tankInMessageBean.getPlayer().getIconUrl()),holder.mImg, FootBallApplication.circOptions);
            holder.mTxt.setText(tankInMessageBean.getPlayer().getName());
            if(tankInMessageBean.getAuditStatus()!=null && tankInMessageBean.getAuditStatus() == 1){
                holder.mcView.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(null);
            }else {
                holder.mcView.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submit(tankInMessageBean);
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            if(dataList != null){
                return dataList.size();
            }
            return 0;
        }
    }

    public class SpotViewHolder extends RecyclerView.ViewHolder{
        ImageView mImg,mcView;
        TextView mTxt;
        RelativeLayout relHeader;
        public SpotViewHolder(View itemView) {
            super(itemView);
            relHeader = (RelativeLayout) itemView.findViewById(R.id.rel_header);
            mcView = (ImageView) itemView.findViewById(R.id.mcView);
            mImg = (ImageView) itemView.findViewById(R.id.image);
            mTxt = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
