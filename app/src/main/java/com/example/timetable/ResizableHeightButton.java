package com.example.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ResizableHeightButton extends Button {

    public ResizableHeightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(height, height);
    }
}
