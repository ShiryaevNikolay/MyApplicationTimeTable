package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.adapters.HomeworkAdapter;
import com.example.timetable.database.HomeworkDBHelper;
import com.example.timetable.database.ItemDBHelper;
import com.example.timetable.modules.OnItemListener;
import com.example.timetable.modules.ToolbarBtnBackListener;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ListHomeworkActivity extends AppCompatActivity implements ToolbarBtnBackListener, OnItemListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<RecyclerHomework> listItems;
    HomeworkAdapter homeworkAdapter;
    SQLiteDatabase database;
    int idItem = 0;
    String nameItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_homework);

        toolbar = findViewById(R.id.toolbar_homework);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        listItems = new ArrayList<>();

        HomeworkDBHelper homeworkDBHelper = new HomeworkDBHelper(this);
        database = homeworkDBHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(HomeworkDBHelper.TABLE_HOMEWORK, null, null, null, null, null, null);

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                nameItem = cursor.getString(cursor.getColumnIndex(HomeworkDBHelper. KEY_TASKS));
                idItem = cursor.getInt(cursor.getColumnIndex(HomeworkDBHelper. KEY_ID));
                listItems.add(new RecyclerHomework(nameItem, idItem));
            }while (cursor.moveToNext());
        }
        cursor.close();

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_homework);
        // то, как будет выглядеть RecyclerView (то есть список)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(gridLayoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab_list_homework);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHomeworkActivity.this, AddHomeworkActivity.class);
                intent.putExtra("requestCode", RequestCode.REQUEST_CODE_HOMEWORK);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_HOMEWORK);
            }
        });

        homeworkAdapter = new HomeworkAdapter(listItems, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(homeworkAdapter);
    }

    @Override
    public void onClickBtnBack() {
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentValues contentValues = new ContentValues();
        if(resultCode==RESULT_OK){
            contentValues.put(HomeworkDBHelper.KEY_TASKS, data.getStringExtra("text"));
            if (requestCode == RequestCode.REQUEST_CODE_HOMEWORK_CHANGE) {
                database.update(HomeworkDBHelper.TABLE_HOMEWORK, contentValues, HomeworkDBHelper.KEY_ID + " = " + Objects.requireNonNull(getIntent().getExtras()).getInt("idItem"), null);
//                nameItem = data.getStringExtra("text");
//                listItems.get(Objects.requireNonNull(data.getExtras()).getInt("position")).setText(nameItem);
            } else if (requestCode == RequestCode.REQUEST_CODE_HOMEWORK) {
                database.insert(HomeworkDBHelper.TABLE_HOMEWORK, null, contentValues);
//                nameItem = data.getStringExtra("text");
//                idItem = data.getIntExtra("idItem", 0);
//                listItems.add(new RecyclerHomework(nameItem, idItem));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        listItems.clear();
        @SuppressLint("Recycle") Cursor cursor = database.query(HomeworkDBHelper.TABLE_HOMEWORK, null, null, null, null, null, null);

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                nameItem = cursor.getString(cursor.getColumnIndex(HomeworkDBHelper. KEY_TASKS));
                idItem = cursor.getInt(cursor.getColumnIndex(HomeworkDBHelper. KEY_ID));
                listItems.add(new RecyclerHomework(nameItem, idItem));
            }while (cursor.moveToNext());
        }
        cursor.close();

        homeworkAdapter = new HomeworkAdapter(listItems, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(homeworkAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, AddHomeworkActivity.class);
        intent.putExtra("requestCode", RequestCode.REQUEST_CODE_HOMEWORK_CHANGE);
        intent.putExtra("text", listItems.get(position).getText());
        intent.putExtra("idItem", listItems.get(position).getId());
        intent.putExtra("position", position);
        startActivityForResult(intent, RequestCode.REQUEST_CODE_HOMEWORK_CHANGE);
    }
}
