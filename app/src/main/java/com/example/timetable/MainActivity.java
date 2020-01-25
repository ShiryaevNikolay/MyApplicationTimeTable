package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.timetable.adapters.ScheduleAdapter;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.util.RequestCode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    private String daySchedule = "";
    TextView textView1DayOff;

    SQLiteDatabase database;
    ArrayList<RecyclerSchedule> listItems;
    RecyclerView recyclerView;
    ScheduleAdapter itemsAdapter;

    private String clockSchedule;
    private String nameSchedule;
    private String teacherSchedule;
    private String classSchedule;
    private int idItem = 0;
    private int hours = 0;
    private int minutes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view_main_activity);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        TextView textView = findViewById(R.id.tv_ll_day_main_activity);
        textView.setAllCaps(true);
        textView1DayOff = findViewById(R.id.tv_day_off_main_activity);
        switch (day) {
            case Calendar.MONDAY:
                daySchedule = "mon";
                textView.setText("Понедельник");
                break;
            case Calendar.TUESDAY:
                daySchedule = "tues";
                textView.setText("Вторник");
                break;
            case Calendar.WEDNESDAY:
                daySchedule = "wed";
                textView.setText("Среда");
                break;
            case Calendar.THURSDAY:
                daySchedule = "thurs";
                textView.setText("Четверг");
                break;
            case Calendar.FRIDAY:
                daySchedule = "fri";
                textView.setText("Пятница");
                break;
            case Calendar.SATURDAY:
                daySchedule = "sat";
                textView.setText("Суббота");
                break;
            case Calendar.SUNDAY:
                daySchedule = "sun";
                textView.setText("Воскресенье");
                break;
        }
        boolean flag = false;
        ScheduleDBHelper dbHelper = new ScheduleDBHelper(this);
        database = dbHelper.getWritableDatabase();
        listItems = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                // проверяем, совпадает ли день недели в базе данных с днём, куда добавляем данные
                if (cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)).equals(daySchedule)){
                    flag = true;
                    //для добавление в список и базы данных
                    clockSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK));
                    nameSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME));
                    teacherSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER));
                    classSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLASS));
                    idItem = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID));
                    hours = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_HOURS));
                    minutes = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_MINUTES));
                    sortList(listItems, hours, minutes);
                }
            }while (cursor.moveToNext());
        }
        if (flag) {
            textView1DayOff.setVisibility(View.GONE);
        } else {
            textView1DayOff.setVisibility(View.VISIBLE);
        }

        // Находим RecyclerView
        recyclerView = findViewById(R.id.rv_main_activity);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        itemsAdapter = new ScheduleAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_items:
                launchAddItemsActivity();
                break;
            case R.id.nav_teacher:
                launchAddTeacherActivity();
                break;
            case R.id.nav_schedule:
                launchMakeScheduleActivity();
                break;
            case R.id.nav_homework:
                launchHomeworkActivity();
                break;
        }
        return false;
    }

    public void launchAddItemsActivity(){
        Intent intent = new Intent(this, ListItemsActivity.class);
        startActivity(intent);
    }

    public void launchAddTeacherActivity(){
        Intent intent = new Intent(this, ListTeacherActivity.class);
        startActivity(intent);
    }

    private void launchMakeScheduleActivity(){
        Intent intent = new Intent(this, ListScheduleActivity.class);
        startActivityForResult(intent, RequestCode.REQUEST_CODE_MAIN);
    }

    private void launchHomeworkActivity(){
        Intent intent = new Intent(this, ListHomeworkActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == RequestCode.REQUEST_CODE_MAIN){
            //для добавление в список и базы данных
            if(resultCode==RESULT_OK){
                boolean flag = false;
                ScheduleDBHelper dbHelper = new ScheduleDBHelper(this);
                database = dbHelper.getWritableDatabase();
                listItems.clear();
                @SuppressLint("Recycle") Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    do {
                        // проверяем, совпадает ли день недели в базе данных с днём, куда добавляем данные
                        if (cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)).equals(daySchedule)){
                            flag = true;
                            //для добавление в список и базы данных
                            clockSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK));
                            nameSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME));
                            teacherSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER));
                            classSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLASS));
                            idItem = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID));
                            hours = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_HOURS));
                            minutes = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_MINUTES));
                            sortList(listItems, hours, minutes);
                        }
                    }while (cursor.moveToNext());
                }
                if (flag) {
                    textView1DayOff.setVisibility(View.GONE);
                } else {
                    textView1DayOff.setVisibility(View.VISIBLE);
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

        itemsAdapter = new ScheduleAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    private void sortList(ArrayList<RecyclerSchedule> listItems, int hours, int minutes) {
        if (!listItems.isEmpty()) {
            boolean flagLoopOne = false;
            for (int i = 0; i < listItems.size(); i++) {
                if (flagLoopOne) {
                    break;
                }
                if (hours == listItems.get(i).getHours()) {
                    flagLoopOne = true;
                    boolean flagLoopTwo = false;
                    int indexI = 0;
                    for (int j = 0; j < listItems.size(); j++) {
                        if (flagLoopTwo) {
                            break;
                        }
                        if (hours == listItems.get(j).getHours()) {
                            indexI = j;
                            if (minutes < listItems.get(j).getMinutes()) {
                                flagLoopTwo = true;
                                listItems.add(indexI, new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes, classSchedule));
                            }
                        }
                    }
                    if (!flagLoopTwo) {
                        listItems.add(++indexI, new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes, classSchedule));
                    }
                } else if (hours < listItems.get(i).getHours()) {
                    flagLoopOne = true;
                    listItems.add(i, new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes, classSchedule));
                }
            }
            if (!flagLoopOne) {
                listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes, classSchedule));
            }
        } else {
            listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes, classSchedule));
        }
    }
}
