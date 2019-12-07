package com.example.timetable.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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

public class ScheduleFragment extends AbstractTabFragment implements View.OnClickListener {
    public static final String ACCESS_MESSAGE_CLOCK="ACCESS_MESSAGE_CLOCK";
    public static final String ACCESS_MESSAGE_NAME="ACCESS_MESSAGE_NAME";
    public static final String ACCESS_MESSAGE_TEACHER="ACCESS_MESSAGE_TEACHER";
    public static final String ACCESS_MESSAGE_DAY="ACCESS_MESSAGE_DAY";

    private List<RecyclerSchedule> listItems;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;

    private String clockSchedule;
    private String nameSchedule;
    private String teacherSchedule;

    private static final int LAYOUT = R.layout.fragment_schedule;
    private String daySchedule = "";
    private int position;

    public static ScheduleFragment getInstance(Context context, int position) {
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.position = position;
        fragment.setArguments(args);
        fragment.setContext(context);
        switch (position){
            case 0:
                fragment.setTitle(context.getString(R.string.tab_item_mon));
                fragment.daySchedule = "mon";
                break;
            case 1:
                fragment.setTitle(context.getString(R.string.tab_item_tues));
                fragment.daySchedule = "tues";
                break;
            case 2:
                fragment.setTitle(context.getString(R.string.tab_item_wed));
                fragment.daySchedule = "wed";
                break;
            case 3:
                fragment.setTitle(context.getString(R.string.tab_item_thurs));
                fragment.daySchedule = "thurs";
                break;
            case 4:
                fragment.setTitle(context.getString(R.string.tab_item_fri));
                fragment.daySchedule = "fri";
                break;
            case 5:
                fragment.setTitle(context.getString(R.string.tab_item_sat));
                fragment.daySchedule = "sat";
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT, container, false);

        FloatingActionButton btn = view.findViewById(R.id.fab_fragment_schedule);
        switch(position){
            case 0:
                btn.setId(R.id.fab_fragment_schedule_mon);
                break;
            case 1:
                btn.setId(R.id.fab_fragment_schedule_tues);
                break;
            case 2:
                btn.setId(R.id.fab_fragment_schedule_wed);
                break;
            case 3:
                btn.setId(R.id.fab_fragment_schedule_thurs);
                break;
            case 4:
                btn.setId(R.id.fab_fragment_schedule_fri);
                break;
            case 5:
                btn.setId(R.id.fab_fragment_schedule_sat);
                break;
        }
        btn.setOnClickListener(this);

        //для базы данных
        ScheduleDBHelper scheduleDBHelper = new ScheduleDBHelper(getActivity());
        database = scheduleDBHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);

        listItems = new ArrayList<>();
        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                // проверяем, совпадает ли день недели в базе данных с днём, куда добавляем данные
                if (cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)).equals(daySchedule)){
                    //для добавление в список и базы данных
                    clockSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK));
                    nameSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME));
                    teacherSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER));
                    listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule));
                }
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
        ScheduleAdapter itemsAdapter = new ScheduleAdapter(listItems, database, recyclerView);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemsAdapter, context);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
        switch(v.getId()){
            case R.id.fab_fragment_schedule_mon:
                daySchedule = "mon";
                intent.putExtra("day", daySchedule);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
                break;
            case R.id.fab_fragment_schedule_tues:
                daySchedule = "tues";
                intent.putExtra("day", daySchedule);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
                break;
            case R.id.fab_fragment_schedule_wed:
                daySchedule = "wed";
                intent.putExtra("day", daySchedule);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
                break;
            case R.id.fab_fragment_schedule_thurs:
                daySchedule = "thurs";
                intent.putExtra("day", daySchedule);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
                break;
            case R.id.fab_fragment_schedule_fri:
                daySchedule = "fri";
                intent.putExtra("day", daySchedule);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
                break;
            case R.id.fab_fragment_schedule_sat:
                daySchedule = "sat";
                intent.putExtra("day", daySchedule);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == RequestCode.REQUEST_CODE_SCHEDULE){
            //для добавление в список и базы данных
            if(resultCode==RESULT_OK){
                if (daySchedule.equals(data.getStringExtra(ACCESS_MESSAGE_DAY))){
                    clockSchedule = data.getStringExtra(ACCESS_MESSAGE_CLOCK);
                    nameSchedule = data.getStringExtra(ACCESS_MESSAGE_NAME);
                    teacherSchedule = data.getStringExtra(ACCESS_MESSAGE_TEACHER);
                    listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule));
                }
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

        ScheduleAdapter itemsAdapter = new ScheduleAdapter(listItems, database, recyclerView);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }
}
