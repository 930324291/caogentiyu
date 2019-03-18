package com.football.net.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 订阅频道栏目-自定义用户未订阅栏目gridview
 */
public class OtherGrid extends GridView {

	public OtherGrid(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
