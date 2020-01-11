package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timetable.adapters.ItemsAdapter;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.database.TeacherDBHelper;
import com.example.timetable.fragments.ScheduleFragment;
import com.example.timetable.util.RequestCode;

import java.util.Calendar;
import java.util.Objects;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    private String day = "";
    private String clock = "";
    private int idItem = 0;
    private int hours = 0;
    private int minutes = 0;

    private TextView tvClock;
    private TextView tvItem;
    private TextView tvTeacher;

    private SQLiteDatabase database;
    private ContentValues contentValues;

    private Button okBtn;
    //==============================================================================================
    ScheduleDBHelper scheduleDBHelper;

    public AddScheduleActivity() {
    }
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        final Intent intent = getIntent();
        day = intent.getStringExtra("day");

        //==========================================================================================
        scheduleDBHelper = new ScheduleDBHelper(this);

        database = scheduleDBHelper.getWritableDatabase();

        // для добавления новых строк в таблицу
        contentValues = new ContentValues();
        //==========================================================================================

        Toolbar toolbar = findViewById(R.id.toolbar_add_schedule);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        Button cancelBtn = findViewById(R.id.add_schedule_cancel_btn);
        okBtn = findViewById(R.id.add_schedule_ok_btn);

        tvClock = findViewById(R.id.tv_clock_schedule);
        tvClock.setText("");
        tvItem = findViewById(R.id.tv_item_schedule);
        tvItem.setText("");
        tvTeacher = findViewById(R.id.tv_teacher_schedule);
        tvTeacher.setText("");

        tvClock.setOnClickListener(this);
        tvItem.setOnClickListener(this);
        tvTeacher.setOnClickListener(this);

        // при нажатии на "Отмена" закрывается текущее окно activity
        cancelBtn.setOnClickListener(this);

        // при нажатии на "Ок", отправляется текст в ListItemActivity и закрывается текущее окно activity
        okBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!tvClock.getText().equals("") && !tvItem.getText().equals("") && !tvTeacher.getText().equals("")) {
            clock = tvClock.getText().toString();
            String name = tvItem.getText().toString();
            String teacher = tvTeacher.getText().toString();

            contentValues.put(ScheduleDBHelper.KEY_DAY, day);
            contentValues.put(ScheduleDBHelper.KEY_CLOCK, clock);
            contentValues.put(ScheduleDBHelper.KEY_HOURS, hours);
            contentValues.put(ScheduleDBHelper.KEY_MINUTES, minutes);
            contentValues.put(ScheduleDBHelper.KEY_NAME, name);
            contentValues.put(ScheduleDBHelper.KEY_TEACHER, teacher);

            //вставляем данные в таблицу базы данных
            database.insert(ScheduleDBHelper.TABLE_SCHEDULE, null, contentValues);

            // ВЫВОД ДАННЫХ ИЗ БАЗЫ ДАННЫХ В ТЕРМИНАЛ
            //==============================================================================
            Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);

            if (cursor.moveToFirst()){
                idItem = cursor.getColumnIndex(ScheduleDBHelper.KEY_ID);
            }
            cursor.close();
            scheduleDBHelper.close();
            //==============================================================================
            sendMessage(day, clock, name, teacher, idItem, hours, minutes);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_clock_schedule:
                callTimePicker();
                checkEmptyField(tvClock, tvItem, tvTeacher, okBtn);
                break;
            case R.id.tv_item_schedule:
                Intent intentItem = new Intent(this, SelectListItemActivity.class);
                intentItem.putExtra("selectBtn", "item");
                startActivityForResult(intentItem, RequestCode.REQUEST_CODE_ITEM);
                break;
            case R.id.tv_teacher_schedule:
                Intent intentTeacher = new Intent(this, SelectListItemActivity.class);
                intentTeacher.putExtra("selectBtn", "teacher");
                startActivityForResult(intentTeacher, RequestCode.REQUEST_CODE_ITEM);
                break;
            case R.id.add_schedule_cancel_btn:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.add_schedule_ok_btn:
                if (!tvClock.getText().equals("") && !tvItem.getText().equals("") && !tvTeacher.getText().equals("")) {
                    clock = tvClock.getText().toString();
                    String name = tvItem.getText().toString();
                    String teacher = tvTeacher.getText().toString();

                    contentValues.put(ScheduleDBHelper.KEY_DAY, day);
                    contentValues.put(ScheduleDBHelper.KEY_CLOCK, clock);
                    contentValues.put(ScheduleDBHelper.KEY_HOURS, hours);
                    contentValues.put(ScheduleDBHelper.KEY_MINUTES, minutes);
                    contentValues.put(ScheduleDBHelper.KEY_NAME, name);
                    contentValues.put(ScheduleDBHelper.KEY_TEACHER, teacher);

                    //вставляем данные в таблицу базы данных
                    database.insert(ScheduleDBHelper.TABLE_SCHEDULE, null, contentValues);

                    // ВЫВОД ДАННЫХ ИЗ БАЗЫ ДАННЫХ В ТЕРМИНАЛ
                    //==============================================================================
                    Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);

                    if (cursor.moveToFirst()){
                        idItem = cursor.getColumnIndex(ScheduleDBHelper.KEY_ID);
                    }

                    cursor.close();
                    scheduleDBHelper.close();
                    //==============================================================================
                    sendMessage(day, clock, name, teacher, idItem, hours, minutes);
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCode.REQUEST_CODE_ITEM){
            if(resultCode==RESULT_OK){
                if (Objects.equals(data.getStringExtra("selectBtn"), "item")) {
                    tvItem.setText(data.getStringExtra(ACCESS_MESSAGE));
                    checkEmptyField(tvClock, tvItem, tvTeacher, okBtn);
                } else if (Objects.equals(data.getStringExtra("selectBtn"), "teacher")) {
                    tvTeacher.setText(data.getStringExtra(ACCESS_MESSAGE));
                    checkEmptyField(tvClock, tvItem, tvTeacher, okBtn);
                }
            }
            else if(resultCode==RESULT_CANCELED) {
                if (Objects.equals(data.getStringExtra("selectBtn"), "item")) {
                    tvItem.setText("");
                    checkEmptyField(tvClock, tvItem, tvTeacher, okBtn);
                } else if (Objects.equals(data.getStringExtra("selectBtn"), "teacher")) {
                    tvTeacher.setText("");
                    checkEmptyField(tvClock, tvItem, tvTeacher, okBtn);
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void callTimePicker(){
        // получаем текущее время
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String editTextTimeParam;
                boolean flag = true;
                if (hourOfDay < 10) {
                    editTextTimeParam = "0" + hourOfDay + ":";
                } else {
                    editTextTimeParam = hourOfDay + ":";
                }
                if (minute < 10) {
                    editTextTimeParam = editTextTimeParam + "0" + minute;
                } else {
                    editTextTimeParam = editTextTimeParam + minute;
                }
                @SuppressLint("Recycle") Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    do {
                        if (cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK)).equals(editTextTimeParam)
                            && cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)).equals(day)){
                            flag = false;
                        }
                    } while (cursor.moveToNext());
                }
                if (flag) {
                    tvClock.setText(editTextTimeParam);
                    clock = tvClock.getText().toString();
                    hours = hourOfDay;
                    minutes = minute;
                } else {
                    Toast.makeText(AddScheduleActivity.this, "В это время уже есть занятие", Toast.LENGTH_SHORT).show();
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    protected void checkEmptyField(TextView tvClock, TextView tvItem, TextView tvTeacher, final Button btn){
        if (!tvClock.getText().equals("") && !tvItem.getText().equals("") && !tvTeacher.getText().equals("")) {
            btn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
        } else {
            btn.setBackground(getDrawable(R.drawable.btn_no_activated));
        }
    }

    // отправка результата EditText в ListItemActivity
    private void sendMessage(String day, String clock, String name, String teacher, int idItem, int hours, int minutes){
        Intent data = new Intent();
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_DAY, day);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_CLOCK, clock);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_HOURS, hours);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_MINUTES, minutes);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_NAME, name);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_TEACHER, teacher);
        data.putExtra("idItem", idItem);
        setResult(RESULT_OK, data);
        finish();
    }
}