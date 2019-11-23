package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button addItemsBtn;
    Button addTeacherBtn;
    Button makeScheduleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Переход в activity "Добавить предметы"
        addItems();
//        Переход в activity "Добавить преподавателей"
        addTeacher();
//        Переход в activity "составить расписание"
        makeSchedule();
    }

    private void addItems(){
        addItemsBtn = findViewById(R.id.main_btn_add_items);
        addItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddItemsActivity();
            }
        });
    }

    private void addTeacher(){
        addTeacherBtn = findViewById(R.id.main_btn_add_teacher);
        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddTeacherActivity();
            }
        });
    }

    private void makeSchedule(){
        makeScheduleBtn = findViewById(R.id.main_btn_make_schedule);
        makeScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMakeScheduleActivity();
            }
        });
    }

    private void launchAddItemsActivity(){
        Intent intent = new Intent(this, ListItemsActivity.class);
        startActivity(intent);
    }

    private void launchAddTeacherActivity(){
        Intent intent = new Intent(this, ListTeacherActivity.class);
        startActivity(intent);
    }

    private void launchMakeScheduleActivity(){
        Intent intent = new Intent(this, ListScheduleActivity.class);
        startActivity(intent);
    }
}
