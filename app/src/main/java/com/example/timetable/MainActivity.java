package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button addItemsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResizableButton button1 = findViewById(R.id.main_btn_add_items);

//        Переход в activity "Добавить предметы"
        addItems();
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

    public void launchAddItemsActivity(){
        Intent intent = new Intent(this, ListItemsActivity.class);
        startActivity(intent);
    }
}
