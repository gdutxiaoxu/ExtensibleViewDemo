package com.xujun.administrator.collapseviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * @ explain:
 * @ author：xujun on 2016/6/27 16:14
 * @ email：gdutxiaoxu@163.com
 */
public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(false);
        int horizontalCenter       = getWidth()/2;
        int verticalCenter = getHeight()/2;
        int radius=Math.max(horizontalCenter,verticalCenter);
        canvas.drawCircle(horizontalCenter,verticalCenter,radius,paint);
    }
}
