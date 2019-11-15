package com.example.timetable;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

public class ButtonToReturnToMainActivity {
    

    ButtonToReturnToMainActivity(Toolbar toolbar, final Activity activity){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
