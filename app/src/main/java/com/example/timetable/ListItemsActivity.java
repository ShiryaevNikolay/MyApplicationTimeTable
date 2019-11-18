package com.example.timetable;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.timetable.util.DBHelper;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    Toolbar toolbar;
    String nameItem;
    int numberItems = 0;
    RecyclerView recyclerView;

    public List<RecyclerItem> listItems;

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        toolbar = findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        //для базы данных
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_ITEMS, null, null, null, null, null, null);

        new ButtonToReturnToMainActivity(toolbar, this);

        listItems = new ArrayList<>();

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            String nameItem = cursor.getString(cursor.getColumnIndex(DBHelper. KEY_NAME));
            do {
                listItems.add(new RecyclerItem(nameItem));
            }while (cursor.moveToNext());
        }

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_item);
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
                Intent intent = new Intent(ListItemsActivity.this, AddItemsActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_ITEM);
            }
        });

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        ItemsAdapter itemsAdapter = new ItemsAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCode.REQUEST_CODE_ITEM){
            if(resultCode==RESULT_OK){
                nameItem = data.getStringExtra(ACCESS_MESSAGE);
                numberItems++;
                listItems.add(new RecyclerItem(nameItem));
            }
            else{
                nameItem = "Ошибка доступа";
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

        ItemsAdapter itemsAdapter = new ItemsAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }
}
