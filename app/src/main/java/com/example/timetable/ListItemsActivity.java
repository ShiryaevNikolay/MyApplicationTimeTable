package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    private static  final int REQUEST_ACCESS_TYPE=1;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    Toolbar toolbar;
    String nameItem;
    int numberItems = 0;
    RecyclerView recyclerView;

    public List<RecyclerItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        toolbar = findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        listItems = new ArrayList<>();

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
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
                startActivityForResult(intent, 1);
            }
        });

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        ItemsAdapter itemsAdapter = new ItemsAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ACCESS_TYPE){
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

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        ItemsAdapter itemsAdapter = new ItemsAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }
}
