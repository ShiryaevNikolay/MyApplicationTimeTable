package com.example.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.timetable.adapters.ItemsAdapter;
import com.example.timetable.database.ItemDBHelper;
import com.example.timetable.modules.OnItemListener;
import com.example.timetable.modules.SimpleItemTouchHelperCallback;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity implements OnItemListener {

    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    Toolbar toolbar;
    String nameItem;
    RecyclerView recyclerView;

    public List<RecyclerItem> listItems;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        toolbar = findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        //для базы данных
        ItemDBHelper itemDbHelper = new ItemDBHelper(this);
        database = itemDbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(ItemDBHelper.TABLE_ITEMS, null, null, null, null, null, null);

        new ButtonToReturnToMainActivity(toolbar, this);

        listItems = new ArrayList<>();

//        // ОЧИСТКА БАЗЫ ДАННЫХ
//        database.delete(ItemDBHelper.TABLE_ITEMS, null, null);

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                String nameItem = cursor.getString(cursor.getColumnIndex(ItemDBHelper. KEY_NAME));
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

        FloatingActionButton fab = findViewById(R.id.fab_list_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListItemsActivity.this, AddItemsActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_ITEM);
            }
        });

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        ItemsAdapter itemsAdapter = new ItemsAdapter(listItems, database, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCode.REQUEST_CODE_ITEM){
            if(resultCode==RESULT_OK){
                nameItem = data.getStringExtra(ACCESS_MESSAGE);
                listItems.add(new RecyclerItem(nameItem));
            }
            else{
                nameItem = "Ошибка доступа";
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

        ItemsAdapter itemsAdapter = new ItemsAdapter(listItems, database, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}
