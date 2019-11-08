package com.example.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ResizableButton extends Button {

    public ResizableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}