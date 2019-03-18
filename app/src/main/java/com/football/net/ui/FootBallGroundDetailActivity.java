package com.football.net.ui;

import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.FieldBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/11.
 */
public class FootBallGroundDetailActivity extends BasicActivity {

    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.introduce)
    TextView introduce;

    @Override
    public int getLayoutId() {
        return R.layout.activity_football_ground_detail;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_football_ground_detail;
    }

    @Override
    protected void initView() {
        FieldBean bean = (FieldBean) getIntent().getSerializableExtra("beandata");
        name.setText(bean.getName());
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+ CommonUtils.getRurl(bean.getUrl()), imageview, FootBallApplication.options);
        address.setText("地址："+bean.getAddress());
        contact.setText("联系人："+bean.getContact());
        phone.setText("电话："+bean.getMobile());
        if(bean.getContent() != null){
            introduce.setText(Html.fromHtml(bean.getContent()));
        }else{
            introduce.setText("");
        }
    }
}
