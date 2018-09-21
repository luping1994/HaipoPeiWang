package net.suntrans.haipopeiwang.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import net.suntrans.haipopeiwang.R;

/**
 * Created by Looney on 2018/9/17.
 * Des:带颜色的 {@link SwipeRefreshLayout}
 */
public class ColorfulSwipeRefreshLayout extends SwipeRefreshLayout{
    public ColorfulSwipeRefreshLayout(Context context) {
        super(context);
        setColorSchemeColors(context.getResources().getColor(R.color.colorPrimary)
        ,context.getResources().getColor(R.color.colorAccent));
    }

    public ColorfulSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(context.getResources().getColor(R.color.colorPrimary)
                ,context.getResources().getColor(R.color.colorAccent));
    }
}
