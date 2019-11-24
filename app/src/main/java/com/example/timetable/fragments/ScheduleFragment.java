package com.example.timetable.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.AddScheduleActivity;
import com.example.timetable.R;
import com.example.timetable.RecyclerSchedule;
import com.example.timetable.adapters.ScheduleAdapter;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.modules.SimpleItemTouchHelperCallback;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment {
    public static final String ACCESS_MESSAGE_CLOCK="ACCESS_MESSAGE_CLOCK";
    public static final String ACCESS_MESSAGE_NAME="ACCESS_MESSAGE_NAME";
    public static final String ACCESS_MESSAGE_TEACHER="ACCESS_MESSAGE_TEACHER";

    private static final int LAYOUT = R.layout.fragment_schedule;
    private static int position;

    //для добавление в список и базы данных
    private String clockSchedule;
    private String nameSchedule;
    private String teacherSchedule;
    private int numberItems = 0;
    public List<RecyclerSchedule> listItems;
    ScheduleDBHelper scheduleDBHelper;
    SQLiteDatabase database;
    RecyclerView recyclerView;

    public static ScheduleFragment getInstance(int position) {
        ScheduleFragment.position = position;

        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT, container, false);

        //для базы данных
        scheduleDBHelper = new ScheduleDBHelper(getActivity());
        database = scheduleDBHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);

        listItems = new ArrayList<>();
        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                clockSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK));
                nameSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME));
                teacherSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER));
                listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule));
            }while (cursor.moveToNext());
        }

        // Находим RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_schedule);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        ScheduleAdapter itemsAdapter = new ScheduleAdapter(listItems, database);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        // кнопка добавления элемента в список расписания
        FloatingActionButton fab = view.findViewById(R.id.fab_fragment_schedule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
            if(requestCode == RequestCode.REQUEST_CODE_SCHEDULE){
            if(resultCode==RESULT_OK){
                clockSchedule = data.getStringExtra(ACCESS_MESSAGE_CLOCK);
                nameSchedule = data.getStringExtra(ACCESS_MESSAGE_NAME);
                teacherSchedule = data.getStringExtra(ACCESS_MESSAGE_TEACHER);
                numberItems++;
                listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule));
            }
            else{
                clockSchedule = "hh:mm";
                nameSchedule = "Ошибка доступа";
                teacherSchedule = "Ошибка доступа";
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

        ScheduleAdapter itemsAdapter = new ScheduleAdapter(listItems, database);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }
}
