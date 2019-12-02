package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.timetable.adapters.ItemsAdapter;
import com.example.timetable.adapters.TeachersAdapter;
import com.example.timetable.database.ItemDBHelper;
import com.example.timetable.database.TeacherDBHelper;
import com.example.timetable.modules.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectListItemActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemDBHelper itemDBHelper;
    TeacherDBHelper teacherDBHelper;
    SQLiteDatabase database;

    public List<RecyclerItem> listItem;

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_list_item);

        listItem = new ArrayList<>();
        Intent data = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar_select);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        recyclerView = findViewById(R.id.recyclerView_list);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        if (Objects.equals(data.getStringExtra("selectBtn"), "item")) {
            itemDBHelper = new ItemDBHelper(this);
            database = itemDBHelper.getWritableDatabase();
        } else if (Objects.equals(data.getStringExtra("selectBtn"), "teacher")) {
            teacherDBHelper = new TeacherDBHelper(this);
            database = teacherDBHelper.getWritableDatabase();
        }

        Cursor cursor = null;

        if (Objects.equals(data.getStringExtra("selectBtn"), "item")) {
            cursor = database.query(ItemDBHelper.TABLE_ITEMS, null, null, null, null, null, null);
        } else if (Objects.equals(data.getStringExtra("selectBtn"), "teacher")) {
            cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, null, null, null, null, null);
        }

        String nameItem = "";
        assert cursor != null;
        if (cursor.moveToFirst()){
            do {
                if (Objects.equals(data.getStringExtra("selectBtn"), "item")) {
                    nameItem = cursor.getString(cursor.getColumnIndex(ItemDBHelper. KEY_NAME));
                } else if (Objects.equals(data.getStringExtra("selectBtn"), "teacher")) {
                    nameItem = cursor.getString(cursor.getColumnIndex(TeacherDBHelper. KEY_NAME));
                }
                listItem.add(new RecyclerItem(nameItem));
            }while (cursor.moveToNext());
        }

        ItemsAdapter itemsAdapter = new ItemsAdapter(listItem, database);
        TeachersAdapter teachersAdapter = new TeachersAdapter(listItem, database);
        if (Objects.equals(data.getStringExtra("selectBtn"), "item")) {
            //назначаем RecyclerView созданный Adapter
            recyclerView.setAdapter(itemsAdapter);
        } else if (Objects.equals(data.getStringExtra("selectBtn"), "teacher")) {
            //назначаем RecyclerView созданный Adapter
            recyclerView.setAdapter(teachersAdapter);
        }
    }

    protected void sendMessage(String message){
        Intent data = new Intent();
        data.putExtra(AddScheduleActivity.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();
    }
}
