package com.codeclan.example.rememberme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 10/08/2016.
 */
public class DragShadow extends View.DragShadowBuilder {

//    ColorDrawable greyBox;
    View mView;

    public DragShadow(View view) {
        super(view);
//        greyBox = new ColorDrawable(Color.parseColor("#FFDEF3"));
        mView = view;
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
//        greyBox.draw(canvas);
        mView.draw(canvas);

    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        int height = mView.getHeight();
        int width = mView.getWidth();

//        greyBox.setBounds(0, 0, width, height);
//        mView.setBounds(0, 0, width, height);

        shadowSize.set(width, height);
        shadowTouchPoint.set(width/2, height/2);   // centers touch point
    }

}
