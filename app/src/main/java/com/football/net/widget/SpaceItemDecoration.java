package com.football.net.widget;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.football.net.common.util.UIUtils;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace ;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space) {
        this.mSpace = UIUtils.dip2px(space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int pos = parent.getChildAdapterPosition(view);

        outRect.left = 0;
        outRect.top = 0;
        outRect.bottom = 0;


        if (pos != (itemCount -1)) {
            outRect.right = mSpace;
        } else {
            outRect.right = 0;
        }
    }
}