package net.suntrans.suntranscomponents.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Looney on 2017/9/4.
 */

public class DefaultDecoration extends RecyclerView.ItemDecoration {

    private int offsets=0;
    private Paint mPaint;
    private int mOffsetsColor = Color.parseColor("#eeeeee");
    private Rect mBounds = new Rect();
    private int padding =0;
    private Context context;

    public DefaultDecoration(int offsets, int mOffsetsColor, Context context) {
        this.offsets = offsets;
        this.mOffsetsColor = mOffsetsColor;
        this.context = context;
        offsets = dp2px(2);
        init();
    }

    public DefaultDecoration(Context context) {
        this.context = context;
        offsets = dp2px(2);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mOffsetsColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = offsets;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft() + padding;
        int right = parent.getWidth() - parent.getPaddingRight() - padding;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + offsets;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
        canvas.restore();
    }

    private int dp2px(final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
