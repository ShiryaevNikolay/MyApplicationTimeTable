package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.timetable.adapters.TabsFragmentAdapter;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.modules.ToolbarBtnBackListener;
import com.google.android.material.tabs.TabLayout;

public class ListScheduleActivity extends AppCompatActivity implements ToolbarBtnBackListener {

    private ScheduleDBHelper scheduleDBHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);

        scheduleDBHelper = new ScheduleDBHelper(this);
        database = scheduleDBHelper.getWritableDatabase();
        initTabs();

        Toolbar toolbar = findViewById(R.id.toolbar_schedule);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);
        new ButtonToReturnToMainActivity(toolbar, this);
    }

    private void initTabs() {
        ViewPager viewPager = findViewById(R.id.viewPager_schedule);

        TabsFragmentAdapter adapter = new TabsFragmentAdapter(this, getSupportFragmentManager(), database);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tablayout_schedule);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickBtnBack() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
