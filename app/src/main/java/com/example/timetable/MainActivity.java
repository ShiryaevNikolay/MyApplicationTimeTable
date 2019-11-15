package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button addItemsBtn;
    Button addTeacherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Переход в activity "Добавить предметы"
        addItems();
//        Переход в activity "Добавить преподавателей"
        addTeacher();
    }

    public void addItems(){
        addItemsBtn = findViewById(R.id.main_btn_add_items);
        addItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddItemsActivity();
            }
        });
    }

    public void addTeacher(){
        addTeacherBtn = findViewById(R.id.main_btn_add_teacher);
        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddTeacherActivity();
            }
        });
    }

    public void launchAddItemsActivity(){
        Intent intent = new Intent(this, ListItemsActivity.class);
        startActivity(intent);
    }

    public void launchAddTeacherActivity(){
        Intent intent = new Intent(this, ListTeacherActivity.class);
        startActivity(intent);
    }
}
