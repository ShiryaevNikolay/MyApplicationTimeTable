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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.AddScheduleActivity;
import com.example.timetable.R;
import com.example.timetable.RecyclerSchedule;
import com.example.timetable.adapters.ScheduleAdapter;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.dialogs.DeleteDialog;
import com.example.timetable.modules.DialogListener;
import com.example.timetable.modules.ItemTouchHelperAdapter;
import com.example.timetable.modules.SimpleItemTouchHelperCallback;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends AbstractTabFragment implements View.OnClickListener, ItemTouchHelperAdapter, DialogListener {
    public static final String ACCESS_MESSAGE_CLOCK="ACCESS_MESSAGE_CLOCK";
    public static final String ACCESS_MESSAGE_HOURS="ACCESS_MESSAGE_HOURS";
    public static final String ACCESS_MESSAGE_MINUTES="ACCESS_MESSAGE_MINUTES";
    public static final String ACCESS_MESSAGE_NAME="ACCESS_MESSAGE_NAME";
    public static final String ACCESS_MESSAGE_TEACHER="ACCESS_MESSAGE_TEACHER";
    public static final String ACCESS_MESSAGE_DAY="ACCESS_MESSAGE_DAY";

    private ArrayList<RecyclerSchedule> listItems;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private ScheduleAdapter itemsAdapter;

    private String clockSchedule;
    private String nameSchedule;
    private String teacherSchedule;
    private int idItem = 0;
    private int hours = 0;
    private int minutes = 0;

    private static final int LAYOUT = R.layout.fragment_schedule;
    private String daySchedule = "";
    private int position;
    private RecyclerSchedule item;

    public static ScheduleFragment getInstance(Context context, int position, SQLiteDatabase database) {
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.position = position;
        fragment.database = database;
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
                    idItem = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID));
                    hours = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_HOURS));
                    minutes = cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_MINUTES));
                    if (!listItems.isEmpty()) {
                        boolean flagLoop = false;
                        for (RecyclerSchedule i : listItems) {
                            if (hours < i.getHours() && minutes < i.getMinutes()) {
                                flagLoop = true;
                                int indexPosition = listItems.indexOf(i);
                                listItems.add(indexPosition, new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes));
                                break;
                            }
                        }
                        if (!flagLoop) {
                            listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes));
                        }
                    } else {
                        listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes));
                    }
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
        itemsAdapter = new ScheduleAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this, getContext());
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
                    idItem = data.getIntExtra("idItem", 0);
                    hours = data.getIntExtra(ACCESS_MESSAGE_HOURS, 0);
                    minutes = data.getIntExtra(ACCESS_MESSAGE_MINUTES, 0);
                    if (!listItems.isEmpty()) {
                        boolean flagLoop = false;
                        for (RecyclerSchedule i : listItems) {
                            if (hours < i.getHours() && minutes < i.getMinutes()) {
                                flagLoop = true;
                                int indexPosition = listItems.indexOf(i);
                                listItems.add(indexPosition, new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes));
                                break;
                            }
                        }
                        if (!flagLoop) {
                            listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes));
                        }
                    } else {
                        listItems.add(new RecyclerSchedule(clockSchedule, nameSchedule, teacherSchedule, idItem, hours, minutes));
                    }
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

        itemsAdapter = new ScheduleAdapter(listItems);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    @Override
    public void onItemDismiss(int position) {
        item = listItems.get(position);
        listItems.remove(position);
        itemsAdapter.notifyItemRemoved(position);

        DialogFragment deleteDialog = new DeleteDialog(this, position);
        FragmentManager fragmentManager = (getFragmentManager());
        assert fragmentManager != null;
        deleteDialog.show(fragmentManager, "deleteDialog");
    }

    @Override
    public void onClickRemoveDialog() {
        database.delete(ScheduleDBHelper.TABLE_SCHEDULE, ScheduleDBHelper.KEY_ID + " = " + item.getId(), null);
    }

    @Override
    public void onClickCancelDialog(int position) {
        listItems.add(position, item);
        itemsAdapter.notifyItemInserted(position);
    }
}
