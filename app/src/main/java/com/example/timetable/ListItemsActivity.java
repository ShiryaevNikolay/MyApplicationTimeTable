package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemsActivity extends AppCompatActivity {

    private static  final int REQUEST_ACCESS_TYPE=1;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    Toolbar toolbar;
    TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        toolbar = findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListItemsActivity.this, AddItemsActivity.class);
                startActivity(intent);
            }
        });

        // Находим RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);
        // 50 - кол-во элементов в списке
        ItemsAdapter itemsAdapter = new ItemsAdapter(0);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ACCESS_TYPE){
            if(resultCode==RESULT_OK){
                String accessMessage = data.getStringExtra(ACCESS_MESSAGE);
                textView.setText(accessMessage);
            }
            else{
                textView.setText("Ошибка доступа");
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        };
    }
}
