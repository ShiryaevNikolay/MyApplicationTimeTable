package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.adapters.ScheduleAdapter;
import com.example.timetable.adapters.TabsFragmentAdapter;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ListScheduleActivity extends AppCompatActivity {
//    public static final String ACCESS_MESSAGE_CLOCK="ACCESS_MESSAGE_CLOCK";
//    public static final String ACCESS_MESSAGE_NAME="ACCESS_MESSAGE_NAME";
//    public static final String ACCESS_MESSAGE_TEACHER="ACCESS_MESSAGE_TEACHER";
//    public static final String ACCESS_MESSAGE_DAY="ACCESS_MESSAGE_DAY";

    private static String daySchedule;
    private List<RecyclerSchedule> listItems;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);

        initTabs();

        Toolbar toolbar = findViewById(R.id.toolbar_schedule);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);
        new ButtonToReturnToMainActivity(toolbar, this);

//        //для базы данных
//        ScheduleDBHelper scheduleDBHelper = new ScheduleDBHelper(this);
//        database = scheduleDBHelper.getWritableDatabase();
//        @SuppressLint("Recycle") Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);
//
//        listItems = new ArrayList<>();
//        // добавляем в список данные (названия предметов) из базы данных
//        if (cursor.moveToFirst()){
//            do {
//                // проверяем, совпадает ли день недели в базе данных с днём, куда добавляем данные
//                if (cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)).equals(daySchedule)){
//                    clockSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK));
//                    nameSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME));
//                    teacherSchedule = cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER));
//                    listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule));
//                }
//            }while (cursor.moveToNext());
//        }
//
//        // Находим RecyclerView
//        recyclerView = findViewById(R.id.recyclerView_schedule);
//        // то, как будет выглядеть RecyclerView (то есть список)
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        // передаём layoutManager в RecyclerView
//        recyclerView.setLayoutManager(layoutManager);
//        // значит, что список фиксированный
//        recyclerView.setHasFixedSize(true);
//
//        // numberItems - кол-во элементов в списке, nameItem - название предмета
//        ScheduleAdapter itemsAdapter = new ScheduleAdapter(listItems, database);
//        //назначаем RecyclerView созданный Adapter
//        recyclerView.setAdapter(itemsAdapter);
//
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemsAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void initTabs() {
        ViewPager viewPager = findViewById(R.id.viewPager_schedule);

        TabsFragmentAdapter adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tablayout_schedule);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(requestCode == RequestCode.REQUEST_CODE_SCHEDULE){
//            //для добавление в список и базы данных
//            String clockSchedule;
//            String nameSchedule;
//            String teacherSchedule;
//            if(resultCode==RESULT_OK){
//                if (daySchedule.equals(data.getStringExtra(ACCESS_MESSAGE_DAY))){
//                    clockSchedule = data.getStringExtra(ACCESS_MESSAGE_CLOCK);
//                    nameSchedule = data.getStringExtra(ACCESS_MESSAGE_NAME);
//                    teacherSchedule = data.getStringExtra(ACCESS_MESSAGE_TEACHER);
//                    listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule));
//                }
//            }
//            else{
//                clockSchedule = "hh:mm";
//                nameSchedule = "Ошибка доступа";
//                teacherSchedule = "Ошибка доступа";
//            }
//        }
//        else{
            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//        ScheduleAdapter itemsAdapter = new ScheduleAdapter(listItems, database);
//        //назначаем RecyclerView созданный Adapter
//        recyclerView.setAdapter(itemsAdapter);
    }
}
