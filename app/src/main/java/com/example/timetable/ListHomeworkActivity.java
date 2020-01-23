package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.adapters.HomeworkAdapter;
import com.example.timetable.modules.ToolbarBtnBackListener;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListHomeworkActivity extends AppCompatActivity implements ToolbarBtnBackListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<RecyclerHomework> listItems;
    HomeworkAdapter homeworkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_homework);

        toolbar = findViewById(R.id.toolbar_homework);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        listItems = new ArrayList<>();

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_homework);
        // то, как будет выглядеть RecyclerView (то есть список)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(gridLayoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab_list_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ListHomeworkActivity.this, AddHomeworkActivity.class);
//                intent.putExtra("requestCode", RequestCode.REQUEST_CODE_HOMEWORK);
//                startActivityForResult(intent, RequestCode.REQUEST_CODE_HOMEWORK);
            }
        });

        homeworkAdapter = new HomeworkAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(homeworkAdapter);
    }

    @Override
    public void onClickBtnBack() {
        finish();
    }
}
