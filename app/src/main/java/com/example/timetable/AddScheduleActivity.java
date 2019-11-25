package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.fragments.ScheduleFragment;

public class AddScheduleActivity extends AppCompatActivity {

    private String day = "";
    private String clock = "";
    private String name = "";
    private String teacher = "";

    //==============================================================================================
    ScheduleDBHelper scheduleDBHelper;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        final Intent intent = getIntent();
        day = intent.getStringExtra("day");

        //==========================================================================================
        scheduleDBHelper = new ScheduleDBHelper(this);

        final SQLiteDatabase database = scheduleDBHelper.getWritableDatabase();

        // для добавления новых строк в таблицу
        final ContentValues contentValues = new ContentValues();
        //==========================================================================================

        Toolbar toolbar = findViewById(R.id.toolbar_add_schedule);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        Button cancelBtn = findViewById(R.id.add_schedule_cancel_btn);
        Button okBtn = findViewById(R.id.add_schedule_ok_btn);

        //__________________________________________________________________________________________
        //__________________________________________________________________________________________
        //__________________________________________________________________________________________
        // находим поля с вводом текста "Фамилия, имя, отчество"
        final EditText etClock = findViewById(R.id.editText_clock_schedule);
        final EditText etName = findViewById(R.id.editText_name_schedule);
        final EditText etTeacher = findViewById(R.id.editText_teacher_schedule);

        // проверяем, пустое ли поле ввода
        checkEmptyField(etClock, okBtn);
        checkEmptyField(etName, okBtn);
        checkEmptyField(etTeacher, okBtn);
        //__________________________________________________________________________________________
        //__________________________________________________________________________________________
        //__________________________________________________________________________________________

        // при нажатии на "Отмена" закрывается текущее окно activity
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // при нажатии на "Ок", отправляется текст в ListItemActivity и закрывается текущее окно activity
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etClock.getText().toString().equals("") || !etName.getText().toString().equals("") || !etTeacher.getText().toString().equals("")){
                    clock = etClock.getText().toString();
                    name = etName.getText().toString();
                    teacher = etTeacher.getText().toString();

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
                } else {
                    clock = "";
                    name = "";
                    teacher = "";
                }
            }
        });
    }

    protected void checkEmptyField(final EditText editText, final Button btn){
        editText.addTextChangedListener(new TextWatcher() {
            // действия перед тем, как что то введено
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                if (s.toString().equals("")){
                    btn.setBackground(getDrawable(R.drawable.btn_no_activated));
                } else { // иначе зеленая (активная)
                    btn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
                }
            }
            // действия, когда вводится какой то текст
            // s - то, что вводится, для преобразования в строку - s.toString()
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            // действия после того, как что то введено
            // editable - то, что введено. В строку - editable.toString()
            @Override
            public void afterTextChanged(Editable s) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                if (s.toString().equals("")){
                    btn.setBackground(getDrawable(R.drawable.btn_no_activated));
                } else { // иначе зеленая (активная)
                    btn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
                }
            }
        });
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