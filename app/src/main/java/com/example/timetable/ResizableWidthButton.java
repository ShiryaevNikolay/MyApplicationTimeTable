package com.example.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ResizableWidthButton extends Button {

    public ResizableWidthButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
