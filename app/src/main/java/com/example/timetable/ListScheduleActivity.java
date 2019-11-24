package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.timetable.adapters.TabsPagerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class ListScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);

        initTabs();

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
