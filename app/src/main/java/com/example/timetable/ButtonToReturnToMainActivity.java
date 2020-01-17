package com.example.timetable;

import android.view.View;
import androidx.appcompat.widget.Toolbar;
import com.example.timetable.modules.ToolbarBtnBackListener;

public class ButtonToReturnToMainActivity {

    ButtonToReturnToMainActivity(Toolbar toolbar, final ToolbarBtnBackListener toolbarListener){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarListener.onClickBtnBack();
            }
        });
    }
}
