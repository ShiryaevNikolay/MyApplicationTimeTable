package com.example.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ListTeacherActivity extends AppCompatActivity {

    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    Toolbar toolbar;
    String nameTeacher;
    int numberTeacher = 0;
    RecyclerView recyclerView;

    public List<RecyclerItem> listTeacher;

    TeacherDBHelper teacherDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teacher);
        toolbar = findViewById(R.id.toolbar_teacher);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        //для базы данных
        teacherDBHelper = new TeacherDBHelper(this);
        SQLiteDatabase database = teacherDBHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, null, null, null, null, null);

        new ButtonToReturnToMainActivity(toolbar, this);

        listTeacher = new ArrayList<>();

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                String nameTeacher = cursor.getString(cursor.getColumnIndex(TeacherDBHelper. KEY_NAME));
                listTeacher.add(new RecyclerItem(nameTeacher));
            }while (cursor.moveToNext());
        }

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_teacher);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTeacherActivity.this, AddTeachersActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_TEAHER);
            }
        });

        TeachersAdapter teachersAdapter = new TeachersAdapter(listTeacher);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(teachersAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.REQUEST_CODE_TEAHER){
            if (resultCode == RESULT_OK){
                nameTeacher = data.getStringExtra(ACCESS_MESSAGE);
                numberTeacher++;
                listTeacher.add(new RecyclerItem(nameTeacher));
            } else {
                nameTeacher = "Ошибка доступа";
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        TeachersAdapter teachersAdapter = new TeachersAdapter(listTeacher);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(teachersAdapter);
    }
}
