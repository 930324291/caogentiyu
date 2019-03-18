package com.football.net.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.util.UIUtils;


public class CreateOrJoinTeamDialog {

    public interface MyDialogListener {
        void onLeftBtnClick();
        void onRightBtnClick();
    }

    Dialog dialog;
    public CreateOrJoinTeamDialog(Context context, final MyDialogListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_createor_join,null);

        View layout1 =  view.findViewById(R.id.layout1);
        View layout2 =  view.findViewById(R.id.layout2);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(listener != null){
                    listener.onLeftBtnClick();
                }
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onRightBtnClick();
                }
            }
        });
        dialog = new Dialog(context,R.style.dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UIUtils.dip2px(270);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}
