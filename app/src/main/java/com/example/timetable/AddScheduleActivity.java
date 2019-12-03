package com.example.timetable;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.timetable.adapters.ItemsAdapter;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.fragments.ScheduleFragment;
import com.example.timetable.util.RequestCode;

import java.util.Calendar;
import java.util.Objects;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    private String day = "";
    private String clock = "";

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
                    contentValues.put(ScheduleDBHelper.KEY_NAME, name);
                    contentValues.put(ScheduleDBHelper.KEY_TEACHER, teacher);

                    //вставляем данные в таблицу базы данных
                    database.insert(ScheduleDBHelper.TABLE_SCHEDULE, null, contentValues);

                    // ВЫВОД ДАННЫХ ИЗ БАЗЫ ДАННЫХ В ТЕРМИНАЛ
                    //==============================================================================
                    Cursor cursor = database.query(ScheduleDBHelper.TABLE_SCHEDULE, null, null, null, null, null, null);

                    if (cursor.moveToFirst()){
                        int dayIndex = cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY);
                        int idIndex = cursor.getColumnIndex(ScheduleDBHelper.KEY_ID);
                        int clockIndex = cursor.getColumnIndex(ScheduleDBHelper.KEY_CLOCK);
                        int nameIndex = cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME);
                        int teacherIndex = cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER);
                        do {
                            System.out.println("ID = " + cursor.getInt(idIndex) +
                                    ", day = " + cursor.getString(dayIndex) +
                                    ", clock = " + cursor.getString(clockIndex) +
                                    ", name = " + cursor.getString(nameIndex) +
                                    ", teacher = " + cursor.getString(teacherIndex));
                            }while (cursor.moveToNext());
                    } else {
                        System.out.println("0 rows");
                    }

                    cursor.close();
                    scheduleDBHelper.close();
                    //==============================================================================
                    sendMessage(day, clock, name, teacher);
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
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String editTextTimeParam = hourOfDay + " : " + minute;
                tvClock.setText(editTextTimeParam);
                clock = tvClock.getText().toString();
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
    private void sendMessage(String day, String clock, String name, String teacher){
        Intent data = new Intent();
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_DAY, day);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_CLOCK, clock);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_NAME, name);
        data.putExtra(ScheduleFragment.ACCESS_MESSAGE_TEACHER, teacher);
        setResult(RESULT_OK, data);
        finish();
    }
}