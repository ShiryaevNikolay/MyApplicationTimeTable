package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.adapters.TabsPagerFragmentAdapter;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ListScheduleActivity extends AppCompatActivity {
    static final String ACCESS_MESSAGE_CLOCK="ACCESS_MESSAGE_CLOCK";
    static final String ACCESS_MESSAGE_NAME="ACCESS_MESSAGE_NAME";
    static final String ACCESS_MESSAGE_TEACHER="ACCESS_MESSAGE_TEACHER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);

        initTabs();

        // кнопка добавления элемента в список расписания
//        FloatingActionButton fab = findViewById(R.id.fab_fragment_schedule);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ListScheduleActivity.this, AddScheduleActivity.class);
//                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
//            }
//        });

        Toolbar toolbar = findViewById(R.id.toolbar_schedule);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);
        new ButtonToReturnToMainActivity(toolbar, this);


    }

    private void initTabs() {
        ViewPager viewPager = findViewById(R.id.viewPager_schedule);

        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tablayout_schedule);
        tabLayout.setupWithViewPager(viewPager);
    }
}
