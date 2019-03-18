package com.football.net.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.football.net.R;
import com.football.net.adapter.SelectTeamAdapter;
import com.football.net.common.util.UIUtils;


public class SelectTeamsDialog {

    public interface MyDialogListener {
        void onItemClickListener(int positon);
    }

    Dialog dialog;
    public SelectTeamsDialog(Context context, final MyDialogListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_teams,null);
        GridView footballTeamGridview = (GridView) view.findViewById(R.id.football_team_gridview);
        footballTeamGridview.setAdapter(new SelectTeamAdapter(context));
        footballTeamGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener != null){
                    listener.onItemClickListener(position);
                    dialog.dismiss();
                }
            }
        });

        dialog = new Dialog(context,R.style.dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UIUtils.dip2px(200);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}
